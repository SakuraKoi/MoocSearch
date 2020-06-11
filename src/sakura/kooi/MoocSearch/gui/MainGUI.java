package sakura.kooi.MoocSearch.gui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import kong.unirest.Unirest;
import lombok.Getter;
import org.apache.commons.codec.binary.StringUtils;
import sakura.kooi.MoocSearch.Constants;
import sakura.kooi.MoocSearch.sources.QuestionSources;
import sakura.kooi.MoocSearch.utils.AnswerQueue;

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
    private JCheckBox checkSource14;
    private JSpinner connectTimeout;
    private JSpinner socketTimeout;

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
        loadConfiguration();
        registerConfiguration();
        registerSources();
    }

    private void loadConfiguration() {
        File configFile = new File("MoocSearch.json");
        if (configFile.exists()) {
            try {
                configuration = JSON.parseObject(StringUtils.newStringUtf8(Files.readAllBytes(configFile.toPath())));
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
            Files.write(configFile.toPath(), StringUtils.getBytesUtf8(JSON.toJSONString(configuration, SerializerFeature.PrettyFormat)));
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
        panelAnswerContainer.removeAll();
        Stream.of(questions).forEach(question -> threadPool.submit(() -> AnswerQueue.answer(question)));
    }

    private void copyZhihuishuScript() {
        setClipboard("var sub = $(\".subject_describe\");\nvar element;\nfor(element in $(\".subject_describe\")) {\n	console.log(sub[element].innerText);\n}");
    }

    private void setClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    private void registerSources() {
        registerSource(checkSource1, QuestionSources.XUANXIU365_COM);
        registerSource(checkSource2, QuestionSources.CXMOOCTOOL);
        registerSource(checkSource3, QuestionSources.WWW_150S_CN);
        registerSource(checkSource4, QuestionSources.FM210_CN_1);
        registerSource(checkSource5, QuestionSources.FM210_CN_2);
        registerSource(checkSource6, QuestionSources.FM210_CN_3);

        registerSource(checkSource10, QuestionSources.IYTWL_CN);
        registerSource(checkSource11, QuestionSources.WK_92E_WIN);

        registerSource(checkSource14, QuestionSources.SHUAKELA_TOP);
    }

    private void registerConfiguration() {
        SpinnerNumberModel connectTimeoutModel = new SpinnerNumberModel(
                configuration.containsKey("connectTimeOut") ? configuration.getIntValue("connectTimeOut") : 2000
                , 1000, 30000, 500);
        connectTimeout.setModel(connectTimeoutModel);
        connectTimeoutModel.addChangeListener(e -> {
            if (Unirest.config().isRunning()) Unirest.config().reset();
            Unirest.config().connectTimeout(connectTimeoutModel.getNumber().intValue());
            configuration.put("connectTimeOut", connectTimeoutModel.getValue());
            saveConfiguration();
        });

        SpinnerNumberModel socketTimeoutModel = new SpinnerNumberModel(
                configuration.containsKey("socketTimeout") ? configuration.getIntValue("socketTimeout") : 10000
                , 1000, 30000, 500);
        socketTimeout.setModel(socketTimeoutModel);
        socketTimeoutModel.addChangeListener(e -> {
            if (Unirest.config().isRunning()) Unirest.config().reset();
            Unirest.config().socketTimeout(socketTimeoutModel.getNumber().intValue());
            configuration.put("socketTimeout", socketTimeout.getValue());
            saveConfiguration();
        });

        Unirest.config().connectTimeout(connectTimeoutModel.getNumber().intValue());
        Unirest.config().socketTimeout(socketTimeoutModel.getNumber().intValue());
    }

    private void registerSource(JCheckBox check, QuestionSources source) {
        boolean enabled =
                !configuration.containsKey(source.getKey()) ||
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
