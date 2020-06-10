package sakura.kooi.MoocSearch.gui;

import sakura.kooi.MoocSearch.utils.VerticalFlowLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QuestionPanel extends JPanel {
    private JPanel contentPane;
    private JLabel lblQuestion;
    private JLabel lblAnswering;
    private ArrayList<AnswerPanel> answerPanels = new ArrayList<>();

    public QuestionPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.add(contentPane);
        contentPane.setLayout(new VerticalFlowLayout(0, 0));
    }

    public void setQuestion(String question) {
        lblQuestion.setText(question);
    }
    public void completeAnswer() {
        this.contentPane.remove(lblAnswering);
        boolean found = false;
        for (AnswerPanel answerPanel : answerPanels) {
            if (answerPanel.isFound()) {
                found = true;
                break;
            }
        }
        if (found) {
            ArrayList<String> has = new ArrayList<>();
            for (AnswerPanel answerPanel : answerPanels) {
                if (!answerPanel.isFound()) {
                    this.contentPane.remove(answerPanel);
                } else {
                    String answer = answerPanel.getAnswer();
                    if (has.contains(answer)) {
                        this.contentPane.remove(answerPanel);
                    } else {
                        has.add(answer);
                    }
                }
            }
        }
    }

    public AnswerPanel newAnswer(String source) {
        AnswerPanel answerPanel = new AnswerPanel(source);
        this.contentPane.add(answerPanel);
        this.contentPane.updateUI();
        answerPanels.add(answerPanel);
        return answerPanel;
    }
}
