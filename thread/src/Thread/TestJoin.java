package Thread;

import java.io.IOException;

public class TestJoin {

	public static void main(String[] args) throws IOException {
		System.out.println("�����߳�" + Thread.currentThread().getName());
		TestJoin test = new TestJoin();
		MyThread thread1 = test.new MyThread();
		thread1.start();
		try {
			System.out.println("�߳�" + Thread.currentThread().getName() + "�ȴ�");
			thread1.join();// ��ִ�������߳�
			System.out
					.println("�߳�" + Thread.currentThread().getName() + "����ִ��");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			System.out.println("�����߳�" + Thread.currentThread().getName());
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
			System.out
					.println("�߳�" + Thread.currentThread().getName() + "ִ�����");
		}
	}
}