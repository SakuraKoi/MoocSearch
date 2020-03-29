package sakura.kooi.MoocSearch.gui;

import lombok.Getter;
import sakura.kooi.MoocSearch.Constants;
import sakura.kooi.MoocSearch.utils.AnswerTask;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class MainGUI extends JFrame {
    @Getter private static ExecutorService threadPool = Executors.newCachedThreadPool();
    @Getter private static MainGUI instance;
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    @Getter private JTextArea textQuestions;
    @Getter private JPanel panelAnswerContainer;
    private JPanel panelAnswers;
    private JButton btnScriptZhihuishu;

    public MainGUI() {
        instance = this;
        this.setContentPane(contentPane);
        this.setPreferredSize(new Dimension(880, 520));
        this.setTitle("MoocSearch 多题库联合慕课答案查询 | 构建版本 " + Constants.BUILD_VERSION + " | SakuraKooi ~☆");

        panelAnswers.setFocusable(true);
        panelAnswerContainer.setLayout(new BoxLayout(panelAnswerContainer, BoxLayout.Y_AXIS));

        textQuestions.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiers() == KeyEvent.CTRL_MASK) {
				    answerQuestions(textQuestions.getText().replace("\r", "").replaceAll("(VM.*? )", "").split("\n"));
                }
            }
        });
        panelAnswers.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    backToQuestions();
                }
            }
        });
        btnScriptZhihuishu.addActionListener(e -> copyZhihuishuScript());
    }

    private void backToQuestions() {
        tabbedPane.setSelectedIndex(0);
        textQuestions.requestFocus();
        textQuestions.setText("");
        panelAnswerContainer.removeAll();
        threadPool.shutdownNow();
        threadPool = Executors.newCachedThreadPool();
    }

    private void answerQuestions(String[] questions) {
        tabbedPane.setSelectedIndex(1);
        panelAnswers.requestFocus();
        Stream.of(questions).forEach(question -> threadPool.submit(new AnswerTask(question)));
    }

    private void copyZhihuishuScript() {
        setClipboard("var sub = $(\".subject_describe\");\nvar element;\nfor(element in $(\".subject_describe\")) {\n	console.log(sub[element].innerText);\n}");
    }

    private void setClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }
}
