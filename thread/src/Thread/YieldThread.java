package Thread;

public class YieldThread extends Thread {
	public YieldThread(String name) {
		super(name);
	}

	public YieldThread() {

	}

	public void run() {
		for (int i = 0; i < 50; i++) {
			System.out.println(getName() + "--->" + i);
			if (i == 20) {
				Thread.yield();
			}
		}
	}

	public static void main(String[] args) {
		YieldThread t1 = new YieldThread("高级");
		t1.start();
		// 若当前线程优先级最高，那么即使调用了yield()方法，线程调度器又会将这个线程调度出来重新执行
		// t1.setPriority(Thread.MAX_PRIPORITY);
		YieldThread t2 = new YieldThread("低级");
		t2.start();
		t2.setPriority(Thread.MIN_PRIORITY);
		// System.out.println("Hello World!");
	}
}