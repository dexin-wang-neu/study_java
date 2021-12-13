package cn.neu.function;

import java.util.function.Consumer;
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
