package CountDownLatch;

/**
CountDownLatch����һ��ͬ��������,����ʱ����int����,�ò������Ǽ������ĳ�ʼֵ��ÿ����һ��countDown()��������������1,����������0 ʱ��await()�����������������ִ��
CountDownLatch������д����һ����������������������������0ʱ�����ض����¼��������������ԣ����������̵߳ȴ����̵߳Ľ�����������һ��ģ���˶�Ա���������Ӽ���˵����
*/
  import java.util.concurrent.CountDownLatch;
  import java.util.concurrent.ExecutorService;
  import java.util.concurrent.Executors;
 
  public class CountDownLatchDemo {
     private static final int PLAYER_AMOUNT = 5;
     public CountDownLatchDemo() {
         // TODO Auto-generated constructor stub    
      }
     /**
      * @param args
      */
     public static void main(String[] args) {
         // TODO Auto-generated method stub
         //����ÿλ�˶�Ա��CountDownLatch��1�󼴽�������
         CountDownLatch begin = new CountDownLatch(1);
         //�������������������˶�Ա������������
         CountDownLatch end = new CountDownLatch(PLAYER_AMOUNT);
         Player[] plays = new Player[PLAYER_AMOUNT];
         
         for(int i=0;i<PLAYER_AMOUNT;i++)
             plays[i] = new Player(i+1,begin,end);
         
         //�����ض����̳߳أ���СΪ5
         ExecutorService exe = Executors.newFixedThreadPool(PLAYER_AMOUNT);
         for(Player p:plays)
             exe.execute(p);            //�����߳�
         System.out.println("Race begins!");
         begin.countDown();
         try{
             end.await();            //�ȴ�end״̬��Ϊ0����Ϊ��������
         }catch (InterruptedException e) {
             // TODO: handle exception
             e.printStackTrace();
         }finally{
             System.out.println("Race ends!");
         }
         exe.shutdown();
     }
     
     private static class Player implements Runnable {
    	 
         private int id;
         private CountDownLatch begin;
         private CountDownLatch end;
         public Player(int i, CountDownLatch begin, CountDownLatch end) {
             // TODO Auto-generated constructor stub
             super();
             this.id = i;
             this.begin = begin;
             this.end = end;
         }
     
         @Override
         public void run() {
             // TODO Auto-generated method stub
             try{
                 begin.await();        //�ȴ�begin��״̬Ϊ0
                 Thread.sleep((long)(Math.random()*100));    //�������ʱ�䣬���˶�Ա���ʱ��
                 System.out.println("Play"+id+" arrived.");
             }catch (InterruptedException e) {
                 // TODO: handle exception
                 e.printStackTrace();
             }finally{
                 end.countDown();    //ʹend״̬��1�����ռ���0
             }
         }
     }
 }