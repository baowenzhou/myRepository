package Thread;

/**
 * A.join，在API中的解释是，堵塞当前线程B，直到A执行完毕并死掉，再执行B。
 * 首先b线程执行，a线程join后，直接执行完a，然后才执行b，证实上述说法。
 * @author baowenzhou
 *
 */
public class JoinYield1 {

static class ThreadA extends Thread {
    @Override
    public void run() {
      // TODO Auto-generated method stub
      super.run();
      for (int i = 0; i < 10; i++) {
        System.out.println("ThreadA" + i);
      }
    }
  }

  static class ThreadB extends Thread {
    ThreadA a;

    public ThreadB(ThreadA a) {
      // TODO Auto-generated constructor stub
      this.a = a;
    }

    @Override
    public void run() {
      // TODO Auto-generated method stub
      super.run();
      System.out.println("ThreadB start");
      try {
        a.join();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println("ThreadB end");
    }
  }

  public static void main(String[] args) {
    ThreadA a = new ThreadA();
    ThreadB b = new ThreadB(a);
    b.start();
    a.start();
  }
}