package com.roryokane.calcudoc;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import frink.errors.FrinkException;
import frink.parser.Frink;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DocumentWindow {
    public JPanel root;
    private JTextArea calculationsTextArea;
    private JTextArea lineNumbersTextArea;
    private JTextArea resultsTextArea;
    private JScrollPane lineNumbersScrollArea;
    private JScrollPane calculationsScrollPane;
    private JScrollPane resultsScrollPane;
    private final Executor resultsUpdateScheduler;
    private final Executor lineNumbersUpdateScheduler;

    public DocumentWindow() {
        resultsUpdateScheduler = Executors.newSingleThreadExecutor();
        lineNumbersUpdateScheduler = Executors.newSingleThreadExecutor();

        calculationsTextArea.getDocument().addDocumentListener(new CalculationDocumentChangeListener());
        prefillSampleCalculations();
        updateResultsAndLineNumbers();
    }

    private void prefillSampleCalculations() {
        calculationsTextArea.setText(getDefaultCalculations());
    }

    private String getDefaultCalculations() {
        try {
            return readTextFileResourceAsString("introduction.calcudoc");
        } catch (IOException e) {
            return "";
        }
    }

    private String readTextFileResourceAsString(String resourceName) throws IOException {
        // written with the help of:
        // http://stackoverflow.com/q/3861989/578288
        // http://stackoverflow.com/a/3238954/578288
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
            String contentsString = CharStreams.toString(inputStreamReader);
            Closeables.closeQuietly(inputStream);
            return contentsString;
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
        String lineNumbersText = StringLinesConversion.joinLinesIntoString(lineLabels);

        lineNumbersTextArea.setText(lineNumbersText);
        forceLineNumbersAreaResize();
    }

    private void forceLineNumbersAreaResize() {
        root.updateUI();
    }

    private List<String> getCalculationsLines() {
        String[] lines = StringLinesConversion.splitStringIntoLines(getCalculationsText());
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
        lineNumbersUpdateScheduler.execute(new Runnable() {
            @Override
            public void run() {
                populateLineNumbers();
            }
        });
        resultsUpdateScheduler.execute(new Runnable() {
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

        String resultsText = StringLinesConversion.joinLinesIntoString(resultLines);
        resultsTextArea.setText(resultsText);
    }
}
