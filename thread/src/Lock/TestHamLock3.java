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
 * 增加客户线程
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

			System.out.println("+++厨师们:开业前，先做点汉堡!!!!!");

			// 厨师
			for (int i = 1; i <= 5; i++) {
				pool.execute(test.new Hmaker(ham, i));
			}

			ham.getPremake().await();
			System.out.println("---营业员们:顾客朋友们,开始营业了!!!!!");

			// 营业员
			for (int i = 1; i <= 3; i++) {
				// tasks.add(pool.submit(test.new Hassistant(ham, i)));
				pool.submit(test.new Hassistant(ham, i));
			}

			// 顾客
			tasks.add(pool.submit(test.new Hcustom(ham)));

			ham.getTotalmaterial().await();
			System.out.println("+++厨师们:没有材料了!!!!!");

			ham.getTotalsales().await();
			System.out.println("---营业员们:顾客朋友们,汉堡全卖完了!!!!!");

			for (Future<?> task : tasks) {
				task.cancel(true);
			}

			pool.shutdown();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 汉堡
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
			System.out.println("+++厨师" + this.id + ":" + content);
		}

		private boolean make() {
			boolean haslock = ham.getLock().tryLock();
			if (!haslock) {
				// System.out.println("厨师" + this.id + ":"+ "抢锁失败！");
				return true;
			}
			// System.out.println("厨师" + this.id + ":"+ "抢锁成功！");

			try {

				if (ham.getTotalmaterial().getCount() == 0) {
					this.say("没材料下班了！");
					return false;
				}

				if (ham.getCansales() == ham.getSize()) {
					this.say("汉堡装不下了！");
					ham.getFull().await();
				} else {
					ham.setProduction(ham.getProduction() + 1);
					this.say("汉堡做好了，（总共做了" + ham.getProduction() + "个，可卖"
							+ ham.getCansales() + "）");

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
			System.out.println("---营业员" + this.id + ":" + content);
		}

		private boolean consume() {
			boolean haslock = false;

			haslock = ham.getLock().tryLock();
			if (!haslock) {
				// System.out.println("营业员" + this.id + ":"+ "抢锁失败！");
				return true;
			}
			// System.out.println("营业员" + this.id + ":"+ "抢锁成功！");

			try {

				if (ham.getCansales() == 0) {
					this.say("顾客朋友们，请稍微等一下，汉堡还没做好！");
					ham.getEmpty().await();
				} else {
					ham.setSales(ham.getSales() + 1);
					this.say("顾客好，汉堡上来了，（总共卖了:" + ham.getSales() + "个，可卖"
							+ ham.getCansales() + "）");

//					this.say("释放盘子");
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
				// System.out.println("营业员" + this.id + ":"+ "抢锁失败！");
				return true;
			}
//			this.say("抢购买锁成功！");

			try {

				if (ham.getTotalsales().getCount() == 0) {
					this.say("卖完下班了！");
					return false;
				}

				this.say("等待顾客！");

				if (!ham.getBuy().await(5, TimeUnit.SECONDS)) {
					this.say("没顾客，休息会！");
					return true;
				}

				if (!this.consume()) {
					return false;
				}

//				this.say("告诉顾客");
				ham.getSell().signal();
			} catch (InterruptedException e) {
				 e.printStackTrace();
//				this.say("卖完，下班了！");
//				return false;
			} finally {
//				this.say("释放购买锁！");

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
					// System.out.println("营业员" + this.id + ":"+ "等"+time);

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
			System.out.println("===顾客" + this.id + ":" + content);
		}

		private boolean buy() {
			ham.getLockbuy().lock();
			try {
				this.say("我要买个汉堡！");

				ham.getBuy().signal();

//				Thread.sleep(3000);

				if (ham.getSell().await(3, TimeUnit.SECONDS)) {
					this.say("谢谢！");
				} else {
					this.say("没人招待，不买了！");
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