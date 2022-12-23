import javax.swing.*;
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
        setContentPane(mainPanel);
        setTitle("Text Editor");
        setSize(400,400);
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
                            System.out.println(input.getText());

                            // Runtime rt = Runtime.getRuntime();
                            // Process pr = rt.exec("swift foo.swift");
                            // Processbuilder is newer!

                            ProcessBuilder builder = new ProcessBuilder("/usr/bin/env", "swift", "foo.swift");
                            builder.redirectErrorStream(true);
                            Process executionProcess = builder.start();

                            out.close();

                            BufferedReader stdInput = new BufferedReader(new
                                    InputStreamReader(executionProcess.getInputStream()));

                            // Read the output from the command
                            System.out.println("Here is the standard output of the command:");
                            String s;
                            while (true) {
                                running.setText("Running");
                                if ((s = stdInput.readLine()) == null) break;
                                System.out.println(s);
                                output.append(s + "\n");
                                System.out.println("EDT Thread: " + SwingUtilities.isEventDispatchThread());
        //                        SwingUtilities.invokeLater(new Runnable() {
        //                            @Override
        //                            public void run() {
        //                                output.append(finalS1);
        ////                                try {
        ////                                    doc.insertString(doc.getLength(), finalS1, null);
        ////                                } catch (BadLocationException e) {
        ////                                    e.printStackTrace();
        ////                                }
        //                            }
        //                        });
                            }
                            executionProcess.waitFor();
                            exitCode.setText("Process finished with exit code " + executionProcess.exitValue());
                            running.setText("Idle");
                            System.out.println("Done");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            throw new RuntimeException(ex);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                            throw new RuntimeException(ex);
                        }
                    }
                });
                updateOutput.start();
            }
        });
    }

    public static void main(String[] args) {
        Editor editor = new Editor();
    }
}
