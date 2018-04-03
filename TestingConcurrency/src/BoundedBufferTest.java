import static org.junit.Assert.*;

import java.util.Random;

import org.junit.*;

public class BoundedBufferTest {
    private static final long LOCKUP_DETECT_TIMEOUT = 1000;

    @Test
    public void testIsEmptyWhenConstructued() {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    @Test
    public void testIsFullAfterPuts() throws InterruptedException {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);

        Runnable task = () -> {
            try {
                bb.put((new Random()).nextInt());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }

    @Test
    public void testIsEmptyAfterTakes() throws InterruptedException {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++)
            bb.put(1);

        assertTrue(bb.isFull());
        Runnable task = () -> {
            try {
                int num = bb.take();
                assertTrue(num == 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    @Test
    public void testTakeBlocksWhenEmpty() {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        Thread taker = new Thread(() -> {
            try {
                int unused = bb.take();
                fail();
            } catch (InterruptedException success) {
                // System.out.println("Exception caught!");
            }
        });

        try {
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive()); //the taker should not be alive for some time
        } catch (Exception unexpected) {
            fail();
        }
    }

    @Test
    public void testPutBlocksWhenFull() throws InterruptedException {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++)
            bb.put(i);

        Thread putter = new Thread(() -> {
            try {
                bb.put(11);
                fail();
            } catch (InterruptedException success) {
                // System.out.println("Exception caught!");
            }
        });

        try {
            putter.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            putter.interrupt();
            putter.join();
            assertFalse(putter.isAlive());
        } catch (Exception unexpected) {
            fail();
        }
    }
}
