package Synchronized;

public class SyncTwo {
	final static Object object = new Object();

	public  void say(Thread thread) {//两个对象，不抢锁
//	public synchronized void say(Thread thread) {//两个对象，不抢锁
		synchronized (object) {//一个对象，抢锁
//		synchronized(this) {//两个对象，不抢锁
			System.out.println(thread.getName()+"------ i am here ");
			try {
				Thread.sleep(5000);
				
			} catch (Exception e) {
			}
			System.out.println(thread.getName()+"------ i am here2 ");

		}
	}

	public static void main(String[] args) {
		new Thread() {
			public void run() {
				new SyncTwo().say(this);
			}
		}.start();
		
		new Thread() {
			public void run() {
				new SyncTwo().say(this);
			}
		}.start();
	}
}
