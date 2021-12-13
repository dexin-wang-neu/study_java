package cn.neu.unsafe;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {
    public static void main(String[] args) {
        //map 是这样用的吗？默认等价于什么？不是，工作中不用HashMap
//        HashMap<String, String> map = new HashMap<>();
        //加载因子，初始化容量 new HashMap<>(16,0.75);

        //解决方案
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 5));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }
}
