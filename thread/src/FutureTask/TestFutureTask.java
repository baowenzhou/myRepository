package FutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class TestFutureTask {

	public static void main(String[] args) {
		ExecutorService exec=Executors.newCachedThreadPool();
		
		FutureTask<String> task=new FutureTask<String>(new Callable<String>(){//FutrueTask�Ĺ��������һ��Callable�ӿ�
			@Override
			public String call() throws Exception {
				Thread.sleep(3000);
				return Thread.currentThread().getName();//���������һ���첽����
			}});
			
			try {
				exec.execute(task);//FutureTaskʵ����Ҳ��һ���߳�
				String result=task.get();//ȡ���첽����Ľ�������û�з��أ��ͻ�һֱ�����ȴ�
				System.out.printf("get:%s%n",result);
				exec.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
	}

}
