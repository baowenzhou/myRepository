package Thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 参考http://www.zhihu.com/question/40856557
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
		System.out.println("运行线程:"+m_ID+"(增量为:"+d+")");
		for(int i=0;i<m_times;i++){			
			for(int j=0;j<m_times;j++){
//					m_data+=1;
				
				// 方法1，因为线程是两个对象，必须定义静态obj
//				synchronized (obj) {
//					m_data+=1;
//				}
				
				// 方法2，原子性的AtomicInteger
//				m_data.addAndGet(d);
				
				// 方法3，因为线程是两个对象，执行静态的同步方法
//				increment(d);
				
				// 方法4， 因为线程是两个对象，定义静态锁
//				increment2(d);
			}
		}
		m_done=true;
		System.out.println("线程结束:"+m_ID);
	}
	
	public static void main(String[] args){
		J_ThreadSun t1=new J_ThreadSun(1);
		J_ThreadSun t2=new J_ThreadSun(2);
		t1.m_done=false;
		t2.m_done=false;
		t1.start();
		t2.start();
		while(true){
			if(t1.m_done && t2.m_done){//当t1,t2的m_done都为true时跳出循环
				break;
			}
			//如果此处加入代码，可以正常执行
		}
		System.out.println("结果: m_data="+t1.m_data+","+t2.m_data+","+m_data);
	}
}