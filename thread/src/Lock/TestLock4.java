package Lock;

/**
* ����һ�����������ӵ���
* totalmaterialָ���е������������Ĳ���
 * box��һ������������
 * sales��ָ�����˶��ٸ�������
 * production��ָ�ܹ��ж��ٸ�������
 */
class Ham
{
	static Object box = new Object();
	static int totalmaterial = 10;
	static int sales = 0;
	// ԭ������
//	static int production = 5;
	// ���Դ���
	static int production = 1;

}

/**
 * ����һ����ʦ�߳���
 * make����ʹ����һ��ͬ���飬�����������᲻�ϵ�����������
 * run���������߳���Ҫ���е�����
 * ʹ����ѭ��������ϸ�֤�ں�������������֮ǰ�����ϵ�����������
 * ʹ���ж�����ж�ֻҪ�к���������ʦ����֪ͨӪҵԱ��������
 */
class Hmaker extends Thread
{
	public void make()
	{
		synchronized(Ham.box)
		{
			(Ham.production)++;
			Ham.box.notify();
		}
	}
	public void run()
	{
		while(Ham.production <= Ham.totalmaterial)
		{
			if(Ham.production > 0)
			{
				// ԭ������
//				System.out.println("��ʦ" + getName()+ ":"+ "�������ˣ��ܹ�" + 
//						(Ham.production - Ham.sales) + "����");
				
				// ���Դ���
				System.out.println("��ʦ" + getName()+ ":"+ "�������ˣ���" + 
						(Ham.production) + "����");
			}
			//3����һ��
			try {sleep(3000);} 
			catch (InterruptedException e){e.printStackTrace();}
			make();
		}
	}
}

/**
 *����һ��ӪҵԱ�߳���
 *sell������ӪҵԱ�������ķ���
 *run���������߳���Ҫ���е�����
 *ʹ���ж�������жϺ��������������Ƿ��к�����
 *��run������ʹ��ѭ�������ʹ��ӪҵԱ�ں������к���������²��ϵ���
 */
class Hassistant extends Thread
{
	public void sell()
	{
		
		// ԭ������
//		if(Ham.production == 0)
//		{
//			System.out.println("ӪҵԱ���˿������ǣ�����΢��һ�£�����û�ˣ���");
//		}
//		
//		synchronized(Ham.box) {//û��ͬ���飬����java.lang.IllegalMonitorStateException�쳣
//			try {Ham.box.wait();} 
//			catch (InterruptedException e) {e.printStackTrace();}
//			Ham.sales++;
//			System.out.println("ӪҵԱ���˿ͺã����������ˣ����ܹ�����:"+ Ham.sales +"����");
//		}
		
		// ���Դ���
		synchronized(Ham.box) {
			if(Ham.production - Ham.sales <= 0)
			{
				System.out.println("ӪҵԱ���˿������ǣ�����΢��һ�£�����û�ˣ���");
				
				try {Ham.box.wait();} 
				catch (InterruptedException e) {e.printStackTrace();}
			}
		
			
			Ham.sales++;
			System.out.println("ӪҵԱ���˿ͺã����������ˣ����ܹ�����:"+ Ham.sales +"����");
		}
	}
	
	public void run()
	{
		// ԭ������
//		while(Ham.sales < Ham.production)
		// ���Դ���
		while(true)
		{
			//1����һ��
			try {sleep(1000);} 
			catch (InterruptedException e) {e.printStackTrace();}
			sell();
			

			// ���Դ���
//			System.out.println("sales:"+Ham.sales+",production"+Ham.production);

			if(Ham.production >= Ham.totalmaterial) {
				System.out.println("ӪҵԱ���˿�������,����ȫ�����ˣ���");
				break;
			}
		}
	}
}

public class TestLock4
{
	public static void main(String[] args)
	{
		Hmaker maker = new Hmaker();
		Hassistant assistant = new Hassistant();
		maker.setName("��");
		maker.start();
		assistant.start();
	}
}
