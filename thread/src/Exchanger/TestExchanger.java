package Exchanger;

import java.util.ArrayList;
import java.util.concurrent.Exchanger;

public class TestExchanger {

	public static void main(String[] args) {
		final Exchanger<ArrayList<Integer>> exchanger = new Exchanger<ArrayList<Integer>>();
		final ArrayList<Integer> buff1 = new ArrayList<Integer>(10);
		final ArrayList<Integer> buff2 = new ArrayList<Integer>(10);

		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Integer> buff = buff1;
				try {
					while (true) {
						if (buff.size() >= 10) {
							System.out.println("exchange buff11");

							buff = exchanger.exchange(buff);//开始跟另外一个线程交互数据
							System.out.println("exchange buff12");
							buff.clear();
						}
						System.out.println("exchange buff13");

						buff.add((int)(Math.random()*100));
						System.out.println("exchange buff14");

						Thread.sleep((long)(Math.random()*1000));
						System.out.println("exchange buff15");

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				ArrayList<Integer> buff=buff2;
				while(true){
					try {
						System.out.println("exchange buff21");

						for(Integer i:buff){
							System.out.println(i);
						}
						Thread.sleep(1000);
						System.out.println("exchange buff22");

						buff=exchanger.exchange(buff);//开始跟另外一个线程交换数据
						System.out.println("exchange buff23");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}}).start();
	}
}

