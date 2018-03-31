import java.util.Vector;

@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
public class FirstExampleFixed {
    public static Object getLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }

    public static boolean contains(Vector list, Object obj) {
        synchronized (list) {
            for (Object aList : list) {
                if (aList.equals(obj)) {
                    return true;
                }
            }

            return false;
        }
    }

}
