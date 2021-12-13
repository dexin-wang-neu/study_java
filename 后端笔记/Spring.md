

## 1.Spring AOP

### 1.1使用AOP好处

**1.集中处理某一关注点/横切逻辑**
**2.可以很方便的添加/删除关注点**
**3.侵入性少，增强代码可读性及可维护性**

### 1.2 AOP的术语

**1.Join point（连接点）**
Spring 官方文档的描述：

> A point during the execution of a program, such as the execution of a method or the handling of an exception. In Spring AOP, a join point always represents a method execution.

程序执行过程中的一个点，如方法的执行或异常的处理。在Spring AOP中，连接点总是表示方法的执行。通俗的讲，连接点即表示类里面可以被增强的方法
**2.Pointcut（切入点）**

> Pointcut are expressions that is matched with join points to determine whether advice needs to be executed or not. Pointcut uses different kinds of expressions that are matched with the join points and Spring framework uses the AspectJ pointcut expression language

切入点是与连接点匹配的表达式，用于确定是否需要执行通知。切入点使用与连接点匹配的不同类型的表达式，Spring框架使用AspectJ切入点表达式语言。我们可以将切入点理解为需要被拦截的Join point
**3.Advice（增强/通知）**
所谓通知是指拦截到Joinpoint之后所要做的事情就是通知，通知分为前置通知、后置通知、异常通知、最终通知和环绕通知(切面要完成的功能)
**4.Aspect（切面）**
Aspect切面表示Pointcut（切入点）和Advice（增强/通知）的结合

### 1.3 Spring AOP用法

**资源代码**

```java
/**
 * 设置登录用户名
 */
public class CurrentUserHolder {

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static String get() {
        return holder.get();
    }
    public static void set(String user) {
        holder.set(user);
    }
}
/**
 * 校验用户权限
 */
@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Override
    public void checkAccess() {
        String user = CurrentUserHolder.get();

        if(!"admin".equals(user)) {
            throw new RuntimeException("该用户无此权限！");
        }
    }
}
/**
 * 业务逻辑类
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private AuthService authService;

    @Override
    public Long deleteProductById(Long id) {
        System.out.println("删除商品id为" + id + "的商品成功！");
        return id；
    }

    @Override
    public void deleteProductByName(String name) {
        System.out.println("删除商品名称为" + name + "的商品成功！");
    }

    @Override
    public void selectProduct(Long id) {
        if("100".equals(id.toString())) {
            System.out.println("查询商品成功！");
        } else {
            System.out.println("查询商品失败！");
            throw new RuntimeException("该商品不存在！");
        }
    }
}
```

**对业务逻辑类进行增强的方法**

**1.使用within表达式匹配包类型**

```java
//匹配ProductServiceImpl类里面的所有方法
@Pointcut("within(com.aop.service.impl.ProductServiceImpl)")
public void matchType() {}

//匹配com.aop.service包及其子包下所有类的方法
@Pointcut("within(com.aop.service..*)")
public void matchPackage() {}
```

**2.使用this、target、bean表达式匹配对象类型**

```java
//匹配AOP对象的目标对象为指定类型的方法，即ProductServiceImpl的aop代理对象的方法
@Pointcut("this(com.aop.service.impl.ProductServiceImpl)")
public void matchThis() {}

//匹配实现ProductService接口的目标对象
@Pointcut("target(com.aop.service.ProductService)")
public void matchTarget() {}

//匹配所有以Service结尾的bean里面的方法
@Pointcut("bean(*Service)")
public void matchBean() {}
```

**3.使用args表达式匹配参数**

```java
//匹配第一个参数为Long类型的方法
@Pointcut("args(Long, ..) ")
public void matchArgs() {}
```

**4.使用@annotation、@within、@target、@args匹配注解**

```java
//匹配标注有AdminOnly注解的方法
@Pointcut("@annotation(com.aop.annotation.AdminOnly)")
public void matchAnno() {}

//匹配标注有Beta的类底下的方法，要求annotation的Retention级别为CLASS
@Pointcut("@within(com.google.common.annotations.Beta)")
public void matchWithin() {}

//匹配标注有Repository的类底下的方法，要求annotation的Retention级别为RUNTIME
@Pointcut("@target(org.springframework.stereotype.Repository)")
public void matchTarget() {}

//匹配传入的参数类标注有Repository注解的方法
@Pointcut("@args(org.springframework.stereotype.Repository)")
public void matchArgs() {}
```

**5.使用execution表达式**
execution表达式是我们在开发过程中最常用的，它的语法如下：

![execution表达式](JUC.assets\1240)

**modifier-pattern**：用于匹配public、private等访问修饰符
**ret-type-pattern**：用于匹配返回值类型，不可省略
**declaring-type-pattern**：用于匹配包类型
**modifier-pattern（param-pattern）**：用于匹配类中的方法，不可省略
**throws-pattern**：用于匹配抛出异常的方法

> 切面Aspect代码
>
> @Aspect：告诉Spring当前类是一个切面类

```java
@Component
@Aspect    //一定要写这个注解
public class SecurityAspect {

    @Autowired
    private AuthService authService;

    //匹配com.aop.service.impl.ProductServiceImpl类下的方法名以delete开头、参数类型为Long的public方法
    @Pointcut("execution(public * com.aop.service.impl.ProductServiceImpl.delete*(Long))")
    public void matchCondition() {}

    //使用matchCondition这个切入点进行增强
    @Before("matchCondition()")
    public void before() {
        System.out.println("before 前置通知......");
        authService.checkAccess();
    }
}
```

对切面类增强的方法进行测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    public void contextLoads() {
        //设置用户名
        CurrentUserHolder.set("hello");

        productService.selectProduct(100L);
        productService.deleteProductByName("衣服");
        productService.deleteProductById(100L);
    }
}
//运行结果，（只有deleteProductById方法拦截成功）：
查询商品成功！
删除商品名称为衣服的商品成功！
before 前置通知......

java.lang.RuntimeException: 该用户无此权限！

	at com.aop.service.impl.AuthServiceImpl.checkAccess(AuthServiceImpl.java:15)
	at com.aop.security.SecurityAspect.before(SecurityAspect.java:50)
```

> 可以在多个表达式之间使用连接符匹配多个条件， 如使用||表示“或”，使用 &&表示“且”

```java
//匹配com.aop.service.impl.ProductServiceImpl类下方法名以select或delete开头的所有方法
@Pointcut("execution(* com.aop.service.impl.ProductServiceImpl.select*(..)) || " +
            "execution(* com.aop.service.impl.ProductServiceImpl.delete*(..))")
public void matchCondition() {}

//使用matchCondition这个切入点进行增强
@Before("matchCondition()")
public void before() {
   System.out.println("before 前置通知......");
   authService.checkAccess();
}
```

```java
 @Test
    public void contextLoads() {
        CurrentUserHolder.set("admin");

        productService.selectProduct(100L);
        productService.deleteProductByName("衣服");
        productService.deleteProductById(100L);
    }
//运行结果；全部拦截成功
before 前置通知......
查询商品成功！
before 前置通知......
删除商品名称为衣服的商品成功！
before 前置通知......
删除商品id为100的商品成功！
```

### 1.4 Advice注解

Advice注解一共有五种，分别是：
**1.@Before前置通知**
前置通知在切入点运行前执行，不会影响切入点的逻辑
**2.@After后置通知**
后置通知在切入点正常运行结束后执行，如果切入点抛出异常，则在抛出异常前执行
**3.@AfterThrowing异常通知**
异常通知在切入点抛出异常前执行，如果切入点正常运行（未抛出异常），则不执行
**4.@AfterReturning返回通知**
返回通知在切入点正常运行结束后执行，如果切入点抛出异常，则不执行
**5.@Around环绕通知**
环绕通知是功能最强大的通知，可以在切入点执行前后自定义一些操作。环绕通知需要负责决定是继续处理join point(调用ProceedingJoinPoint的proceed方法)还是中断执行

```java
//匹配com.aop.service.impl.ProductServiceImpl类下面的所有方法
    @Pointcut("execution(* com.aop.service.impl.ProductServiceImpl.*(..))")
    public void matchAll() {}

    @Around("matchAll()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object result = null;
        authService.checkAccess();
        System.out.println("befor 在切入点执行前运行");

        try{
            result = joinPoint.proceed(joinPoint.getArgs());//获取参数
            System.out.println("after 在切入点执行后运行,result = " + result);
        } catch (Throwable e) {
            System.out.println("after 在切入点执行后抛出exception运行");
            e.printStackTrace();
        } finally {
            System.out.println("finally......");
        }

       return result;
    }
```

```java
 @Test
    public void contextLoads() {
        CurrentUserHolder.set("admin");

        productService.deleteProductById(100L);
        productService.selectProduct(10L);
    }

//运行结果
before 在切入点执行前运行
删除商品id为100的商品成功！
after 在切入点执行后运行,result = 100
finally......
before 在切入点执行前运行
查询商品失败！
after 在切入点执行后抛出exception运行
java.lang.RuntimeException: 该商品不存在！
	at com.aop.service.impl.ProductServiceImpl.selectProduct(ProductServiceImpl.java:41)
finally......
```

在执行ProceedingJoinPoint对象的proceed方法前相当于Before前置通知；执行proceed方法相当于运行切入点（同时可以获取参数）；在方法执行之后相当于After后置通知，如果运行切入点抛出异常，则catch中的内容相当于AfterThrowing异常通知;finally中的内容无论切入点是否抛出异常，都将执行

### 1.5 使用总结

> AOP：【动态代理】
>           指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式；
>
>   1、导入aop模块；Spring AOP：(spring-aspects)
>   2、定义一个业务逻辑类（MathCalculator）；在业务逻辑运行的时候将日志进行打印（方法之前、方法运行结束、方法出现异常，xxx）
>   3、定义一个日志切面类（LogAspects）：切面类里面的方法需要动态感知MathCalculator.div运行到哪里然后执行；
>           通知方法：
>               前置通知(@Before)：logStart：在目标方法(div)运行之前运行
>               后置通知(@After)：logEnd：在目标方法(div)运行结束之后运行（无论方法正常结束还是异常结束）
>               返回通知(@AfterReturning)：logReturn：在目标方法(div)正常返回之后运行
>               异常通知(@AfterThrowing)：logException：在目标方法(div)出现异常以后运行
>               环绕通知(@Around)：动态代理，手动推进目标方法运行（joinPoint.procced()）
>   4、给切面类的目标方法标注何时何地运行（通知注解）；
>   5、将切面类和业务逻辑类（目标方法所在类）都加入到容器中;
>   6、必须告诉Spring哪个类是切面类(给切面类上加一个注解：@Aspect)
>   7、给配置类中加 @EnableAspectJAutoProxy 【开启基于注解的aop模式】
>           在Spring中很多的 @EnableXXX;
>
>   三步：
>       1）、将业务逻辑组件和切面类都加入到容器中；告诉Spring哪个是切面类（@Aspect）
>       2）、在切面类上的每一个通知方法上标注通知注解，告诉Spring何时何地运行（切入点表达式）
>       3）、开启基于注解的aop模式；@EnableAspectJAutoProxy

### 1.7 案例

```java
/**
我定义了一个除法方法，作为一个切面：需要增强的业务方法
*/
package com.kun.aop;

public class MathCalculator {
    
    public int div(int i,int j){
        System.out.println("MathCalculator...div...");
        return i/j;    
    }

}
```

```java
package com.kun.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 切面类
 * 
 * @Aspect： 告诉Spring当前类是一个切面类
 *
 */
@Aspect
public class LogAspects {
    
    //抽取公共的切入点表达式
    //1、本类引用
    //2、其他的切面引用
    @Pointcut("execution(public int com.kun.aop.MathCalculator.*(..))")
    public void pointCut(){};
    
    //@Before在目标方法之前切入；切入点表达式（指定在哪个方法切入）
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        System.out.println(""+joinPoint.getSignature().getName()+"运行。。。@Before:参数列表是：{"+Arrays.asList(args)+"}");
    }
    
    @After("com.kun.aop.LogAspects.pointCut()")
    public void logEnd(JoinPoint joinPoint){
        System.out.println(""+joinPoint.getSignature().getName()+"结束。。。@After");
    }
    
    //JoinPoint一定要出现在参数表的第一位
    @AfterReturning(value="pointCut()",returning="result")
    public void logReturn(JoinPoint joinPoint,Object result){
        System.out.println(""+joinPoint.getSignature().getName()+"正常返回。。。@AfterReturning:运行结果：{"+result+"}");
    }
    
    @AfterThrowing(value="pointCut()",throwing="exception")
    public void logException(JoinPoint joinPoint,Exception exception){
        System.out.println(""+joinPoint.getSignature().getName()+"异常。。。异常信息：{"+exception+"}");
    }

}
```

> @EnableAspectJAutoProxy:注解开启切面代理

```java
package com.kun.config;
/**
aop的配置
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.kun.aop.LogAspects;
import com.kun.aop.MathCalculator;

@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAOP {
     
    //业务逻辑类加入容器中
    @Bean
    public MathCalculator calculator(){
        return new MathCalculator();
    }

    //切面类加入到容器中
    @Bean
    public LogAspects logAspects(){
        return new LogAspects();
    }
}
```

### 1.8代理对象创建过程

#### 1.8.1@EnableAspectJAutoProxy

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {
    /**
     * Indicate whether subclass-based (CGLIB) proxies are to be created as opposed
     * to standard Java interface-based proxies. The default is {@code false}.
     */
    boolean proxyTargetClass() default false;

}
```

> 它导入了一个**AspectJAutoProxyRegistrar**组件，进一步查看其代码：

```java
package org.springframework.context.annotation;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import static org.springframework.context.annotation.MetadataUtils.*;

/**
 * Registers an {@link org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
 * AnnotationAwareAspectJAutoProxyCreator} against the current {@link BeanDefinitionRegistry}
 * as appropriate based on a given @{@link EnableAspectJAutoProxy} annotation.
 * @author Chris Beams
 * @see EnableAspectJAutoProxy
 * @since 3.1
 */
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * Register, escalate, and configure the AspectJ auto proxy creator based on the value
     * of the @{@link EnableAspectJAutoProxy#proxyTargetClass()} attribute on the importing
     * {@code @Configuration} class.
     */
    public void registerBeanDefinitions(
            AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

        AnnotationAttributes enableAJAutoProxy =
                attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
        if (enableAJAutoProxy.getBoolean("proxyTargetClass")) {
            AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
        }
    }

}
```

> 它实现了**ImportBeanDefinitionRegistrar**接口，这个接口可以向IOC容器中注册bean。 由此可以推测aop利用AspectJAutoProxyRegistrar自定义给容器中注册bean；BeanDefinetion。

![img](JUC.assets\1140836-20181031200151572-1248357045.png)

> *IOC容器中注入了一个internalAutoProxyCreator=AnnotationAwareAspectJAutoProxyCreator的bean，*到此可以得出结论，@EnableAspectJAutoProxy给容器中注册一个AnnotationAwareAspectJAutoProxyCreator。

#### 1.8.2 AnnotationAwareAspectJAutoProxyCreator创建过程

![img](JUC.assets\1140836-20181031201123887-743732675.png)

> 在此需要关注两点内容：
>
> 1）关注后置处理器SmartInstantiationAwareBeanPostProcessor（在bean初始化完成前后做事情）
>
> 2）关注自动装配BeanFactory。

> 通过代码查看，发现父类AbstractAutoProxyCreator中有后置处理器的内容；AbstactAdvisorAutoProxyCreator类中重写了其父类AbstractAutoProxyCreator中setBeanFactory()方法，在AnnotationAwareAspectJAutoProxyCreator类中initBeanFactory()方法完成了自动装配BeanFactory。



1）、 @EnableAspectJAutoProxy 开启AOP功能
 2）、 @EnableAspectJAutoProxy 会给容器中注册一个组件 AnnotationAwareAspectJAutoProxyCreator
 3）、AnnotationAwareAspectJAutoProxyCreator是一个后置处理器；
 4）、容器的创建流程：
        1）、registerBeanPostProcessors（）注册后置处理器；创建AnnotationAwareAspectJAutoProxyCreator对象
        2）、finishBeanFactoryInitialization（）初始化剩下的单实例bean
            1）、创建业务逻辑组件和切面组件
            2）、AnnotationAwareAspectJAutoProxyCreator拦截组件的创建过程
            3）、组件创建完之后，判断组件是否需要增强
                     是：切面的通知方法，包装成增强器（Advisor）;给业务逻辑组件创建一个代理对象（cglib）；
 5）、执行目标方法：
        1）、代理对象执行目标方法
        2）、CglibAopProxy.intercept()；
             1）、得到目标方法的拦截器链（增强器包装成拦截器MethodInterceptor）
             2）、利用拦截器的链式机制，依次进入每一个拦截器进行执行；
             3）、效果：
                      正常执行：前置通知-》目标方法-》后置通知-》返回通知
                      出现异常：前置通知-》目标方法-》后置通知-》异常通知

### 1.9 面试回答AOP

简单说说 AOP 的设计：

1. 每个 Bean 都会被 JDK 或者 Cglib 代理。取决于是否有接口。
2. 每个 Bean 会有多个“方法拦截器”。注意：拦截器分为两层，外层由 Spring 内核控制流程，内层拦截器是用户设置，也就是 AOP。
3. 当代理方法被调用时，先经过外层拦截器，外层拦截器根据方法的各种信息判断该方法应该执行哪些“内层拦截器”。内层拦截器的设计就是职责连的设计。

**可以将 AOP 分成 2 个部分。**
第一：代理的创建；
第二：代理的调用。

1. 代理的创建（按步骤）：

- 首先，需要创建代理工厂，代理工厂需要 3 个重要的信息：拦截器数组，目标对象接口数组，目标对象。
- 创建代理工厂时，默认会在拦截器数组尾部再增加一个默认拦截器 —— 用于最终的调用目标方法。
- 当调用 getProxy 方法的时候，会根据接口数量大余 0 条件返回一个代理对象（JDK or Cglib）。
- 注意：创建代理对象时，同时会创建一个外层拦截器，这个拦截器就是 Spring 内核的拦截器。用于控制整个 AOP 的流程。

2. 代理的调用

- 当对代理对象进行调用时，就会触发外层拦截器。
- 外层拦截器根据代理配置信息，创建内层拦截器链。创建的过程中，会根据表达式判断当前拦截是否匹配这个拦截器。而这个拦截器链设计模式就是职责链模式。
- 当整个链条执行到最后时，就会触发创建代理时那个尾部的默认拦截器，从而调用目标方法。最后返回。

题外话：Spring 的事务也就是个拦截器。

![img](JUC.assets\124011111)

![img](JUC.assets\AOP调用流程)

## 2. Spring 三级缓存

### **2.1 Spring Bean的循环依赖**

```java
//这其实就是Spring环境下典型的循环依赖场景
@Service
public class AServiceImpl implements AService {
    @Autowired
    private BService bService;
    ...
}
@Service
public class BServiceImpl implements BService {
    @Autowired
    private AService aService;
    ...
}
```

在Spring环境中，因为我们的Bean的实例化、初始化都是交给了容器，因此它的循环依赖主要表现为下面三种场景。为了方便演示，我准备了如下两个类：

![循环依赖](JUC.assets\循环依赖.png)

1. **构造器注入循环依赖**

   ```java
   @Service
   public class A {
       public A(B b) {
       }
   }
   @Service
   public class B {
       public B(A a) {
       }
   }
   ```

   结果：项目启动失败抛出异常`BeanCurrentlyInCreationException`

   ![image-20210912191502658](JUC.assets\image-20210912191502658.png)

>  构造器注入构成的循环依赖，此种循环依赖方式**是无法解决的**，只能抛出`BeanCurrentlyInCreationException`异常表示循环依赖。这也是构造器注入的最大劣势（它有很多独特的优势，请小伙伴自行发掘） 
>
>  `根本原因`：Spring解决循环依赖依靠的是Bean的“中间态”这个概念，而这个中间态指的是`已经实例化`，但还没初始化的状态。而构造器是完成实例化的东东，所以构造器的循环依赖无法解决~~~

2. **field属性注入（setter方法注入）循环依赖**

   ```java
   @Service
   public class A {
       @Autowired
       private B b;
   }
   
   @Service
   public class B {
       @Autowired
       private A a;
   }
   ```

   ​	**结果：项目启动成功，能够正常work**

3. **`prototype` field属性注入循环依赖**

   ```java
   @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
   @Service
   public class A {
       @Autowired
       private B b;
   }
   
   @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
   @Service
   public class B {
       @Autowired
       private A a;
   }
   ```

   结果：**需要注意的是**本例中**启动时是不会报错的**（因为非单例Bean`默认`不会初始化，而是使用时才会初始化），所以很简单咱们只需要手动`getBean()`或者在一个单例Bean内`@Autowired`一下它即可

   ```javascript
   // 在单例Bean内注入
       @Autowired
       private A a;
   ```

   ![image-20210912191727007](JUC.assets\image-20210912191727007.png)

> 对于Spring循环依赖的情况总结如下：
>
> 1. 不能解决的情况： 1. 构造器注入循环依赖 2. `prototype` field属性注入循环依赖
> 2. 能解决的情况： 1. field属性注入（setter方法注入）循环依赖

**Spring创建Bean的流程**

![20210912](JUC.assets\20210912.png)

对Bean的创建最为核心三个方法解释如下：

- `createBeanInstance`：例化，其实也就是调用对象的**构造方法**实例化对象
- `populateBean`：填充属性，这一步主要是对bean的依赖属性进行注入(`@Autowired`)
- `initializeBean`：回到一些形如`initMethod`、`InitializingBean`等方法

从对`单例Bean`的初始化可以看出，循环依赖主要发生在**第二步（populateBean）**，也就是field属性注入的处理。

**Spring容器的`'三级缓存'`**

在Spring容器的整个声明周期中，单例Bean有且仅有一个对象。这很容易让人想到可以用缓存来加速访问。 从源码中也可以看出Spring大量运用了Cache的手段，在循环依赖问题的解决过程中甚至不惜使用了“三级缓存”，这也便是它设计的精妙之处~

`三级缓存`其实它更像是Spring容器工厂的内的`术语`，采用三级缓存模式来解决循环依赖问题，这三级缓存分别指：

```java
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {
	...
	// 从上至下 分表代表这“三级缓存”
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256); //一级缓存
	private final Map<String, Object> earlySingletonObjects = new HashMap<>(16); // 二级缓存
	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16); // 三级缓存
	...
	
	/** Names of beans that are currently in creation. */
	// 这个缓存也十分重要：它表示bean创建过程中都会在里面呆着~
	// 它在Bean开始创建时放值，创建完成时会将其移出~
	private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

	/** Names of beans that have already been created at least once. */
	// 当这个Bean被创建完成后，会标记为这个 注意：这里是set集合 不会重复
	// 至少被创建了一次的  都会放进这里~~~~
	private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<>(256));
}
```

注：`AbstractBeanFactory`继承自`DefaultSingletonBeanRegistry`~

1. `singletonObjects`：用于存放完全初始化好的 bean，**从该缓存中取出的 bean 可以直接使用**
2. `earlySingletonObjects`：提前曝光的单例对象的cache，存放原始的 bean 对象（尚未填充属性），用于解决循环依赖
3. `singletonFactories`：单例对象工厂的cache，存放 bean 工厂对象，用于解决循环依赖

**获取单例Bean的源码如下：**

```java
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {
	...
	@Override
	@Nullable
	public Object getSingleton(String beanName) {
		return getSingleton(beanName, true);
	}
	@Nullable
	protected Object getSingleton(String beanName, boolean allowEarlyReference) {
		Object singletonObject = this.singletonObjects.get(beanName);
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
			synchronized (this.singletonObjects) {
				singletonObject = this.earlySingletonObjects.get(beanName);
				if (singletonObject == null && allowEarlyReference) {
					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
					if (singletonFactory != null) {
						singletonObject = singletonFactory.getObject();
						this.earlySingletonObjects.put(beanName, singletonObject);
						this.singletonFactories.remove(beanName);
					}
				}
			}
		}
		return singletonObject;
	}
	...
	public boolean isSingletonCurrentlyInCreation(String beanName) {
		return this.singletonsCurrentlyInCreation.contains(beanName);
	}
	protected boolean isActuallyInCreation(String beanName) {
		return isSingletonCurrentlyInCreation(beanName);
	}
	...
}
```

1. 先从`一级缓存singletonObjects`中去获取。（如果获取到就直接return）
2. 如果获取不到或者对象正在创建中（`isSingletonCurrentlyInCreation()`），那就再从`二级缓存earlySingletonObjects`中获取。（如果获取到就直接return）
3. 如果还是获取不到，且允许singletonFactories（allowEarlyReference=true）通过`getObject()`获取。就从`三级缓存singletonFactory`.getObject()获取。**（如果获取到了就从**`**singletonFactories**`**中移除，并且放进**`**earlySingletonObjects**`**。其实也就是从三级缓存**`**移动（是剪切、不是复制哦~）**`**到了二级缓存）**

>  **加入`singletonFactories`三级缓存的前提是执行了构造器，所以构造器的循环依赖没法解决**

`getSingleton()`从缓存里获取单例对象步骤分析可知，Spring解决循环依赖的诀窍：**就在于singletonFactories这个三级缓存**。这个Cache里面都是`ObjectFactory`，它是解决问题的关键。

```javascript
// 它可以将创建对象的步骤封装到ObjectFactory中 交给自定义的Scope来选择是否需要创建对象来灵活的实现scope。  具体参见Scope接口
@FunctionalInterface
public interface ObjectFactory<T> {
	T getObject() throws BeansException;
}
```

>  经过ObjectFactory.getObject()后，此时放进了二级缓存`earlySingletonObjects`内。这个时候对象已经实例化了，`虽然还不完美`，但是对象的引用已经可以被其它引用了。 

**此处说一下二级缓存`earlySingletonObjects`它里面的数据什么时候添加什么移除？？?**

**添加**：向里面添加数据只有一个地方，就是上面说的`getSingleton()`里从三级缓存里挪过来 **移除**：`addSingleton、addSingletonFactory、removeSingleton`从语义中可以看出添加单例、添加单例工厂`ObjectFactory`的时候都会删除二级缓存里面对应的缓存值，是互斥的

### 2.2 源码解析

`Spring`容器会将每一个正在创建的Bean 标识符放在一个“当前创建Bean池”中，Bean标识符在创建过程中将一直保持在这个池中，而对于创建完毕的Bean将从`当前创建Bean池`中清除掉。 这个“当前创建Bean池”指的是上面提到的`singletonsCurrentlyInCreation`那个集合。

```javascript
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {
	...
	protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType, @Nullable final Object[] args, boolean typeCheckOnly) throws BeansException {
		...
		// Eagerly check singleton cache for manually registered singletons.
		// 先去获取一次，如果不为null，此处就会走缓存了~~
		Object sharedInstance = getSingleton(beanName);
		...
		// 如果不是只检查类型，那就标记这个Bean被创建了~~添加到缓存里 也就是所谓的  当前创建Bean池
		if (!typeCheckOnly) {
			markBeanAsCreated(beanName);
		}
		...
		// Create bean instance.
		if (mbd.isSingleton()) {
		
			// 这个getSingleton方法不是SingletonBeanRegistry的接口方法  属于实现类DefaultSingletonBeanRegistry的一个public重载方法~~~
			// 它的特点是在执行singletonFactory.getObject();前后会执行beforeSingletonCreation(beanName);和afterSingletonCreation(beanName);  
			// 也就是保证这个Bean在创建过程中，放入正在创建的缓存池里  可以看到它实际创建bean调用的是我们的createBean方法~~~~
			sharedInstance = getSingleton(beanName, () -> {
				try {
					return createBean(beanName, mbd, args);
				} catch (BeansException ex) {
					destroySingleton(beanName);
					throw ex;
				}
			});
			bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
		}
	}
	...
}

// 抽象方法createBean所在地  这个接口方法是属于抽象父类AbstractBeanFactory的   实现在这个抽象类里
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
	...
	protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args) throws BeanCreationException {
		...
		// 创建Bean对象，并且将对象包裹在BeanWrapper 中
		instanceWrapper = createBeanInstance(beanName, mbd, args);
		// 再从Wrapper中把Bean原始对象（非代理~~~）  这个时候这个Bean就有地址值了，就能被引用了~~~
		// 注意：此处是原始对象，这点非常的重要
		final Object bean = instanceWrapper.getWrappedInstance();
		...
		// earlySingletonExposure 用于表示是否”提前暴露“原始对象的引用，用于解决循环依赖。
		// 对于单例Bean，该变量一般为 true   但你也可以通过属性allowCircularReferences = false来关闭循环引用
		// isSingletonCurrentlyInCreation(beanName) 表示当前bean必须在创建中才行
		boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName));
		if (earlySingletonExposure) {
			if (logger.isTraceEnabled()) {
				logger.trace("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
			}
			// 上面讲过调用此方法放进一个ObjectFactory，二级缓存会对应删除的
			// getEarlyBeanReference的作用：调用SmartInstantiationAwareBeanPostProcessor.getEarlyBeanReference()这个方法  否则啥都不做
			// 也就是给调用者个机会，自己去实现暴露这个bean的应用的逻辑~~~
			// 比如在getEarlyBeanReference()里可以实现AOP的逻辑~~~  参考自动代理创建器AbstractAutoProxyCreator  实现了这个方法来创建代理对象
			// 若不需要执行AOP的逻辑，直接返回Bean
			addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
		}
		Object exposedObject = bean; //exposedObject 是最终返回的对象
		...
		// 填充属于，解决@Autowired依赖~
		populateBean(beanName, mbd, instanceWrapper);
		// 执行初始化回调方法们~~~
		exposedObject = initializeBean(beanName, exposedObject, mbd);
		
		// earlySingletonExposure：如果你的bean允许被早期暴露出去 也就是说可以被循环引用  那这里就会进行检查
		// 此段代码非常重要~~~~~但大多数人都忽略了它
		if (earlySingletonExposure) {
			// 此时一级缓存肯定还没数据，但是呢此时候二级缓存earlySingletonObjects也没数据
			//注意，注意：第二参数为false  表示不会再去三级缓存里查了~~~

			// 此处非常巧妙的一点：：：因为上面各式各样的实例化、初始化的后置处理器都执行了，如果你在上面执行了这一句
			//  ((ConfigurableListableBeanFactory)this.beanFactory).registerSingleton(beanName, bean);
			// 那么此处得到的earlySingletonReference 的引用最终会是你手动放进去的Bean最终返回，完美的实现了"偷天换日" 特别适合中间件的设计
			// 我们知道，执行完此doCreateBean后执行addSingleton()  其实就是把自己再添加一次  **再一次强调，完美实现偷天换日**
			Object earlySingletonReference = getSingleton(beanName, false);
			if (earlySingletonReference != null) {
			
				// 这个意思是如果经过了initializeBean()后，exposedObject还是木有变，那就可以大胆放心的返回了
				// initializeBean会调用后置处理器，这个时候可以生成一个代理对象，那这个时候它哥俩就不会相等了 走else去判断吧
				if (exposedObject == bean) {
					exposedObject = earlySingletonReference;
				} 

				// allowRawInjectionDespiteWrapping这个值默认是false
				// hasDependentBean：若它有依赖的bean 那就需要继续校验了~~~(若没有依赖的 就放过它~)
				else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
					// 拿到它所依赖的Bean们~~~~ 下面会遍历一个一个的去看~~
					String[] dependentBeans = getDependentBeans(beanName);
					Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
					
					// 一个个检查它所以Bean
					// removeSingletonIfCreatedForTypeCheckOnly这个放见下面  在AbstractBeanFactory里面
					// 简单的说，它如果判断到该dependentBean并没有在创建中的了的情况下,那就把它从所有缓存中移除~~~  并且返回true
					// 否则（比如确实在创建中） 那就返回false 进入我们的if里面~  表示所谓的真正依赖
					//（解释：就是真的需要依赖它先实例化，才能实例化自己的依赖）
					for (String dependentBean : dependentBeans) {
						if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
							actualDependentBeans.add(dependentBean);
						}
					}

					// 若存在真正依赖，那就报错（不要等到内存移除你才报错，那是非常不友好的） 
					// 这个异常是BeanCurrentlyInCreationException，报错日志也稍微留意一下，方便定位错误~~~~
					if (!actualDependentBeans.isEmpty()) {
						throw new BeanCurrentlyInCreationException(beanName,
								"Bean with name '" + beanName + "' has been injected into other beans [" +
								StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +
								"] in its raw version as part of a circular reference, but has eventually been " +
								"wrapped. This means that said other beans do not use the final version of the " +
								"bean. This is often the result of over-eager type matching - consider using " +
								"'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
					}
				}
			}
		}
		
		return exposedObject;
	}

	// 虽然是remove方法 但是它的返回值也非常重要
	// 该方法唯一调用的地方就是循环依赖的最后检查处~~~~~
	protected boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName) {
		// 如果这个bean不在创建中  比如是ForTypeCheckOnly的  那就移除掉
		if (!this.alreadyCreated.contains(beanName)) {
			removeSingleton(beanName);
			return true;
		}
		else {
			return false;
		}
	}

}
```

这里举例：例如是`field`属性依赖注入，在`populateBean`时它就会先去完成它所依赖注入的那个bean的实例化、初始化过程，最终返回到本流程继续处理，**因此Spring这样处理是不存在任何问题的。**

这里有个小细节：

```javascript
if (exposedObject == bean) {
	exposedObject = earlySingletonReference;
}
```

这一句如果`exposedObject == bean`表示最终返回的对象就是原始对象，说明在`populateBean`和`initializeBean`没对他代理过，那就啥话都不说了`exposedObject = earlySingletonReference`，最终把二级缓存里的引用返回即可~

### 2.3 流程总结（`非常重要`）

此处以如上的A、B类的互相依赖注入为例，在这里表达出**关键代码**的走势：

1、入口处即是**实例化、初始化A这个单例Bean**。`AbstractBeanFactory.doGetBean("a")`

```javascript
protected <T> T doGetBean(...){
	... 
	// 标记beanName a是已经创建过至少一次的~~~ 它会一直存留在缓存里不会被移除（除非抛出了异常）
	// 参见缓存Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<>(256))
	if (!typeCheckOnly) {
		markBeanAsCreated(beanName);
	}

	// 此时a不存在任何一级缓存中，且不是在创建中  所以此处返回null
	// 此处若不为null，然后从缓存里拿就可以了(主要处理FactoryBean和BeanFactory情况吧)
	Object beanInstance = getSingleton(beanName, false);
	...
	// 这个getSingleton方法非常关键。
	//1、标注a正在创建中~
	//2、调用singletonObject = singletonFactory.getObject();（实际上调用的是createBean()方法）  因此这一步最为关键
	//3、此时实例已经创建完成  会把a移除整整创建的缓存中
	//4、执行addSingleton()添加进去。（备注：注册bean的接口方法为registerSingleton，它依赖于addSingleton方法）
	sharedInstance = getSingleton(beanName, () -> { ... return createBean(beanName, mbd, args); });
}
```

2、下面进入到最为复杂的`AbstractAutowireCapableBeanFactory.createBean/doCreateBean()`环节，创建A的实例

```javascript
protected Object doCreateBean(){
	...
	// 使用构造器/工厂方法   instanceWrapper是一个BeanWrapper
	instanceWrapper = createBeanInstance(beanName, mbd, args);
	// 此处bean为"原始Bean"   也就是这里的A实例对象：A@1234
	final Object bean = instanceWrapper.getWrappedInstance();
	...
	// 是否要提前暴露（允许循环依赖）  现在此处A是被允许的
	boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName));
	
	// 允许暴露，就把A绑定在ObjectFactory上，注册到三级缓存`singletonFactories`里面去保存着
	// Tips:这里后置处理器的getEarlyBeanReference方法会被促发，自动代理创建器在此处创建代理对象（注意执行时机 为执行三级缓存的时候）
	if (earlySingletonExposure) {
		addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
	}
	...
	// exposedObject 为最终返回的对象，此处为原始对象bean也就是A@1234,下面会有用处
	Object exposedObject = bean; 
	// 给A@1234属性完成赋值，@Autowired在此处起作用~
	// 因此此处会调用getBean("b")，so 会重复上面步骤创建B类的实例
	// 此处我们假设B已经创建好了 为B@5678
	
	// 需要注意的是在populateBean("b")的时候依赖有beanA，所以此时候调用getBean("a")最终会调用getSingleton("a")，
	//此时候上面说到的getEarlyBeanReference方法就会被执行。这也解释为何我们@Autowired是个代理对象，而不是普通对象的根本原因
	
	populateBean(beanName, mbd, instanceWrapper);
	// 实例化。这里会执行后置处理器BeanPostProcessor的两个方法
	// 此处注意：postProcessAfterInitialization()是有可能返回一个代理对象的，这样exposedObject 就不再是原始对象了  特备注意哦~~~
	// 比如处理@Aysnc的AsyncAnnotationBeanPostProcessor它就是在这个时间里生成代理对象的（有坑，请小心使用@Aysnc）
	exposedObject = initializeBean(beanName, exposedObject, mbd);

	... // 至此，相当于A@1234已经实例化完成、初始化完成（属性也全部赋值了~）
	// 这一步我把它理解为校验：校验：校验是否有循环引用问题~~~~~

	if (earlySingletonExposure) {
		// 注意此处第二个参数传的false，表示不去三级缓存里singletonFactories再去调用一次getObject()方法了~~~
		// 上面建讲到了由于B在初始化的时候，会触发A的ObjectFactory.getObject()  所以a此处已经在二级缓存earlySingletonObjects里了
		// 因此此处返回A的实例：A@1234
		Object earlySingletonReference = getSingleton(beanName, false);
		if (earlySingletonReference != null) {
		
			// 这个等式表示，exposedObject若没有再被代理过，这里就是相等的
			// 显然此处我们的a对象的exposedObject它是没有被代理过的  所以if会进去~
			// 这种情况至此，就全部结束了~~~
			if (exposedObject == bean) {
				exposedObject = earlySingletonReference;
			}
	
			// 继续以A为例，比如方法标注了@Aysnc注解，exposedObject此时候就是一个代理对象，因此就会进到这里来
			//hasDependentBean(beanName)是肯定为true，因为getDependentBeans(beanName)得到的是["b"]这个依赖
			else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
				String[] dependentBeans = getDependentBeans(beanName);
				Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);

				// A@1234依赖的是["b"]，所以此处去检查b
				// 如果最终存在实际依赖的bean：actualDependentBeans不为空 那就抛出异常  证明循环引用了~
				for (String dependentBean : dependentBeans) {
					// 这个判断原则是：如果此时候b并还没有创建好，this.alreadyCreated.contains(beanName)=true表示此bean已经被创建过，就返回false
					// 若该bean没有在alreadyCreated缓存里，就是说没被创建过(其实只有CreatedForTypeCheckOnly才会是此仓库)
					if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
						actualDependentBeans.add(dependentBean);
					}
				}
				if (!actualDependentBeans.isEmpty()) {
					throw new BeanCurrentlyInCreationException(beanName,
							"Bean with name '" + beanName + "' has been injected into other beans [" +
							StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +
							"] in its raw version as part of a circular reference, but has eventually been " +
							"wrapped. This means that said other beans do not use the final version of the " +
							"bean. This is often the result of over-eager type matching - consider using " +
							"'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
				}
			}
		}
	}
}
```

------

由于关键代码部分的步骤不太好拆分，为了更具象表达，那么使用下面一副图示帮助小伙伴们理解： 

![循环依赖流程图](JUC.assets\循环依赖流程图.png)

------

**最后的最后，由于我太暖心了_，再来个纯文字版的总结。** 依旧以上面`A`、`B`类使用属性`field`注入循环依赖的例子为例，对整个流程做文字步骤总结如下：

1. 使用`context.getBean(A.class)`，旨在获取容器内的单例A(若A不存在，就会走A这个Bean的创建流程)，显然初次获取A是不存在的，因此走**A的创建之路~**
2. `实例化`A（注意此处仅仅是实例化），并将它放进`缓存`（此时A已经实例化完成，已经可以被引用了）
3. `初始化`A：`@Autowired`依赖注入B（此时需要去容器内获取B）
4. 为了完成依赖注入B，会通过`getBean(B)`去容器内找B。但此时B在容器内不存在，就走向**B的创建之路~**
5. `实例化`B，并将其放入缓存。（此时B也能够被引用了）
6. `初始化`B，`@Autowired`依赖注入A（此时需要去容器内获取A）
7. `此处重要`：初始化B时会调用`getBean(A)`去容器内找到A，上面我们已经说过了此时候因为A已经实例化完成了并且放进了缓存里，所以这个时候去看缓存里是已经存在A的引用了的，所以`getBean(A)`能够正常返回
8. **B初始化成功**（此时已经注入A成功了，已成功持有A的引用了），return（注意此处return相当于是返回最上面的`getBean(B)`这句代码，回到了初始化A的流程中~）。
9. 因为B实例已经成功返回了，因此最终**A也初始化成功**
10. **到此，B持有的已经是初始化完成的A，A持有的也是初始化完成的B，完美~**

站的角度高一点，宏观上看Spring处理循环依赖的整个流程就是如此。希望这个宏观层面的总结能更加有助于小伙伴们对Spring解决循环依赖的原理的了解，**同时也顺便能解释为何构造器循环依赖就不好使的原因。**

------

------

------

### 2.4 循环依赖对AOP代理对象创建`流程和结果`的影响

我们都知道**Spring AOP、事务**等都是通过代理对象来实现的，而**事务**的代理对象是由自动代理创建器来自动完成的。也就是说Spring最终给我们放进容器里面的是一个代理对象，**而非原始对象**。

本文结合`循环依赖`，回头再看AOP代理对象的创建过程，和最终放进容器内的动作，非常有意思。

```java
@Service
public class HelloServiceImpl implements HelloService {
    @Autowired
    private HelloService helloService;
    
    @Transactional
    @Override
    public Object hello(Integer id) {
        return "service hello";
    }
}
```

此`Service`类使用到了事务，所以最终会生成一个JDK动态代理对象`Proxy`。刚好它又存在`自己引用自己`的循环依赖。看看这个Bean的创建概要描述如下：

```java
protected Object doCreateBean( ... ){
	...
	
	// 这段告诉我们：如果允许循环依赖的话，此处会添加一个ObjectFactory到三级缓存里面，以备创建对象并且提前暴露引用~
	// 此处Tips：getEarlyBeanReference是后置处理器SmartInstantiationAwareBeanPostProcessor的一个方法，它的功效为：
	// 保证自己被循环依赖的时候，即使被别的Bean @Autowire进去的也是代理对象~~~~  AOP自动代理创建器此方法里会创建的代理对象~~~
	// Eagerly cache singletons to be able to resolve circular references
	// even when triggered by lifecycle interfaces like BeanFactoryAware.
	boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName));
	if (earlySingletonExposure) { // 需要提前暴露（支持循环依赖），就注册一个ObjectFactory到三级缓存
		addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
	}

	// 此处注意：如果此处自己被循环依赖了  那它会走上面的getEarlyBeanReference，从而创建一个代理对象从三级缓存转移到二级缓存里
	// 注意此时候对象还在二级缓存里，并没有在一级缓存。并且此时可以知道exposedObject仍旧是原始对象~~~
	populateBean(beanName, mbd, instanceWrapper);
	exposedObject = initializeBean(beanName, exposedObject, mbd);
	
	// 经过这两大步后，exposedObject还是原始对象（注意此处以事务的AOP为例子的，
	// 因为事务的AOP自动代理创建器在getEarlyBeanReference创建代理后，initializeBean就不会再重复创建了，二选一的，下面会有描述~~~）
	
	...
	
	// 循环依赖校验（非常重要）~~~~
	if (earlySingletonExposure) {
		// 前面说了因为自己被循环依赖了，所以此时候代理对象还在二级缓存里~~~（备注：本利讲解的是自己被循环依赖了的情况）
		// so，此处getSingleton，就会把里面的对象拿出来，我们知道此时候它已经是个Proxy代理对象~~~
		// 最后赋值给exposedObject  然后return出去，进而最终被addSingleton()添加进一级缓存里面去  
		// 这样就保证了我们容器里**最终实际上是代理对象**，而非原始对象~~~~~
		Object earlySingletonReference = getSingleton(beanName, false);
		if (earlySingletonReference != null) {
			if (exposedObject == bean) { // 这个判断不可少（因为如果initializeBean改变了exposedObject ，就不能这么玩了，否则就是两个对象了~~~）
				exposedObject = earlySingletonReference;
			}
		}
		...
	}
	
}
```

上演示的是`代理对象+自己存在循环依赖`的case：Spring用三级缓存很巧妙的进行解决了。 若是这种case：代理对象，但是自己并**不存在循环依赖**，过程稍微有点不一样儿了，如下描述：

```java
protected Object doCreateBean( ... ) {
		...
		// 这些语句依旧会执行，三级缓存里是会加入的  表示它支持被循环引用嘛~~~
		addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
	...
	
	// 此处注意，因为它没有被其它Bean循环引用（注意是循环引用，而不是直接引用~）,所以上面getEarlyBeanReference不会执行~
	// 也就是说此时二级缓存里并不会存在它~~~ 知晓这点特别的重要
	populateBean(beanName, mbd, instanceWrapper);
	// 重点在这：AnnotationAwareAspectJAutoProxyCreator自动代理创建器此处的postProcessAfterInitialization方法里，会给创建一个代理对象返回
	// 所以此部分执行完成后，exposedObject **已经是个代理对象**而不再是个原始对象了~~~~ 此时二级缓存里依旧无它，更别提一级缓存了
	exposedObject = initializeBean(beanName, exposedObject, mbd);

	...
	
	// 循环依赖校验
	if (earlySingletonExposure) {
		// 前面说了一级、二级缓存里都木有它，然后这里传的又是false（表示不看三级缓存~~）
		// 所以毋庸置疑earlySingletonReference = null  so下面的逻辑就不用看了，直接return出去~~
		// 然后执行addSingleton()方法，由此可知  容器里最终存在的也还是代理对象~~~~~~
		Object earlySingletonReference = getSingleton(beanName, false);
		if (earlySingletonReference != null) {
			if (exposedObject == bean) { // 这个判断不可少（因为如果initializeBean改变了exposedObject ，就不能这么玩了，否则就是两个对象了~~~）
				exposedObject = earlySingletonReference;
			}
		}...
		...
		...
	}
}
```

分析可知，即使自己只需要代理，并不被循环引用，最终存在Spring容器里的**仍旧是**代理对象。（so此时别人直接`@Autowired`进去的也是代理对象呀~~~）

**终极case：如果我关闭Spring容器的循环依赖能力，也就是把`allowCircularReferences`设值为false，那么会不会造成什么问题呢？**

```java
// 它用于关闭循环引用（关闭后只要有循环引用现象就直接报错~~）
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ((AbstractAutowireCapableBeanFactory) beanFactory).setAllowCircularReferences(false);
    }
}
```

若关闭了循环依赖后，还存在上面A、B的循环依赖现象，启动便会报错如下：

```javascript
Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'a': Requested bean is currently in creation: Is there an unresolvable circular reference?
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.beforeSingletonCreation(DefaultSingletonBeanRegistry.java:339)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:215)
```

>  注意此处异常类型也是`BeanCurrentlyInCreationException`异常，但是文案内容和上面强调的有所区别~~ 它报错位置在：`DefaultSingletonBeanRegistry.beforeSingletonCreation`这个位置~ 

`报错浅析`：在实例化A后给其属性赋值时，会去实例化B。B实例化完成后会继续给B属性赋值，这时由于此时我们`关闭了循环依赖`，所以不存在`提前暴露`引用这么一说来给实用。因此B无法直接拿到A的引用地址，因此只能又去创建A的实例。**而此时我们知道A其实已经正在创建中了**，不能再创建了。so，就报错了~

```java
@Service
public class HelloServiceImpl implements HelloService {

	// 因为管理了循环依赖，所以此处不能再依赖自己的
	// 但是：我们的此bean还是需要AOP代理的~~~
    //@Autowired
    //private HelloService helloService;
    
    @Transactional
    @Override
    public Object hello(Integer id) {
        return "service hello";
    }
}
```

这样它的大致运行如下：

```java
protected Object doCreateBean( ... ) {
	// 毫无疑问此时候earlySingletonExposure = false  也就是Bean都不会提前暴露引用了  显然就不能被循环依赖了~
	boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName));
	...
	populateBean(beanName, mbd, instanceWrapper);
	// 若是事务的AOP  在这里会为源生Bean创建代理对象（因为上面没有提前暴露这个代理）
	exposedObject = initializeBean(beanName, exposedObject, mbd);

	if (earlySingletonExposure) {
		... 这里更不用说，因为earlySingletonExposure=false  所以上面的代理对象exposedObject 直接return了~
	}
}
```

可以看到即使把这个开关给关了，最终放进容器了的仍旧是代理对象，显然`@Autowired`给属性赋值的也一定是代理对象。

最后，以`AbstractAutoProxyCreator`为例看看自动代理创建器是怎么配合实现：循环依赖+创建代理

>  `AbstractAutoProxyCreator`是抽象类，它的三大实现子类`InfrastructureAdvisorAutoProxyCreator`、`AspectJAwareAdvisorAutoProxyCreator`、`AnnotationAwareAspectJAutoProxyCreator`小伙伴们应该会更加的熟悉些 

该抽象类实现了创建代理的动作：

```java
// @since 13.10.2003  它实现代理创建的方法有如下两个
// 实现了SmartInstantiationAwareBeanPostProcessor 所以有方法getEarlyBeanReference来只能的解决循环引用问题：提前把代理对象暴露出去~
public abstract class AbstractAutoProxyCreator extends ProxyProcessorSupport implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {
	...
	// 下面两个方法是自动代理创建器创建代理对象的唯二的两个节点~

	// 提前暴露代理对象的引用  它肯定在postProcessAfterInitialization之前执行
	// 所以它并不需要判断啥的~~~~  创建好后放进缓存earlyProxyReferences里  注意此处value是原始Bean
	@Override
	public Object getEarlyBeanReference(Object bean, String beanName) {
		Object cacheKey = getCacheKey(bean.getClass(), beanName);
		this.earlyProxyReferences.put(cacheKey, bean);
		return wrapIfNecessary(bean, beanName, cacheKey);
	}

	// 因为它会在getEarlyBeanReference之后执行，所以此处的重要逻辑是下面的判断
	@Override
	public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
		if (bean != null) {
			Object cacheKey = getCacheKey(bean.getClass(), beanName);
			// remove方法返回被移除的value，上面说了它记录的是原始bean
			// 若被循环引用了，那就是执行了上面的`getEarlyBeanReference`方法，所以此时remove返回值肯定是==bean的（注意此时方法入参的bean还是原始对象）
			// 若没有被循环引用，getEarlyBeanReference()不执行 所以remove方法返回null，所以就进入if执行此处的创建代理对象方法~~~
			if (this.earlyProxyReferences.remove(cacheKey) != bean) {
				return wrapIfNecessary(bean, beanName, cacheKey);
			}
		}
		return bean;
	}
	...
}
```

由上可知，自动代理创建器它保证了代理对象只会被创建一次，而且支持循环依赖的自动注入的依旧是代理对象。

**`上面分析了三种case，现给出结论如下：`** 不管是自己被循环依赖了还是没有，**甚至是把Spring容器的循环依赖给关了**，它对AOP代理的创建流程有影响，**但对结果是无影响的。**

## 3. Spring MVC

![img](JUC.assets\springMVC)

![image-20210912190512831](JUC.assets\image-20210912190512831.png)

## 4. [java动态代理](https://www.cnblogs.com/gonjan-blog/p/6685611.html)

关于Java中的动态代理，我们首先需要了解的是一种常用的设计模式--代理模式，而对于代理，根据创建代理类的时间点，又可以分为静态代理和动态代理。

###  一、代理模式

  代理模式是常用的java设计模式，他的特征是代理类与委托类有同样的接口，代理类主要负责为委托类预处理消息、过滤消息、把消息转发给委托类，以及事后处理消息等。代理类与委托类之间通常会存在关联关系，一个代理类的对象与一个委托类的对象关联，代理类的对象本身并不真正实现服务，而是通过调用委托类的对象的相关方法，来提供特定的服务。简单的说就是，我们在访问实际对象时，是通过代理对象来访问的，代理模式就是在访问实际对象时引入一定程度的间接性，因为这种间接性，可以附加多种用途。在后面我会

解释这种间接性带来的好处。代理模式结构图（图片来自《大话设计模式》）：

![img](JUC.assets\1085268-20170409105440082-1652546649.jpg)                             

### 二、静态代理

  **1、静态代理**

静态代理：由程序员创建或特定工具自动生成源代码，也就是在编译时就已经将接口，被代理类，代理类等确定下来。在程序运行之前，代理类的.class文件就已经生成。

  **2、静态代理简单实现**

 根据上面代理模式的类图，来写一个简单的静态代理的例子。我这儿举一个比较粗糙的例子，假如一个班的同学要向老师交班费，但是都是通过班长把自己的钱转交给老师。这里，班长就是代理学生上交班费，

班长就是学生的代理。

  首先，我们创建一个Person接口。这个接口就是学生（被代理类），和班长（代理类）的公共接口，他们都有上交班费的行为。这样，学生上交班费就可以让班长来代理执行。

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```java
/**
 * 创建Person接口
 * @author Gonjan
 */
public interface Person {
    //上交班费
    void giveMoney();
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

Student类实现Person接口。Student可以具体实施上交班费的动作。

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```java
public class Student implements Person {
    private String name;
    public Student(String name) {
        this.name = name;
    }
    
    @Override
    public void giveMoney() {
       System.out.println(name + "上交班费50元");
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

StudentsProxy类，这个类也实现了Person接口，但是还另外持有一个学生类对象，由于实现了Peson接口，同时持有一个学生对象，那么他可以代理学生类对象执行上交班费（执行giveMoney()方法）行为。

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```java
/**
 * 学生代理类，也实现了Person接口，保存一个学生实体，这样既可以代理学生产生行为
 * @author Gonjan
 *
 */
public class StudentsProxy implements Person{
    //被代理的学生
    Student stu;
    
    public StudentsProxy(Person stu) {
        // 只代理学生对象
        if(stu.getClass() == Student.class) {
            this.stu = (Student)stu;
        }
    }
    
    //代理上交班费，调用被代理学生的上交班费行为
    public void giveMoney() {
        stu.giveMoney();
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

下面测试一下，看如何使用代理模式：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```java
public class StaticProxyTest {
    public static void main(String[] args) {
        //被代理的学生张三，他的班费上交有代理对象monitor（班长）完成
        Person zhangsan = new Student("张三");
        
        //生成代理对象，并将张三传给代理对象
        Person monitor = new StudentsProxy(zhangsan);
        
        //班长代理上交班费
        monitor.giveMoney();
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

运行结果：

![img](JUC.assets\1085268-20170409141805347-556037068.png)

这里并没有直接通过张三（被代理对象）来执行上交班费的行为，而是通过班长（代理对象）来代理执行了。这就是代理模式。

代理模式最主要的就是有一个公共接口（Person），一个具体的类（Student），一个代理类（StudentsProxy）,代理类持有具体类的实例，代为执行具体类实例方法。上面说到，代理模式就是在访问实际对象时引入一定程度的间接性，因为这种间接性，可以附加多种用途。这里的间接性就是指不直接调用实际对象的方法，那么我们在代理过程中就可以加上一些其他用途。就这个例子来说，加入班长在帮张三上交班费之前想要先反映一下张三最近学习有很大进步，通过代理模式很轻松就能办到：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```java
public class StudentsProxy implements Person{
    //被代理的学生
    Student stu;
    
    public StudentsProxy(Person stu) {
        // 只代理学生对象
        if(stu.getClass() == Student.class) {
            this.stu = (Student)stu;
        }
    }
    
    //代理上交班费，调用被代理学生的上交班费行为
    public void giveMoney() {
        System.out.println("张三最近学习有进步！");
        stu.giveMoney();
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

运行结果：

![img](JUC.assets\1085268-20170409143428488-1662654340.png)

可以看到，只需要在代理类中帮张三上交班费之前，执行其他操作就可以了。这种操作，也是使用代理模式的一个很大的优点。最直白的就是在Spring中的面向切面编程（AOP），我们能在一个切点之前执行一些操作，在一个切点之后执行一些操作，这个切点就是一个个方法。这些方法所在类肯定就是被代理了，在代理过程中切入了一些其他操作。

### **三、动态代理**

   **1.动态代理**

  代理类在程序运行时创建的代理方式被成为动态代理。 我们上面静态代理的例子中，代理类(studentProxy)是自己定义好的，在程序运行之前就已经编译完成。然而动态代理，代理类并不是在Java代码中定义的，而是在运行时根据我们在Java代码中的“指示”动态生成的。相比于静态代理， 动态代理的优势在于可以很方便的对代理类的函数进行统一的处理，而不用修改每个代理类中的方法。 比如说，想要在每个代理的方法前都加上一个处理方法：

```java
 public void giveMoney() {
        //调用被代理方法前加入处理方法
        beforeMethod();
        stu.giveMoney();
    }
```

 

这里只有一个giveMoney方法，就写一次beforeMethod方法，但是如果出了giveMonney还有很多其他的方法，那就需要写很多次beforeMethod方法，麻烦。那看看下面动态代理如何实现。

   **2、动态代理简单实现**

在java的java.lang.reflect包下提供了一个Proxy类和一个InvocationHandler接口，通过这个类和这个接口可以生成JDK动态代理类和动态代理对象。

创建一个动态代理对象步骤，具体代码见后面：

- 创建一个InvocationHandler对象

```java
//创建一个与代理对象相关联的InvocationHandler
 InvocationHandler stuHandler = new MyInvocationHandler<Person>(stu);
```

 

 

- 使用Proxy类的getProxyClass静态方法生成一个动态代理类stuProxyClass 

```java
  Class<?> stuProxyClass = Proxy.getProxyClass(Person.class.getClassLoader(), new Class<?>[] {Person.class});
```

 

- 获得stuProxyClass 中一个带InvocationHandler参数的构造器constructor

```
Constructor<?> constructor = PersonProxy.getConstructor(InvocationHandler.class);
```

 

- 通过构造器constructor来创建一个动态实例stuProxy

```
Person stuProxy = (Person) cons.newInstance(stuHandler);
```

 

就此，一个动态代理对象就创建完毕，当然，上面四个步骤可以通过Proxy类的newProxyInstances方法来简化：

```java
 //创建一个与代理对象相关联的InvocationHandler
  InvocationHandler stuHandler = new MyInvocationHandler<Person>(stu);
//创建一个代理对象stuProxy，代理对象的每个执行方法都会替换执行Invocation中的invoke方法
  Person stuProxy= (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{Person.class}, stuHandler);
```

 

到这里肯定都会很疑惑，这动态代理到底是如何执行的，是如何通过代理对象来执行被代理对象的方法的，先不急，我们先看看一个简单的完整的动态代理的例子。还是上面静态代理的例子，班长需要帮学生代交班费。**
**首先是定义一个Person接口:

```java
/**
 * 创建Person接口
 * @author Gonjan
 */
public interface Person {
    //上交班费
    void giveMoney();
}
```

创建需要被代理的实际类：

```java
public class Student implements Person {
    private String name;
    public Student(String name) {
        this.name = name;
    }
    
    @Override
    public void giveMoney() {
        try {
          //假设数钱花了一秒时间
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       System.out.println(name + "上交班费50元");
    }
}
        
```

再定义一个检测方法执行时间的工具类，在任何方法执行前先调用start方法，执行后调用finsh方法，就可以计算出该方法的运行时间，这也是一个最简单的方法执行时间检测工具。

```java
public class MonitorUtil {
    
    private static ThreadLocal<Long> tl = new ThreadLocal<>();
    
    public static void start() {
        tl.set(System.currentTimeMillis());
    }
    
    //结束时打印耗时
    public static void finish(String methodName) {
        long finishTime = System.currentTimeMillis();
        System.out.println(methodName + "方法耗时" + (finishTime - tl.get()) + "ms");
    }
}
```

创建StuInvocationHandler类，实现InvocationHandler接口，这个类中持有一个被代理对象的实例target。InvocationHandler中有一个invoke方法，所有执行代理对象的方法都会被替换成执行invoke方法。

再再invoke方法中执行被代理对象target的相应方法。当然，在代理过程中，我们在真正执行被代理对象的方法前加入自己其他处理。这也是Spring中的AOP实现的主要原理，这里还涉及到一个很重要的关于java反射方面的基础知识。

```java
public class StuInvocationHandler<T> implements InvocationHandler {
   //invocationHandler持有的被代理对象
    T target;
    
    public StuInvocationHandler(T target) {
       this.target = target;
    }
    
    /**
     * proxy:代表动态代理对象
     * method：代表正在执行的方法
     * args：代表调用目标方法时传入的实参
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理执行" +method.getName() + "方法");
     */   
        //代理过程中插入监测方法,计算该方法耗时
        MonitorUtil.start();
        Object result = method.invoke(target, args);
        MonitorUtil.finish(method.getName());
        return result;
    }
}
```

做完上面的工作后，我们就可以具体来创建动态代理对象了，上面简单介绍了如何创建动态代理对象，我们使用简化的方式创建动态代理对象：

```java
public class ProxyTest {
    public static void main(String[] args) {
        
        //创建一个实例对象，这个对象是被代理的对象
        Person zhangsan = new Student("张三");
        
        //创建一个与代理对象相关联的InvocationHandler
        InvocationHandler stuHandler = new StuInvocationHandler<Person>(zhangsan);
        
        //创建一个代理对象stuProxy来代理zhangsan，代理对象的每个执行方法都会替换执行Invocation中的invoke方法
        Person stuProxy = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{Person.class}, stuHandler)；

       //代理执行上交班费的方法
        stuProxy.giveMoney();
    }
}
```

我们执行这个ProxyTest类，先想一下，我们创建了一个需要被代理的学生张三，将zhangsan对象传给了stuHandler中，我们在创建代理对象stuProxy时，将stuHandler作为参数了的，上面也有说到所有执行代理对象的方法都会被替换成执行invoke方法，也就是说，最后执行的是StuInvocationHandler中的invoke方法。所以在看到下面的运行结果也就理所当然了。

运行结果：

![img](JUC.assets\1085268-20170409164136175-1515319571.png)

上面说到，动态代理的优势在于可以很方便的对代理类的函数进行统一的处理，而不用修改每个代理类中的方法。是因为所有被代理执行的方法，都是通过在InvocationHandler中的invoke方法调用的，所以我们只要在invoke方法中统一处理，就可以对所有被代理的方法进行相同的操作了。例如，这里的方法计时，所有的被代理对象执行的方法都会被计时，然而我只做了很少的代码量。

动态代理的过程，代理对象和被代理对象的关系不像静态代理那样一目了然，清晰明了。因为动态代理的过程中，我们并没有实际看到代理类，也没有很清晰地的看到代理类的具体样子，而且动态代理中被代理对象和代理对象是通过InvocationHandler来完成的代理过程的，其中具体是怎样操作的，为什么代理对象执行的方法都会通过InvocationHandler中的invoke方法来执行。带着这些问题，我们就需要对java动态代理的源码进行简要的分析，弄清楚其中缘由。

### **四、动态代理原理分析**

  **1、Java动态代理创建出来的动态代理类**

上面我们利用Proxy类的newProxyInstance方法创建了一个动态代理对象，查看该方法的源码，发现它只是封装了创建动态代理类的步骤(红色标准部分)：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```java
    public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          InvocationHandler h)
        throws IllegalArgumentException
    {
        Objects.requireNonNull(h);

        final Class<?>[] intfs = interfaces.clone();
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
        }

        /*
         * Look up or generate the designated proxy class.
         */
        Class<?> cl = getProxyClass0(loader, intfs);

        /*
         * Invoke its constructor with the designated invocation handler.
         */
        try {
            if (sm != null) {
                checkNewProxyPermission(Reflection.getCallerClass(), cl);
            }

            final Constructor<?> cons = cl.getConstructor(constructorParams);
            final InvocationHandler ih = h;
            if (!Modifier.isPublic(cl.getModifiers())) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        cons.setAccessible(true);
                        return null;
                    }
                });
            }
            return cons.newInstance(new Object[]{h});
        } catch (IllegalAccessException|InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new InternalError(t.toString(), t);
            }
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString(), e);
        }
    }
```

其实，我们最应该关注的是 Class<?> cl = getProxyClass0(loader, intfs);这句，这里产生了代理类，后面代码中的构造器也是通过这里产生的类来获得，可以看出，这个类的产生就是整个动态代理的关键，由于是动态生成的类文件，我这里不具体进入分析如何产生的这个类文件，只需要知道这个类文件时缓存在java虚拟机中的，我们可以通过下面的方法将其打印到文件里面，一睹真容：

```java
        byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy0", Student.class.getInterfaces());
        String path = "G:/javacode/javase/Test/bin/proxy/StuProxy.class";
        try(FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(classFile);
            fos.flush();
            System.out.println("代理类class文件写入成功");
        } catch (Exception e) {
           System.out.println("写文件错误");
        }
```



对这个class文件进行反编译，我们看看jdk为我们生成了什么样的内容：\

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import proxy.Person;

public final class $Proxy0 extends Proxy implements Person
{
  private static Method m1;
  private static Method m2;
  private static Method m3;
  private static Method m0;
  
  /**
  *注意这里是生成代理类的构造方法，方法参数为InvocationHandler类型，看到这，是不是就有点明白
  *为何代理对象调用方法都是执行InvocationHandler中的invoke方法，而InvocationHandler又持有一个
  *被代理对象的实例，不禁会想难道是....？ 没错，就是你想的那样。
  *
  *super(paramInvocationHandler)，是调用父类Proxy的构造方法。
  *父类持有：protected InvocationHandler h;
  *Proxy构造方法：
  *    protected Proxy(InvocationHandler h) {
  *         Objects.requireNonNull(h);
  *         this.h = h;
  *     }
  *
  */
  public $Proxy0(InvocationHandler paramInvocationHandler)
    throws 
  {
    super(paramInvocationHandler);
  }
  
  //这个静态块本来是在最后的，我把它拿到前面来，方便描述
   static
  {
    try
    {
      //看看这儿静态块儿里面有什么，是不是找到了giveMoney方法。请记住giveMoney通过反射得到的名字m3，其他的先不管
      m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[] { Class.forName("java.lang.Object") });
      m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
      m3 = Class.forName("proxy.Person").getMethod("giveMoney", new Class[0]);
      m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new NoSuchMethodError(localNoSuchMethodException.getMessage());
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new NoClassDefFoundError(localClassNotFoundException.getMessage());
    }
  }
 
  /**
  * 
  *这里调用代理对象的giveMoney方法，直接就调用了InvocationHandler中的invoke方法，并把m3传了进去。
  *this.h.invoke(this, m3, null);这里简单，明了。
  *来，再想想，代理对象持有一个InvocationHandler对象，InvocationHandler对象持有一个被代理的对象，
  *再联系到InvacationHandler中的invoke方法。嗯，就是这样。
  */
  public final void giveMoney()
    throws 
  {
    try
    {
      this.h.invoke(this, m3, null);
      return;
    }
    catch (Error|RuntimeException localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new UndeclaredThrowableException(localThrowable);
    }
  }

  //注意，这里为了节省篇幅，省去了toString，hashCode、equals方法的内容。原理和giveMoney方法一毛一样。

}
```

 

jdk为我们的生成了一个叫$Proxy0（这个名字后面的0是编号，有多个代理类会一次递增）的代理类，这个类文件时放在内存中的，我们在创建代理对象时，就是通过反射获得这个类的构造方法，然后创建的代理实例。通过对这个生成的代理类源码的查看，我们很容易能看出，动态代理实现的具体过程。

我们可以对InvocationHandler看做一个中介类，中介类持有一个被代理对象，在invoke方法中调用了被代理对象的相应方法。通过聚合方式持有被代理对象的引用，把外部对invoke的调用最终都转为对被代理对象的调用。

代理类调用自己方法时，通过自身持有的中介类对象来调用中介类对象的invoke方法，从而达到代理执行被代理对象的方法。也就是说，动态代理通过中介类实现了具体的代理功能。

### **五、总结**

生成的代理类：$Proxy0 extends Proxy implements Person，我们看到代理类继承了Proxy类，所以也就决定了java动态代理只能对接口进行代理，Java的继承机制注定了这些动态代理类们无法实现对class的动态代理。
上面的动态代理的例子，其实就是AOP的一个简单实现了，在目标对象的方法执行之前和执行之后进行了处理，对方法耗时统计。Spring的AOP实现其实也是用了Proxy和InvocationHandler这两个东西的。