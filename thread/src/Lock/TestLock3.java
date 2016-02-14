package Lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestLock3 {
    private Lock lock = new ReentrantLock();   
    public static void main(String[] args)  {
    	TestLock3 test = new TestLock3();
        MyThread thread1 = new MyThread(test);
        MyThread thread2 = new MyThread(test);
        thread1.start();
        thread2.start();
         
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
    }  
     
    public void insert(Thread thread) throws InterruptedException{
        lock.lockInterruptibly();   //ע�⣬�����Ҫ��ȷ�жϵȴ������̣߳����뽫��ȡ���������棬Ȼ��InterruptedException�׳�
        try {  
//            lock.lockInterruptibly();   //�·�catch�Ჶ���쳣�ͷ���

            System.out.println(thread.getName()+"�õ�����");
            long startTime = System.currentTimeMillis();
            for(    ;     ;) {
                if(System.currentTimeMillis() - startTime >= Integer.MAX_VALUE)
                    break;
                //��������
            }
        }
//        catch (Exception e) {
//            System.out.println(Thread.currentThread().getName()+"���ж�!!!");
//        }
        finally {
            System.out.println(Thread.currentThread().getName()+"ִ��finally");
            lock.unlock();
            System.out.println(thread.getName()+"�ͷ�����");
        }  
    }
}
 
class MyThread extends Thread {
    private TestLock3 test = null;
    public MyThread(TestLock3 test) {
        this.test = test;
    }
    @Override
    public void run() {
         
        try {
            test.insert(Thread.currentThread());
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+"���ж�");
        }
    }
}