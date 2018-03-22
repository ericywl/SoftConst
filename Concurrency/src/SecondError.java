import java.util.concurrent.atomic.AtomicInteger;

public class SecondError {
	public static AtomicInteger amount = new AtomicInteger(5000);
	
	public static void main(String args[]){   	
		int numOfThreads = 10000;
		Withdrawer[] threads = new Withdrawer[numOfThreads];
	
		for (int i = 0; i < numOfThreads; i++) {
			threads[i] = new Withdrawer();
			threads[i].start();
		}
		
		int totalWithdraw = 0;
		
		try {
			for (int i = 0; i < numOfThreads; i++) {
				threads[i].join();
				totalWithdraw += threads[i].getResult();
			}
		} catch (InterruptedException e) {
			System.out.println("Some thread has not finished");
		}
		
		System.out.print("The result is ... ");
		System.out.print("wait for it ... ");
		System.out.println(totalWithdraw);		
	}
}

class Withdrawer extends Thread {
	private int whatIGot = 0;
	
	public void run () {
		/*if (SecondBlood.amount >= 1000) {
			System.out.println("I am doing something.");			
			SecondBlood.amount = SecondBlood.amount - 1000;
			whatIGot = 1000;
		}*/
		if (SecondError.amount.get() >= 1000) {
			System.out.println("I am doing something.");			
			SecondError.amount.set(SecondError.amount.get()-1000);;
			whatIGot = 1000;
		}		
	}	
	
	public int getResult() {
		return whatIGot;
	}
}

