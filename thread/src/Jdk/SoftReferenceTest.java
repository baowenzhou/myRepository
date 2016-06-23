package Jdk;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class SoftReferenceTest {
	public static void main(String[] args) {
		String str = new String("hello"); //��
		ReferenceQueue<String> rq = new ReferenceQueue<String>(); //��
		WeakReference<String> wf = new WeakReference<String>(str, rq); // ��
		SoftReference<String> sr = new SoftReference<String>(str, rq); // ��
		str=null; //��
		//���δߴ��������������������"hello"���󱻻��յĿ�����
		System.gc(); //��
		System.gc(); //��
		System.gc(); //��

		String str1=wf.get(); //�� ����"hello"���󱻻��գ�str1Ϊnull
		String str2=sr.get(); //�� ����"hello"���󱻻��գ�str1Ϊnull

		System.out.println(str1);
		System.out.println(str2);

//		Reference<String> ref=(Reference<String>) rq.poll(); //��
	}
}
