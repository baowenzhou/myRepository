package Collection;

import java.util.ArrayList;
import java.util.Vector;

public class TestVector2 {
    static Vector<Integer> vector = new Vector<Integer>();
//    static ArrayList<Integer> vector = new ArrayList<Integer>();

    public static void main(String[] args) throws InterruptedException {
    	
        while(true) {
            for(int i=0;i<10;i++)
                vector.add(i);
            
	            Thread thread1 = new Thread(){
	                public void run() {
	                	synchronized(vector) {//�޴��߼����̲߳���ȫ����Ϊsize��remove�������ܲ�һ��
	                    for(int i=0;i<vector.size();i++)
	                        vector.remove(i);
	                	}
	                };
	            };
	            Thread thread2 = new Thread(){
	                public void run() {
	                	synchronized(vector) {//�޴��߼����̲߳���ȫ����Ϊsize��get�������ܲ�һ��
		                    for(int i=0;i<vector.size();i++)
		                        System.out.println(vector.get(i));
	                	}
	                };
	            };
	            thread1.start();
	            thread2.start();
	            while(Thread.activeCount()>10)   {
                 
            }
        }
    }
}