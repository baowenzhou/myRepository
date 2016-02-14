package Thread;

/**
 * A.join����API�еĽ����ǣ�������ǰ�߳�B��ֱ��Aִ����ϲ���������ִ��B��
 * ����b�߳�ִ�У�a�߳�join��ֱ��ִ����a��Ȼ���ִ��b��֤ʵ����˵����
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