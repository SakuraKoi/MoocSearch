package sakura.kooi.MoocSearch.gui;

import lombok.Getter;
import sakura.kooi.MoocSearch.utils.MultiLineLabel;

import javax.swing.*;
import java.awt.*;

public class AnswerPanel extends JPanel {
    private JPanel contentPane;
    private JLabel lblSource;
    private MultiLineLabel lblAnswer;
    @Getter
    private boolean found = false;

    public AnswerPanel(String source) {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.setBackground(Color.WHITE);
        this.add(contentPane);
        lblSource.setText("["+source+"] ");
    }

    private void createUIComponents() {
        lblAnswer = new MultiLineLabel();
    }

    public void setAnswer(String answer, boolean found) {
        this.found = found;
        lblAnswer.setText(answer);
        lblAnswer.setForeground(found ? new Color(0, 128, 0) : Color.RED);
        this.updateUI();
    }

    public String getAnswer() {
        return lblAnswer.getText();
    }
}
