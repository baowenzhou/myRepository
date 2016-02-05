package Queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestBlockingQueue {

	public static void main(String[] args) {
		final BlockingQueue<Integer> queue=new LinkedBlockingQueue<Integer>(3);
		final Random random=new Random();
		
		class Producer implements Runnable{
			@Override
			public void run() {
				while(true){
					try {
						int i=random.nextInt(100);
						queue.put(i);//�����дﵽ����ʱ�򣬻��Զ�������
						if(queue.size()==3)
						{
							System.out.println("full"+queue);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		class Consumer implements Runnable{
			@Override
			public void run() {
				while(true){
					try {
						int i=queue.take();//������Ϊ��ʱ��Ҳ���Զ�����
						System.out.println(i);

						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		new Thread(new Producer()).start();
		new Thread(new Consumer()).start();
	}

}

