package Lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * synchronized��ֻ��ͬʱ��һ��
 * readlock+readlock��������ͬʱ��
 * readlock+writelock/writelock+writelock��ֻ��ͬʱ��һ��
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
     
    // synchronizedͬ���鷽ʽ
    public synchronized void get(Thread thread) {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 1) {
            System.out.println(thread.getName()+"���ڽ��ж�����");
        }
        System.out.println(thread.getName()+"���������");
    }
    
    public synchronized void set(Thread thread) {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 1) {
            System.out.println(thread.getName()+"���ڽ���д����");
        }
        System.out.println(thread.getName()+"д�������");
    }
    
    // ������ʽ������ͬʱ��
    public void get2(Thread thread) {
        rwl.readLock().lock();
        
        try {
            long start = System.currentTimeMillis();
             
            while(System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName()+"���ڽ��ж�����");

            }
            System.out.println(thread.getName()+"���������");

        } finally {
            rwl.readLock().unlock();
        }
    }
    
    // д����ʽ��һ��д�꣬д��һ��
    public void set2(Thread thread) {
        rwl.writeLock().lock();

        try {
            long start = System.currentTimeMillis();
             
            while(System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName()+"���ڽ���д����");

            }
            System.out.println(thread.getName()+"д�������");

        } finally {
        	rwl.writeLock().unlock();
        }
    }
}