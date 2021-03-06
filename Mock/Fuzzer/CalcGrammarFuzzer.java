import java.util.Random;

/**
 * Grammar
 * <p>
 * S := Expr
 * Expr := Expr + Term | Expr - Term | Term
 * Term := Term * Factor | Term / Factor | Factor
 * Factor := -Integer | (Expr) | Integer | Integer.Integer
 * Integer := Digit | IntegerDigit
 * Digit := [0-9]
 */
public class CalcGrammarFuzzer {
    private static int count;
    private static int limit = 20000;
    private static Random random = new Random();

    public static String fuzz() {
        count = 0;
        return genExpression();
    }

    public static String fuzz(int newLimit) {
        count = 0;
        limit = newLimit;
        return genExpression();
    }

    private static String genExpression() {
        int randNum = random.nextInt(3);
        if (count > limit) randNum = 2;

        switch (randNum) {
            case 0:
                count += 2;
                return genExpression() + " + " + genTerm();
            case 1:
                count += 2;
                return genExpression() + " - " + genTerm();
            case 2:
                count++;
                return genTerm();
            default:
                return null;
        }
    }

    private static String genTerm() {
        int randNum = random.nextInt(3);
        if (count > limit) randNum = 2;

        switch (randNum) {
            case 0:
                count += 2;
                return genTerm() + " * " + genFactor();
            case 1:
                count += 2;
                return genTerm() + " / " + genFactor();
            case 2:
                count++;
                return genFactor();
            default:
                return null;
        }
    }

    private static String genFactor() {
        int randNum = random.nextInt(4);
        if (count > limit) randNum = 3;

        switch (randNum) {
            case 0:
                count++;
                return "-" + genInteger();
            case 1:
                count++;
                return "(" + genExpression() + ")";
            case 2:
                count += 2;
                return genInteger() + "." + genInteger();
            case 3:
                count++;
                return genInteger();
            default:
                return null;

        }
    }

    private static String genInteger() {
        int randNum = random.nextInt(2);
        if (count > limit) randNum = 0;

        switch (randNum) {
            case 0:
                count++;
                return genDigit();
            case 1:
                count += 2;
                return genInteger() + genDigit();
            default:
                return null;
        }
    }

    private static String genDigit() {
        int randNum = random.nextInt(10);
        return String.valueOf(randNum);
    }

    public static void main(String[] args) {
        System.out.println("Generating based on grammar...");
        System.out.println("Result: " + CalcGrammarFuzzer.fuzz());
    }
}
