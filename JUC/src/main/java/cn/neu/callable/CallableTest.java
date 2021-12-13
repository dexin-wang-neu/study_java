package cn.neu.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //new Thread(new MyThread()).start();//传统的多线程方式  类实现Runnable(功能有限，没有返回值)
        //使用Callable接口
        //怎么启动Callable,因为Thread只能接收Runnable类型
        //Runnable的实现类有FutureTask,FutureTask可以接收Callable和Runnable的构造参数，所以能够启动Callable的线程。
        //1.new Thread(new Runnable()).start();
        //2.new Thread(new FutureTask<V>(Callable)).start();
        new Thread().start();//怎么启动

        MyThread myThread = new MyThread();
        FutureTask futureTask = new FutureTask(myThread); //适配类
        new Thread(futureTask, "A").start();
        new Thread(futureTask, "B").start();//结果会被缓存，提高效率
        String o = (String) futureTask.get();//这个get方法可能会产生阻塞！把他放到最后，或者使用异步通信来处理
        System.out.println(o);

    }
}

class MyThread implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("call()");
        return "123456";
    }
}