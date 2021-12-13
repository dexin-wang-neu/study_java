package cn.neu.pool;

import java.util.concurrent.*;

//Executors 工具类、三大方法
//使用了线程池之后，使用线程池来创建线程
//AbortPolicy();//银行满了，还有人进来，不处理这个人的，抛出异常
//CallerRunsPolicy());//哪来的去哪里
//DiscardPolicy());//队列满了，丢掉任务，不会抛出异常
//DiscardOldestPolicy());//队列满了，尝试去和最早的竞争，也不会抛出异常
public class Demo01 {
    public static void main(String[] args) {
        // ExecutorService threadPool = Executors.newSingleThreadExecutor();//单个线程
        //ExecutorService threadPool = Executors.newFixedThreadPool(5);//创建一个固定的线程池的大小
        //ExecutorService threadPool = Executors.newCachedThreadPool();//可伸缩的线程池大小

        //自定义线程池
        //最大线程到底如何定义
        //1.CPU密集型  ，几核的CPU就是几个，可以保持CPU效率最高
        //2.IO密集型   ，程序有15个大型任务，io十分占用资源！判断你程序中十分耗IO的线程，设置大于他就行
        int cpuCount = Runtime.getRuntime().availableProcessors();//获取CPU核数
        ExecutorService threadPool = new ThreadPoolExecutor(2,
                5,
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());//队列满了，尝试去和最早的竞争，也不会抛出异常
        try {
            //最大承载：DEQUE + MAX
            for (int i = 1; i <= 9; i++) {
                //使用了线程池之后，使用线程池来创建线程
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "ok");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //线程池用完、程序结束、关闭线程池
            threadPool.shutdown();
        }
    }
}
