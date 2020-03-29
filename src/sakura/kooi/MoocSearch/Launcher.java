package sakura.kooi.MoocSearch;

import com.formdev.flatlaf.FlatLightLaf;
import kong.unirest.Unirest;
import sakura.kooi.MoocSearch.gui.MainGUI;

import javax.swing.*;
import java.awt.*;

public class Launcher {
    static {
        try {
            Unirest.config().verifySsl(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            FlatLightLaf.install();
            MainGUI gui = new MainGUI();
            gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            gui.pack();
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);
            gui.getTextQuestions().requestFocus();
        });
    }
}
