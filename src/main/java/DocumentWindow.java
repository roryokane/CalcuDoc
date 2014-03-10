import com.google.common.base.Joiner;

import javax.swing.*;
import java.util.*;

public class DocumentWindow {
    public JPanel root;
    private JTextArea calculationsTextArea;
    private JTextArea lineNumbersTextArea;
    String calculationsText = "";

    public DocumentWindow() {
        populateLineNumbers();
    }

    private void populateLineNumbers() {
        int numLines = calculationsLines().size();

        List<String> lineLabels = new ArrayList<String>();
        for (int i = 1; i <= numLines; i++) {
            lineLabels.add(Integer.toString(i));
        }
        String lineNumbersText = Joiner.on("\n").join(lineLabels);

        lineNumbersTextArea.setText(lineNumbersText);
    }

    private List<String> calculationsLines() {
        String[] lines = calculationsText.split("\r\n|\r|\n");
        return Arrays.asList(lines);
    }

    private JTextArea resultsTextArea;
    private JScrollPane lineNumbersScrollArea;
    private JScrollPane calculationsScrollPane;
    private JScrollPane resultsScrollPane;
}
