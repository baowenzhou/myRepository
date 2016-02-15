package Lock;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * ʹ��Lock��ʵ�������ߺ�����������
 * 
 * @author ����
 *
 */
public class ProducerConsumer {
    public static void main(String[] args) {
        Basket b = new Basket();
        Product p = new Product(b);
        Consumer c = new Consumer(b);
        Consumer c1 = new Consumer(b);
        new Thread(p).start();
        new Thread(c).start();
        new Thread(c1).start();
    }
}
//��ͷ
class ManTou{
    int id;
    public ManTou(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "ManTou"+id;
    }
}

//װ��ͷ������
class Basket{
    int max = 6;
    LinkedList<ManTou> manTous = new LinkedList<ManTou>();
    Lock lock = new ReentrantLock(); //������
    Condition full = lock.newCondition();  //������������Ƿ�����Conditionʵ��
    Condition empty = lock.newCondition(); //������������Ƿ�յ�Conditionʵ��
    //�������������ͷ
    public void push(ManTou m){
        lock.lock();
        try {
            while(max == manTous.size()){
                System.out.println("���������ģ������������...");
                full.await();
            }
            manTous.add(m);
            empty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
    //����������ȡ��ͷ
    public ManTou pop(){
        ManTou m = null;
        lock.lock();
        try {
            while(manTous.size() == 0){
                System.out.println("�����ǿյģ�������ٳ�...");
                empty.await();
            }
            m = manTous.removeFirst();
            full.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lock.unlock();
            return m;
        }
    }
}
//������
class Product implements Runnable{
    Basket basket;
    public Product(Basket basket) {
        this.basket = basket;
    }
    public void run() {
        for (int i = 0; i < 40; i++) {
            ManTou m = new ManTou(i);
            basket.push(m);
            System.out.println("������"+m);
            try {
                Thread.sleep((int)(Math.random()*2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}

//������
class Consumer implements Runnable{
    Basket basket;
    public Consumer(Basket basket) {
        this.basket = basket;
    }
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep((int)(Math.random()*2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ManTou m = basket.pop();
            System.out.println("������"+m);
        }
    }
}