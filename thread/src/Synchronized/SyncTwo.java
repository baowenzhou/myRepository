package Synchronized;

public class SyncTwo {
	final static Object object = new Object();

	public  void say(Thread thread) {//�������󣬲�����
//	public synchronized void say(Thread thread) {//�������󣬲�����
		synchronized (object) {//һ����������
//		synchronized(this) {//�������󣬲�����
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
