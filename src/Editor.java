import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor extends JFrame {
    private JPanel mainPanel;
    private JButton save;
    private JButton clear;
    private JTextPane input;
    private JTextArea output;
    private JLabel running;
    private JLabel exitCode;

    String text;

    private static Pattern pattern = Pattern.compile("(var|let)");
    private static Matcher matcher;
    private static AttributeSet keywordAttributes;
    private static StyleContext sc = StyleContext.getDefaultStyleContext();

    private static AttributeSet defaultAttributes;


    public Editor() {
         ((AbstractDocument) input.getDocument()).setDocumentFilter(new SyntaxHighlighting(input));
        this.keywordAttributes = new SimpleAttributeSet();
        this.keywordAttributes =  sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.red);
        this.defaultAttributes = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.black);

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
                            // Runtime rt = Runtime.getRuntime();
                            // Process pr = rt.exec("swift foo.swift");
                            // Processbuilder is newer!

                            ProcessBuilder builder = new ProcessBuilder("/usr/bin/env", "swift", "foo.swift");
                            builder.redirectErrorStream(true);
                            Process executionProcess = builder.start();
                            running.setText("Running");

                            BufferedReader stdInput = new BufferedReader(new
                                    InputStreamReader(executionProcess.getInputStream()));

                            // Read the output from the command
                            //System.out.println("Here is the standard output of the command:");
                            String s;
                            while (true) {
                                if ((s = stdInput.readLine()) == null) break;
                                System.out.println(s);
                                output.append(s + "\n");
                                //System.out.println("EDT Thread: " + SwingUtilities.isEventDispatchThread());
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
                            //System.out.println("Done");
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
//        input.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
////                text += String.valueOf(e.getKeyChar());
////                System.out.println(text);
////                matcher = pattern.matcher(text);
////                if (matcher.lookingAt()) {
////                    while (matcher.find()) {
////                        try {
////                            doc.remove(matcher.start(), matcher.end() - matcher.end());
////                            doc.insertString(matcher.start(), text.substring(matcher.start(), matcher.end()), textAttributes);
////                        } catch (BadLocationException ex) {
////                            throw new RuntimeException(ex);
////                        }
////                    }
////                }
////                try {
////                    input.getDocument().insertString(input.getCaretPosition(), String.valueOf(e.getKeyChar()), null);
////                } catch (BadLocationException ex) {
////                    throw new RuntimeException(ex);
////                }
//                try {
//                    text = input.getDocument().getText(0, input.getDocument().getLength());
//                    highlight(input);
//                } catch (BadLocationException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
    }
    public static void highlight(JTextPane input) throws BadLocationException {
        Document doc = input.getDocument();
        if (input.getCaretPosition() == 0) {
            return;
        }
        int offset = input.getCaretPosition();
        while(!doc.getText(offset, 1).equals(" ")) {
            offset--;
            if (offset < 0) {
                break;
            }
        }
        offset++;

        String toCheck = doc.getText(offset, input.getCaretPosition() - offset);
        matcher = pattern.matcher(toCheck);
        if (matcher.matches()) {
            System.out.println(matcher.start());
            System.out.println(matcher.end());
//            doc.remove(offset, matcher.end() - matcher.start());
//            doc.insertString(offset, matcher.group(), keywordAttributes);
//            input.setCharacterAttributes(defaultAttributes, false);
            input.getStyledDocument().setCharacterAttributes(offset, matcher.group().length(), keywordAttributes, false );
        } else {
            input.getStyledDocument().setCharacterAttributes(offset, toCheck.length(),defaultAttributes, true );
        }
    }
}
