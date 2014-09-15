import java.math.BigInteger;
import java.util.ArrayList;


public class CheckPrime implements Runnable{
	
	static boolean isPrime=true;
	BigInteger start;
	BigInteger end;
	BigInteger num;
	
	public CheckPrime(BigInteger num, BigInteger start, BigInteger end)
	{
		this.num = num;
		this.start = start;
		this.end = end;
	}
	
	public static synchronized void setIsPrime(boolean p){
		if(isPrime){
			isPrime = p;
		}
	}
	
	public void run(){
		
		boolean prime = true;
		
		BigInteger i = start;
		while(i.compareTo(end) <= 0 && prime == true)
		{
			//num!=i  since the division of the original might not be equal so there might have cases like 5%5
			if(num.mod(i).equals(BigInteger.ZERO) && !num.equals(i))
			{
				prime = false;
			}
			else prime = true;
			i = i.add(BigInteger.ONE);

		}
		setIsPrime(prime);
	}
	
	public static BigInteger getSqrt(BigInteger n) {
		  BigInteger a = BigInteger.ONE;
		  BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		  
		  while(b.compareTo(a) >= 0) {
			  
		    BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
		    if(mid.multiply(mid).compareTo(n) > 0) b = mid.subtract(BigInteger.ONE);
		    else a = mid.add(BigInteger.ONE);
		    
		  }
		  return a.subtract(BigInteger.ONE);
	}
	
	public static void checkIsPrime(BigInteger num, int nThread)
	{
		BigInteger end = getSqrt(num);
		//distribution on each thread
		BigInteger increment = end.divide(BigInteger.valueOf(nThread));
		
		BigInteger start = new BigInteger("2");
		
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		
		for(int i = 0; i < nThread; i++)
		{
			BigInteger temp  = start.add(increment);
			threadList.add(new Thread(new CheckPrime(num, start, temp)));
			start = start.add(increment);
		}
		
		
		//start and joining threads
		for(int i = 0; i < nThread; i++){
			threadList.get(i).start();
		}
		
		for(int i = 0; i < nThread; i++){
			try {
				threadList.get(i).join();
				System.out.println("Thread "+ i +" Finished.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[])
	{
		//input number
		BigInteger num = new BigInteger("33251122313");
		
		//# of threads
		int nThread = 1;
		
		long start = System.currentTimeMillis();
		
		checkIsPrime(num, nThread);
		
		long end = System.currentTimeMillis();
		
		System.out.print(CheckPrime.isPrime? "Prime.":"Not Prime.");
		
		System.out.println(" Number: " + num + ". Thread #: " + nThread+". RunTime: "+ (end-start) + " milliseconds.");
	}
}
