package cn.neu.add;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        //总数是6,必须要执行任务的手，再使用
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "GO OUT");
                countDownLatch.countDown();//数量减一
            }, String.valueOf(i)).start();
        }
        countDownLatch.await();//等待计数器归零，然后再向下执行
//        countDownLatch.countDown();//-1
        System.out.println("Closed door");
    }
}
