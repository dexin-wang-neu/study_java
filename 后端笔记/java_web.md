# JavaWeb

## 1.基本概念

### 1.1前言

web开发：

* 静态web:
   * html,css
   * 数据不会变化

* 动态web
  * 技术栈：Servlet/JSP



### 1.2 web应用程序

## 

## 2.Tomcat

tomcat更改端口号和主机名

![image-20210610202145517](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210610202145517.png)

为了能够访问，需要把drivers配置文件里的etc里的host文件修改127.0.0.1 ————>  其他名

#### 高难度面试题

网站是如何访问的！

* 1.输入一个域名，回车

* 2.检查本机的C:\Windows\System32\drivers\etc\hosts配置文件下有没有这个域名映射

  * 有：直接返回对应的ip地址，这个地址中，有我们需要访问的web程序，可以直接访问

    ```java
    127.0.0.1  www.qingjiang.com
    ```

  * 没有：去DNS服务器找，找到就返回，找不到就返回null



## 3.Http

### 3.1两个时代

* http1.0
  * HTTP/1.0:客户端可以与web服务器连接后，只能获得一个web资源，断开连接
* http2.0
  * HTTP/1.1: 客户端可以与web服务器连接后，可以获得多个web资源

### 3.2http请求

* 客户端 -- 发请求---服务器

百度：

#### 1.请求行

* 请求行中的方式：get
* 请求方式：get,post
  * get:请求能够携带的参数较少，大小有限制，会在浏览器的URL地址栏显示数据，不安全，但高效
  * post:没有限制，不会在URL地址栏显示数据，安全，不高效

```java
Request URL: https://www.baidu.com/
Request Method: GET
Status Code: 200 OK
Remote Address: 182.61.200.7:443
Referrer Policy: strict-origin-when-cross-origin
```

#### 2.请求头

```java
Accept: text/html 		//告诉浏览器，它支持的数据类型
Accept-Encoding: gzip, deflate, br		//支持哪种编码
Accept-Language: zh-CN,zh;q=0.9		
Cache-Control: max-age=0		//缓存控制
Connection: keep-alive		//告诉浏览器，请求完成是断开还是保持连接
```



### 3.3 http响应

* 服务器--相应---客户端

百度：

```java
Cache-Control: private		//缓存控制
Connection: keep-alive		//连接
Content-Encoding: gzip
Content-Type: text/html;charset=utf-8
```

#### 1.状态码

* 200：请求成功
* 3xx:请求重定向
* 4xx:找不到资源
* 5xx:服务器代码错误  500   502：网关错误



#### 2.常见面试题

当你的浏览器中地址栏输入地址并回车的一瞬间到页面能够展示回来，经历了什么？



## 4.Maven

**为什么使用maven**？

1. 在java web中要使用大量的jar包
2. maven可以自动导入和配置jar包

### 4.1maven架构管理工具

maven的核心思想：**约定大于配置**

### 4.2配置环境变量

* M2_HOME  :maven下的bin目录
* MAVEN_HOME: MAVEN的目录
* 在系统的path中配置MAVEN_HOME： %MAVEN_HOME%\bin

### 4.3 配置镜像

```xml
  <mirrors>
    <mirror>
    <id>aliyunmaven</id>
    <mirrorOf>central</mirrorOf>
    <name>aliyun maven</name>
    <url>https://maven.aliyun.com/repository/public </url>
    </mirror>
  </mirrors>
```

### 4.4 本地仓库

**建立一个本地仓库**

```xml
<localRepository>D:\environment\apache-maven-3.8.1\maven-repo</localRepository>
```

### 4.5 在IDEA中使用MAVEN



### 4.6创建一个普通的Maven项目

### 4.7标记文件夹功能

### 4.8在IDEA中配置Tomcat



![image-20210610225048745](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210610225048745.png)

解决警告问题

**为什么会有这个问题：我们访问一个网站，需要指定一个文件夹的而名字。**



![image-20210610225227107](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210610225227107.png)

![image-20210610225618734](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210610225618734.png)

![image-20210610225951759](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210610225951759.png)







maven由于他的约定大于配置，之后可能遇到写的配置文件，无法被导出或生效的问题。

```xml
    <!--  在build中配置resource,来防止我们资源导出失败的问题  -->
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
```

### 4.9 IDEA操作

![image-20210611104654694](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611104654694.png)

### 4.10 解决问道的问题

1. maven3.6.2无法导入的问题

   解决方法：降低版本至3.6.1

2. tomcat闪退

   

3. IDEA中重复配置maven

   在IDEA中的全局默认配置中去配置

   ![image-20210611105413061](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611105413061.png)

4. maven默认web项目中的web.xml版本问题

   ![image-20210611105822457](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611105822457.png)

   

5. 替换为webapp4.0版本和tomcat一致

![image-20210611110059067](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611110059067.png)



## 5.Servlet

### 5.1 hello servlet

​	利用tomcat例子，查看servlet源码怎么写。

![image-20210611111244776](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611111244776.png)

![image-20210611111310122](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611111310122.png)



![image-20210611111322155](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611111322155.png)

###  5.2 maven仓库中查找servlet



**要写一个serlvet要继承HttpServlet类**

在IDEA中编写，发现本地仓库没有这个jar包，去maven仓库找。

![image-20210611111557277](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611111557277.png)

![image-20210611111615091](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611111615091.png)

![image-20210611111721081](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611111721081.png)

在maven仓库中找到的httpServlet都不是，回过头去查看tomcat例子中导入的jar包，是javax.io.servlet。发现也没找到，而tomcat作为服务器能启动，应该有该jar包作为支持，所以去tomcat的lib目录下找，与servlet相关的jar包。

![image-20210611111944047](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611111944047.png)

找到了相关的一个去maven仓库中查找。

![image-20210611112102064](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611112102064.png)

上图，找用的人最多的，且查看所在的文件加目录为  **javax.servlet-api**，确定就是他。

![image-20210611112238992](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611112238992.png)

点进去后，找到使用人数最多的。

![image-20210611112345100](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611112345100.png)

根据上图可以决定下载jar包，还是导入maven依赖

### 5.3 从java ee工程自动生成servlet查看导入了什么包

![image-20210611113021937](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611113021937.png)

![image-20210611113335995](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611113335995.png)

![image-20210611113544354](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611113544354.png)

## 6.Servlet开始

### 6.1Servlet简介

* Servlet就是sun公司开发动态web的一门技术
* Sun在这些API中提供了一个接口叫做：Servlet,如果你想开发一个Servlet程序，只需要万册灰姑娘两个小步骤：
  * 编写一个类，实现Servlet接口
  * 把开发号的java类部署到web服务器中

**把实现了Servlet接口的java程序叫做：Servlet**

### 6.2 HelloServlet

Servlet接口Sun公司有两个默认实现类：HttpServlet,GenericServlet

1. 构建一个普通的Maven项目，删掉里面的src目录，以后再这里建立Moudel;这个空工程就是Maven主工程

2. 关于Maven父子工程的理解

   父项目中会有

   ```xml
       <modules>
           <module>servlet-01</module>
       </modules>
   ```

   子项目中有

   ```xml
       <parent>
           <artifactId>javaweb-02-servlet</artifactId>
           <groupId>cn.neu</groupId>
           <version>1.0-SNAPSHOT</version>
       </parent>
   ```

   父项目中的jar子项目中可以使用

   

3. Maven环境优化

   1. 修改web.xml为最新的
   2. 将maven的结构搭建完整

4. 编写servlet

   1. 编写一个普通类
   2. 实现Servlet接口，这里我们直接继承HttpServlet

   ![image-20210611162555531](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611162555531.png)

   ```java
   public class HelloServlet extends HttpServlet {
       //由于get或post只是请求实现的不同方式，可以相互调用，业务逻辑都一样
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
   //        ServletOutputStream outputStream = resp.getOutputStream();
           PrintWriter writer = resp.getWriter();
           writer.print("hello,servlet");
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           super.doPost(req, resp);
       }
   }
   ```

5. 编写Servlet的映射

   为什么需要映射：我们写的是java程序，但是需要通过浏览器访问，而浏览器需要连接web服务器，所以我们需要在web服务中注册我们写的Servlet.

   还需要给他一个浏览器能够访问的路径。

   ```xml
     <!-- 注册servlet -->
     <servlet>
       <servlet-name>hello</servlet-name>
       <servlet-class>cn.neu.HelloServlet</servlet-class>
     </servlet>
   
     <!-- servlet请求路径 -->
     <servlet-mapping>
       <servlet-name>hello</servlet-name>
       <url-pattern>/hello</url-pattern>
     </servlet-mapping>
   ```

   

6. 配置tomcat

   注意配置tomcat访问的路径

7. 启动测试

### 6.3 servlet运行原理

servlet是由web服务器调用，web服务器收到浏览器请求之后，会：

![image-20210611170232903](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611170232903.png)



流程解释：

1. 浏览器向web容器（tomcat）发起http请求；

2. 如果请求是第一次访问，web容器会产生servlet  class，之后访问就是调用

3. web容器产生两个对象：request（请求头，请求体）、response（响应头，响应体）。对应Java中的Request,Reponse对象

4. web容器调用Servlet,servlet中有一个service方法，同时把request,reponse对象传给service方法。

   ```java
   service(ServletRequest req,ServletReponse res);
   ```

5. service方法调用完会返回，web容器读取响应信息，web容器拿到相应信息之后返回给客户端
6. 我们自己的实现类，重写了这些方法：
   1. 接收并处理请求
   2. 给出响应的信息

### 6.4 mapping问题

1. 一个servlet可以指定一个映射路径

   ```xml
   <!-- servlet请求路径 -->
   <servlet-mapping>
     <servlet-name>hello</servlet-name>
     <url-pattern>/hello</url-pattern>
   </servlet-mapping>
   ```

2. 一个servlet可以指定多个映射路径

   ```xml
   <!-- servlet请求路径 -->
   <servlet-mapping>
     <servlet-name>hello</servlet-name>
     <url-pattern>/hello</url-pattern>
   </servlet-mapping>
   <!-- servlet请求路径 -->
   <servlet-mapping>
     <servlet-name>hello</servlet-name>
     <url-pattern>/hello1</url-pattern>
   </servlet-mapping>
   <!-- servlet请求路径 -->
   <servlet-mapping>
     <servlet-name>hello</servlet-name>
     <url-pattern>/hello2</url-pattern>
   </servlet-mapping>
   <!-- servlet请求路径 -->
   <servlet-mapping>
     <servlet-name>hello</servlet-name>
     <url-pattern>/hello3</url-pattern>
   </servlet-mapping>
   ```

   

3. 一个servlet可以指定通用映射路径

   ```xml
   <!-- servlet请求路径 -->
   <servlet-mapping>
     <servlet-name>hello</servlet-name>
     <url-pattern>/hello/*</url-pattern>  //hello下的任何请求都会进入hello  servlet
   </servlet-mapping>
   ```

4. 默认请求路径

   ```xml
   <!-- 默认请求路径 -->
   <servlet-mapping>
     <servlet-name>hello</servlet-name>
     <url-pattern>/*</url-pattern>		
   </servlet-mapping>
   ```

5. 指定一些前缀或后缀

   ```xml
   <!-- 可以自定义后缀实现请求映射
   注意点：*前面不能加项目映射的路径 
   -->
   <servlet-mapping>
     <servlet-name>hello</servlet-name>
     <url-pattern>*.qing</url-pattern>
   </servlet-mapping>
   ```

6. 优先级问题

   制定了固有的映射路径优先级最高，找不到去找默认的处理请求

   ```xml
     <!-- 注册servlet -->
     <servlet>
       <servlet-name>error</servlet-name>
       <servlet-class>cn.neu.ErrorServlet</servlet-class>
     </servlet>
     <!-- 404路径 -->
     <servlet-mapping>
       <servlet-name>error</servlet-name>
       <url-pattern>/*</url-pattern>
     </servlet-mapping>
   ```

### 6.5  ServletContext

web容器在启动的时候，他会为每个web程序都会创建一个对应的ServletContext对象，他代表了当前的web应用；

#### 1. 共享数据

在这个Servlet中保存的数据，在另一个Servlet中共享

![image-20210611215621523](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210611215621523.png)

放置数据的类：

```java
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        this.getInitParameter();  //初始化参数
//        this.getServletConfig();    //servelt配置
//        this.getServletContext();   //上下文
        ServletContext context = this.getServletContext();
        String username = "秦将"; //数据
        context.setAttribute("username",username);;//将一个数据保存在了ServletContext中，名字：username,值：username

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

读取数据的类：

```java
public class GetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        String username = (String) context.getAttribute("username");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().print("名字"+username);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

web.xml中配置注册和映射

```xml
  <servlet>
    <servlet-name>hello</servlet-name>
    <servlet-class>cn.neu.HelloServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>hello</servlet-name>
    <url-pattern>/hello</url-pattern>
  </servlet-mapping>

  <servlet>
    <servlet-name>getc</servlet-name>
    <servlet-class>cn.neu.GetServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>getc</servlet-name>
    <url-pattern>/getc</url-pattern>
  </servlet-mapping>
```

#### 2. 获取初始化参数

```xml
  <!-- 配置一些web应用初始化参数 -->
  <context-param>
    <param-name>url</param-name>
    <param-value>jdbc:mysql://localhost:3306/mybatis</param-value>
  </context-param>
```

```java
public class ServletDemo03 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = this.getServletContext();
        String url = servletContext.getInitParameter("url");
        resp.getWriter().print(url);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

#### 3.请求转发

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ServletContext servletContext = this.getServletContext();
    RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/gp");//转发到具体地址
    requestDispatcher.forward(req,resp);//实现请求转发
}
```

#### 4.读取资源文件

Properties类：

* 在Java目录下新建properties
* 在resources目录下新建properties

发现：都被打包到了同一个路径下：classes,我们俗称这个路径为类路径：classespath

思路：需要一个文件流

```properties
username=root
password=123456
```

```java
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream is = this.getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties");
        Properties properties = new Properties();
        properties.load(is);
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        resp.getWriter().print(username+":"+password);
    }
```

### 6.6 HttpServletResponse

web服务器接收到客户端的http请求，针对这个请求，分别创建一个代表请求的HttpServletRequest对象，代表响应的HttpServletResponse对象。

* 如果要获取客户端请求火来的参数：找HttpServletRequest
* 如果要给客户端响应一些信息：找HttpServletResponse

#### 1.简单分类

**负责向浏览器发送数据的方法**

```java
public ServletOutputStream getOutputStream() throws IOException;
public PrintWriter getWriter() throws IOException;//写中文用
```

**负责向浏览器发送响应头的方法**

```java
//这四个是ServletResponse接口的方法
public void setCharacterEncoding(String charset);
public void setContentLength(int len);
public void setContentLengthLong(long len);
public void setContentLengthLong(long len);
//这些是HttpServletResponse接口的方法
public void setDateHeader(String name, long date);
public void addDateHeader(String name, long date);
public void setHeader(String name, String value);
public void addHeader(String name, String value);
public void setIntHeader(String name, int value);
public void addIntHeader(String name, int value);
```

**响应的状态码常量**

```java
    public static final int SC_CONTINUE = 100;
    public static final int SC_SWITCHING_PROTOCOLS = 101;
    public static final int SC_OK = 200;
    public static final int SC_CREATED = 201;
    public static final int SC_ACCEPTED = 202;
    public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int SC_NO_CONTENT = 204;
    public static final int SC_RESET_CONTENT = 205;
    public static final int SC_PARTIAL_CONTENT = 206;
    public static final int SC_MULTIPLE_CHOICES = 300;
    public static final int SC_MOVED_PERMANENTLY = 301;
    public static final int SC_MOVED_TEMPORARILY = 302;
    public static final int SC_FOUND = 302;
    public static final int SC_SEE_OTHER = 303;
    public static final int SC_NOT_MODIFIED = 304;
    public static final int SC_USE_PROXY = 305;
    public static final int SC_TEMPORARY_REDIRECT = 307;
    public static final int SC_BAD_REQUEST = 400;
    public static final int SC_UNAUTHORIZED = 401;
    public static final int SC_PAYMENT_REQUIRED = 402;
    public static final int SC_FORBIDDEN = 403;
    public static final int SC_NOT_FOUND = 404;
    public static final int SC_METHOD_NOT_ALLOWED = 405;
    public static final int SC_NOT_ACCEPTABLE = 406;
    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int SC_REQUEST_TIMEOUT = 408;
    public static final int SC_CONFLICT = 409;
    public static final int SC_GONE = 410;
    public static final int SC_LENGTH_REQUIRED = 411;
    public static final int SC_PRECONDITION_FAILED = 412;
    public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;
    public static final int SC_REQUEST_URI_TOO_LONG = 414;
    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    public static final int SC_EXPECTATION_FAILED = 417;
    public static final int SC_INTERNAL_SERVER_ERROR = 500;
    public static final int SC_NOT_IMPLEMENTED = 501;
    public static final int SC_BAD_GATEWAY = 502;
    public static final int SC_SERVICE_UNAVAILABLE = 503;
    public static final int SC_GATEWAY_TIMEOUT = 504;
    public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
```

#### 2.常见应用

1. 向浏览器输出消息

2. 下载文件

   1. 要获取下载文件的路径
   2. 下载的文件名是啥
   3. 想办法让浏览器支持下载我们需要的东西
   4. 获取下载文件的输入流
   5. 创建缓冲区
   6. 获取OutputStream对象
   7. 将FileOutputStream流写入到buffer缓冲区
   8. 使用OutputStream将缓冲区中的数据输出到客户端

   ```java
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           //1. 要获取下载文件的路径
           String realPath = "F:\\java-work\\maven_web\\javaweb-02-servlet\\response\\src\\main\\resources\\image.png";
           System.out.println("下载文件的路径："+realPath);
           //2. 下载的文件名是啥
           String fileName = realPath.substring(realPath.lastIndexOf("\\") + 1);
           //3. 想办法让浏览器支持下载我们需要的东西;中文文件名用URLEncoder.encode(fileName,"UTF-8")解决乱码
           resp.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
           //4. 获取下载文件的输入流
           FileInputStream in = new FileInputStream(realPath);
           //5. 创建缓冲区
           int len = 0;
           byte[] buffer = new byte[1024];
           //6. 获取OutputStream对象
           ServletOutputStream out = resp.getOutputStream();
           //7. 将FileOutputStream流写入到buffer缓冲区
           while ((in.read(buffer)) > 0){
               out.write(buffer,0,len);
           }
           //8. 使用OutputStream将缓冲区中的数据输出到客户端
           in.close();
           out.close();
       }
   ```

#### 3.验证码功能

验证怎么来的？

* 前端实现
* 后端实现，需要用到java的图片实现类，生成一个图片

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //如何让浏览器3秒刷新一次
    resp.setHeader("refresh","3");;

    //在内存中创建一个图片
    BufferedImage image = new BufferedImage(80, 20, BufferedImage.TYPE_INT_RGB);
    //得到图片
    Graphics2D graphics = (Graphics2D) image.getGraphics();//笔
    //是指图片的背景颜色
    graphics.setColor(Color.white);
    graphics.fillRect(0,0,80,20);//填充颜色区域
    //给图片些数据
    graphics.setColor(Color.blue);//画笔颜色
    graphics.setFont(new Font(null,Font.BOLD,20));
    graphics.drawString(makeNum(),0,20);

    //告诉浏览器，这个请求用图片的方式打开
    resp.setContentType("image/jpeg");
    //网站存在缓存，设置不让浏览器缓存
    resp.setDateHeader("expires",-1);
    resp.setHeader("Cache-Control","no-cache");
    resp.setHeader("Pragma","no-cache");

    //把图片写给浏览器
    ImageIO.write(image, "jpg", resp.getOutputStream());
}
//生成随机数
private String makeNum(){
    Random random = new Random();
    String num = random.nextInt(9999999) + "";
    StringBuffer sb = new StringBuffer();
    for (int i = 0;i < 7 - num.length();i++){
        sb.append("0");     //随机生成的位数最大为7位，其他用0填充
    }
    num = sb.toString() + num;   //随机生成的位数最大为7位，其他用0填充
    return num;     //保证必须是7位
}
```

#### 4.重定向

一个web资源收到客户端请求，他会通知客户端去访问两一个web资源，这个过程叫做重定向。

常见场景：

* 用户登录

```java
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    /*
    原理：
    resp.setHeader("Location","/response/image");
    resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);//302
    * */
    resp.sendRedirect("/response/image");//重定向
}
```

**重定向和转发的区别**

相同点：

* 页面都会跳转

不同点：

* 请求转发的时候，url不会产生变化  307
* 重定向的时候，Url会变化  302

### 6.7 HttpServletRequest

 HttpServletRequest代表客户端的请求，用户通过Http协议访问服务器，HTTP请求中的所有信息会被封装到 HttpServletRequest，通过这个 HttpServletRequest的方法，可以获取到客户端的所有信息。

![image-20210612163954404](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210612163954404.png)

#### 1.获取前端传递的参数

![image-20210612164045458](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210612164045458.png)



#### 2.请求转发

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //后台接收中文乱码问题
    req.setCharacterEncoding("utf-8");
    String username = req.getParameter("username");
    String password = req.getParameter("password");
    String[] hobbys = req.getParameterValues("hobbys");

    System.out.println(username );
    System.out.println(password);
    System.out.println(Arrays.toString(hobbys));

    //通过请求转发
    resp.setCharacterEncoding("utf-8");
    //请求转发不需要写项目路径，相对就可以。
    req.getRequestDispatcher("/success.jsp").forward(req,resp);
}
```







## 7.Cookie,Session

### 7.1会话

**会话**：用户打开了一个浏览器，点击了很多超链接，访问了多个web资源，关闭浏览器，这个过程可以称为会话

**有状态会话**：

### 7.2保存会话的两种技术

**cookie**

* 客户端技术（响应，请求）

**session**

* 服务器技术，利用这个技术，可以保存用户的会话信息。可以把信息保存在session种



常见场景：网站登录之后，下次就不用再登录了



### 7.3 Cookie

1. 从请求中获取Cookie信息
2. 服务器响应给客户端cookie

```java
//Cookie,服务器端从客户端获取
Cookie[] cookies = req.getCookies();//这里返回数组，说明Cookie可能有多个
cookie.getName();//获得cookie中的key
cookie.getValue()//获得cookie中的value

//服务器给客户端响应一个Cookie
Cookie cookie = new Cookie("lastLoginTime", System.currentTimeMillis()+"");
//cookie有效期为一天
cookie.setMaxAge(24*60*60);
resp.addCookie(cookie);//响应给客户端一个cookie
```

**cookie一般保存再本地的用户目录下appdata**



* 一个Cookie只能保存一个信息
* 一个web站点可以给浏览器发送多个cookie,最多存放20个cookie
* cookie大小有限制4kb
* 300个cookie浏览器上线



**删除cookie**:

* 不设置有效期，关闭浏览器，自动失效
* 设置有效期为0



**编码解码**

```java
URLEncoder.encode("实验室","utf-8");//编码
URLDecoder.decode(cookie.getValue(),"utf-8");//解码
```



### 7.4 Session(重点)

什么是session:

* 服务器会给每一个用户（浏览器）创建一个Session对象；
* 一个Session独占一个浏览器，只要浏览器没有关闭，这个Session就存在
* 用户登录之后，整个网站他都可以访问。--->保存用户的信息；保存购物车的信息....



Session跟Cookie的区别：

* Cookie是把用户的数据写给用户的浏览器，浏览器保存（可以保存多个）
* Session把用户的数据写到用户独占的Session中，服务器端保存（保存重要的信息，减少服务器资源浪费）
* Session对象由服务器创建

使用场景：

* 保存一个登录用户的信息
* 购物车信息
* 在整个网站中，经常会使用的数据，将他保存咋Session中

使用Session:

```java
 //得到Session
        HttpSession session = req.getSession();

        //给Session中存东西
        session.setAttribute("name",new Person("关晓彤",24));

        //获取Session的ID
        String id = session.getId();

        //判断Session是不是新创建的
        if(session.isNew()){
            resp.getWriter().write("session创建成功，ID:"+id);
        }else{
            resp.getWriter().write("session已经在服务器中了，ID:"+id);
        }

        //session创建的时候做了什么事情：
//        Cookie co = new Cookie("JSESSIONID", id);
//        resp.addCookie(co);

//得到Session
        HttpSession session = req.getSession();

        Person name = (Person) session.getAttribute("name");

//得到Session
        HttpSession session = req.getSession();
        session.removeAttribute("name");
        //手动注销session
        session.invalidate();
```



会话自动过期：

```xml
<!--  设置session默认的失效时间-->
  <session-config>
  <!--    1分钟后session自动失效，以分钟为单位-->
    <session-timeout>1</session-timeout>
  </session-config>
```



## 8. JSP

### 8.1什么是JSP

java Server Pages: Java 服务器端页面，也和Servlet一样，用于动态web技术

最大的特点：

* JSP就像HTML
* 区别：
  * HTML只给用户提供静态数据
  * JSP页面可以嵌入JAVA代码，为用户提供动态数据



### 8.2 JSP原理

**JSP是怎么执行的**

* 服务器内部工作

  * tomcat中有一个work目录

  * IDEA中使用Tomcat的会在IDEA的tomcat生成一个work目录

    ![image-20210613212821408](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210613212821408.png)

    ![image-20210613212911207](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210613212911207.png)

**浏览器向服务器发送请求，不管访问什么资源，其实都是在访问Servlet**

JSP最终会被转换为一个Java类

JSP本质上就是一个Servlet

```java
//初始化
public void _jspInit() {
  }
//销毁
  public void _jspDestroy() {
  }
//JSPService
  public void _jspService(http.HttpServletRequest request, http.HttpServletResponse response)throws java.io.IOException, javax.servlet.ServletException {
```

1. 判断请求

2. 内置对象

   ```java
       final javax.servlet.jsp.PageContext pageContext;//页面上下文
       javax.servlet.http.HttpSession session = null;	//session
       final javax.servlet.ServletContext application;	//applicationContext
       final javax.servlet.ServletConfig config;		//congig
       javax.servlet.jsp.JspWriter out = null;			//out
       final java.lang.Object page = this;				//page:当前
   	HttpServletRequest request;						//请求
   	HttpServletResponse response;					//响应
   ```

   

3. 输出前增加的代码

   ```java
   response.setContentType("text/html");			//设置响应的页面类型
   pageContext = _jspxFactory.getPageContext(this, request, response,null, true, 8192, true);
   _jspx_page_context = pageContext;
   application = pageContext.getServletContext();
   config = pageContext.getServletConfig();
   session = pageContext.getSession();
   out = pageContext.getOut();
   _jspx_out = out;
   ```

4. 以上的这些个对象在jsp页面直接使用

   ![image-20210613215215521](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210613215215521.png)

   在JSP页面中：

   只要是JAVA代码，就会原封不动的输出；

   如果是HTML代码，就会被转换为：

   ```java
   out.write("name");
   ```

   这样的格式，返回到前端。



### 8.3 JSP基础语法

任何语言都有自己的语法，JAVA中有，JSP作为Java技术的一种应用，它拥有一些自己扩充的语法（了解即可），Java的所有语法都支持。



**JSP表达式**

```jsp
<%--  JSP表达式
 作用：用来将程序的输出，输出到客户端
 <%= 变量或表达式%>
 --%>
<%= new java.util.Date()  %>
```

**JSP脚本片段**

```jsp
  <%--  jsp脚本片段 --%>
  <%
    int sum = 0;
    for (int i = 0; i <100 ; i++) {
      sum+=i;
    }
    out.println("<h1>SUM="+sum+"<h1/>");
  %>
```

脚本片段的再实现

```jsp
  <%-- 在代码中嵌入HTML元素 --%>
  <%
    for (int i = 0; i < 5; i++){
  %>
    <h1>Hello,World  <%=i%></h1>
  <%
    }
  %>
```

**JSP声明**

```jsp
  <%!
    static {
      System.out.println("Loading Servlet");
    }
    private int globalVar = 0;
    public void test(){
      System.out.println("进入了TEST方法");
    }
  %>
```

JSP声明会被编译到JSP生成的JAVA类的类中，其他的两个会被生成到service方法中

在JSP中嵌入JAVA代码即可。



JSP的注释不会在客户端中显示，HTML的就会



### 8.4 JSP指令



