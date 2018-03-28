import java.util.HashMap;
import java.util.Map;

class TestTracker extends Thread {
	private Tracker tracker;
	
	public TestTracker(Tracker tracker) {
		this.tracker = tracker;
	}
	
	public void run () {
		MutablePoint loc = tracker.getLocation("somestring");
		loc.x = -1212000;
	}

    public static void main(String[] args) {
	    Map<String, MutablePoint> map = new HashMap<>();
	    map.put("somestring", new MutablePoint(1, 2));
	    Tracker tracker = new Tracker(map);
        TestTracker test = new TestTracker(tracker);

        test.run();
        try {
            test.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MutablePoint loc = tracker.getLocation("somestring");
        System.out.println(loc.x);
    }
}

public class Tracker {
	// @guarded by this
	private final Map<String, MutablePoint> locations;
	
	public Tracker(Map<String, MutablePoint> locations) {
        this.locations = DeepCopy.deepCopyMap(locations);
	}
	
	public synchronized Map<String, MutablePoint> getLocations () {
        return DeepCopy.deepCopyMap(locations);
	}

	public synchronized MutablePoint getLocation (String id) {
		MutablePoint location = locations.get(id);
		return new MutablePoint(location);
	}
	
	public void setLocation (String id, int x, int y) {
		MutablePoint loc = locations.get(id);
		
		if (loc == null) {
			throw new IllegalArgumentException ("No such ID: " + id);			
		}
		
		loc.x = x;
		loc.y = y;
	}

	static class DeepCopy {
	    static Map<String, MutablePoint> deepCopyMap(Map<String, MutablePoint> oldMap) {
	        Map<String, MutablePoint> newMap = new HashMap<>();
            for (String str : oldMap.keySet()) {
                MutablePoint location = oldMap.get(str);
                MutablePoint newLocation = new MutablePoint(location);
                newMap.putIfAbsent(str, newLocation);
            }

            return newMap;
        }
    }
}

// this class is not thread-safe (why?) and keep it unmodified.
class MutablePoint {
    public int x, y;

    public MutablePoint (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MutablePoint (MutablePoint p) {
        this.x = p.x;
        this.y = p.y;
    }
}
