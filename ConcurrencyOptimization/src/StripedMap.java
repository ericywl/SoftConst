public class StripedMap {
    // synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
    private static final int N_LOCKS = 16;
    private final Node[] buckets;
    private final Object[] locks;

    public StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[N_LOCKS];

        for (int i = 0; i < N_LOCKS; i++) {
            locks[i] = new Object();
        }
    }

    public Object put(Object key, Object value) {
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]) {
            for (Node m = buckets[hash]; m != null; m = m.next)
                if (m.key.equals(key)) {
                    m.value = value;
                    return m.value;
                }
            buckets[hash] = new Node(key, value, buckets[hash]);
        }

        return null;
    }

    public Object get(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]) {
            for (Node m = buckets[hash]; m != null; m = m.next)
                if (m.key.equals(key)) {
                    return m.value;
                }
        }

        return null;
    }

    public void clear() {
        for (int i = 0; i < locks.length; i++) {
            synchronized (locks[i]) {
                for (int j = i; j < buckets.length; j += N_LOCKS) {
                    buckets[j] = null;
                }
            }
        }
    }

    public int size() {
        int count = 0;
        for (int i = 0; i < locks.length; i++) {
            synchronized (locks[i]) {
                for (int j = i; j < buckets.length; j += N_LOCKS) {
                    for (Node m = buckets[j]; m != null; m = m.next)
                        count++;
                }
            }
        }

        return count;
    }

    private int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    class Node {
        Node next;
        Object key;
        Object value;

        Node(Object key, Object value, Node next) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}

