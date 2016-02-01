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
							buff = exchanger.exchange(buff);//��ʼ������һ���߳̽�������
							System.out.println("exchange buff1");
							buff.clear();
						}
						buff.add((int)(Math.random()*100));
						Thread.sleep((long)(Math.random()*1000));
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
						for(Integer i:buff){
							System.out.println(i);
						}
						Thread.sleep(1000);
						buff=exchanger.exchange(buff);//��ʼ������һ���߳̽�������
						System.out.println("exchange buff2");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}}).start();
	}
}

