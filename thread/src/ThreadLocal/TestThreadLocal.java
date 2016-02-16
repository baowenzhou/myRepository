package ThreadLocal;

public class TestThreadLocal {
//    ThreadLocal<Long> longLocal = new ThreadLocal<Long>();
//    ThreadLocal<String> stringLocal = new ThreadLocal<String>();
 
	// 若不执行set，则需初始化
    ThreadLocal<Long> longLocal = new ThreadLocal<Long>() {
    	protected Long initialValue() {
            return Thread.currentThread().getId();
        };
    };
    ThreadLocal<String> stringLocal = new ThreadLocal<String>() {
    	protected String initialValue() {
            return Thread.currentThread().getName();
        };
    };
     
    public void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }
     
    public long getLong() {
        return longLocal.get();
    }
     
    public String getString() {
        return stringLocal.get();
    }
     
    public static void main(String[] args) throws InterruptedException {
        final TestThreadLocal test = new TestThreadLocal();
         
         
        test.set();//空时会创建对象，若不执行，则报空指针异常
        System.out.println(test.getLong());
        System.out.println(test.getString());
     
         
        Thread thread1 = new Thread(){
            public void run() {
                test.set();//空时会创建对象，若不执行，则报空指针异常
                System.out.println(test.getLong());
                System.out.println(test.getString());
            };
        };
        thread1.start();
        thread1.join();
         
        System.out.println(test.getLong());
        System.out.println(test.getString());
    }
}