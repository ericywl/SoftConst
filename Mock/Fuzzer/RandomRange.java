public class RandomRange {
    public static int randomWithRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max.");
        }

        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }
}
