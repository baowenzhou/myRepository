package Thread;

/**
 * A.yield��A�ó�λ�ã���Bִ�У�Bִ�н���A��ִ�С���join��˼�����෴��
 * ����Bִ�У�Ȼ��Aִ�У���B��ѭ���У�i=2ʱ��Aִ��yield������Bִ���꣬���ֵ�Aִ�С�
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