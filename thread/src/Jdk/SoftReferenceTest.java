package Jdk;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class SoftReferenceTest {
	public static void main(String[] args) {
		String str = new String("hello"); //①
		ReferenceQueue<String> rq = new ReferenceQueue<String>(); //②
		WeakReference<String> wf = new WeakReference<String>(str, rq); // ③
		SoftReference<String> sr = new SoftReference<String>(str, rq); // ③
		str=null; //④
		//两次催促垃圾回收器工作，提高"hello"对象被回收的可能性
		System.gc(); //⑤
		System.gc(); //⑥
		System.gc(); //⑥

		String str1=wf.get(); //⑦ 假如"hello"对象被回收，str1为null
		String str2=sr.get(); //⑦ 假如"hello"对象被回收，str1为null

		System.out.println(str1);
		System.out.println(str2);

//		Reference<String> ref=(Reference<String>) rq.poll(); //⑧
	}
}
