## 1.什么是JUC

![image-20210620105003163](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620105003163.png)

java.util工具包

业务：普通的线程代码 Thread

Runnable:m:没有返回值，效率相比callable较低

![image-20210620105330818](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620105330818.png)

## 2.线程和进程

进程：一个程序，QQ.exe Music.exe  程序的集合；

* 一个进程往往由多个线程，至少包含一个！
* Java默认有两个线程：Main  GC(垃圾回收)

线程：开了一个进程Typora，写字，自动保存（线程负责）

对于Java而言：Thread、Runnable、Callable

>  ｊａva不能开启线程

```java
    public synchronized void start() {
        /**
         * This method is not invoked for the main method thread or "system"
         * group threads created/set up by the VM. Any new functionality added
         * to this method in the future may have to also be added to the VM.
         *
         * A zero status value corresponds to state "NEW".
         */
        if (threadStatus != 0)
            throw new IllegalThreadStateException();

        /* Notify the group that this thread is about to be started
         * so that it can be added to the group's list of threads
         * and the group's unstarted count can be decremented. */
        group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                /* do nothing. If start0 threw a Throwable then
                  it will be passed up the call stack */
            }
        }
    }
	//本地方法，用C++写的,Java无法直接操作硬件
    private native void start0();
```

> 并发、并行

并发编程：并发、并行

并发：多线程操作同一个资源

​	CPU一核，模拟出来多条线程、快速交替

并行：多核CPU，多个线程可以同时执行

```java
public class Test1 {
    public static void main(String[] args) {
        //获取CPU的核数
        //cpu 密集型 IO密集型
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
```

并发编程的本质：**充分利用CPU的资源**

>  线程有几个状态

```java
public enum State {
   //新生
    NEW,

    // 运行
    RUNNABLE,

    //阻塞
    BLOCKED,

    //等待，死等
    WAITING,

   //超时等待
    TIMED_WAITING,

    //终止
    TERMINATED;
}
```



> wait/sleep 区别

1. 来自不同的类

   wait => Object

   sleep => Thread

2. **关于锁的释放**

   wait会释放锁，sleep不会释放

3. **使用的范围不同**

   wait:必须在同步代码块

   sleep可以在任何地方

4. **是否需要捕获异常**

   wait不需要捕获异常

   sleep必须捕获异常



## 3.lock锁

> 传统Synchronized

```java
/**
 * 线程是一个单独的资源类，没有任何操作
 *
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
        new Thread(()->{
            for (int i = 1;i < 60;i++){
                ticket.sale();
            }
            },"A").start();
        new Thread(() ->{
            for (int i = 1;i < 60;i++){
            ticket.sale();
        }},"B").start();
        new Thread(() ->{
            for (int i = 1;i < 60;i++){
            ticket.sale();
        }},"C").start();
    }
}


//资源类  OOP
class Ticket{
    private int number = 50;

    //买票方式
    // synchronize本质  ：队列  锁
    public synchronized void sale(){
        if(number > 0){
            System.out.println(Thread.currentThread().getName() +"卖出了第"+(number--)+"张票，剩余："+number);
        }
    }
}
```



> Lock 接口

![image-20210620115435601](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620115435601.png)

![image-20210620115548843](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620115548843.png)

![image-20210620115753684](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620115753684.png)

公平锁：先来后到

**非公平锁：可以插队（默认）**

```java
public class SaleTickedDemo02{
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
        new Thread(()->{
            for (int i = 1;i < 60;i++){
                ticket.sale();
            }
            },"A").start();
        new Thread(() ->{
            for (int i = 1;i < 60;i++){
            ticket.sale();
        }},"B").start();
        new Thread(() ->{
            for (int i = 1;i < 60;i++){
            ticket.sale();
        }},"C").start();
    }
}


//lock三部曲
//1.new ReentrantLock();
//2.  lock.lock();//加锁
//3.lock.unlock();//解锁
class Ticket2{
    private int number = 50;

    //锁
    Lock lock = new ReentrantLock();

    public synchronized void sale(){
        lock.lock();
        try {
            //业务代码
            if(number > 0){
                System.out.println(Thread.currentThread().getName() +"卖出了第"+(number--)+"张票，剩余："+number);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
}
```

> synchronized 和lock的区别

1. Synchronized 内置关键字，Lock是一个Java类
2. Synchronized无法判断获取锁的状态，Lock可以判断是否获取到了锁
3. Synchronized会自动释放锁，lock必须要手动释放锁，如果不释放，**死锁**
4. Synchronized 线程1（获得锁，阻塞）、线程2（等待，傻傻的等），lock就不会傻傻的等
5. Synchronized可重入锁，不可以中断的，非公平；lock，可重入锁，可以判断锁，非公平（可以自己配置）
6. Synchronized适合锁少量的代码同步问题，lock适合锁大量的同步代码



> 锁是什么，如何判断锁的是谁





## 4.生产者和消费者

> 生产者和消费者问题 Synchronized版：

```java
/**
 * 线程之间的通信问题：生产者和消费者问题!  等待唤醒，通知唤醒
 * 线程交替执行  A  B  操作同一个变量  num = 0
 *
 * A num + 1
 * B num - 1
 *
 *
 */
public class A {
    public static void main(String[] args) {
        Data data = new Data();
        new Thread(()->{
            for (int i = 0;i < 10;i++){
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
    }

    //资源类
    //判断等待，业务，通知
    static class Data{
        private int number = 0;

        //+1操作
        public synchronized void increment() throws InterruptedException {
            if(number != 0){
                //等待
                this.wait();
            }
            number ++;
            System.out.println(Thread.currentThread().getName()+"=>"+number);
            //通知其他线程，加操作完成
            this.notifyAll();
        }

        //-1 操作
        public synchronized void decrement() throws InterruptedException {
            if(number ==0){
                this.wait();
            }
            number --;
            System.out.println(Thread.currentThread().getName()+"=>"+number);
            //通知其他线程，减操作完成
            this.notifyAll();
        }
    }
}
```

> 问题 A B C D 四个线程

![image-20210620165915100](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620165915100.png)

**if 改为 while**(上面的if有可能使多个线程都拿到锁，然后都释放，执行加减操作)

```java
static class Data{
    private int number = 0;

    //+1操作
    public synchronized void increment() throws InterruptedException {
        while(number != 0){
            //等待
            this.wait();
        }
        number ++;
        System.out.println(Thread.currentThread().getName()+"=>"+number);
        //通知其他线程，加操作完成
        this.notifyAll();
    }

    //-1 操作
    public synchronized void decrement() throws InterruptedException {
        while(number ==0){
            this.wait();
        }
        number --;
        System.out.println(Thread.currentThread().getName()+"=>"+number);
        //通知其他线程，减操作完成
        this.notifyAll();
    }
}
```



> JUC版的生产者和消费者问题

![image-20210620170957316](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620170957316.png)

![image-20210620170831801](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620170831801.png)

实现：

```java
/**
 * A执行完 调用B,
 * B执行完 调用C,
 * C执行完 调用A,
 */
public class C {
    public static void main(String[] args) {
        Data3 data3 = new Data3();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data3.printA();
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data3.printB();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data3.printC();
            }
        },"C").start();
    }
}

class Data3{    //资源类
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();
    private int number = 1;//1A  2B  3C

    public void printA(){
        lock.lock();
        try{
            while (number != 1){
                condition1.await();
            }
            System.out.println(Thread.currentThread().getName()+"=>AAAAAAAAA");
            //唤醒，唤醒指定的人 B
            number = 2;
            condition2.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void printB(){
        lock.lock();
        try {
            while (number != 2){
                condition2.await();
            }
            //唤醒C
            System.out.println(Thread.currentThread().getName()+"=>BBBBBBBBB");
            number = 3;
            condition3.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void printC(){
        lock.lock();
        try {
            while (number != 3){
                condition3.await();
            }
            //唤醒C
            System.out.println(Thread.currentThread().getName()+"=>CCCCCCCC");
            number = 1;
            condition1.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
```

## 5. 8锁现象

如何判断锁的是谁！

对象、Class

```java
/**
 * 8锁：就是关于锁的8个问题
 * 1.标准情况下，两个线程是先打印发短信还是打电话  1.发短信   2.打电话
 * 2.sendSms延迟4秒时，两个线程是先打印发短信还是打电话  1.发短信   2.打电话
 */
public class Test1 {
    public static void main(String[] args) {
        Phone phone = new Phone();

        //
        new Thread(()->{
            phone.sendSms();
        },"A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone.call();
        },"B").start();
    }
}
class Phone{
    //synchronized 修饰方法，锁的对象是方法的调用者 phone ！
    //两个方法用的是同一把锁，谁先拿到先执行
    public synchronized void  sendSms(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    public synchronized void  call(){
        System.out.println("打电话");
    }
}
```

```java
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
        new Thread(()->{
            phone1.sendSms();
        },"A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
//            phone.call();
//            phone.hello();
            phone2.call();
        },"B").start();
    }
}

class Phone2{
    //synchronized 修饰方法，锁的对象是方法的调用者 phone ！
    //两个方法用的是同一把锁，谁先拿到先执行
    public synchronized void  sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    public synchronized void  call(){
        System.out.println("打电话");
    }

    //这里没有锁，不是同步方法，不受锁的影响
    public void  hello(){
        System.out.println("hello");
    }
}
```

```java
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
        new Thread(()->{
            phone1.sendSms();
        },"A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone2.call();
        },"B").start();
    }
}

//Phone3只有唯一的一个Class对象
class Phone3{
    //static 静态方法
    //类一加载就有了！Class 模板
    //锁的是Class
    public static synchronized void  sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    public static synchronized void  call(){
        System.out.println("打电话");
    }

}
```

```java
/**
 * 8锁：就是关于锁的8个问题（有延迟的情况）
    7.一个静态同步方法，一个普通同步方法
    8.一个静态同步方法，一个普通同步方法，两个对象
 */
public class Test4 {
    public static void main(String[] args) {
        //两个对象的Class类模板只有一个,static,锁的是模板
        Phone4 phone1 = new Phone4();
        Phone4 phone2 = new Phone4();
        //
        new Thread(()->{
            phone1.sendSms();
        },"A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            phone2.call();
        },"B").start();
    }
}


class Phone4{
    //静态的同步方法  锁的是Class类模板
    public static synchronized void  sendSms(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    //普通同步方法  锁的是调用者对象
    public synchronized void  call(){
        System.out.println("打电话");
    }
}
```

> 小节

new this 具体的对象

static Class 唯一的类模板

## 6.集合类不安全

> List不安全

```java
//ConcurrentModificationException
public class ListTest {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2", "3");
        //并发下，ArrayList是不安全的
        /*
         *  解决方案：
         * 1.  Vector<String> strings = new Vector<>();
         * 2.List<Object> synList = Collections.synchronizedList(new ArrayList<>());
         * 3.CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();
         */
//        ArrayList<String> slist = new ArrayList<>();
//        Vector<String> strings = new Vector<>();//1
//        List<Object> synList = Collections.synchronizedList(new ArrayList<>());//2

        //CopyOnWrite写入时复制  COW 计算机程序设计领域的一种优化策略
        //多个线程调用的时候，list,读取的时候，固定的，写入（覆盖）
        //在写入的时候避免覆盖，造成数据问题
        CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();//3
        //CopyOnWriteArrayList比Vector好在：用lock锁实现，Vector使用Synchronized实现效率低

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                copy.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(copy);
            },String.valueOf(i)).start();
        }
    }
}
```

> Set不安全

```java
/**
 * ConcurrentModificationException
 */
public class SetTest {
    public static void main(String[] args) {
//        Set<String> set = new HashSet<>();
        //解决方案1：Set<String> set = Collections.synchronizedSet(new HashSet<>());
        //解决方案2：
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0;i < 30;i++){
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }
}
```

HashSet的底层是什么？

```java
public HashSet() {
    map = new HashMap<>();
}

//add方法  本质就是map  key是无法重复的
 public boolean add(E e) {
      return map.put(e, PRESENT)==null;
 }
private static final Object PRESENT = new Object();//常量
```

> HashMap不安全

```java
public class MapTest {
    public static void main(String[] args) {
        //map 是这样用的吗？默认等价于什么？不是，工作中不用HashMap
//        HashMap<String, String> map = new HashMap<>();
        //加载因子，初始化容量 new HashMap<>(16,0.75);

        //解决方案
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }
}
```

## 7.Callable(简单)

![image-20210623103936109](.\JUC.assets\image-20210623103936109.png)

1. 多线程的第三种创建方式
2. 可以有返回值，可以抛出异常。
3. 方法不同，run()/call()

![image-20210623105117161](.\JUC.assets\image-20210623105117161-1624416678610.png)

![image-20210623105306057](.\JUC.assets\image-20210623105306057-1624416787146.png)

```java
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
        new Thread(futureTask,"A").start();
        String o = (String) futureTask.get();
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
```

细节：

1. 有缓存
2. 结果可能需要等待，会阻塞



## 8.常用的辅助类

### 8.1 CountDownLatch

![image-20210623110521611](.\JUC.assets\image-20210623110521611-1624417522603.png)

**计数辅助类**

```java
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        //总数是6,必须要执行任务的手，再使用
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"GO OUT");
                countDownLatch.countDown();//数量减一
            },String.valueOf(i)).start();
        }
        countDownLatch.await();//等待计数器归零，然后再向下执行
//        countDownLatch.countDown();//-1
        System.out.println("Closed door");
    }
}
```

原理：

==countDownLatch.countDown();==//数量减一

==countDownLatch.await();==//等待计数器归零，然后再向下执行

每次有线程调用countDown()数据量-1，假设计数器变为0，countDownLatch.await()就会被唤醒，继续执行。

### 8.2 CyclicBarrier

![image-20210623113200827](.\JUC.assets\image-20210623113200827-1624419127855.png)

```java
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("召唤神龙");
        });
        for (int i = 0; i < 7; i++) {
            final int temp = i;
            //lambda能拿到i吗
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"收集"+temp+"颗龙珠");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

### 8.3 Semaphore

semaphore:信号量

![image-20210623113953458](.\JUC.assets\image-20210623113953458-1624419594670.png)

```java
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        //线程数量，停车位！限流！
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                //acquire()得到
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"抢到车位");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName()+"离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();//release()释放
                }

            },String.valueOf(i)).start();
        }
    }
}
```

原理：

==semaphore.acquire();==获得，假设如果已经满了，等待，等待被释放为止！

==semaphore.release();==  释放 ，会将当前的信号量释放+1，然后唤醒等待的线程！

作用：多个共享资源互斥的使用！并发限流！



## 9.读写锁

==ReadWriteLock==

![image-20210623120523436](.\JUC.assets\image-20210623120523436-1624421126081.png)

```java
public class ReadWriteLockDemo {
    public static void main(String[] args) {
//        MyCache myCache = new MyCache();
        MyCache2 myCache = new MyCache2();
        //写入
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(()->{
                myCache.put(temp+"",temp+"");
            },String.valueOf(i)).start();
        }
        //读取
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(()->{
                myCache.get(temp+"");
            },String.valueOf(i)).start();
        }
    }
}
//加锁的
class MyCache2{

    private volatile Map<String,Object> map = new HashMap<>();
    //读写锁
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    //存，写
    public void put(String key,Object val){
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+"写入"+key);
            map.put(key,val);
            System.out.println(Thread.currentThread().getName()+"写入完毕");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readWriteLock.writeLock().unlock();
        }


    }
    //取，读
    public void get(String key){
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+"读取"+key);
            Object o = map.get(key);
            System.out.println(Thread.currentThread().getName()+"读取完毕");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readWriteLock.readLock().unlock();
        }
    }
}

/**
 * 自定义缓存
 */
class MyCache{
    private volatile Map<String,Object> map = new HashMap<>();

    //存，写
    public void put(String key,Object val){
        System.out.println(Thread.currentThread().getName()+"写入"+key);
        map.put(key,val);
        System.out.println(Thread.currentThread().getName()+"写入完毕");
    }
    //取，读
    public void get(String key){
        System.out.println(Thread.currentThread().getName()+"读取"+key);
        Object o = map.get(key);
        System.out.println(Thread.currentThread().getName()+"读取完毕");
    }
}
```



## 10.阻塞队列

阻塞队列：BlockingQueue<E>

![image-20210623152851695](.\JUC.assets\image-20210623152851695-1624433333035.png)

![image-20210623153243151](.\JUC.assets\image-20210623153243151-1624433564002.png)

BlockingQueue不是新东西

什么情况下会使用阻塞队列：多线程并发处理、线程池。

队列使用：

|     方式     | 抛出异常 | 有返回值，不抛出异常 | 阻塞等待 |                超时等待                |      |
| :----------: | :------: | :------------------: | :------: | :------------------------------------: | ---- |
|     添加     |   add    |        offer         |   put    |     offer("d",2,TimeUnit.SECONDS)      |      |
|     移除     |  remove  |         poll         |   take   | blockingQueue.poll(2,TimeUnit.SECONDS) |      |
| 检测队首元素 | element  |         peek         |    -     |                   -                    |      |
|              |          |                      |          |                                        |      |

1. 抛出异常

```java
public static void test1(){
    ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
    System.out.println(blockingQueue.add("a"));
    System.out.println(blockingQueue.add("b"));
    System.out.println(blockingQueue.add("c"));

    //IllegalStateException: Queue full
    //System.out.println(blockingQueue.add("d"));

    System.out.println("==============================");
    System.out.println(blockingQueue.remove());
    System.out.println(blockingQueue.remove());
    System.out.println(blockingQueue.remove());

    //java.util.NoSuchElementException
    //System.out.println(blockingQueue.remove());
}
```

2. 不会抛出异常

```java
public static void test2(){
    ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
    System.out.println(blockingQueue.offer("a"));
    System.out.println(blockingQueue.offer("b"));
    System.out.println(blockingQueue.offer("c"));

    System.out.println(blockingQueue.peek());
    //System.out.println(blockingQueue.offer("d"));//false，不抛出异常
    System.out.println("==============================");

    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.poll());
    //System.out.println(blockingQueue.poll());//null,不抛出异常

}
```

3. 阻塞等待

```java
public static void test3() throws InterruptedException {
    ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
    //一直阻塞
    blockingQueue.put("a");
    blockingQueue.put("b");
    blockingQueue.put("c");
    //blockingQueue.put("d");//队列没有位置了，一直阻塞

    System.out.println(blockingQueue.take());
    System.out.println(blockingQueue.take());
    System.out.println(blockingQueue.take());
    System.out.println(blockingQueue.take());//没有元素了，一直阻塞


}
```

4. 超时等待

```java
public static void test4() throws InterruptedException {
    ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
    //一直阻塞
    blockingQueue.offer("a");
    blockingQueue.offer("b");
    blockingQueue.offer("c");
    blockingQueue.offer("d",2,TimeUnit.SECONDS);//两秒后超时推出

    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.poll());
    System.out.println(blockingQueue.poll(2,TimeUnit.SECONDS));//两秒后超时推出


}
```



> SynchronizedQueue  同步队列

没有容量，进去一个元素必须等待取出来之后，才能再往里边存放一个元素

## 11.线程池（重点）

线程池：三大方法、七大参数、四种拒绝策略

> 池化技术

​    事先准备好一些资源，有人要用，就来这里拿，用完之后还给我。

线程池的好处：

	1. 降低资源消耗
	2. 提高相应速度（创建和销毁十分消耗资源）
	3. 方便管理

==**线程复用、可以控制最大并发数、管理线程**==

> 线程：三大方法

```java
//Executors 工具类、三大方法
//使用了线程池之后，使用线程池来创建线程
public class Demo01 {
    public static void main(String[] args) {
        // ExecutorService threadPool = Executors.newSingleThreadExecutor();//单个线程
        //ExecutorService threadPool = Executors.newFixedThreadPool(5);//创建一个固定的线程池的大小
        ExecutorService threadPool = Executors.newCachedThreadPool();//可伸缩的线程池大小
        try {
            for (int i = 0; i < 10; i++) {
                //使用了线程池之后，使用线程池来创建线程
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"ok");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //线程池用完、程序结束、关闭线程池
            threadPool.shutdown();
        }
    }
}
```

> 7大参数

```java
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}
public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
}
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
}

//本质 ThreadPoolExecutor
    public ThreadPoolExecutor(int corePoolSize,  //核心线程池大小
                              int maximumPoolSize,//最大核心线程池大小
                              long keepAliveTime,//超时了没有人调用就是释放
                              TimeUnit unit,		//超时单位
                              BlockingQueue<Runnable> workQueue,//阻塞队列
                              ThreadFactory threadFactory,		//线程工厂：创建线程的，一般不用动
                              RejectedExecutionHandler handler) //拒绝策略{
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
```

> 创建一个线程池

```java
//自定义线程池
ExecutorService threadPool = new ThreadPoolExecutor(2,
            5,
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());//队列满了，尝试去和最早的竞争，也不会抛出异常
```

>  ==4种拒绝策略==

![image-20210623170419807](.\JUC.assets\image-20210623170419807-1624439061628.png)

```java
//AbortPolicy();//银行满了，还有人进来，不处理这个人的，抛出异常
//CallerRunsPolicy());//哪来的去哪里
//DiscardPolicy());//队列满了，丢掉任务，不会抛出异常
//DiscardOldestPolicy());//队列满了，尝试去和最早的竞争，也不会抛出异常
```

> IO密集型   CPU密集型(线程池的最大线程数量)

```java
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
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"ok");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //线程池用完、程序结束、关闭线程池
            threadPool.shutdown();
        }
    }
}
```

## 12.四大函数式接口

新时代程序员：lambda表达式、链式编程、函数式接口、Stream流式计算

> 函数式接口：只有一个方法的接口，方法是抽象的

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
//超级多@FunctionalInterface
//简化编程模型，在西南版本的框架底层大量应用
//foreach (消费者类的函数式接口)
```

<img src=".\JUC.assets\image-20210623172602095-1624440363998.png" alt="image-20210623172602095" style="zoom: 67%;" />

**代码测试：**

==函数式接口==：

![image-20210623173006361](.\JUC.assets\image-20210623173006361-1624440607309.png)



```java
import java.util.function.Function;

/**
 * Function 函数型接口
 */
public class Demo01 {
    public static void main(String[] args) {
        //工具类：输出输入的值
//       Function function = new Function<String,String>() {
//            @Override
//            public String apply(String str) {
//                return str;
//            }
//        };
        Function function = (Function<String, String>) str -> str;
        System.out.println(function.apply("adb"));
    }
}
```



![image-20210623173645917](.\JUC.assets\image-20210623173645917-1624441007623.png)

 ==断定型接口：有一个输入参数，返回值只能是 布尔值==

```java
/**
 * 断定型接口：有一个输入参数，返回值只能是 布尔值！
 */
public class Demo02 {
    public static void main(String[] args) {
        //判断字符串是否为空
       // Predicate<String> predicate = new Predicate<String>() {
        //    @Override
        //    public boolean test(String str) {
         //       return str.isEmpty();
        //    }
       // };
        Predicate<String> predicate = str -> str.isEmpty();
        System.out.println(predicate.test(""));
    }
}
```

> Consumer消费型接口:只有输入，没有返回值

![image-20210623174253777](.\JUC.assets\image-20210623174253777-1624441374663.png)



```java
package cn.neu.function;

import java.util.function.Consumer;

public class Demo03 {
    public static void main(String[] args) {
//        Consumer<String> consumer = new Consumer<String>(){
//            @Override
//            public void accept(String str) {
//                System.out.println(str);
//            }
//        };
        Consumer<String> consumer = str -> System.out.println(str);
       consumer.accept("123");
    }
}
```

> Supplier供给型接口  没有参数，只有返回值

![image-20210623182846117](.\JUC.assets\image-20210623182846117-1624444127530.png)

```java
import java.util.function.Supplier;

public class Demo04 {
    public static void main(String[] args) {
//        Supplier<String> supplier = new Supplier<String>(){
//            @Override
//            public String get() {
//                return "123";
//            }
//        };
        Supplier<String> supplier = () -> "123";
        System.out.println(supplier.get());
    }
}
```

## 13.Stream流式计算

> 什么是Stream流式计算

​    ![image-20210623183613710](.\JUC.assets\image-20210623183613710-1624444575195.png)

## 14.ForkJoin

> 什么是ForkJoin

   在JDK1.7中出现，并行执行任务！提高效率

​    类似于分治算法。

>  ForkJoin特点：工作窃取

   这个里边维护的都是双端队列。

>  ForkJoin操作

```java
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
    public ForkJoinDemo(long start,long end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if((end - start)>temp){
            long sum = 0L;
            for (Long i = start;i <= end;i++){
                sum+=i;
            }
            return sum;
        }else { //forkJion
            long middle = (start + end) / 2;//中间值
            ForkJoinDemo task1 = new ForkJoinDemo(start, middle);
            task1.fork();//拆分任务，把任务压入线程队列
            ForkJoinDemo task2 = new ForkJoinDemo(middle+1, middle);
            task2.fork();
            return task1.join()+task2.join();
        }
    }
}

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test3();
    }
    //
    public static void test1(){
        long sum = 0;
        long start = System.currentTimeMillis();

        for (long i = 1;i <= 10_0000_0000;i++){
            sum+=i;
        }
        long end = System.currentTimeMillis();
        System.out.println("sum="+sum+", 时间:"+(end-start));
    }
    public static void test2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinDemo(0, 10_0000_0000);
        ForkJoinTask<Long> submit = forkJoinPool.submit(task);//提交任务
        Long sum = submit.get();
        long end = System.currentTimeMillis();
        System.out.println("sum="+sum+", 时间:"+(end-start));
    }
    //stream并行流
    public static void test3(){
        long start = System.currentTimeMillis();
        long sum = LongStream.rangeClosed(0,10_0000_0000).parallel().reduce(0,Long::sum);
        long end = System.currentTimeMillis();
        System.out.println("sum="+sum+", 时间:"+(end-start));
    }
}
```

## 15.异步回调

> Future 设计的初衷：对将来的某个事件的结果进行建模

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 异步调用：CompletableFuture
 *
 */
public class Demo01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //没有返回值的runAysnc异步回调
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName()+"runAsync=>Void");
//        });
//        System.out.println("11111111");
//        completableFuture.get();//阻塞获取执行结果
//        System.out.println("22222222");

        //有返回值的异步回调
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"supplyAsync=>Integer");
            return 1234;
        });
        System.out.println(completableFuture.whenComplete((t, u) -> {
            System.out.println("t=>" + t);//正常的返回结果
            System.out.println("u=>" + u);//错误信息：
        }).exceptionally((e) -> {
            System.out.println(e.getMessage());
            return 500;
        }).get());
    }
}
```



## 16.JMM

> Volatile理解：是Java虚拟机提供的**轻量级的同步机制**

    1.  保证可见性
       2.  不保证原子性 
       3.  禁止指令重排

> JMM: java 内存模型，不存在的东西，概念，约定。

关于JMM的同步约定：

1. 线程解锁前：必须把共享变量==立刻==刷回内存
2. 线程加锁前：必须读取主存中的最新值到工作内存中。
3. 加锁和解锁是同一把锁。

线程：**工作内存**，**主内存**

8种操作

![image-20210624110402520](.\JUC.assets\image-20210624110402520-1624503843536.png)

![image-20210624110715196](.\JUC.assets\image-20210624110715196.png)

关于主内存与工作内存之间的具体交互协议，即一个变量如何从主内存拷贝到工作内存、如何从工作内存同步到主内存之间的实现细节，Java内存模型定义了以下八种操作来完成：

- **lock（锁定）**：作用于主内存的变量，把一个变量标识为一条线程独占状态。
- **unlock（解锁）**：作用于主内存变量，把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定。
- **read（读取）**：作用于主内存变量，把一个变量值从主内存传输到线程的工作内存中，以便随后的load动作使用
- **load（载入）**：作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作内存的变量副本中。
- **use（使用）**：作用于工作内存的变量，把工作内存中的一个变量值传递给执行引擎，每当虚拟机遇到一个需要使用变量的值的字节码指令时将会执行这个操作。
- **assign（赋值）**：作用于工作内存的变量，它把一个从执行引擎接收到的值赋值给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作。
- **store（存储）**：作用于工作内存的变量，把工作内存中的一个变量的值传送到主内存中，以便随后的write的操作。
- **write（写入）**：作用于主内存的变量，它把store操作从工作内存中一个变量的值传送到主内存的变量中。

Java内存模型还规定了在执行上述八种基本操作时，必须满足如下规则：

- 如果要把一个变量从主内存中复制到工作内存，就需要按顺寻地执行read和load操作， 如果把变量从工作内存中同步回主内存中，就要按顺序地执行store和write操作。但Java内存模型只要求上述操作必须按顺序执行，而没有保证必须是连续执行。
- 不允许read和load、store和write操作之一单独出现
- 不允许一个线程丢弃它的最近assign的操作，即变量在工作内存中改变了之后必须同步到主内存中。
- 不允许一个线程无原因地（没有发生过任何assign操作）把数据从工作内存同步回主内存中。
- 一个新的变量只能在主内存中诞生，不允许在工作内存中直接使用一个未被初始化（load或assign）的变量。即就是对一个变量实施use和store操作之前，必须先执行过了assign和load操作。
- 一个变量在同一时刻只允许一条线程对其进行lock操作，但lock操作可以被同一条线程重复执行多次，多次执行lock后，只有执行相同次数的unlock操作，变量才会被解锁。lock和unlock必须成对出现
- 如果对一个变量执行lock操作，将会清空工作内存中此变量的值，在执行引擎使用这个变量前需要重新执行load或assign操作初始化变量的值
- 如果一个变量事先没有被lock操作锁定，则不允许对它执行unlock操作；也不允许去unlock一个被其他线程锁定的变量。
- 对一个变量执行unlock操作之前，必须先把此变量同步到主内存中（执行store和write操作）。

https://zhuanlan.zhihu.com/p/29881777

**问题**：程序不知道主内存种的值已经被改了

## 17.Volatile

![image-20210725112623270](.\JUC.assets\image-20210725112623270.png)

> 1.保证可见性

```java
import java.util.concurrent.TimeUnit;

public class JMMDemo {
    private volatile static int num = 0;//不加volatile程序就会死循环，加了保证可见性
    public static void main(String[] args) {
        new Thread(()->{  //线程对主内存的变化不知道的
            while (num ==0){

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
```

> 2.不保证原子性

原子性：不可分割

线程A在执行任务的时候，是不能被打扰的，也不能被分割；要么同时成功，要么同时失败。

```java
//不保证原子性
public class VDemo02 {
    private volatile static int num = 0;
    public static void add(){
        num ++;
    }
    public static void main(String[] args) {

        //理论上num结果为20000
        for (int i = 0;i < 20;i++){
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount()>2){ //main gc
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName()+"  "+num);
    }
}
```

**如果不加lock和synchronized，怎么保证原子性**

![62967a9e46fd38f9617b49d3b138170](.\JUC.assets\62967a9e46fd38f9617b49d3b138170-1624505034784.png)

使用原子类，解决原子性问题

<img src=".\JUC.assets\image-20210624112615945-1624505176922.png" alt="image-20210624112615945" style="zoom:67%;" />

```java
//不保证原子性
public class VDemo02 {
//    private volatile static AtomicInteger num = 0;
    private volatile static AtomicInteger num = new AtomicInteger();
    public static void add(){
//        num ++;//不是一个原子性操作
        num.getAndIncrement();//原子性的+1方法，底层用到CAS
    }
    public static void main(String[] args) {

        //理论上num结果为20000
        for (int i = 0;i < 20;i++){
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount()>2){ //main gc
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName()+"  "+num);
    }
}
```

这些类的底层都直接和操作系统挂钩。

> ==指令重排：==写的程序，计算机并不是按照写的那样去执行。

源代码	---->  编译器优化的重排 --->  指令并行也可能回重排 ---->    内存系统也会重排----> 执行

==处理器在进行指令重排的手，考虑：数据之间的依赖性==

**volatile可以避免指令重排**：由于内存屏障。

## 18.单例模式

>  饿汉式、

```JAVA
//饿汉式单例
public class Hungry {
    //可能回浪费空间
    private byte[] data1 = new byte[1024*1024];
    private byte[] data2 = new byte[1024*1024];
    private byte[] data3 = new byte[1024*1024];
    private byte[] data4 = new byte[1024*1024];

    //构造器私有
    private Hungry(){

    }

    private final static Hungry HUNGRY = new Hungry();

    public static Hungry getInstance(){
        return HUNGRY;
    }

}
```

> DCL懒汉式、

```JAVA
package cn.neu.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//懒汉式单例
public class LazyMan {

    private LazyMan(){
        synchronized (LazyMan.class){
            if(lazyMan != null){//防止反射破坏
                throw new RuntimeException("不要试图使用反射破坏异常");
            }
        }
        System.out.println(Thread.currentThread().getName()+"ok");
    }

    private volatile static LazyMan lazyMan;

    //双重检测锁模式的  饿汉式单例 = DCL懒汉式
    public static LazyMan getInstance(){
        if (lazyMan == null){
            synchronized (LazyMan.class){
                if (lazyMan == null){
                    lazyMan = new LazyMan();//不是原子性操作
                    /*
                     * 1.分配内存空间
                     * 2.执行构造方法，初始化对象
                     * 3.把这个对象指向这个空间
                     */
                }
            }
        }
        return lazyMan;
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                LazyMan.getInstance();
            }).start();
        }
        LazyMan instance = LazyMan.getInstance();

        //反射可以破坏单例
        Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);
        LazyMan instance2 = declaredConstructor.newInstance();

        System.out.println(instance == instance2);
    }

}
```

> 静态内部类

```java
package cn.neu.single;


//静态内部类
public class Holder {
    private Holder(){

    }
    public static Holder getInstance(){
        return InnerClass.HOLDER;
    }
    public static class InnerClass{
        private static final Holder HOLDER = new Holder();
    }

}
```

> 枚举

```java
package cn.neu.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//enum 是什么：本身是一个Class类
public enum EnumSingle {
    INSTANCE;
    public EnumSingle getInstance(){
        return INSTANCE;
    }
}

class Test{
    public static void main(String[] args) throws Exception {
        EnumSingle instance1 = EnumSingle.INSTANCE;

        Constructor<EnumSingle> declaredConstructor = EnumSingle.class.getDeclaredConstructor(String.class, int.class);
        declaredConstructor.setAccessible(true);
        EnumSingle instance2 = declaredConstructor.newInstance();

        System.out.println(instance1);
        // java.lang.IllegalArgumentException: Cannot reflectively create enum objects
        System.out.println(instance2);
    }
}
```

## 19.CAS

> 什么是CAS

```java
import java.util.concurrent.atomic.AtomicInteger;

public class CASDemo {

    //CAS  compareAndSet比较并交换
    public static void main(String[] args) {
        AtomicInteger  atomicInteger  =  new AtomicInteger(2021);

        //期望、更新
        //public final boolean compareAndSet(int expect, int update)
        //如果期望的值达到了，就更新，否则不更新,CAS是CPU的并发原语
        atomicInteger.compareAndSet(2021,2022);
        System.out.println(atomicInteger.get());
    }
}
```



> Unsafe类

![image-20210624144406829](.\JUC.assets\image-20210624144406829-1624517048707.png)

![image-20210624145003276](.\JUC.assets\image-20210624145003276-1624517404681.png)

 CAS:比较当前工作内存中的值和主内存种的值，如果这个值是期望的，那么则执行操作！如果不是就一直循环。

**缺点：**

1. 循环耗时
2. 一次性只能保证一个共享变量的原子性
3. 存在ABA问题



> CAS:ABA问题（狸猫换太子）

![481034a168cb89fe0e7f3146c649e8d](.\JUC.assets\481034a168cb89fe0e7f3146c649e8d-1624517652114.png)

## 20.原子引用

带版本号的原子操作 ==解决ABA问题==：对应的思想：乐观锁

![1034a56ca844ced56d5fd27411ac261](.\JUC.assets\1034a56ca844ced56d5fd27411ac261-1624519485524.png)

```java
package cn.neu.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class CASDemo {

    //CAS  compareAndSet比较并交换
    public static void main(String[] args) {
        AtomicInteger  atomicInteger  =  new AtomicInteger(2021);

        //期望、更新
        //public final boolean compareAndSet(int expect, int update)
        //如果期望的值达到了，就更新，否则不更新,CAS是CPU的并发原语
        atomicInteger.compareAndSet(2021,2022);
        System.out.println(atomicInteger.get());
        System.out.println("===================================");
        //原子引用
        new AtomicReference<>();//基本类
        AtomicStampedReference<Object> atomicStampedReference = new AtomicStampedReference<>(1,1);//带时间的
        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();//获得版本号
            System.out.println("a1=>"+stamp);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //version + 1
            System.out.println(atomicStampedReference.compareAndSet(1, 2,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));

            System.out.println("a2=>"+atomicStampedReference.getStamp());

            System.out.println(atomicStampedReference.compareAndSet(2, 1,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));

            System.out.println("a3=>"+atomicStampedReference.getStamp());
        },"A").start();

        //乐观锁的原理一样
        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();//获得版本号
            System.out.println("b1=>"+stamp);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(atomicStampedReference.compareAndSet(1, 66,
                    stamp, stamp + 1));

            System.out.println("b2=>"+atomicStampedReference.getStamp());
        },"B").start();
    }
}
```

## 21.各种锁的理解

### 1.公平锁、非公平锁

公平锁：不能插队，先来后到

非公平锁：可以插队，默认的是他

### 2.可重入锁

可重入锁（递归锁）



## 22.synchronized与volatile对比

![image-20210725111610271](.\JUC.assets\image-20210725111610271.png)

