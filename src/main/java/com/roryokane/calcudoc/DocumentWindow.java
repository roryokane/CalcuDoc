package com.roryokane.calcudoc;

import com.google.common.base.Joiner;
import frink.errors.FrinkEvaluationException;
import frink.errors.FrinkException;
import frink.parser.Frink;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;
import java.util.concurrent.Executors;

public class DocumentWindow {
    public JPanel root;
    private JTextArea calculationsTextArea;
    private JTextArea lineNumbersTextArea;
    private JTextArea resultsTextArea;
    private JScrollPane lineNumbersScrollArea;
    private JScrollPane calculationsScrollPane;
    private JScrollPane resultsScrollPane;
    private final CurrentAndNextOverwritingCommandScheduler resultsUpdateScheduler;
    private final CurrentAndNextOverwritingCommandScheduler lineNumbersUpdateScheduler;

    public DocumentWindow() {
        resultsUpdateScheduler = new CurrentAndNextOverwritingCommandScheduler();
        lineNumbersUpdateScheduler = new CurrentAndNextOverwritingCommandScheduler();
        Executors.newSingleThreadExecutor().execute(resultsUpdateScheduler);
        Executors.newSingleThreadExecutor().execute(lineNumbersUpdateScheduler);

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
        forceLineNumbersAreaResize();
    }

    private void forceLineNumbersAreaResize() {
        root.updateUI();
    }

    private List<String> getCalculationsLines() {
        String[] lines = splitStringIntoLines(getCalculationsText());
        return Arrays.asList(lines);
    }

    private String[] splitStringIntoLines(String string) {
        return splitStringIntoLines(string, true);
    }

    private String[] splitStringIntoLines(String string, boolean allowTrailingBlankLines) {
        final int limit = allowTrailingBlankLines ? -1 : 0;
        return string.split("\r\n|\r|\n", limit);
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
        lineNumbersUpdateScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                populateLineNumbers();
            }
        });
        resultsUpdateScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                recalculateAllResults();
            }
        });
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
