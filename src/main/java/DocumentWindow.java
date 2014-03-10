import com.google.common.base.Joiner;
import frink.errors.FrinkEvaluationException;
import frink.errors.FrinkException;
import frink.parser.Frink;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;

public class DocumentWindow {
    public JPanel root;
    private JTextArea calculationsTextArea;
    private JTextArea lineNumbersTextArea;
    private JTextArea resultsTextArea;
    private JScrollPane lineNumbersScrollArea;
    private JScrollPane calculationsScrollPane;
    private JScrollPane resultsScrollPane;

    public DocumentWindow() {
        calculationsTextArea.getDocument().addDocumentListener(new CalculationDocumentChangeListener());
        prefillSampleCalculations();
        updateResultsAndLineNumbers();
    }

    private void prefillSampleCalculations() {
        calculationsTextArea.setText("height = 5 feet + 9 inches -> \"feet\"\npeople_in_room = 53\nsize_of_stack = height * people_in_room");
    }

    public void focusCalculationsArea() {
        calculationsTextArea.grabFocus();
    }

    private void populateLineNumbers() {
        int numLines = getCalculationsLines().size();

        List<String> lineLabels = new ArrayList<String>();
        for (int i = 1; i <= numLines; i++) {
            lineLabels.add(Integer.toString(i));
        }
        String lineNumbersText = Joiner.on("\n").join(lineLabels);

        lineNumbersTextArea.setText(lineNumbersText);
    }

    private List<String> getCalculationsLines() {
        String[] lines = getCalculationsText().split("\r\n|\r|\n");
        return Arrays.asList(lines);
    }

    private String getCalculationsText() {
        return calculationsTextArea.getText();
    }

    private class CalculationDocumentChangeListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            updateResultsAndLineNumbers();
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            updateResultsAndLineNumbers();
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            updateResultsAndLineNumbers();
        }
    }

    private void updateResultsAndLineNumbers() {
        // TODO do in a separate thread. Register an event to be either run immediately or right after the current event (overwriting other pending similar events).
        populateLineNumbers();
        recalculateAllResults();
    }

    private void recalculateAllResults() {
        final List<String> calculationsLines = getCalculationsLines();
        List<String> resultLines = new ArrayList<String>();
        Frink interpreter = new Frink();

        for (String calculationsLine : calculationsLines) {
            String result;
            try {
                result = interpreter.parseString(calculationsLine);
            } catch (FrinkException e) {
                result = e.toString();
            }
            resultLines.add(result);
        }

        String resultsText = Joiner.on("\n").join(resultLines);
        resultsTextArea.setText(resultsText);
    }
}
