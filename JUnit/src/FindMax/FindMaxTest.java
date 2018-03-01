package FindMax;

import static org.junit.Assert.*;

import org.junit.Test;

public class FindMaxTest {
    @Test
    public void findMaxTrue() {
        int max = FindMax.max(new int[]{5, 6, 17, 8, 2});
        assertTrue(max == 17);
    }

    @Test
    public void findMaxFalse() {
        int max = FindMax.max(new int[]{5, 6, 8, 2, 17});
        assertTrue(max == 17);
    }

    @Test
    public void findMaxError() {
        FindMax.max(null);
    }
}
