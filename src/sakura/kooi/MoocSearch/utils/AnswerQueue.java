package sakura.kooi.MoocSearch.utils;

import com.google.common.base.Joiner;
import kong.unirest.UnirestException;
import sakura.kooi.MoocSearch.gui.AnswerPanel;
import sakura.kooi.MoocSearch.gui.MainGUI;
import sakura.kooi.MoocSearch.gui.QuestionPanel;
import sakura.kooi.MoocSearch.sources.QuestionSources;
import sakura.kooi.logger.Logger;

import java.awt.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class AnswerQueue {
    private static Logger logger = Logger.of("AnswerQueue");

    public static void answer(String question) {
        if (question.trim().isEmpty()) return;
        QuestionPanel questionPanel = new QuestionPanel();

        EventQueue.invokeLater(() -> {
            MainGUI.getInstance().getPanelAnswerContainer().add(questionPanel);
            questionPanel.setQuestion(question);
            MainGUI.getInstance().getPanelAnswerContainer().updateUI();
        });

        logger.info("正在查询问题 \"{}\"", question);
        ArrayList<Future> futures = new ArrayList<>();

        Stream.of(QuestionSources.values()).forEach(source -> {
            if (Thread.currentThread().isInterrupted()) return;
            if (!source.isEnabled()) return;
            AnswerPanel answerPanel = questionPanel.newAnswer(source.getName());

            futures.add(MainGUI.getThreadPool().submit(() -> answer(question, source, answerPanel)));
        });

        for (Future future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        logger.info("问题 \"{}\" 查询完毕", question);
        questionPanel.completeAnswer();
    }

    private static void answer(String question, QuestionSources source, AnswerPanel answerPanel) {
        AnswerCallback callback = new AnswerCallback() {
            @Override
            public void completed(String answer) {
                ArrayList<String> answers = new ArrayList<>(Arrays.asList(answer.split("\n")));
                Collections.sort(answers);
                String finalAnswer = Joiner.on('\n').join(answers);
                logger.success("{} 查询成功 {} = {}", source.getName(), question, finalAnswer.replace('\n', ','));
                EventQueue.invokeLater(() -> answerPanel.setAnswer(finalAnswer, true));
            }

            @Override
            public void failed(String message) {
                logger.warn("{} 查询失败 {} -> {}", source.getName(), question, message.replace('\n', ','));
                EventQueue.invokeLater(() -> answerPanel.setAnswer(message, false));
            }
        };
        try {
            source.get(question, callback).run();
        } catch (UnirestException e) {
            logger.warnEx("{} 查询失败 {} -> {}", e, source.getName(), question, e.getMessage());
            if (e.getCause() instanceof SocketTimeoutException) {
                EventQueue.invokeLater(() -> answerPanel.setAnswer("请求超时", false));
            } else if (e.getCause() instanceof ConnectException) {
                EventQueue.invokeLater(() -> answerPanel.setAnswer("无法连接到服务器", false));
            } else {
                EventQueue.invokeLater(() -> answerPanel.setAnswer("查询失败", false));
            }
        } catch (Exception e) {
            logger.warnEx("{} 查询失败 {} -> {}", e, source.getName(), question, e.getMessage());
            EventQueue.invokeLater(() -> answerPanel.setAnswer("查询出错", false));
        }
    }
}
