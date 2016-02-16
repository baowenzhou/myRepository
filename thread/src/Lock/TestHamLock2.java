package Lock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 修改计次方法
 * 
 * @see Lock.TestHamLock
 * @author baowenzhou
 *
 */
public class TestHamLock2 {
	
	public static void main(String[] args) {
		
		TestHamLock2 test = new TestHamLock2();
		Ham ham = test.new Ham(20, 5);
		
		try {
			ExecutorService pool = Executors.newCachedThreadPool();
			// 厨师
			for(int i=1;i<=5;i++) {
				pool.execute(test.new Hmaker(ham, i));
			}
			
			ham.getPremake().await();
			System.out.println("营业员们:顾客朋友们,开始营业了!!!!!");

			// 营业员
			for(int i=1;i<=3;i++) {
				pool.execute(test.new Hassistant(ham, i));
			}
			
			pool.shutdown();
			
			ham.getTotalmaterial().await();
			System.out.println("厨师们:没有材料了!!!!!");
			
			ham.getTotalsales().await();
			System.out.println("营业员们:顾客朋友们,汉堡全卖完了!!!!!");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 汉堡
	 * @author baowenzhou
	 *
	 */
	class Ham {
		private Lock lock;
		private Condition full;
		private Condition empty;

		// 可做汉堡的材料数
		private CountDownLatch totalmaterial;
		// 可销售数量
		private CountDownLatch totalsales;
		// 预先做汉堡数量
		private CountDownLatch premake;

		// 最多可做数量
		private int size;
		// 销售数量
		private int sales;
		// 制作数量
		private int production;

		public Ham(int total, int size) {
			this.lock = new ReentrantLock();
			this.full = lock.newCondition();
			this.empty = lock.newCondition();
			

			this.totalmaterial = new CountDownLatch(total);
			this.totalsales = new CountDownLatch(total);
			this.premake = new CountDownLatch(size);

			this.sales = 0;
			this.production = 0;
			this.size = size;
		}
		
		public CountDownLatch getTotalmaterial() {
			return totalmaterial;
		}
		
		public CountDownLatch getTotalsales() {
			return totalsales;
		}
		
		public CountDownLatch getPremake() {
			return premake;
		}

		public int getSales() {
			return sales;
		}

		public void setSales(int sales) {
			this.sales = sales;
		}

		public int getProduction() {
			return production;
		}

		public void setProduction(int production) {
			this.production = production;
		}

		public int getSize() {
			return size;
		}
		
		public Lock getLock() {
			return lock;
		}
		
		public Condition getFull() {
			return full;
		}

		public Condition getEmpty() {
			return empty;
		}

		public int getCansales() {
			return production - sales;
		}
	}
	
	class Hmaker implements Runnable {
		
		private Ham ham;
		private int id;

		public Hmaker(Ham ham, int id) {
			this.ham = ham;
			this.id = id;
		}
		
		private boolean make() {
			ham.getLock().lock();
			try{
				if (ham.getTotalmaterial().getCount() == 0) {
					return false;
				}
				
				if (ham.getCansales() == ham.getSize()) {
					System.out.println("厨师" + this.id + ":"+ "汉堡装不下了！");
					ham.getFull().await();
				} else {
					ham.setProduction(ham.getProduction()+1);
					ham.getTotalmaterial().countDown();
					ham.getPremake().countDown();

					System.out.println("厨师" + this.id + ":"+ "汉堡做好了，（总共做了"+ham.getProduction()+"）");
					ham.getEmpty().signal();
				}
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			finally{
				ham.getLock().unlock();
			}
			
			return true;
		}

		@Override
		public void run() {
			boolean ret = false;
			Random r = new Random();
			while(true) {
				
				ret = this.make();
				if (!ret) break;
				
				try{
					Thread.sleep(r.nextInt(10000));
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	class Hassistant implements Runnable {
		
		private Ham ham;
		private int id;
		
		public Hassistant(Ham ham, int id) {
			this.ham = ham;
			this.id = id;
		}

		private boolean sell() {
			ham.getLock().lock();
			try{
				if (ham.getTotalsales().getCount() == 0) {
					return false;
				}
				
				if (ham.getCansales() == 0) {
					System.out.println("营业员" + this.id + ":"+ "顾客朋友们，请稍微等一下，汉堡还没做好！");
					ham.getEmpty().await();
				} else {
					ham.setSales(ham.getSales()+1);
					ham.getTotalsales().countDown();

					System.out.println("营业员" + this.id + ":"+ "顾客好，汉堡上来了，（总共卖了:"+ ham.getSales() +"个）");
					ham.getFull().signal();
				}
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			finally{
				ham.getLock().unlock();
			}
			
			return true;
		}
		
		@Override
		public void run() {
			boolean ret = false;
			Random r = new Random();

			for(;;) {
				ret = this.sell();
				if (!ret) break;
				
				try{
					Thread.sleep(r.nextInt(5000));
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
		}
	}
}