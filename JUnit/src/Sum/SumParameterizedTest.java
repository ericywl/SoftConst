package Sum;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import java.util.*;

// sudiptac: include this for running parameterized tests
@RunWith(Parameterized.class)
public class SumParameterizedTest {
    private int sum, a, b;

    // classic constructor
    public SumParameterizedTest(int sum, int a, int b) {
        this.sum = sum;
        this.a = a;
        this.b = b;
    }

    @Parameters
    // sudiptac: decide the list of parameters to be sent to the class
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {1, 0, 0},
                {2, 1, 1},
                {3, 4, -1}
        });
    }

    @Test
    // sudiptac: This test is invoked for each of the parameter sent via parameters()
    public void additionTest() {
        assertEquals(sum, Sum.sum(a, b));
    }
}
