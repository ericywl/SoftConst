package FuzzingCalc;

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
public class CalcGrammarFuzz {
    private int count;
    private int limit;

    public String generate(int limit) {
        count = 0;
        this.limit = limit;

        return genExpression();
    }

    private String genExpression() {
        int randNum = randomWithRange(1, 3);
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

    private String genTerm() {
        int randNum = randomWithRange(1, 3);
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

    private String genFactor() {
        int randNum = randomWithRange(1, 4);
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

    private String genInteger() {
        int randNum = randomWithRange(1, 2);
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

    private String genDigit() {
        int randNum = randomWithRange(0, 9);
        return String.valueOf(randNum);
    }

    private int randomWithRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max.");
        }

        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }

    public static void main(String[] args) {
        CalcGrammarFuzz fuzz = new CalcGrammarFuzz();
        System.out.println(fuzz.generate(20000));
    }
}
