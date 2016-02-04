package ScheduledThreadPoolExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TestScheduledThreadPoolExecutor {
	
	public static void main(String[] args) {
		ScheduledThreadPoolExecutor exec=new ScheduledThreadPoolExecutor(1);
		
		exec.scheduleAtFixedRate(new Runnable(){//每隔一段时间就触发异常
			@Override
			public void run() {
				throw new RuntimeException();
			}}, 1000, 5000, TimeUnit.MILLISECONDS);//初期延迟1秒，每5秒执行
		
		
		System.out.println(System.nanoTime());//毫微秒

		exec.scheduleAtFixedRate(new Runnable(){//每隔一段时间打印系统时间，证明两者是互不影响的
			@Override
			public void run() {
//				System.out.println(System.currentTimeMillis());//毫微秒
				System.out.println(System.nanoTime());//毫秒
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}}, 5000, 2000, TimeUnit.MILLISECONDS);//初期延迟5秒，每2秒执行
		
		System.out.println("#"+System.nanoTime());//毫微秒

		exec.schedule(new Runnable(){//每隔一段时间打印系统时间，证明两者是互不影响的
			@Override
			public void run() {
//				System.out.println(System.currentTimeMillis());//毫微秒
				System.out.println("#"+System.nanoTime());//毫秒
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

			}}, 5000, TimeUnit.MILLISECONDS);//初期延迟5秒
	}

}

