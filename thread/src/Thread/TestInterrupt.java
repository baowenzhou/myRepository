package Thread;

import java.io.IOException;

public class TestInterrupt {

	public static void main(String[] args) throws IOException {
		TestInterrupt test = new TestInterrupt();
		MyThread thread = test.new MyThread();
		thread.start();
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {

		}
		thread.interrupt();
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			try {
				System.out.println("����˯��״̬");
				Thread.currentThread().sleep(10000);
				System.out.println("˯�����");
			} catch (InterruptedException e) {
				System.out.println("�õ��ж��쳣");
			}
			System.out.println("run����ִ�����");
		}
	}
}