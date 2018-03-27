public class MyStackThreadSafeComplete {
    private final int maxSize;
    private long[] stackArray; // guarded by "this"
    private int top; // invariant: top < stackArray.length && top >= -1; guarded by "this"

    // pre-condition: s > 0
    // post-condition: maxSize == s && top == -1 && stackArray != null
    public MyStackThreadSafeComplete(int s) { // Do we need "synchronized" for the constructor?
        maxSize = s;
        stackArray = new long[maxSize];
        top = -1;
    }

    // pre-condition: top < stackArray.length
    // pose-condition: element added to the top
    public synchronized void push(long j) {
        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stackArray[++top] = j;
        notifyAll();
    }

    // pre-condition: top >= 0
    // post-condition: the top element is removed
    public synchronized long pop() {
        long toReturn;

        while (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        toReturn = stackArray[top--];
        notifyAll();
        return toReturn;
    }

    // pre-condition: top >= 0
    // post-condition: the stack is un-changed, the return value is the top element
    public synchronized long peek() {
        long toReturn;

        while (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        toReturn = stackArray[top];
        notifyAll();
        return toReturn;
    }

    // pre-condition: true
    // post-condition: the elements are un-changed, the return value is true iff the stack is empty.
    public synchronized boolean isEmpty() {
        return (top == -1);
    }

    // pre-condition: true
    // post-condition: the elements are un-changed, the return value is true iff the stack is full.
    public synchronized boolean isFull() {
        return (top == maxSize - 1);
    }

}