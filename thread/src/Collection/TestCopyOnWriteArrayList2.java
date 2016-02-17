package Collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestCopyOnWriteArrayList2 {

static CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>();

	public static void main(String[] args) {
		
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		
		
		Thread thread1 = new Thread() {
			public void run() {
//				synchronized (list) {// 需加同步块
					Iterator<Integer> iterator = list.iterator();

					while (iterator.hasNext()) {
						Integer integer = iterator.next();
						System.out.println(integer);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
//				}

			};
		};
		Thread thread2 = new Thread() {
			public void run() {
//				synchronized (list) {// 需加同步块
					Iterator<Integer> iterator = list.iterator();

					while (iterator.hasNext()) {
						Integer integer = iterator.next();
						if (integer == 2)
							iterator.remove();// 报java.lang.UnsupportedOperationException错
					}
//				}
			};
		};
		thread1.start();
		thread2.start();
	}
}