import frink.parser.Frink;

public class Main {
    public static void main(String[] args) {
        Frink interpreter = new Frink();
        // interpreter.setRestrictiveSecurity(true);
        // I want to set the above, but I get a random error about function definition when I do

        try {
            String results = interpreter.parseString("2+2");
            System.out.println(results);
        } catch (frink.errors.FrinkEvaluationException fee) {
            // Do whatever you want with the exception
        }
    }
}
