package sakura.kooi.MoocSearch.gui;

import lombok.Getter;
import sakura.kooi.MoocSearch.utils.VerticalFlowLayout;

import javax.swing.*;
import java.awt.*;

public class QuestionPanel extends JPanel {
    private JPanel contentPane;
    private JLabel lblQuestion;
    private JLabel lblAnswering;

    public QuestionPanel() {
        this.setLayout(new BorderLayout());
        this.add(contentPane);
        contentPane.setLayout(new VerticalFlowLayout(0, 0));
    }

    public void setQuestion(String question) {
        lblQuestion.setText(question);
    }
    public void completeAnswer() {
        this.contentPane.remove(lblAnswering);
    }

    public AnswerPanel newAnswer(String source) {
        AnswerPanel answerPanel = new AnswerPanel(source);
        this.contentPane.add(answerPanel);
        this.contentPane.updateUI();
        return answerPanel;
    }
}
