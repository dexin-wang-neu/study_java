# Tomcat

## 1.Tomcat底层架构

![image-20210817221121173](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817221121173.png)

### 1.1 连接器(Connector):Coyote

#### 1.1.1架构介绍

Coyote是Tomcat的连接器框架的名称，是Tomcat服务器提供的供客户端访问的外部接口。客户端通过Coyote与服务器建立连接、发送请求并接收响应。

Coyote封装了底层的网络通信（Socker请求即响应处理），为Catalina容器提供了统一的接口，使Catalina容器与具体的请求协议即IO操作方式完全解耦。Coyote将Socket输入封装为Request对象，交由Catalina容器进行处理，处理请求完成 后，Catalina通过Coyote提供的Response对象将结果写入输出流。

Coyote作为独立的模块，只负责具体的协议和IO的相关操作，与Servlet规范实现没有任何关系，因此即便是Request和Response对象也并未实现Servlet规范的接口，而是在Catalina中将他们进一步封装为ServletResquest和ServletResponse.

![image-20210817221855041](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817221855041.png)

#### 1.1.2 IO模型和协议

在Coyote中，Tomcat支持的多种I/O模型和应用协议，具体包含如下：

Tomcat支持的IO模型（自8.5/9.0版本起，Tomcat移除了BIO):

| IO模型 | 描述                                                         |
| ------ | ------------------------------------------------------------ |
| NIO    | 非阻塞I/O，采用java NIO类库实现。                            |
| NIO2   | 异步I/O，采用JDK 7最新的NIO2类库实现                         |
| APR    | 采用Apache可移植运行库实现，使C/C++编写的本地库。如果选择该方案，需要单独安装APR库。 |



Tomcat支持的应用协议:

| 应用协议 | 描述                                                         |
| -------- | ------------------------------------------------------------ |
| HTTP/1.1 | 这是大部分web应用采用的访问协议                              |
| AJP      | 用于和web服务器集成（如Apache），以实现对静态资源的优化以及集群部署，当前支持AJP/1.3 |
| HTTP/2   | HTTP 2.0大幅度的提升了web性能。下一代HTTP协议，自8.5和9.0版本后支持。 |

应用分层：

![image-20210817222906538](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817222906538.png)

Tomcat为了实现支持多种I/O模型和应用协议，一个容易可能对接多个连接器，就好比一个房间由多个们，但是单独的连接器或容器都不能对外提供服务，需要把他们组装起来才嗯那个工作，组装后的这个整体叫做Service组件。

#### 1.1.3 连接器组件

![image-20210817223222876](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817223222876.png)

**EndPoint**

1. EndPoint：Coyote通信端点，即通信监听的接口，使具体Scoket接收和发送处理器，是对传输层的抽象，因此EndPoint用来实现TCP/IP协议的。
2. Tomcat并没有EndPoint接口，而是提供了一个抽象类AbstractEndpoint，里面定义了两个内部类：Acceptor和SockerProcessor。Acceptor用于监听Scoket连接请求。SockerProcessor用于处理接收到的Scoket请求，它实现Runnalbe接口，在Run方法里面调用通信协议处理组件Processor进行处理。为了提高处理能力，SockerProcessor被提交到线程池来执行。而这个线程池叫做执行器（Executor）。

**Processor**

Processor：Coyote协议处理接口，如果说EndPoint是用来实现TCP/IP协议的，那么Processor用来实现HTTP协议，Processor接收来自EndPoint的Scoket，读取字节流解析成Tomcat  Request和Response对象，并通过Adapter将其交给容器处理，Processor是对应用层协议的抽象。

**Adapter**:

![image-20210817224443275](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817224443275.png)

### 1.2 容器 - Catalina

Tomcat是一个由一系列可配置的组件构成的web容器，而Catalina是Tomcat的Servlet容器

Catalina是Servlet容器实现，包括之前所有的容器组件。

#### 1.2.1 Catalina地位

![image-20210817224813861](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817224813861.png)

#### 1.2.2 Catalina结构

![image-20210817225055523](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817225055523.png)

Catalina负责管理server，而server表示着整个服务器。server下面由多个服务service，每个服务都包含着多个连接器组件connector（coyote实现）和一个容器组件container。在tomcat启动的时候，会初始化一个catalina的实例。

Catalina各个组件的职责：

![image-20210817225430577](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817225430577.png)

#### 1.2.3 Container结构

tomcat设计了4中容器，分别是Engine，Host，Context，Wrapper。这4中容器不是平行关系，而是父子管理。tomcat通过一种分层的架构，使得servlet容器具有很好的灵活性。

![image-20210817225844574](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817225844574.png)

各个组件的含义：

![image-20210817225932967](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817225932967.png)

![image-20210817231058250](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817231058250.png)

![image-20210817231212925](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817231212925.png)

### 1.3 Tomcat启动流程

<img src="F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817231401335.png" alt="image-20210817231401335" style="zoom:200%;" />

![image-20210817232055812](F:\java-work\我的坚果云\Java部分\后端笔记\JUC.assets\image-20210817232055812.png)

