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
		YieldThread t1 = new YieldThread("�߼�");
		t1.start();
		// ����ǰ�߳����ȼ���ߣ���ô��ʹ������yield()�������̵߳������ֻὫ����̵߳��ȳ�������ִ��
		// t1.setPriority(Thread.MAX_PRIPORITY);
		YieldThread t2 = new YieldThread("�ͼ�");
		t2.start();
		t2.setPriority(Thread.MIN_PRIORITY);
		// System.out.println("Hello World!");
	}
}