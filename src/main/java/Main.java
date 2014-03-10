import frink.errors.FrinkConversionException;
import frink.parser.Frink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Main {
    public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(500, 600);

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // TODO set up application bundle: http://stackoverflow.com/q/8955638/578288

        JFrame frame = new JFrame("CalcuDoc");
        final DocumentWindow documentWindow = new DocumentWindow();
        frame.setContentPane(documentWindow.root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setJMenuBar(createMenu());

        frame.pack();
        frame.setSize(DEFAULT_WINDOW_SIZE);

        documentWindow.focusCalculationsArea();

        frame.setVisible(true);
    }

    public static JMenuBar createMenu() {
        JMenuItem item;

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        item = new JMenuItem("Open…");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.META_MASK));
        fileMenu.add(item);
        JMenu openRecent = new JMenu("Open Recent"); // TODO save for later and update live
        fileMenu.add(openRecent);
        fileMenu.addSeparator();
        item = new JMenuItem("Save…");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK));
        fileMenu.add(item);
        item = new JMenuItem("Save As…");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK + ActionEvent.SHIFT_MASK));
        fileMenu.add(item);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        item = new JMenuItem("Cut");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.META_MASK));
        editMenu.add(item);
        item = new JMenuItem("Copy");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.META_MASK));
        editMenu.add(item);
        item = new JMenuItem("Paste");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.META_MASK));
        editMenu.add(item);
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);

        JMenu viewMenu = new JMenu("View");
        item = new JMenuItem("Decrease Text Size");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.META_MASK));
        viewMenu.add(item);
        item = new JMenuItem("Increase Text Size");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.META_MASK));
        viewMenu.add(item);
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(viewMenu);

        JMenu helpMenu = new JMenu("Help");
        item = new JMenuItem("Open Example Document");
        helpMenu.add(item);
        item = new JMenuItem("Open Frink Language Documentation");
        helpMenu.add(item);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private static void testFrinkInterpreter() {
        Frink interpreter = new Frink();
        // interpreter.setRestrictiveSecurity(true);
        // I want to set the above, but I get a random error about function definition when I do

        try {
            String results = interpreter.parseString("2 inch+2 inches");
            System.out.println(results);
        } catch (frink.errors.FrinkEvaluationException fee) {
            System.out.println("ill-formed expression");
//        } catch (FrinkConversionException ex) {
//            System.out.println("can't convert");
        }
    }
}
