package cn.neu.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 8锁：就是关于锁的8个问题（有延迟的情况）
 * 5.增加两个静态的同步方法，只有一个对象
 * 6.两个对象，两个静态同步方法，
 */
public class Test3 {
    public static void main(String[] args) {
        //两个对象的Class类模板只有一个,static,锁的是模板
        Phone3 phone1 = new Phone3();
        Phone3 phone2 = new Phone3();
        //
        new Thread(() -> {
            phone1.sendSms();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            phone2.call();
        }, "B").start();
    }
}

//Phone3只有唯一的一个Class对象
class Phone3 {
    //static 静态方法
    //类一加载就有了！Class 模板
    //锁的是Class
    public static synchronized void sendSms() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    public static synchronized void call() {
        System.out.println("打电话");
    }

}