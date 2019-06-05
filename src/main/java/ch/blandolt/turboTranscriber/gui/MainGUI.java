package ch.blandolt.turboTranscriber.gui;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private JTabbedPane mainTabbedPane;
    private JPanel mainPanel;

    public MainGUI() {
        super("Turbo Transcriber");

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void showMainGUI(){
        pack();
        setExtendedState(MAXIMIZED_BOTH);

        setVisible(true);
    }
}
