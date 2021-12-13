package cn.neu.function;

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
