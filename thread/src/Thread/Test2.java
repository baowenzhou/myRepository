package Thread;


public class Test2 {
//ÓĞÈ¤µÄ²âÊÔ
    public void test3(){
        Thread thread = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    System.out.println("Runnable run method is runing~~~");
                }
            }
        ){
            @Override
            public void run() {
                System.out.println("Thread run method is runing~~~");
            }
        };
        thread.start();
    }
    
    public static void main(String[] args) {
        System.out.println("Main Thread£º" + Thread.currentThread().getName() + " is run ");
        Test2 test = new Test2();
        test.test3();
    }
    
}