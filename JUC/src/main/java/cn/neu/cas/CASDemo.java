package cn.neu.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class CASDemo {

    //CAS  compareAndSet比较并交换
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(2021);

        //期望、更新
        //public final boolean compareAndSet(int expect, int update)
        //如果期望的值达到了，就更新，否则不更新,CAS是CPU的并发原语
        atomicInteger.compareAndSet(2021, 2022);
        System.out.println(atomicInteger.get());
        System.out.println("===================================");
        //原子引用
        new AtomicReference<>();//基本类
        AtomicStampedReference<Object> atomicStampedReference = new AtomicStampedReference<>(1, 1);//带时间的
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();//获得版本号
            System.out.println("a1=>" + stamp);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //version + 1
            System.out.println(atomicStampedReference.compareAndSet(1, 2,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));

            System.out.println("a2=>" + atomicStampedReference.getStamp());

            System.out.println(atomicStampedReference.compareAndSet(2, 1,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));

            System.out.println("a3=>" + atomicStampedReference.getStamp());
        }, "A").start();

        //乐观锁的原理一样
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();//获得版本号
            System.out.println("b1=>" + stamp);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(atomicStampedReference.compareAndSet(1, 66,
                    stamp, stamp + 1));

            System.out.println("b2=>" + atomicStampedReference.getStamp());
        }, "B").start();
    }
}
