package sakura.kooi.MoocSearch;

import com.formdev.flatlaf.FlatLightLaf;
import kong.unirest.Unirest;
import org.fusesource.jansi.AnsiConsole;
import sakura.kooi.MoocSearch.gui.MainGUI;

import javax.swing.*;
import java.awt.*;

public class Launcher {
    static {
        Unirest.config().verifySsl(false);
    }

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        FlatLightLaf.install();
        Runtime.getRuntime().addShutdownHook(new Thread(Unirest::shutDown));

        EventQueue.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            gui.pack();
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);
            gui.getTextQuestions().requestFocus();
        });
    }
}
