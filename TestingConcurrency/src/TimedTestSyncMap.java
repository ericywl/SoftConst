import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TimedTestSyncMap {
    private BarrierTimer timer = new BarrierTimer();
    private final Map<Integer, Integer> map;
    private final int pairs, trials;

    public TimedTestSyncMap(int pairs, int trials) {
        this.map = Collections.synchronizedMap(new HashMap<>());
        this.pairs = pairs;
        this.trials = trials;
    }

    void test() {
        try {
            timer.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
