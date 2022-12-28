import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {
    private static Pattern pattern = Pattern.compile("(var|let)");
    private static Matcher matcher;
    private static Document doc;
    static JTextPane input;
    private static AttributeSet keywordAttributes = new SimpleAttributeSet();
    private static StyleContext sc = StyleContext.getDefaultStyleContext();
    private static AttributeSet defaultAttributes = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.black);




    public static void main(String[] args) throws BadLocationException {
         new Editor();

// IGNORE -------- SIMPLE TESTING ---------------------------------
//        StyleConstants.setForeground((MutableAttributeSet) keywordAttributes, Color.red);
//        JFrame frame = new JFrame();
//        frame.setSize(400,400);
//        input = new JTextPane();
//        doc = input.getDocument();
//        frame.add(input);
//        // ((AbstractDocument) input.getDocument()).setDocumentFilter(new SyntaxHighlighting());
//        frame.setVisible(true);
//        input.requestFocusInWindow();
//        frame.requestFocusInWindow();
//        input.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                try {
//                    text = doc.getText(0, doc.getLength());
//                    highlight();
//                } catch (BadLocationException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
    }
}