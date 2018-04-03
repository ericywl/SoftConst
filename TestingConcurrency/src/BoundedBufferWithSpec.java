import java.util.concurrent.Semaphore;

//this class is thread safe
public class BoundedBufferWithSpec<E> {
    //class invariant: availableItems + availableSpaces = items.length
    //class invariant: availableItems >= 0
    //class invariant: avaliableSpaces > = 0
    //class invariant: putPosition >= 0 && putPosition < items.length
    //class invariant: takePosition >= 0 && takePosition < items.length
    private final Semaphore availableItems, availableSpaces;
    private final E[] items;    //guarded by this
    private int putPosition = 0, takePosition = 0; //guarded by this

    public BoundedBufferWithSpec(int capacity) {
        availableItems = new Semaphore(0);
        availableSpaces = new Semaphore(capacity);
        items = (E[]) new Object[capacity];
    }

    // post-condition: return true if and only if availableSpaces = items.length
    public boolean isEmpty() {
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return availableSpaces.availablePermits() == 0;
    }

    //pre-condition: items is not null
    //post-condition: availableSpaces'.availablePermits() = availableSpaces.availablePermits() - 1
    //post-condition: availableItems'.availablePermits() = availableItems.availablePermits() + 1
    //post-condition: putPosition' = putPosition + 1 if putPosition < items.length-1; 0 otherwise
    public void put(E x) throws InterruptedException {
        availableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract();
        availableSpaces.release();
        return item;
    }

    private synchronized void doInsert(E x) {
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length) ? 0 : i;
    }

    private synchronized E doExtract() {
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length) ? 0 : i;
        return x;
    }
}
