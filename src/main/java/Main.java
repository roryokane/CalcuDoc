import frink.errors.FrinkConversionException;
import frink.parser.Frink;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(500, 600);

    public static void main(String[] args) {
        JFrame frame = new JFrame("CalcuDoc");
        frame.setContentPane(new DocumentWindow().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setJMenuBar(createMenu());

        frame.pack();
        frame.setSize(DEFAULT_WINDOW_SIZE);

        frame.setVisible(true);
    }

    public static JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);

        JMenu helpMenu = new JMenu("Help");
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
