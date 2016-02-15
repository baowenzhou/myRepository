package Volatile;

import java.io.IOException;

public class TestVolatile2 {
 
    private int i = 10;
    private Object object = new Object();
 
    public static void main(String[] args) throws IOException {
        TestVolatile2 test = new TestVolatile2();
        Thread thread1 = test.new MyThread("Thread-1");
        Thread thread2 = test.new MyThread("Thread-2");
        thread1.start();
        thread2.start();
        ((MyThread)thread1).flag = false;
    }
 
    class MyThread extends Thread {
        volatile boolean flag = true;
 
        MyThread(String name) {
            super(name);
        }
         
        @Override
        public void run() {
            while (flag && i<50) {
                synchronized (object) {
                    i++;
                    System.out.println("i:" + i);
                    System.out.println("线程" + Thread.currentThread().getName() + "进入睡眠状态");
                    try {
                        Thread.currentThread().sleep(50);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("线程" + Thread.currentThread().getName() + "睡眠结束");
                    i++;
                    System.out.println("i:" + i);
                }
            }
        }
    }
}