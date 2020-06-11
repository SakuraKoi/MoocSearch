package sakura.kooi.MoocSearch.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QuestionPanel extends JPanel {
    private JPanel contentPane;
    private JLabel lblQuestion;
    private ArrayList<AnswerPanel> answerPanels = new ArrayList<>();

    public QuestionPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.add(contentPane);
        contentPane.setLayout(new VerticalFlowLayout(0, 0, 5, 0));
    }

    public void setQuestion(String question) {
        lblQuestion.setText(question);
    }
    public void completeAnswer() {
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
        } else {
            for (AnswerPanel answerPanel : answerPanels)
                    this.contentPane.remove(answerPanel);
            newAnswer("全部题库").setAnswer("未找到答案", false);
        }
        this.contentPane.updateUI();
    }

    public AnswerPanel newAnswer(String source) {
        AnswerPanel answerPanel = new AnswerPanel(source);
        this.contentPane.add(answerPanel);
        this.contentPane.updateUI();
        answerPanels.add(answerPanel);
        this.contentPane.updateUI();
        return answerPanel;
    }
}
