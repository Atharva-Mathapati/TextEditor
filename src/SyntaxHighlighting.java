import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighting extends DocumentFilter {
    private final Pattern pattern = Pattern.compile("(var |let )");
    private Matcher matcher;
    private final AttributeSet keywordAttributes = new SimpleAttributeSet();
    private final AttributeSet defaultAttributes = new SimpleAttributeSet();
    private final JTextPane pane;

    public SyntaxHighlighting(JTextPane pane) {
        this.pane = pane;
        StyleConstants.setForeground((MutableAttributeSet) keywordAttributes, Color.red);
        StyleConstants.setForeground((MutableAttributeSet) defaultAttributes, Color.black);
        System.out.println("Created");
    }


    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
       super.insertString(fb, offset, text, attr);
       highlight();
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException {
        super.replace(fb, offset, length, text, attr);
        highlight();
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
        highlight();
    }
    public void highlight() throws BadLocationException {
        pane.getStyledDocument().setCharacterAttributes(0, pane.getText().length(), defaultAttributes, false);
        matcher = pattern.matcher(pane.getText());
        while (matcher.find()) {
            // Words like "wallet" should not be highlighted
            // i.e. words at the beginning of a line should be highlighted
            if (matcher.start() != 0 && !pane.getText(matcher.start()-1,1).equals(" ") && !pane.getText(matcher.start()-1,1).equals("\n")) {
                continue;
            }
            pane.getStyledDocument().setCharacterAttributes(matcher.start(), matcher.end()- matcher.start(), keywordAttributes, false);
        }

    }
}
