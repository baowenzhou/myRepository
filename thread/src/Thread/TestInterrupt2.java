package Thread;

import java.io.IOException;

public class TestInterrupt2 {
     
    public static void main(String[] args) throws IOException  {
        TestInterrupt2 test = new TestInterrupt2();
        MyThread thread = test.new MyThread();
        thread.start();
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
             
        }
        thread.interrupt();//不能中断正在运行中的线程
    } 
     
    class MyThread extends Thread{
        @Override
        public void run() {
            int i = 0;
            while(!isInterrupted() && i<Integer.MAX_VALUE){//根据isInterrupted判断是否中断
                System.out.println(i+" while循环");
                i++;
            }
        }
    }
    
    class MyThread2 extends Thread{//一般根据标志位判断，而不是isInterrupted
    	        private volatile boolean isStop = false;
    	        @Override
    	        public void run() {
    	            int i = 0;
    	            while(!isStop){
    	                i++;
    	            }
    	        }
    	         
    	        public void setStop(boolean stop){
    	            this.isStop = stop;
    	        }
    	    }
}