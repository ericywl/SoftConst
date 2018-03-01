package Russian;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RussianWBTest {
    private int product, m, n;

    public RussianWBTest(int m, int n, int product) {
        this.m = m;
        this.n = n;
        this.product = product;
    }

    @Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {4, 6, 24},         // n > 0, n % 2 == 0
                {25, 7, 175},       // n > 0, n % 2 == 1
                {32, -4, -128},     // n < 0
        });
    }

    @Test
    public void testMultiply() {
        assertEquals(product, Russian.multiply(m, n));
    }
}
