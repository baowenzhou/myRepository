package Thread;

/**
 * A.yield，A让出位置，给B执行，B执行结束A再执行。跟join意思正好相反！
 * 首先B执行，然后A执行；在B的循环中，i=2时，A执行yield；接着B执行完，才轮到A执行。
 * @author baowenzhou
 *
 */
public class JoinYield2 {
	static class ThreadA extends Thread {
    @Override
    public void run() {
      // TODO Auto-generated method stub
      super.run();
      for (int i = 0; i < 10; i++) {
        System.out.println("ThreadA " + i);
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
        for (int i = 0; i < 10; i++) {
          if(i==2){
            a.yield();
          }
          System.out.println("ThreadB " + i);
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println("ThreadB end");
    }
  }

  public static void main(String[] args) {
    ThreadA a = new ThreadA();
//    a.setPriority(Thread.MIN_PRIORITY);
    ThreadB b = new ThreadB(a);
//    b.setPriority(Thread.MAX_PRIORITY);
    b.start();
    a.start();
  }
}