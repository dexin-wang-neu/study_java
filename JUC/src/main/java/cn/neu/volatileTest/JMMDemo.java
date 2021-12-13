package cn.neu.volatileTest;

import java.util.concurrent.TimeUnit;

public class JMMDemo {
    private volatile static int num = 0;//不加volatile程序就会死循环，加了保证可见性

    public static void main(String[] args) {
        new Thread(() -> {  //线程对主内存的变化不知道的
            while (num == 0) {

            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        num = 1;
        System.out.println(num);
    }
}
