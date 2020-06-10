package sakura.kooi.MoocSearch.gui;

import lombok.Getter;
import org.apache.commons.codec.binary.StringUtils;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONObject;
import sakura.kooi.MoocSearch.Constants;
import sakura.kooi.MoocSearch.sources.QuestionSources;
import sakura.kooi.MoocSearch.utils.AnswerTask;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class MainGUI extends JFrame {
    @Getter private static ExecutorService threadPool = Executors.newCachedThreadPool();
    @Getter private static MainGUI instance;
    private JSONObject configuration;

    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    @Getter private JTextArea textQuestions;
    @Getter private JPanel panelAnswerContainer;
    private JPanel panelAnswers;
    private JButton btnScriptZhihuishu;
    private JCheckBox checkSource1;
    private JCheckBox checkSource2;
    private JCheckBox checkSource3;
    private JCheckBox checkSource4;
    private JCheckBox checkSource5;
    private JCheckBox checkSource6;
    private JCheckBox checkSource7;
    private JCheckBox checkSource8;
    private JCheckBox checkSource9;
    private JCheckBox checkSource10;
    private JCheckBox checkSource11;
    private JCheckBox checkSource12;
    private JCheckBox checkSource13;

    public MainGUI() {
        instance = this;
        AnsiConsole.systemInstall();
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
        loadConfiguration();
        registerSources();
    }

    private void loadConfiguration() {
        File configFile = new File("MoocSearch.json");
        if (configFile.exists()) {
            try {
                configuration = new JSONObject(StringUtils.newStringUtf8(Files.readAllBytes(configFile.toPath())));
            } catch (IOException e) {
                e.printStackTrace();
                configuration = new JSONObject();
            }
        } else {
            configuration = new JSONObject();
        }
    }

    private void saveConfiguration() {
        File configFile = new File("MoocSearch.json");
        if (configFile.exists()) configFile.delete();
        try {
            configFile.createNewFile();
            Files.write(configFile.toPath(), StringUtils.getBytesUtf8(configuration.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Stream.of(questions).forEach(AnswerTask::answer);
    }

    private void copyZhihuishuScript() {
        setClipboard("var sub = $(\".subject_describe\");\nvar element;\nfor(element in $(\".subject_describe\")) {\n	console.log(sub[element].innerText);\n}");
    }

    private void setClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    private void registerSources() {
        registerSource(checkSource1, QuestionSources.XUANXIU365);
        registerSource(checkSource2, QuestionSources.CXMOOCTOOL);
        registerSource(checkSource3, QuestionSources.KBM);
        registerSource(checkSource4, QuestionSources.HKXY1);
        registerSource(checkSource5, QuestionSources.HKXY2);
        registerSource(checkSource6, QuestionSources.HKXY3);
    }

    private void registerSource(JCheckBox check, QuestionSources source) {
        boolean enabled =
                !configuration.has(source.getKey()) ||
                configuration.getBoolean(source.getKey());
        check.setSelected(enabled);
        source.setEnabled(enabled);
        check.addActionListener(e -> {
            source.setEnabled(check.isSelected());
            configuration.put(source.getKey(), check.isSelected());
            saveConfiguration();
        });
    }
}
