package Thread;

public class SleepDemo extends Thread {
	public SleepDemo(String name) {
		super(name);
	}

	public void run() {
		for (int i = 0; i < 50; i++) {
			System.out.println(getName() + "----" + i);
			if (i == 20) {
				try {
					Thread.sleep(1000 * 10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		SleepDemo t = new SleepDemo("ÐÂÏß³Ì");
		t.start();

		for (int i = 0; i < 30; i++) {

			System.out.println(Thread.currentThread().getName() + "---" + i);
			if (i == 15)
				try {
					Thread.sleep(1000 * 10);
				} catch (Exception e) {
					e.printStackTrace();
				}

		}
	}
}