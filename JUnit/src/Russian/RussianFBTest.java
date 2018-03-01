package Russian;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RussianFBTest {
    private int product, m, n;

    public RussianFBTest(int m, int n, int product) {
        this.m = m;
        this.n = n;
        this.product = product;
    }

    @Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {43, -1, -43},                      // negative integer
                {2347239234.0, 332, 9388956936.0}   // integer overflow
        });
    }

    @Test
    public void testMultiply() {
        assertEquals(product, Russian.multiply(m, n));
    }
}
