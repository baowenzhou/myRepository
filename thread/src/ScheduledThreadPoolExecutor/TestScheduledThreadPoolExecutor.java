package ScheduledThreadPoolExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TestScheduledThreadPoolExecutor {
	
	public static void main(String[] args) {
		ScheduledThreadPoolExecutor exec=new ScheduledThreadPoolExecutor(1);
		
		exec.scheduleAtFixedRate(new Runnable(){//ÿ��һ��ʱ��ʹ����쳣
			@Override
			public void run() {
				throw new RuntimeException();
			}}, 1000, 5000, TimeUnit.MILLISECONDS);//�����ӳ�1�룬ÿ5��ִ��
		
		
		System.out.println(System.nanoTime());//��΢��

		exec.scheduleAtFixedRate(new Runnable(){//ÿ��һ��ʱ���ӡϵͳʱ�䣬֤�������ǻ���Ӱ���
			@Override
			public void run() {
//				System.out.println(System.currentTimeMillis());//��΢��
				System.out.println(System.nanoTime());//����
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}}, 5000, 2000, TimeUnit.MILLISECONDS);//�����ӳ�5�룬ÿ2��ִ��
		
		System.out.println("#"+System.nanoTime());//��΢��

		exec.schedule(new Runnable(){//ÿ��һ��ʱ���ӡϵͳʱ�䣬֤�������ǻ���Ӱ���
			@Override
			public void run() {
//				System.out.println(System.currentTimeMillis());//��΢��
				System.out.println("#"+System.nanoTime());//����
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

			}}, 5000, TimeUnit.MILLISECONDS);//�����ӳ�5��
	}

}

