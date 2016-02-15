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
		// ��ʦ
		for(int i=1;i<=5;i++) {
			pool.execute(test.new Hmaker(ham, i));
		}

		// ӪҵԱ
		for(int i=1;i<=2;i++) {
			pool.execute(test.new Hassistant(ham, i));
		}
		
		pool.shutdown();
	}
	
	/**
	 * ����
	 * @author baowenzhou
	 *
	 */
	class Ham {
		private Lock lock;
		private Condition full;
		private Condition empty;

		// ���������Ĳ�����
		private int totalmaterial;
		// ����������
		private int size;
		
		// ��������
		private volatile int sales;
		// ��������
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
					System.out.println("��ʦ" + this.id + ":"+ "û�в����ˣ�");
					return false;
				}
				
				if (ham.getProduction() - ham.getSales() == ham.getSize()) {
					System.out.println("��ʦ" + this.id + ":"+ "����װ�����ˣ�");
					ham.getFull().await();
				} else {
					ham.setProduction(ham.getProduction()+1);
					System.out.println("��ʦ" + this.id + ":"+ "���������ˣ����ܹ�����"+ham.getProduction()+"��");
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
					System.out.println("ӪҵԱ" + this.id + ":"+ "�˿�������,����ȫ�����ˣ���");
					return false;
				}
				
				if (ham.getProduction() - ham.getSales() == 0) {
					System.out.println("ӪҵԱ" + this.id + ":"+ "�˿������ǣ�����΢��һ�£�������û���ã�");
					ham.getEmpty().await();
				} else {
					ham.setSales(ham.getSales()+1);
					System.out.println("ӪҵԱ" + this.id + ":"+ "�˿ͺã����������ˣ����ܹ�����:"+ ham.getSales() +"����");
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