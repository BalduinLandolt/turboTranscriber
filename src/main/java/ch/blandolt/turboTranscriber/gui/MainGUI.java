package ch.blandolt.turboTranscriber.gui;

import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.Loggable;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame  implements Loggable {
    private JTabbedPane mainTabbedPane;
    private JPanel mainPanel;
    private JPanel logPane;
    private JTextArea logTextArea;
    private JScrollPane logScroller;

    public MainGUI(){

        super("Turbo Transcriber");

        Log.log("Setting up GUI...");

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));

        // TODO: Add Listeners

        Log.addLoggable(this);
        // TODO: moves this to core application?

        Log.log("GUI set up.");
    }

    public void showMainGUI(){
        pack();
        setExtendedState(MAXIMIZED_BOTH);

        setVisible(true);

        Log.log("Showing GUI.");
    }

    @Override
    public void log(String s) {
        logTextArea.append(Log.lineSep);
        logTextArea.append(s);
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }
}
