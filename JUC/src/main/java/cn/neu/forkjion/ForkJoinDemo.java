package cn.neu.forkjion;

import java.util.concurrent.RecursiveTask;

/**
 * 如何使用forkjoin
 * 1.forkJoinPool通过它来执行
 * 2.计算任务forkJoinPool.execute(ForkJoinTask task)
 * 3.计算类要继承ForkJoinTask
 */
public class ForkJoinDemo extends RecursiveTask<Long> {
    private long start;
    private long end;

    //临界值
    private long temp = 10000;

    public ForkJoinDemo(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if ((end - start) > temp) {
            long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else { //forkJion
            long middle = (start + end) / 2;//中间值
            ForkJoinDemo task1 = new ForkJoinDemo(start, middle);
            task1.fork();//拆分任务，把任务压入线程队列
            ForkJoinDemo task2 = new ForkJoinDemo(middle + 1, middle);
            task2.fork();
            return task1.join() + task2.join();
        }
    }
}
