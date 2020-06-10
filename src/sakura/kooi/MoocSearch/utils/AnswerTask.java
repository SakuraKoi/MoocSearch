package sakura.kooi.MoocSearch.utils;

import com.google.common.base.Joiner;
import sakura.kooi.MoocSearch.gui.AnswerPanel;
import sakura.kooi.MoocSearch.gui.MainGUI;
import sakura.kooi.MoocSearch.gui.QuestionPanel;
import sakura.kooi.MoocSearch.sources.QuestionSources;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class AnswerTask {
    public static void answer(String question) {
        if (question.trim().isEmpty()) return;
        System.out.println("Answering question ["+question+"]");
        QuestionPanel questionPanel = new QuestionPanel();
        MainGUI.getInstance().getPanelAnswerContainer().add(questionPanel);
        questionPanel.setQuestion(question);
        MainGUI.getThreadPool().execute(() -> {
            ArrayList<Future> futures = new ArrayList<>();
            Stream.of(QuestionSources.values()).forEach(source -> {
                if (Thread.currentThread().isInterrupted()) return;
                if (!source.isEnabled()) return;
                AnswerPanel answerPanel = questionPanel.newAnswer(source.getName());
                futures.add(MainGUI.getThreadPool().submit(source.get(question, new AnswerCallback() {
                    @Override
                    public void completed(String answer) {
                        ArrayList<String> answers = new ArrayList<>(Arrays.asList(answer.split("\n")));
                        Collections.sort(answers);
                        String finalAnswer = Joiner.on('\n').join(answers);
                        System.out.println("Answered Question from "+source.getName()+" : "+question+" = "+answer.replace('\n', ','));
                        EventQueue.invokeLater(() -> answerPanel.setAnswer(finalAnswer, true));
                    }

                    @Override
                    public void failed(String message) {
                        System.out.println("Answered Failed from "+source.getName()+" : "+question+" -> "+message.replace('\n', ','));
                        EventQueue.invokeLater(() -> answerPanel.setAnswer(message, false));
                    }
                })));
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
            questionPanel.completeAnswer();
        });
    }
}
