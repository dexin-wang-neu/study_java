package cn.neu.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 8锁：就是关于锁的8个问题（有延迟的情况）
 * 3. 增加了一个普通方法时，先执行hello？普通方法
 * 4. 两个对象时，先 执行没延迟的打电话
 */
public class Test2 {
    public static void main(String[] args) {
        //两个对象，两把锁
        Phone2 phone1 = new Phone2();
        Phone2 phone2 = new Phone2();
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
//            phone.call();
//            phone.hello();
            phone2.call();
        }, "B").start();
    }
}

class Phone2 {
    //synchronized 修饰方法，锁的对象是方法的调用者 phone ！
    //两个方法用的是同一把锁，谁先拿到先执行
    public synchronized void sendSms() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    public synchronized void call() {
        System.out.println("打电话");
    }

    //这里没有锁，不是同步方法，不受锁的影响
    public void hello() {
        System.out.println("hello");
    }
}