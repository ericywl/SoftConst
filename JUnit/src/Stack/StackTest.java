package Stack;//package Week4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StackTest {
    private Stack<Integer> stack;

    // setUp method using @Before syntax
    // @Before methods are run before each test
    @Before
    public void runBeforeEachTest() {
        System.out.println("setting up");
        stack = new Stack<>();
    }

    // tear-down method using @After
    // @After methods are run after each test
    @After
    public void runAfterEachTest() {
        stack = null;
        System.out.println("setting down");
    }

    @Test
    public void testToString() {
        System.out.println("testing");
        stack.push(1);
        stack.push(2);
        assertEquals("{2, 1}", stack.toString());
    }

    @Test
    public void testRepOk1() {
        boolean result = stack.repOK();
        assertEquals(true, result);
    }

    @Test
    public void testRepOk2() {
        stack.push(1);
        boolean result = stack.repOK();
        assertEquals(true, result);
    }

    @Test
    public void testRepOk3() {
        stack.pop();
        boolean result = stack.repOK();
        assertEquals(true, result);
    }

    @Test
    public void testRepOk4() {
        stack.push(1);
        stack.pop();
        boolean result = stack.repOK();
        assertEquals(true, result);
    }
}
