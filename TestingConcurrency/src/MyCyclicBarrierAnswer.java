import java.util.logging.Level;
import java.util.logging.Logger;

//this class must be thread-safe. why?
public class MyCyclicBarrierAnswer {
	private int count = 0;
	private int newCount;
	private Runnable toRun;
	
	public MyCyclicBarrierAnswer(int count, Runnable toRun) {
		this.count = count;
		this.newCount = count;
		this.toRun = toRun;
	}

	public MyCyclicBarrierAnswer(int count) {
		this.count = count;
	}
	
	//complete the implementation below.
	//hint: use wait(), notifyAll()
	public synchronized void await () {
		newCount--;
		if (newCount > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
            newCount = count;
            notifyAll();
            toRun.run();
        }
    }

    private static class Task implements Runnable {
        private MyCyclicBarrierAnswer barrier;

        public Task(MyCyclicBarrierAnswer barrier) {
            this.barrier = barrier;
        }

        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " is waiting on barrier");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " has crossed the barrier");
            } catch (Exception ex) {
                Logger.getLogger(MyCyclicBarrierAnswer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String args[]) {
        //A CyclicBarrier supports an optional Runnable command that is run once per barrier point,
        //after the last thread in the party arrives, but before any threads are released. This
        //barrier action is useful for updating shared-state before any of the parties continue.
        final MyCyclicBarrierAnswer cb = new MyCyclicBarrierAnswer(3, new Runnable(){
            public void run() {
                //This task will be executed once all thread reaches barrier
                System.out.println("All parties are arrived at barrier, lets play");
            }
        });

        //starting each of thread
        Thread t1 = new Thread(new Task(cb), "Thread 1");
        Thread t2 = new Thread(new Task(cb), "Thread 2");
        Thread t3 = new Thread(new Task(cb), "Thread 3");

        t1.start();
        t2.start();
        t3.start();
    }
}
