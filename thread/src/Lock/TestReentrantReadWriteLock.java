package Lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * synchronized：只能同时做一个
 * readlock+readlock：两个可同时读
 * readlock+writelock/writelock+writelock：只能同时做一个
 * @author baowenzhou
 *
 */
public class TestReentrantReadWriteLock {
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
     
    public static void main(String[] args)  {
        final TestReentrantReadWriteLock test = new TestReentrantReadWriteLock();
         
        new Thread(){
            public void run() {
                test.get(Thread.currentThread());
//                test.set(Thread.currentThread());

            };
        }.start();
         
        new Thread(){
            public void run() {
//                test.get(Thread.currentThread());
                test.set(Thread.currentThread());

            };
        }.start();
         
    }  
     
    // synchronized同步块方式
    public synchronized void get(Thread thread) {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 1) {
            System.out.println(thread.getName()+"正在进行读操作");
        }
        System.out.println(thread.getName()+"读操作完毕");
    }
    
    public synchronized void set(Thread thread) {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 1) {
            System.out.println(thread.getName()+"正在进行写操作");
        }
        System.out.println(thread.getName()+"写操作完毕");
    }
    
    // 读锁方式，两个同时读
    public void get2(Thread thread) {
        rwl.readLock().lock();
        
        try {
            long start = System.currentTimeMillis();
             
            while(System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName()+"正在进行读操作");

            }
            System.out.println(thread.getName()+"读操作完毕");

        } finally {
            rwl.readLock().unlock();
        }
    }
    
    // 写锁方式，一个写完，写另一个
    public void set2(Thread thread) {
        rwl.writeLock().lock();

        try {
            long start = System.currentTimeMillis();
             
            while(System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName()+"正在进行写操作");

            }
            System.out.println(thread.getName()+"写操作完毕");

        } finally {
        	rwl.writeLock().unlock();
        }
    }
}