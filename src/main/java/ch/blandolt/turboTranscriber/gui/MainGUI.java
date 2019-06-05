package ch.blandolt.turboTranscriber.gui;

import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.Loggable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame  implements Loggable {
    private JTabbedPane mainTabbedPane;
    private JPanel mainPanel;
    private JPanel logPane;
    private JTextArea logTextArea;
    private JScrollPane logScroller;
    private JPanel imageContainer;
    private JPanel controls;
    private JButton cropSelected;
    private JPanel thumbnailPanel;

    private CustomImagePanel imagePanel;

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
        cropSelected.addActionListener(e -> {
            // TODO: implement
        });
    }

    public void showMainGUI(){
        // TODO: create Thumbnails

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
