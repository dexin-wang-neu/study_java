package cn.neu.volatileTest;


import java.util.concurrent.atomic.AtomicInteger;

//不保证原子性
public class VDemo02 {
    //    private volatile static AtomicInteger num = 0;
    private volatile static AtomicInteger num = new AtomicInteger();

    public static void add() {
//        num ++;//不是一个原子性操作
        num.getAndIncrement();//原子性的+1方法，底层用到CAS
    }

    public static void main(String[] args) {

        //理论上num结果为20000
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount() > 2) { //main gc
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "  " + num);
    }
}
