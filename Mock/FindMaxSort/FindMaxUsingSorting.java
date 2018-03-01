public class FindMaxUsingSorting {
    public static int findMax (int[] inputArr, Sorter sorter) {
    	int[] result = sorter.sort(inputArr);
		return result[result.length - 1];
    }
}
