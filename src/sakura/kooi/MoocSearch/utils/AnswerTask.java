package sakura.kooi.MoocSearch.utils;

import sakura.kooi.MoocSearch.gui.AnswerPanel;
import sakura.kooi.MoocSearch.gui.MainGUI;
import sakura.kooi.MoocSearch.gui.QuestionPanel;
import sakura.kooi.MoocSearch.sources.QuestionSources;

import java.awt.*;
import java.util.stream.Stream;

public class AnswerTask implements Runnable {
    private String question;
    public AnswerTask(String question) {
        this.question = question;
    }

    @Override
    public void run() {
        if (question.trim().isEmpty()) return;
        System.out.println("Answering question ["+question+"]");
        QuestionPanel questionPanel = new QuestionPanel();
        MainGUI.getInstance().getPanelAnswerContainer().add(questionPanel);
        questionPanel.setQuestion(question);
        Stream.of(QuestionSources.values()).forEach(source -> {
            if (Thread.currentThread().isInterrupted()) return;
            AnswerPanel answerPanel = questionPanel.newAnswer(source.getName());
            MainGUI.getThreadPool().execute(source.get(question, new AnswerCallback() {
                @Override
                public void completed(String answer) {
                    System.out.println("Answered Question from "+source.getName()+" : "+question+" = "+answer);
                    EventQueue.invokeLater(() -> answerPanel.setAnswer(answer, true));
                }

                @Override
                public void failed(String message) {
                    EventQueue.invokeLater(() -> answerPanel.setAnswer(message, false));
                }
            }));
        });
        questionPanel.completeAnswer();
    }
}
