import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

public class FindMaxTest {
    @Test
    public void testFindMaxSort() {
        // initialize mockery and sorter
        Mockery context = new JUnit4Mockery();
        final Sorter sorter = context.mock(Sorter.class);

        // add expectations
        int[] arr = new int[]{3, 4, 8, 1};
        context.checking(new Expectations() {
            {
                oneOf(sorter).sort(arr);
                will(returnValue(new int[]{1, 3, 4, 8}));
            }
        });

        // call find max and check if any expectation is violated
        FindMaxUsingSorting.findMax(arr, sorter);
        context.assertIsSatisfied();
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindMaxTest");
    }
}
