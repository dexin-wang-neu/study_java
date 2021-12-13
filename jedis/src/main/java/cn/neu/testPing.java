package cn.neu;

import redis.clients.jedis.Jedis;

public class testPing {
    public static void main(String[] args) {
        //1.new jedis对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //jedis 所有的命令就是我们之前学习的所有指令
        System.out.println(jedis.ping());
    }
}
