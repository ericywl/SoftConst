package QuickSort;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class QuickSortTest {
    private QuickSort quickSort;
    private int[] array, result;

    public QuickSortTest(int[] array, int[] result) {
        this.array = array;
        this.result = result;
    }

    @Parameters
    public static Collection parameters() {
        return Arrays.asList(new int[][][]{
                {{1, 3, 2}, {1, 2, 3}},
                {{2, 8, 3, 5, 0}, {0, 2, 3, 5, 8}}
        });
    }

    @Before
    public void runBeforeEachTest() {
        System.out.println("Setting up");
        quickSort = new QuickSort();
    }

    @After
    public void runAfterEachTest() {
        System.out.println("Setting down");
        quickSort = null;
    }

    @Test
    public void sortTest() {
        quickSort.sort(array);
        assertEquals(Arrays.toString(result), Arrays.toString(quickSort.getArray()));
    }
}
