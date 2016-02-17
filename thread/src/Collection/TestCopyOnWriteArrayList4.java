package Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestCopyOnWriteArrayList4 {

	public static void main(String[] args) throws InterruptedException {
		List<String> a = new ArrayList<String>();
		a.add("a");
		a.add("b");
		a.add("c");
//		final ArrayList<String> list = new ArrayList<String>(a);
		final CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>(a);
		Thread t = new Thread(new Runnable() {
			int count = -1;

			@Override
			public void run() {
				while (true) {
					list.add(count++ + "");
				}
			}
		});
		t.setDaemon(true);
		t.start();
		Thread.currentThread().sleep(3);
		for (String s : list) {//ArrayList非线程安全，主线程和子线程不能同时读写，报java.util.ConcurrentModificationException
			System.out.println(s);
		}	
	}
}
