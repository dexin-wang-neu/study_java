### SpringBoot 日志

#### 1.日志框架

JUL、JCL、jboss-logging、logback、log4j、log4j2、slf4j....

| 日志的抽象层                                                 | 日志的实现                                      |
| ------------------------------------------------------------ | ----------------------------------------------- |
| ~~JCL(jakarta commons logging)~~     SLF4J(Simple Logging Facade for Java)    ~~jboss-logging~~ | Log4j  JUL(java.util.logging)  Log4j2   Logback |

日志门面：SLF4J;

日志实现：Logback

SpringBoot:底层是Spring框架，Spring框架默认的是JCL

SpringBoot选用的是SLF4J和Logback

#### 2.SLF4J使用

##### 1.如何在系统中使用SLF4J

以后开发的时候，日志记录方法的调用，不应该直接调用日志的实现类，而是调用日志抽象层里的方法。

给系统里面导入SLF4J的jar和logback的jar。

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```

![slf5j](SpringBoot 日志.assets\slf5j.png)

每一个日志的实现框架都有自己的配置文件。使用slf4j以后，**配置文件还是做成日志框架自己本身的配置文件。**

##### 2. 遗留问题

a系统使用（slf4j+logback）:Spring(commons-logging),Hibernate(jboss-logging),Mybatis,xxx

统一日志记录，即使是别的框架和我仪器统一使用slf4j进行输出。

![legacy](SpringBoot 日志.assets\legacy.png)

> 如何让系统中所有的日志都统一到slf4j:
>
> 1. 将系统中其他日志框架先排除出去；
> 2. 用中间包替换原有的日志框架；
> 3. 导入slf4j其他的实现。

#### 3.SpringBoot日志关系

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

SpringBoot使用它来启动日志：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-logging</artifactId>
  <version>2.5.5</version>
  <scope>compile</scope>
</dependency>
```

![image-20211013220417367](SpringBoot 日志.assets\image-20211013220417367-1634133859595.png)

总结：

* SpringBoot底层也是使用slf4j+logback的方式记录日志

* SpringBoot也把其他的日志都替换成了slf4j;

* 中间的替换包

* 如果我们要引入其他框架，一定要把这个框架的默认日志依赖移除调。

  * ![image-20211013221847468](SpringBoot 日志.assets\image-20211013221847468-1634134728410.png)

  **现在的 版本已经没有移除这一属性了**



**SpringBoot能自动适配所有的日志，而且顶层使用slf4j+logback的方式记录日志，引入其他日志框架的时候，只需要把这个框架依赖的日志框架排除掉。**

#### 4.日志使用

##### 1.默认配置

SpringBoot默认帮我们配置好了日志。

```java
@SpringBootTest
class SpringbootLoggingApplicationTests {
    //记录器
    Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    void contextLoads() {
        //日志的级别：
        //由低到高：trace<debug<info<warn<error
        //可以调整输出的日志级别：日志就只会在这个级别及以后的高级别生效
        logger.trace("这是trace日志...");
        logger.debug("这是debug日志...");
        //spring boot默认是info级别的,没有指定级别的就用SpringBoot默认的级别：root级别
        logger.info("这是info日志...");
        logger.warn("这是warn日志...");
        logger.error("这是error日志...");
    }

}
```

```properties
日志输出格式：
	%d表示日期时间，
	%thread表示线程名，
	%-5level：级别从左到右显示5个字符宽度，
	%logger{50}:logger名字最长显示50个字符，否则按照句点分割
	%msg:日志消息
	%n:换行符
	
logging.level.com.neu = trace
#logging.file.path = D:/logs
logging.file.name= iqc.log
# 在控制台输出的日志的格式
logging.pattern.console= %d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n
# 指定文件中日志输出的格式
logging.pattern.file= %d{yyyy-MM-dd} === [%thread] === %-5level ===%logger{50} === %msg%n
```

##### 2.指定配置

给类路径下放上每个日志框架自己的配置文件即可，；SpringBoot就使用自己默认的配置了。

| Logging System          | Customization                                                |
| ----------------------- | ------------------------------------------------------------ |
| Logback                 | `logback-spring.xml`, `logback-spring.groovy`, `logback.xml` or `logback.groovy` |
| Log4j2                  | `log4j2-spring.xml` or `log4j2.xml`                          |
| JDK (Java Util Logging) | `logging.properties`                                         |

logback.xml:直接就被日志框架识别了

logback-spring.xml:日志框架就不直接加载日志的配置项，由SpringBoot加载，可以使用SpringBoot的高级**Profile**功能。

```xml
<springProfile name="staging">
    <!-- configuration to be enabled when the "staging" profile is active -->
    可以指定某段配置只在某个环境下生效
</springProfile>
```

#### 5.切换日志框架

可以按照slf4j日志适配图，进行相关的切换。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>logback-classic</artifactId>
            <groupId>ch.qos.logback</groupId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
</dependency>
```

切换为log4j2

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>spring-boot-starter-logging</artifactId>
                    <groupId>org.springframework.boot</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>
```

然后在类路径下添加：log4j2-spring.xml文件

