package Volatile;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestVolatile {
	public volatile int inc = 0;// 自增操作不是原子性操作，而且volatile也无法保证对变量的任何操作都是原子性的

	Lock lock = new ReentrantLock();
	
//    public AtomicInteger inc = new AtomicInteger();

//   // 结果不是10000
//	 public void increase() {
//	 inc++;
//	 }
//	
	 // 1.增加synchronized关键字，结果10000
	 public synchronized void increase() {
	 inc++;
	 }
//
//	// 2.增加锁
//	public void increase() {
//		lock.lock();
//		try {
//			inc++;
//		} finally {
//			lock.unlock();
//		}
//	}
//    
//    // 3.运用AtomicInteger
//    public void increase() {
//    	inc.getAndIncrement();
//    }

	public static void main(String[] args) {
		final TestVolatile test = new TestVolatile();
		for (int i = 0; i < 10; i++) {
			new Thread() {
				public void run() {
					for (int j = 0; j < 1000; j++)
						test.increase();
				};
			}.start();
		}

		while (Thread.activeCount() > 1)
			// 保证前面的线程都执行完
			Thread.yield();
		System.out.println(test.inc);
	}
}