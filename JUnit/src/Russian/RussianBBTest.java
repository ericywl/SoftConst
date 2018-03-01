package Russian;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class RussianBBTest {
    private int product, m, n;

    public RussianBBTest(int m, int n, int product) {
        this.m = m;
        this.n = n;
        this.product = product;
    }

    @Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {4, 6, 24},                 // positive single-digit integers
                {237, 14, 3318},            // positive multi-digit integers
                {-4, -2, 8},                // negative single-digit integers
                {-23, -68, 1564},           // negative multi-digit integers
                {-174, 84, -14616},         // mixed integers
                {0, 23, 0},                 // zero
                {1, 338, 338},              // one
                {-1, 539, -539},            // negative one
                {1, MAX_VALUE, MAX_VALUE},  // max integer
                {1, MIN_VALUE, MIN_VALUE}   // min integer
        });
    }

    @Test
    public void testMultiply() {
        assertEquals(product, Russian.multiply(m, n));
    }
}
