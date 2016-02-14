package Synchronized;

public class NotifyTest {
//	private String flag = "true";
    private String flag[] = { "true" };  


	class NotifyThread extends Thread {
		public NotifyThread(String name) {
			super(name);
		}

		public void run() {
			try {
				sleep(3000);// 推迟3秒钟通知
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			synchronized (flag) {// 必须加同步块，获得控制权
//				flag = "false";// 重新赋值，对象更改会导致没有控制权
				flag[0] = "false";
//				flag.notify();// 通知单个,选优先级最大的
				flag.notifyAll();// 通知全部
			}

		}
	};

	class WaitThread extends Thread {
		public WaitThread(String name) {
			super(name);
		}

		public void run() {

			synchronized (flag) {// 必须加同步块，获得控制权

//				while (flag != "false") {
				while (flag[0] != "false") {
					System.out.println(getName() + " begin waiting!");
					long waitTime = System.currentTimeMillis();
					try {
						flag.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					waitTime = System.currentTimeMillis() - waitTime;
					System.out.println("wait time :" + waitTime);
				}
				System.out.println(getName() + " end waiting!");
			}

		}
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Main Thread Run!");
		NotifyTest test = new NotifyTest();
		NotifyThread notifyThread = test.new NotifyThread("notify01");
		WaitThread waitThread01 = test.new WaitThread("waiter01");
		WaitThread waitThread02 = test.new WaitThread("waiter02");
		WaitThread waitThread03 = test.new WaitThread("waiter03");
		notifyThread.start();
		
		waitThread01.setPriority(Thread.MIN_PRIORITY);
		waitThread01.start();
		
		waitThread02.setPriority(Thread.MIN_PRIORITY);
		waitThread02.start();
		
		waitThread03.setPriority(Thread.NORM_PRIORITY);
		waitThread03.start();
	}

}