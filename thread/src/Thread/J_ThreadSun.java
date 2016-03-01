package Thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * �ο�http://www.zhihu.com/question/40856557
 * @author baowenzhou
 *
 */
public class J_ThreadSun extends Thread{
	public static int m_data=0;		
//	public static AtomicInteger m_data=new AtomicInteger();			

	public static int m_times=1000;
	public int m_ID;
//	public boolean m_done;
	public volatile boolean m_done;
	
	public static Object obj = new Object();
	
	public static Lock lock = new ReentrantLock();
	
	J_ThreadSun(int id){
		m_ID=id;
	}
	
	public synchronized static void increment(int d) {
		m_data+=d;
	}
	
	public static void increment2(int d) {
		lock.lock();
		try{
		m_data+=d;
		}finally{
			lock.unlock();
		}
	}
	
	public void run(){
		m_done=false;
		int d=((m_ID % 2==0) ? 1:-1);
		System.out.println("�����߳�:"+m_ID+"(����Ϊ:"+d+")");
		for(int i=0;i<m_times;i++){			
			for(int j=0;j<m_times;j++){
//					m_data+=1;
				
				// ����1����Ϊ�߳����������󣬱��붨�徲̬obj
//				synchronized (obj) {
//					m_data+=1;
//				}
				
				// ����2��ԭ���Ե�AtomicInteger
//				m_data.addAndGet(d);
				
				// ����3����Ϊ�߳�����������ִ�о�̬��ͬ������
//				increment(d);
				
				// ����4�� ��Ϊ�߳����������󣬶��徲̬��
//				increment2(d);
			}
		}
		m_done=true;
		System.out.println("�߳̽���:"+m_ID);
	}
	
	public static void main(String[] args){
		J_ThreadSun t1=new J_ThreadSun(1);
		J_ThreadSun t2=new J_ThreadSun(2);
		t1.m_done=false;
		t2.m_done=false;
		t1.start();
		t2.start();
		while(true){
			if(t1.m_done && t2.m_done){//��t1,t2��m_done��Ϊtrueʱ����ѭ��
				break;
			}
			//����˴�������룬��������ִ��
		}
		System.out.println("���: m_data="+t1.m_data+","+t2.m_data+","+m_data);
	}
}