import java.util.Stack;

public class SafeStack<E> extends Stack<E> {
    public synchronized E pushIfNotFull(E item) {
        while (full()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        push(item);
        notifyAll();
        return item;
    }

    public synchronized E popIfNotEmpty() {
        while (empty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        E item = pop();
        notifyAll();
        return item;
    }

    private boolean full() {
        return size() == capacity();
    }
}
