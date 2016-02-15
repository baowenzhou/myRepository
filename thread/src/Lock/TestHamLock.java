package Lock;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @see Synchronized.TestHam
 * @author baowenzhou
 *
 */
public class TestHamLock {
	
	public static void main(String[] args) {
		TestHamLock test = new TestHamLock();
		Ham ham = test.new Ham();
		ExecutorService pool = Executors.newCachedThreadPool();
		// 厨师
		for(int i=1;i<=5;i++) {
			pool.execute(test.new Hmaker(ham, i));
		}

		// 营业员
		for(int i=1;i<=2;i++) {
			pool.execute(test.new Hassistant(ham, i));
		}
		
		pool.shutdown();
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
		private int totalmaterial;
		// 最多可做数量
		private int size;
		
		// 销售数量
		private volatile int sales;
		// 制作数量
		private volatile int production;

		public Ham() {
			this.lock = new ReentrantLock();
			this.full = lock.newCondition();
			this.empty = lock.newCondition();
			
			this.totalmaterial = 20;
			this.sales = 0;
			this.production = 3;
			this.size = 5;
		}
		
		public int getTotalmaterial() {
			return totalmaterial;
		}

		public void setTotalmaterial(int totalmaterial) {
			this.totalmaterial = totalmaterial;
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

		public void setSize(int size) {
			this.size = size;
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
				if (ham.getProduction() == ham.getTotalmaterial()) {
					System.out.println("厨师" + this.id + ":"+ "没有材料了！");
					return false;
				}
				
				if (ham.getProduction() - ham.getSales() == ham.getSize()) {
					System.out.println("厨师" + this.id + ":"+ "汉堡装不下了！");
					ham.getFull().await();
				} else {
					ham.setProduction(ham.getProduction()+1);
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
				
				try{
					Thread.sleep(r.nextInt(3000));
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				
				ret = this.make();
				if (!ret) break;
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
				if (ham.getProduction() == ham.getTotalmaterial() 
						&& ham.getProduction() == ham.getSales()) {
					System.out.println("营业员" + this.id + ":"+ "顾客朋友们,汉堡全卖完了！！");
					return false;
				}
				
				if (ham.getProduction() - ham.getSales() == 0) {
					System.out.println("营业员" + this.id + ":"+ "顾客朋友们，请稍微等一下，汉堡还没做好！");
					ham.getEmpty().await();
				} else {
					ham.setSales(ham.getSales()+1);
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
					Thread.sleep(r.nextInt(3000));
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
	
}