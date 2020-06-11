package sakura.kooi.MoocSearch.gui;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AnswerPanel extends JPanel {
    private JPanel contentPane;
    private JLabel lblSource;
    private MultiLineLabel lblAnswer;
    @Getter
    private boolean found = false;

    public AnswerPanel(String source) {
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
        this.setLayout(layout);
        this.setBackground(Color.WHITE);
        this.add(contentPane);
        layout.setHgap(0);
        lblSource.setText(" ["+source+"] ");
        lblSource.setBorder(new EmptyBorder(0, 5, 0, 0));
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
