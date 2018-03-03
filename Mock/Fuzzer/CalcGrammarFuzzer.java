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
    private static int limit = 28000;

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
        int randNum = RandomRange.randomWithRange(1, 3);
        if (count > limit) randNum = 3;

        switch (randNum) {
            case 1:
                count += 2;
                return genExpression() + " + " + genTerm();
            case 2:
                count += 2;
                return genExpression() + " - " + genTerm();
            case 3:
                count++;
                return genTerm();
            default:
                return null;
        }
    }

    private static String genTerm() {
        int randNum = RandomRange.randomWithRange(1, 3);
        if (count > limit) randNum = 3;

        switch (randNum) {
            case 1:
                count += 2;
                return genTerm() + " * " + genFactor();
            case 2:
                count += 2;
                return genTerm() + " / " + genFactor();
            case 3:
                count++;
                return genFactor();
            default:
                return null;
        }
    }

    private static String genFactor() {
        int randNum = RandomRange.randomWithRange(1, 4);
        if (count > limit) randNum = 4;

        switch (randNum) {
            case 1:
                count++;
                return "-" + genInteger();
            case 2:
                count++;
                return "(" + genExpression() + ")";
            case 3:
                count += 2;
                return genInteger() + "." + genInteger();
            case 4:
                count++;
                return genInteger();
            default:
                return null;

        }
    }

    private static String genInteger() {
        int randNum = RandomRange.randomWithRange(1, 2);
        if (count > limit) randNum = 1;

        switch (randNum) {
            case 1:
                count++;
                return genDigit();
            case 2:
                count += 2;
                return genInteger() + genDigit();
            default:
                return null;
        }
    }

    private static String genDigit() {
        int randNum = RandomRange.randomWithRange(0, 9);
        return String.valueOf(randNum);
    }

    public static void main(String[] args) {
        System.out.println(CalcGrammarFuzzer.fuzz());
    }
}
