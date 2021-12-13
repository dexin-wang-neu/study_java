package cn.neu.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 8锁：就是关于锁的8个问题（有延迟的情况）
 * 7.一个静态同步方法，一个普通同步方法
 * 8.一个静态同步方法，一个普通同步方法，两个对象
 */
public class Test4 {
    public static void main(String[] args) {
        //两个对象的Class类模板只有一个,static,锁的是模板
        Phone4 phone1 = new Phone4();
        Phone4 phone2 = new Phone4();
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


class Phone4 {
    //静态的同步方法  锁的是Class类模板
    public static synchronized void sendSms() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    //普通同步方法  锁的是调用者对象
    public synchronized void call() {
        System.out.println("打电话");
    }
}