package cn.neu;

/**
 * 线程是一个单独的资源类，没有任何操作
 */
public class SaleTickedDemo01 {
    public static void main(String[] args) {
        //并发：多个线程操作同一个资源类
        final Ticket ticket = new Ticket();

        //@FunctionalInterface  函数式接口，jdk1.8后用lambda表达式    ( 参数 ) ->{  代码  }简化
//        new Thread(new Runnable(){
//            @Override
//            public void run(){
//
//            }
//        }).start();
        new Thread(() -> {
            for (int i = 1; i < 60; i++) {
                ticket.sale();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 1; i < 60; i++) {
                ticket.sale();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 1; i < 60; i++) {
                ticket.sale();
            }
        }, "C").start();
    }
}


//资源类  OOP
class Ticket {
    private int number = 50;

    //买票方式
    // synchronize本质  ：队列  锁
    public synchronized void sale() {
        if (number > 0) {
            System.out.println(Thread.currentThread().getName() + "卖出了第" + (number--) + "张票，剩余：" + number);
        }
    }
}
