import java.util.HashSet;
import java.util.Set;

public class DLExampleFixed {

}

class TaxiFixed {
    private Point location, destination;
    private final DispatcherFixed dispatcher;

    public TaxiFixed(DispatcherFixed dispatcher) {
        this.dispatcher = dispatcher;
    }

	public synchronized Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
    	boolean reachedDestination;
    	synchronized (this) {
            this.location = location;
            reachedDestination = location.equals(destination);
    	}

        if (reachedDestination) {
            dispatcher.notifyAvailable(this);
        }
    }

    public synchronized Point getDestination() {
        return destination;
    }
}

class DispatcherFixed {
    private final Set<TaxiFixed> taxis;
    private final Set<TaxiFixed> availableTaxis;

    public DispatcherFixed() {
        taxis = new HashSet<>();
        availableTaxis = new HashSet<>();
    }

    public synchronized void notifyAvailable(TaxiFixed taxi) {
        availableTaxis.add(taxi); //is this a problem?
    }

    public Image getImage() {
    	Set<TaxiFixed> copy;
    	synchronized (this) {
    		copy = new HashSet<>(taxis);
    	}

        Image image = new Image();
        for (TaxiFixed t : copy)
            image.drawMarker(t.getLocation());
        return image;
    }
}

class Image {
    public void drawMarker(Point p) {
    }
}

class Point {
}

