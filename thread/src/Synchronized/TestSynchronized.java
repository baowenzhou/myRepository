package Synchronized;

import java.util.ArrayList;

public class TestSynchronized {
 
    public static void main(String[] args)  {
        final InsertData insertData = new InsertData();
         
        new Thread() {
            public void run() {
                insertData.insert(Thread.currentThread());
//                insertData.insert2(Thread.currentThread());
//                insertData.insert3(Thread.currentThread());

            };
        }.start();
         
         
        new Thread() {
            public void run() {
//                insertData.insert(Thread.currentThread());
//                insertData.insert2(Thread.currentThread());
//                insertData.insert3(Thread.currentThread());
            	InsertData.insert4(Thread.currentThread());
            };
        }.start();
    }  
}
 
class InsertData {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Object object = new Object();

    
//    public void insert(Thread thread){
    public synchronized void insert(Thread thread){//ͬ���飬��ͬ�߳�˳��ִ��
        System.out.println(thread.getName()+"��ʼִ��");

        for(int i=0;i<5;i++){
            System.out.println(thread.getName()+"�ڲ�������"+i);
            arrayList.add(i);
        }
        
        System.out.println(thread.getName()+"ִ�����");
    }
    
    public void insert2(Thread thread){
        System.out.println(thread.getName()+"��ʼִ��");

        synchronized (this) {
            for(int i=0;i<100;i++){
                System.out.println(thread.getName()+"�ڲ�������"+i);
                arrayList.add(i);
            }
        }
        
        System.out.println(thread.getName()+"ִ�����");

    }
    
    public void insert3(Thread thread){
        System.out.println(thread.getName()+"��ʼִ��");

        synchronized (object) {
            for(int i=0;i<10;i++){
                System.out.println(thread.getName()+"�ڲ�������"+i);
                arrayList.add(i);
            }
        }
        
        System.out.println(thread.getName()+"ִ�����");
    }
    
    public synchronized static void insert4(Thread thread) {
        System.out.println(thread.getName()+"��ʼִ��");
        System.out.println(thread.getName()+"ִ�����");
    }
}