package cn.neu.unsafe;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
            new Thread(() -> {
                copy.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println(copy);
            }, String.valueOf(i)).start();
        }
    }
}
