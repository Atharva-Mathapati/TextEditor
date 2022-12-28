import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Editor extends JFrame {
    private JPanel mainPanel;
    private JButton save;
    private JButton clear;
    private JTextPane input;
    private JTextArea output;
    private JLabel running;
    private JLabel exitCode;


    public Editor() {
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new SyntaxHighlighting(input));

        setContentPane(mainPanel);
        setTitle("Text Editor");
        setSize(600,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        output.setEditable(false);

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setText("");
            }
        });
        save.addActionListener(new ActionListener() {
            /*
            TESTING SWIFT CODE:
            for i in 1...1000000 {print(i)}
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread updateOutput = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PrintWriter out = new PrintWriter("foo.swift");
                            out.println(input.getText());
                            // System.out.println(input.getText());
                            out.close();

                            ProcessBuilder builder = new ProcessBuilder("/usr/bin/env", "swift", "foo.swift");
                            builder.redirectErrorStream(true);
                            Process executionProcess = builder.start();
                            running.setText("Running");

                            BufferedReader stdInput = new BufferedReader(new
                                    InputStreamReader(executionProcess.getInputStream()));

                            // Read the output of the program
                            String s;
                            while ((s = stdInput.readLine()) != null) {
                                System.out.println(s);
                                output.append(s + "\n");
                            }

                            executionProcess.waitFor();
                            exitCode.setText("Process finished with exit code " + executionProcess.exitValue());
                            running.setText("Idle");
                            // System.out.println("Done");
                        } catch (IOException | InterruptedException ex) {
                            ex.printStackTrace();
                            throw new RuntimeException(ex);
                        }
                    }
                });
                updateOutput.start();
            }
        });
    }
}
