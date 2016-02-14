package Lock;

/**
* 创建一个汉堡包盒子的类
* totalmaterial指所有的能做汉堡包的材料
 * box是一个监视器对象
 * sales是指销售了多少个汉堡包
 * production是指总共有多少个汉堡包
 */
class Ham
{
	static Object box = new Object();
	static int totalmaterial = 10;
	static int sales = 0;
	// 原来代码
//	static int production = 5;
	// 调试代码
	static int production = 1;

}

/**
 * 这是一个厨师线程类
 * make方法使用了一个同步块，在这个函数里会不断地生产汉堡包
 * run方法就是线程需要运行的内容
 * 使用了循环语句来合格证在汉堡包材料用完之前，不断的生产汉堡包
 * 使用判断语句判断只要有汉堡包，厨师就能通知营业员可以卖了
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
				// 原来代码
//				System.out.println("厨师" + getName()+ ":"+ "汉堡来了（总共" + 
//						(Ham.production - Ham.sales) + "个）");
				
				// 调试代码
				System.out.println("厨师" + getName()+ ":"+ "汉堡来了（第" + 
						(Ham.production) + "个）");
			}
			//3秒做一个
			try {sleep(3000);} 
			catch (InterruptedException e){e.printStackTrace();}
			make();
		}
	}
}

/**
 *这是一个营业员线程类
 *sell函数是营业员卖汉堡的方法
 *run方法就是线程需要运行的内容
 *使用判断语句来判断汉堡包盒子里面是否还有汉堡包
 *在run方法内使用循环语句来使得营业员在盒子里有汉堡的情况下不断地卖
 */
class Hassistant extends Thread
{
	public void sell()
	{
		
		// 原来代码
//		if(Ham.production == 0)
//		{
//			System.out.println("营业员：顾客朋友们，请稍微等一下，汉堡没了！！");
//		}
//		
//		synchronized(Ham.box) {//没有同步块，会有java.lang.IllegalMonitorStateException异常
//			try {Ham.box.wait();} 
//			catch (InterruptedException e) {e.printStackTrace();}
//			Ham.sales++;
//			System.out.println("营业员：顾客好，汉堡上来了，（总共卖了:"+ Ham.sales +"个）");
//		}
		
		// 调试代码
		synchronized(Ham.box) {
			if(Ham.production - Ham.sales <= 0)
			{
				System.out.println("营业员：顾客朋友们，请稍微等一下，汉堡没了！！");
				
				try {Ham.box.wait();} 
				catch (InterruptedException e) {e.printStackTrace();}
			}
		
			
			Ham.sales++;
			System.out.println("营业员：顾客好，汉堡上来了，（总共卖了:"+ Ham.sales +"个）");
		}
	}
	
	public void run()
	{
		// 原来代码
//		while(Ham.sales < Ham.production)
		// 调试代码
		while(true)
		{
			//1秒卖一个
			try {sleep(1000);} 
			catch (InterruptedException e) {e.printStackTrace();}
			sell();
			

			// 调试代码
//			System.out.println("sales:"+Ham.sales+",production"+Ham.production);

			if(Ham.production >= Ham.totalmaterial) {
				System.out.println("营业员：顾客朋友们,汉堡全卖完了！！");
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
		maker.setName("甲");
		maker.start();
		assistant.start();
	}
}
