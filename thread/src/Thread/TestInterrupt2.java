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
        thread.interrupt();//�����ж����������е��߳�
    } 
     
    class MyThread extends Thread{
        @Override
        public void run() {
            int i = 0;
            while(!isInterrupted() && i<Integer.MAX_VALUE){//����isInterrupted�ж��Ƿ��ж�
                System.out.println(i+" whileѭ��");
                i++;
            }
        }
    }
    
    class MyThread2 extends Thread{//һ����ݱ�־λ�жϣ�������isInterrupted
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