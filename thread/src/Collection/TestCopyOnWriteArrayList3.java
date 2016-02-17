package Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author Alfred Xu <alfred.xu@heweisoft.com>
 * 
 */
public class TestCopyOnWriteArrayList3 {
	private final int threadNumber;

	public TestCopyOnWriteArrayList3(int n) {
		threadNumber = n;
	}

	private static class TestThread implements Runnable {
		private static long totolTime;
		private final int No;
		private final int loop = 100000;
		private final Thread t;
		private final List<Integer> list;
		private final CountDownLatch countDown;

		TestThread(int No, List<Integer> list, CountDownLatch countDown) {
			this.No = No;
			this.list = list;
			t = new Thread(this);
			this.countDown = countDown;
		}

		public void start() {
			t.start();
		}

		public synchronized void addTime(long time) {
			totolTime += time;
		}

		@Override
		public void run() {
			long time = randomAccess();
			addTime(time);
			
			countDown.countDown();
//			System.out.println(countDown.getCount());
		}

		@Override
		public String toString() {
			return "Thread " + No + ":";
		}

		public long randomAccess() {
			Date date1 = new Date();
			Random random = new Random();
			for (int i = 0; i < loop; i++) {
				int n = random.nextInt(loop);
				list.get(n);
			}
			Date date2 = new Date();
			long time = date2.getTime() - date1.getTime();
			// System.out.println(this + list.getClass().getSimpleName()
			// + " time:" + time);
			return time;
		}

	}

	public void initList(List<Integer> list, int size) {
		for (int i = 0; i < size; i++) {
			list.add(new Integer(i));
		}
	}

	public void test(List<Integer> list, CountDownLatch countDown) {
		System.out.println("Test List Performance");
		TestThread[] ts = new TestThread[threadNumber];
		for (int i = 0; i < ts.length; i++) {
			ts[i] = new TestThread(i, list, countDown);
		}
		for (int i = 0; i < ts.length; i++) {
//			System.out.println(i);

			ts[i].start();
		}
	}

	public static void main(String[] args) {
		
		
		TestCopyOnWriteArrayList3 lp = new TestCopyOnWriteArrayList3(64);
		
		CountDownLatch countDown = new CountDownLatch(lp.threadNumber);

		
		List<Integer> al = Collections
				.synchronizedList(new ArrayList<Integer>());
		lp.initList(al, 100000);
		lp.test(al, countDown);
		
		try {
			countDown.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(al.size());
		System.out.println(TestThread.totolTime);

		TestThread.totolTime = 0;
		CopyOnWriteArrayList<Integer> cl = new CopyOnWriteArrayList<Integer>(al);
		
		
		countDown = new CountDownLatch(lp.threadNumber);
		lp.test(cl, countDown);
		
		try {
			countDown.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(cl.size());
		System.out.println(TestThread.totolTime);
	}
}