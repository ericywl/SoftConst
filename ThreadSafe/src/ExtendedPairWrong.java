public class ExtendedPairWrong extends NewPair {
	public ExtendedPairWrong (int x, int y) {
		super(x, y);
	}
	
	public synchronized void incrementXY() {
		incrementX();
		incrementY();
	}
}

class NewPair {
	private int x;
	private int y; 
	private final Object lockX = new Object ();
	private final Object lockY = new Object ();
	
	
	public NewPair(int x, int y) { 
		this.x = x;
		this.y = y;
	}
	
	public void incrementX() {
		synchronized (lockX) {
			x++;			
		}
	}
	
	public void incrementY() {
		synchronized (lockY) {
			y++;			
		}
	}
}