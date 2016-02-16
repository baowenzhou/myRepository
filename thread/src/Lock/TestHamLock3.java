package Lock;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ���ӿͻ��߳�
 * 
 * @see Lock.TestHamLock2
 * @author baowenzhou
 *
 */
public class TestHamLock3 {

	public static void main(String[] args) {

		TestHamLock3 test = new TestHamLock3();
		Ham ham = test.new Ham(10, 5);
		List<Future<?>> tasks = new LinkedList<Future<?>>();

		try {
			ExecutorService pool = Executors.newCachedThreadPool();

			System.out.println("+++��ʦ��:��ҵǰ�������㺺��!!!!!");

			// ��ʦ
			for (int i = 1; i <= 5; i++) {
				pool.execute(test.new Hmaker(ham, i));
			}

			ham.getPremake().await();
			System.out.println("---ӪҵԱ��:�˿�������,��ʼӪҵ��!!!!!");

			// ӪҵԱ
			for (int i = 1; i <= 3; i++) {
				// tasks.add(pool.submit(test.new Hassistant(ham, i)));
				pool.submit(test.new Hassistant(ham, i));
			}

			// �˿�
			tasks.add(pool.submit(test.new Hcustom(ham)));

			ham.getTotalmaterial().await();
			System.out.println("+++��ʦ��:û�в�����!!!!!");

			ham.getTotalsales().await();
			System.out.println("---ӪҵԱ��:�˿�������,����ȫ������!!!!!");

			for (Future<?> task : tasks) {
				task.cancel(true);
			}

			pool.shutdown();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����
	 * 
	 * @author baowenzhou
	 *
	 */
	class Ham {
		private Lock lock;
		private Lock lockbuy;

		private Condition full;
		private Condition empty;
		private Condition buy;
		private Condition sell;

		// ���������Ĳ�����
		private CountDownLatch totalmaterial;
		// ����������
		private CountDownLatch totalsales;
		// Ԥ������������
		private CountDownLatch premake;

		// ����������
		private int size;
		// ��������
		private int sales;
		// ��������
		private int production;

		public Ham(int total, int size) {
			this.lock = new ReentrantLock();
			this.lockbuy = new ReentrantLock();

			this.full = lock.newCondition();
			this.empty = lock.newCondition();
			this.buy = lockbuy.newCondition();
			this.sell = lockbuy.newCondition();

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

		public Lock getLockbuy() {
			return lockbuy;
		}

		public Condition getFull() {
			return full;
		}

		public Condition getEmpty() {
			return empty;
		}

		public Condition getBuy() {
			return buy;
		}

		public Condition getSell() {
			return sell;
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

		private void say(String content) {
			System.out.println("+++��ʦ" + this.id + ":" + content);
		}

		private boolean make() {
			boolean haslock = ham.getLock().tryLock();
			if (!haslock) {
				// System.out.println("��ʦ" + this.id + ":"+ "����ʧ�ܣ�");
				return true;
			}
			// System.out.println("��ʦ" + this.id + ":"+ "�����ɹ���");

			try {

				if (ham.getTotalmaterial().getCount() == 0) {
					this.say("û�����°��ˣ�");
					return false;
				}

				if (ham.getCansales() == ham.getSize()) {
					this.say("����װ�����ˣ�");
					ham.getFull().await();
				} else {
					ham.setProduction(ham.getProduction() + 1);
					this.say("���������ˣ����ܹ�����" + ham.getProduction() + "��������"
							+ ham.getCansales() + "��");

					ham.getTotalmaterial().countDown();
					ham.getPremake().countDown();

					ham.getEmpty().signal();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				ham.getLock().unlock();
			}

			return true;
		}

		@Override
		public void run() {
			boolean ret = false;
			Random r = new Random();
			while (true) {

				ret = this.make();
				if (!ret)
					break;

				try {
					Thread.sleep(r.nextInt(5000));
				} catch (InterruptedException e) {
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

		private void say(String content) {
			System.out.println("---ӪҵԱ" + this.id + ":" + content);
		}

		private boolean consume() {
			boolean haslock = false;

			haslock = ham.getLock().tryLock();
			if (!haslock) {
				// System.out.println("ӪҵԱ" + this.id + ":"+ "����ʧ�ܣ�");
				return true;
			}
			// System.out.println("ӪҵԱ" + this.id + ":"+ "�����ɹ���");

			try {

				if (ham.getCansales() == 0) {
					this.say("�˿������ǣ�����΢��һ�£�������û���ã�");
					ham.getEmpty().await();
				} else {
					ham.setSales(ham.getSales() + 1);
					this.say("�˿ͺã����������ˣ����ܹ�����:" + ham.getSales() + "��������"
							+ ham.getCansales() + "��");

//					this.say("�ͷ�����");
					ham.getFull().signal();
					
					ham.getTotalsales().countDown();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				ham.getLock().unlock();
			}

			return true;
		}

		private boolean sell() {
			boolean haslock = false;

			haslock = ham.getLockbuy().tryLock();
			if (!haslock) {
				// System.out.println("ӪҵԱ" + this.id + ":"+ "����ʧ�ܣ�");
				return true;
			}
//			this.say("���������ɹ���");

			try {

				if (ham.getTotalsales().getCount() == 0) {
					this.say("�����°��ˣ�");
					return false;
				}

				this.say("�ȴ��˿ͣ�");

				if (!ham.getBuy().await(5, TimeUnit.SECONDS)) {
					this.say("û�˿ͣ���Ϣ�ᣡ");
					return true;
				}

				if (!this.consume()) {
					return false;
				}

//				this.say("���߹˿�");
				ham.getSell().signal();
			} catch (InterruptedException e) {
				 e.printStackTrace();
//				this.say("���꣬�°��ˣ�");
//				return false;
			} finally {
//				this.say("�ͷŹ�������");

				ham.getLockbuy().unlock();
			}

			return true;
		}

		@Override
		public void run() {
			boolean ret = false;
			Random r = new Random();
			int time = 0;

			for (;;) {
				ret = this.sell();
				if (!ret)
					break;

				try {
					time = r.nextInt(1000);
					// System.out.println("ӪҵԱ" + this.id + ":"+ "��"+time);

					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class Hcustom implements Runnable {
		private Ham ham;
		private int id;

		public Hcustom(Ham ham) {
			this.ham = ham;
		}

		private void say(String content) {
			System.out.println("===�˿�" + this.id + ":" + content);
		}

		private boolean buy() {
			ham.getLockbuy().lock();
			try {
				this.say("��Ҫ���������");

				ham.getBuy().signal();

//				Thread.sleep(3000);

				if (ham.getSell().await(3, TimeUnit.SECONDS)) {
					this.say("лл��");
				} else {
					this.say("û���д��������ˣ�");
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				ham.getLockbuy().unlock();
			}

			return true;
		}

		@Override
		public void run() {
			boolean ret = false;
			Random r = new Random();

			for (int i = 1; i <= Integer.MAX_VALUE; i++) {
				if (Thread.interrupted()) {
					break;
				}

				this.id = i;

				ret = this.buy();
				if (!ret)
					break;

				try {
					Thread.sleep(r.nextInt(1000));
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
}