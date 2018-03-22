public class DLExampleTest {
    public static void main(String[] args) {
        /* Deadlock occurs when the dispatcher is trying to get the location of the taxi while the taxi is
        * trying to set its location. The dispatcher tries to obtain the taxi's lock while
        * the taxi tries to obtain the dispatcher's lock, resulting in a deadlock */
        Point destination = new Point();
        Dispatcher dispatcher = new Dispatcher();
        Taxi taxi = new Taxi(dispatcher);
        dispatcher.addTaxi(taxi);

        new DispatcherThread(dispatcher).start();
        new TaxiThread(taxi, destination).start();
    }
}

class DispatcherThread extends Thread {
    private Dispatcher dispatcher;

    DispatcherThread(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        System.out.println("Getting image...");
        this.dispatcher.getImage();
        System.out.println("Finished getting image.");
    }
}

class TaxiThread extends Thread {
    private Taxi taxi;
    private Point destination;

    TaxiThread(Taxi taxi, Point destination) {
        this.taxi = taxi;
        this.destination = destination;
        this.taxi.setDestination(destination);
    }

    @Override
    public void run() {
        System.out.println("Setting location...");
        this.taxi.setLocation(destination);
        System.out.println("Finished setting location.");
    }
}