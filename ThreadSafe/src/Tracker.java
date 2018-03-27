import java.util.Map;

class test extends Thread {
	Tracker tracker;
	
	public test (Tracker tra) {
		this.tracker = tra;
	}
	
	public void run () {
		Week8.Tracker.MutablePoint loc = tracker.getLocation("somestring");
		loc.x = -1212000;
	}
}

//is this class thread-safe?
public class Tracker {
	//@guarded by ???
	private final Map<String, MutablePoint> locations;
	
	//the reference locations, is it going to be an escape?
	public Tracker(Map<String, MutablePoint> locations) {
		this.locations = locations;
	}
	
	//is this an escape?
	public Map<String, MutablePoint> getLocations () {
		return locations;
	}
	
	//is this an escape?
	public MutablePoint getLocation (String id) {
		MutablePoint loc = locations.get(id);
		return loc;
	}
	
	public void setLocation (String id, int x, int y) {
		MutablePoint loc = locations.get(id);
		
		if (loc == null) {
			throw new IllegalArgumentException ("No such ID: " + id);			
		}
		
		loc.x = x;
		loc.y = y;
	}
	
	//this class is not thread-safe (why?) and keep it unmodified.
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
}
