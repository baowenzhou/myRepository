package Lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestLock {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();//ע������ط�,һ���������ͻ
    
    public static void main(String[] args)  {
        final TestLock test = new TestLock();
         
        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();
         
        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();
    }  
     
    public void insert(Thread thread) {
//        Lock lock = new ReentrantLock();    //ע������ط�,�������������ͻ
        lock.lock();
        try {
            System.out.println(thread.getName()+"�õ�����");
            for(int i=0;i<5;i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            System.out.println(thread.getName()+"�ͷ�����");
            lock.unlock();
        }
    }
}