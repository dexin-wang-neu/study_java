package cn.neu;

public class Test1 {
    public static void main(String[] args) {
        //获取CPU的核数
        //cpu 密集型 IO密集型
        System.out.println(Runtime.getRuntime().availableProcessors());

    }
}
