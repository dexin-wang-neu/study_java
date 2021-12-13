package cn.neu.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//enum 是什么：本身是一个Class类
public enum EnumSingle {
    INSTANCE;

    public EnumSingle getInstance() {
        return INSTANCE;
    }
}

class Test {
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
