

#### Nosql概述

##### 为什么要用Nosql

> 1、单机MYSQL的年代

* 数据量如果太大，一个机器放不下了
* 数据的索引（B + Tree）,一个机器内存也放不下
* 访问量（读写混合），一个服务器承受不了

![image-20210602111138714](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210602111138714.png)



出现以上三种情况之一，就必须要晋级

> 2、Memacshed(缓存) + MYSQL + 垂直拆分（读写分离）

希望减轻服务器的压力、使用缓存来保证效率

发展过程：优化数据结构和索引 ---> 文件缓存（IO） --> Memcacahed(当时最热门的技术)

> 3、分库分表+水平拆分+MYSQL集群

> 4、如今时代

* MYSQL等关系型数据库就不够用了！数据量很多 
* MYSQL有的使用它来存储一些较大的文件，博客，图片！数据表很大，效率就低了！如果有一种数据库来专门处理这种数据，MySQL的压力就小了

> 5.目前的互联网项目

![image-20210602110847515](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210602110847515.png)

> 为什么用NoSQL

用户的个人信息、社交网络、地理位置。用户自己产生的数据、用户日志等等爆发式增长。

这时候我们就需要使用NoSQL数据库，NoSQL可以很好的处理以上的情况。

##### 什么是NoSQL

> NoSQL

NoSQL =  Not Only SQL(不仅仅是SQL)

关系型数据库：表格、行、列。

泛指非关系型数据库，随着web2.0互联网的诞生，传统的关系型数据库很难对付web2.0时代。尤其是超大规模的高并发社区。

很多的数据类型用户的个人信息，社交网络，地理位置。这些数据类型的存储不需要一个固定的格式！

不需要多余的操作就可以横向扩展的！Map<String,Object>使用键值对来控制！

> NoSQL特点

* 方便扩展（数据之间没有关系，很好扩展）
* 大数据量高性能（Redis一秒写8万次，读取11万一秒，NoSQL的缓存记录级，是一种细粒度的缓存，性能会比较高）
* 数据类型是多样型的（不需要事先设计数据库。随去随用。如果数据库十分大的表，很多人就无法设计了）
* 传动的关系型数据库（RDBMS）和NoSQL

> 传统的RDBMS
>
> - 结构化祖师
> - SQL
> - 数据和关系都存在单独的表中
> - 数据操作语言，数据定义语言
> - 严格的一致性
> - 基础的事务

> NoSQL
>
> * 不仅仅是数据
> * 没有固定的查询语言
> * 键值对存储，列存储，文档存储，图形数据库（社交关系）
> * 最终一致性
> * CAP定理和BASE(异地多活)
> * 高新能、高可用、高可扩展性

##### 阿里巴巴演进分析

![image-20210602120705404](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210602120705404.png)

##### NoSQL的四大分类

**KV键值对**

* 新浪：Redis
* 美团：Redis + Tair
* 阿里，百度：Redis + memcache

**文档型数据库（bson格式）**

* MongoDB(一般必须要掌握)
  * MongoDB是一个基于分布式文件存储的数据库，C++编写，主要用来处理大量的文档
  * MongoDB是一个介于关系型数据库和非关系型数据库中间的产品！MongoDB是非关心性数据库中功能最丰富，最像关系型数据库的！



**列存储数据库**

* HBase
* 分布式文件系统

**图关系数据库**

* 不是存图形的，放的是关系。如：朋友圈社交网络，广告推荐

**对比**

![image-20210602121947362](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210602121947362.png)





#### Redis入门

##### 概述

> Redis是什么？

Redis(Remote Dictionary Server),远程字典服务

> Redis能做什么？

* 内存存储、持久化，内存中是断电丢失、所以说持久化很重要（rdb,aof）
* 小路高，可以用于高速缓存
* 发布订阅系统
* 地图信息分析
* 计时器、计数器（浏览量）

> 特性

* 多样的数据类型
* 持久化
* 集群
* 事物

> 学习中需要用到的东西

* 官网：https://redis.io/
* 中文网：https://www.redis.cn/
* 下载地址：

#####   windows安装

 *  1.下载安装包 

![image-20210602144746430](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210602144746430.png)



#####   linux安装

![image-20210603190644081](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603190644081.png)

![image-20210603195130190](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603195130190.png)

![image-20210603195512417](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603195512417.png)

![image-20210603195553950](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603195553950.png)

5.redis的默认安装路径 `/usr`下面

![image-20210603195823324](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603195823324.png)

6.将redis配置文件复制到当前目录下

![image-20210603200133459](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603200133459.png)



7.redis默认不知后台启动的，需要修改配置文件`vim redis.conf` 

![](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603200452315.png)

8.启动redis服务

![image-20210603200622845](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603200622845.png)

9.使用redis客户端连接

![image-20210603200835121](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603200835121.png)

10.查看redis进程是否开启

![image-20210603200959309](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603200959309.png)

11.关闭redis服务

![image-20210603201102943](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210603201102943.png)

##### 测试性能

**redis-benchmark**是一个压力测试工具！

官方自带的性能测试工具

redis-benchmark命令参数

![image-20210604140602063](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210604140602063.png)

```bash
# 测试：100各并发连接 10000请求
redis-benchmark -h localhost -p 6379 -c 100 -n 10000
```

##### 基础知识

redis默认有16个数据库

![image-20210604141851021](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210604141851021.png)

默认使用的第0个数据库，可以使用select进行切换数据库

```bash
127.0.0.1:6379> select 3	#切换数据库
OK
127.0.0.1:6379[3]> dbsize	#查看数据库大小
(integer) 0
```

```bash
127.0.0.1:6379[3]> keys *	#查看所有key
1) "name"
127.0.0.1:6379[3]> flushdb	#清空数据库
OK
127.0.0.1:6379[3]> dbsize
(integer) 0
127.0.0.1:6379[3]> 

```

```bash
127.0.0.1:6379[3]> flushall  #清空全部数据库
```

> Redis 是单线程

Redis是基于内存操作，CUP不是Redis的性能瓶颈，Redis的瓶颈是根据机器的内存和网络带宽，既然可以使用单线程来实现，就是用单线程了。

**Redis为什么是单线程的还这么快**

* 误区1：高性能的服务器一定是多线程的？
* 误区2：多线程（CUP上下文会切换！）一定比单线程效率高？

核心：redis是将所有的数据都放在内存中的，所以说使用单线程去操作效率就是最高的，多线程（CPU上下文会切换：耗时的操作！！！！），对于内存系统来说，如果没有上下文切换效率就是最高的！多次读写都是在一个CPU上的，在内存情况下，这个就是最佳的方案！

#### Redis五大数据类型

##### Redis-Key

```bash
127.0.0.1:6379> set name chuchu
OK
127.0.0.1:6379> set age 18
OK
127.0.0.1:6379> keys *
1) "mylist"
2) "myhash"
3) "name"
4) "counter:__rand_int__"
5) "age"
6) "key:__rand_int__"
127.0.0.1:6379> exists name
(integer) 1
127.0.0.1:6379> exists age
(integer) 1
127.0.0.1:6379> del name
(integer) 1
127.0.0.1:6379> get name
(nil)
127.0.0.1:6379> keys *
1) "mylist"
2) "myhash"
3) "counter:__rand_int__"
4) "age"
5) "key:__rand_int__"
127.0.0.1:6379> expire name 60	#设置name的过期时间？
(integer) 0
127.0.0.1:6379> ttl anme
(integer) -2
127.0.0.1:6379> type name	#查看key的类型
string
127.0.0.1:6379> type ange	#查看key的类型
none
```

##### String（字符串）

```bash
127.0.0.1:6379> set key1 v1
OK
127.0.0.1:6379> get key1
"v1"
127.0.0.1:6379> exists key1
(integer) 1
127.0.0.1:6379> append key1 hello	#append命令，向字符串尾部追加字符串。如果key不存在，就会新建这个key
(integer) 7
127.0.0.1:6379> get key1
"v1hello"
127.0.0.1:6379> strlen key1		#获取字符串长度
(integer) 7
127.0.0.1:6379> append key1 ",world"
(integer) 13
127.0.0.1:6379> get key1
"v1hello,world"
###################################################################################################
127.0.0.1:6379> set views 0		#初始浏览量为0
OK
127.0.0.1:6379> get views
"0"
127.0.0.1:6379> incr views		# 每次加1
(integer) 1
127.0.0.1:6379> incr views
(integer) 2
127.0.0.1:6379> get views
"2"
127.0.0.1:6379> decr views		#每次减1
(integer) 1
127.0.0.1:6379> decr views
(integer) 0
127.0.0.1:6379> get views
"0"
127.0.0.1:6379> incrby views 10	#加10
(integer) 10
127.0.0.1:6379> decrby views 5	#减5
(integer) 5
127.0.0.1:6379> get
(error) ERR wrong number of arguments for 'get' command
127.0.0.1:6379> get views
"5"
###################################################################################################
#字符串范围 range
127.0.0.1:6379> set key1 "hello,world"
OK
127.0.0.1:6379> get key1
"hello,world"
127.0.0.1:6379> getrange key1 0 3		#截取字符串[0,3]
"hell"
127.0.0.1:6379> getrange key1 0 -1		#获取全部字符串
"hello,world"

#替换
127.0.0.1:6379> set key2 abcdefg
OK
127.0.0.1:6379> get key2
"abcdefg"
127.0.0.1:6379> setrange key2 1 xx	#替换 1 索引位置的bc 为 xx
(integer) 7
127.0.0.1:6379> get key2
"axxdefg"
###################################################################################################
#setex (set with expire)	#设置过期时间
#setnx (set if not exist)	#不存在设置（在分布式锁中会常常使用）
127.0.0.1:6379> setex key3 30 "hello"		#设置key3的值为"hello"，30秒后过期
OK
127.0.0.1:6379> ttl key3
(integer) 26
127.0.0.1:6379> get key3
"hello"
127.0.0.1:6379> setnx mykey "redis"		#mykey不存在时设置
(integer) 1
127.0.0.1:6379> keys *
1) "key2"
2) "mykey"
3) "key1"
127.0.0.1:6379> ttl key3
(integer) -2
127.0.0.1:6379> setnx key3 "MongDB"
(integer) 1
127.0.0.1:6379> SETNX mykey "mongdb"		#mykey存在时设置失败
(integer) 0
127.0.0.1:6379> get mykey
"redis"
###################################################################################################
#mset #批量设置
#mget #批量获取
127.0.0.1:6379> mset k1 v1 k2 v2 k3 v3
OK
127.0.0.1:6379> keys *
1) "k3"
2) "k2"
3) "k1"
127.0.0.1:6379> mget k1 k2 k3
1) "v1"
2) "v2"
3) "v3"
127.0.0.1:6379> msetnx k1 v1 k5 v5		#msetnx 批量不存在时设置，要么一起成功，要么一起失败
(integer) 0
###################################################################################################
# 对象
set user:1 {name:zhangsan,age:3} #设置一个user:1对象  值为json字符串来保存一个对象
#这里的key时巧妙的设计：  user:{id}:{filed},如此设计
127.0.0.1:6379> mset user:1:name zhangsan user:1:age 20
OK
127.0.0.1:6379> mget user:1:name user:1:age
1) "zhangsan"
2) "20"
###################################################################################################
getset #先get后set

127.0.0.1:6379> getset db redis		#如果不存在值，返回null,如果存在值返回db
(nil)
127.0.0.1:6379> get db
"redis"
127.0.0.1:6379> getset db mongodb	#如果存在值，获取原来的值，并设置新的值
"redis"

```

String类似的使用场景：value除了时字符串还可以时数组

* 计数器
* 统计多单位的数量

---

##### List (列表)

基本的数据类型，列表

在redis中可以将list用成**栈、队列、阻塞队列**

所有的list命令都是**L**开头的

```bash
127.0.0.1:6379> lpush list one		#将一个值或多个值插入列表的头部（左）
(integer) 1
127.0.0.1:6379> lpush list two
(integer) 2
127.0.0.1:6379> lpush list three
(integer) 3
127.0.0.1:6379> lrange list 0 -1		#获取list中的值
1) "three"
2) "two"
3) "one"
127.0.0.1:6379> rpush list four		#将一个值或多个值插入列表的尾部（右）
(integer) 4
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "two"
3) "one"
4) "four"
########################################################################################
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "two"
3) "one"
4) "four"
127.0.0.1:6379> lpop list 		#移除左边的值
"three"
127.0.0.1:6379> lrange list 0 -1
1) "two"
2) "one"
3) "four"
127.0.0.1:6379> rpop list		#移除右边的值
"four"
127.0.0.1:6379> lrange list 0 -1
1) "two"
2) "one"
########################################################################################
lindex #获取索引对应的值
127.0.0.1:6379> lindex list 1		#通过下标获取某一个值
"one"
127.0.0.1:6379> lindex list 0
"two"
########################################################################################
llen #获取list长度
127.0.0.1:6379> llen list		#返回列表的长度
(integer) 2
########################################################################################
移除指定的值
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "two"
3) "one"
127.0.0.1:6379> lrem list 1 one
(integer) 1
127.0.0.1:6379> lrange list 0 -1
1) "three"
2) "two"
127.0.0.1:6379> lpush list three
(integer) 3
127.0.0.1:6379> lrem list 2 three	#移除list中指定个数的value,精确匹配
(integer) 2
127.0.0.1:6379> lrange list 0 -1
1) "two"
########################################################################################
trim 修剪：list 截断
127.0.0.1:6379> rpush mylist "hello"
(integer) 1
127.0.0.1:6379> rpush mylist "hello1"
(integer) 2
127.0.0.1:6379> rpush mylist "hello2" "hello3"
(integer) 4
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "hello1"
3) "hello2"
4) "hello3"
127.0.0.1:6379> ltrim mylist 1 2		#通过下标截取指定的长度，这个list已经被改变了，只剩下截取的元素了
OK
127.0.0.1:6379> lrange mylist 0 -1
1) "hello1"
2) "hello2"
########################################################################################
rpoplpush		#移除列表最后一个元素，并且将它添加到另一个元素并返回
127.0.0.1:6379> rpush mylist "hello" "hello1" "hello2" "hello3"
(integer) 4
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "hello1"
3) "hello2"
4) "hello3"
127.0.0.1:6379> rpoplpush mylist myotherlist		#移除mylist最后一个元素，并且将它添加到myotherlist中并返回
"hello3"
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "hello1"
3) "hello2"
127.0.0.1:6379> lrange myotherlist 0 -1
1) "hello3"
########################################################################################
lset  #将列表指定索引处设置元素值（替换）
127.0.0.1:6379> lpush list value1
(integer) 1
127.0.0.1:6379> lrange list 0 0
1) "value1"
127.0.0.1:6379> lset list 0 item
OK
127.0.0.1:6379> lrange list 0 0
1) "item"
########################################################################################
linsert #在某个元素的前面或者后面插入一个值
127.0.0.1:6379> rpush mylist "hello" "world"
(integer) 2
127.0.0.1:6379> linsert mylist before "world" "other"
(integer) 3
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "other"
3) "world"
127.0.0.1:6379> linsert mylist after "world" "new"
(integer) 4
127.0.0.1:6379> lrange mylist 0 -1
1) "hello"
2) "other"
3) "world"
4) "new"

```

> 小结

* 他实际是一个链表，before node after,left  ,right都可以插入值
* 如果key不存在，创建新的链表
* 如果key存在，新增内容
* 如果移除了所有值，空链表，也代表不存在
* 在两边插入或者改动值，效率最高。中间元素，相对来说效率会低一点。

消息排队！消息队列 (lpush rpop )，栈（lpush,lpop）.

---

##### Set(集合)

set中的值不能重复

```bash
127.0.0.1:6379> sadd myset "hello"
(integer) 1
127.0.0.1:6379> sadd myset "world" "chuchu"
(integer) 2
127.0.0.1:6379> smembers myset
1) "chuchu"
2) "world"
3) "hello"
127.0.0.1:6379> sismember myset hello		#判断某个值是不是在set中
(integer) 1
################################################################
127.0.0.1:6379> scard myset #获取set中元素的个数
(integer) 3
################################################################
127.0.0.1:6379> srem myset "hello"		#移除某个元素
(integer) 1
127.0.0.1:6379> smembers myset	
1) "chuchu"
2) "world"
################################################################
set 无序不重复集合
127.0.0.1:6379> smembers myset
1) "chuchu"
2) "world"
3) "chuchu2"
127.0.0.1:6379> srandmember myset		#随机抽选出一个元素
"chuchu2"
127.0.0.1:6379> srandmember myset 2		#随机抽选出两个元素
"chuchu2" "chuchu"
127.0.0.1:6379> srandmember myset
"chuchu"
127.0.0.1:6379> srandmember myset
"chuchu"
127.0.0.1:6379> srandmember myset
"world"
127.0.0.1:6379> srandmember myset
"world"
127.0.0.1:6379> srandmember myset
"chuchu2"
127.0.0.1:6379> srandmember myset
"chuchu"
127.0.0.1:6379> srandmember myset
"world"
127.0.0.1:6379> srandmember myset
"chuchu"
127.0.0.1:6379> srandmember myset
"chuchu2"
################################################################
删除指定的key,随机删除key
127.0.0.1:6379> smembers myset
1) "chuchu"
2) "world"
3) "chuchu2"
127.0.0.1:6379> spop myset		#随机删除集合中的一些元素
"chuchu2"
127.0.0.1:6379> smembers myset
1) "chuchu"
2) "world"
################################################################
将一个指定的值，移动到另一个集合中
127.0.0.1:6379> sadd myset "hello" "world" "nihao"
(integer) 3
127.0.0.1:6379> smove myset myset2 hello	#将myset中的hello移动到myset2中
(integer) 1
127.0.0.1:6379> smembers myset
1) "world"
2) "nihao"
127.0.0.1:6379> smembers myset2
1) "hello"
################################################################
微博，b站，共同关注（交集）
127.0.0.1:6379> sadd key1 a b c
(integer) 3
127.0.0.1:6379> sadd key2 c d 
(integer) 2
127.0.0.1:6379> sdiff key1 key2		#查看不同的
1) "a"
2) "b"
127.0.0.1:6379> sinter key1 key2		#查看相同的（交集）
1) "c"
127.0.0.1:6379> sunion key1 key2		#并集
1) "d"
2) "a"
3) "b"
4) "c"
################################################################

```

微博，A用户将所有关注的人放在一个set集合中。将它的粉丝也放在一个集合中。

##### Hash(哈希)

---

Map集合，key-map!这时候这个值是一个map集合！本质与String类型没有太大区别，还是一个简单的key-value.

```bash
127.0.0.1:6379> hset myhash field1 chuchu		#往map里添加元素
(integer) 1
127.0.0.1:6379> hget myhash field1				#获取元素
"chuchu"
127.0.0.1:6379> hmset myhash field1 hello field2 world	#一次添加多个
OK
127.0.0.1:6379> hmget myhash field1 field2		#一次获取多个
1) "hello"
2) "world"
127.0.0.1:6379> hgetall myhash		#获取全部
1) "field1"
2) "hello"
3) "field2"
4) "world"
################################################################
127.0.0.1:6379> hdel myhash field2		#删除指定的值
(integer) 1
127.0.0.1:6379> hgetall myhash 
1) "field1"
2) "hello"
################################################################
获取长度
hlen
127.0.0.1:6379> hmset myhash field3 hello field4 redis
OK
127.0.0.1:6379> hlen myhash		#获取字段数量
(integer) 3
127.0.0.1:6379> hgetall myhash
1) "field1"
2) "hello"
3) "field3"
4) "hello"
5) "field4"
6) "redis"
127.0.0.1:6379> hget myhash field3
"hello"
################################################################
判断某个值是否存在
127.0.0.1:6379> hexists myhash field	#判断field是否存在
(integer) 0
127.0.0.1:6379> hexists myhash field1
(integer) 1
################################################################
#只获得所有的字段
#只获得所有的值
127.0.0.1:6379> hkeys myhash		#获取所有的字段key
1) "field1"
2) "field3"
3) "field4"
127.0.0.1:6379> hvals myhash		#获取所有的值value
1) "hello"
2) "hello"
3) "redis"
################################################################
#incr decr
127.0.0.1:6379> hset myhash field 6
(integer) 1
127.0.0.1:6379> hincrby myhash field 2		#增加2
(integer) 8
127.0.0.1:6379> hsetnx myhash field4 9		#不存在时设置自动为9，如果已经存在就不成功
(integer) 0
127.0.0.1:6379> hsetnx myhash field7 2		#不存在时设置自动为9，如果不存在就成功
(integer) 1
```

---

##### Zset(有序集合)

在set的基础上，增加了一个值，set k1 v1 => zset k1 score v1

```bash
127.0.0.1:6379> zadd myset 1 one
(integer) 1
127.0.0.1:6379> zadd myset 2 two 3 three
(integer) 2
127.0.0.1:6379> zrange myset 0 -1
1) "one"
2) "two"
3) "three"
################################################################
127.0.0.1:6379> zadd salary 2500 zhangsan 5000 wangsi 500 liwu
(integer) 3
127.0.0.1:6379> zrangebyscore salary -inf +inf	#从最小值到最大值排序
1) "liwu"
2) "zhangsan"
3) "wangsi"
127.0.0.1:6379> zrevrange salary 0 -1		#从最大值到最小值排序
1) "wangsi"
2) "zhangsan"
127.0.0.1:6379> zrangebyscore salary -inf +inf withscores	#加上key
1) "liwu"
2) "500"
3) "zhangsan"
4) "2500"
5) "wangsi"
6) "5000"
################################################################
移除rem中的元素
127.0.0.1:6379> zrange salary 0 -1
1) "liwu"
2) "zhangsan"
3) "wangsi"
127.0.0.1:6379> zrem salary liwu		#移除指定集合中的元素
(integer) 1
127.0.0.1:6379> zrange salary 0 -1
1) "zhangsan"
2) "wangsi"
127.0.0.1:6379> zcard salary		#获取集合中的个数
(integer) 2
127.0.0.1:6379> zadd myset 1 hello 2 world 3 chuchu 4 dedi		
(integer) 4
127.0.0.1:6379> zcount myset 1 3			#获取指定区间内的个数
(integer) 3

################################################################

```

---

#### 三种特殊数据类型

##### geospatial地理位置

---

> geoadd

```bash
#geoadd 添加地理位置
127.0.0.1:6379> geoadd china:city 116.40 39.90 beijing 	#添加城市数据
(integer) 1
127.0.0.1:6379> geoadd china:city 121.47 31.23 shanghai 106.50 29.53 chongqing 114.05 22.52 shenzhen
(integer) 3
127.0.0.1:6379> geoadd china:city 120.16 30.24 hangzhou
(integer) 1
127.0.0.1:6379> geoadd china:city 108.96 34.26 xian
(integer) 1
```

> geopos

```bash
127.0.0.1:6379> geopos china:city beijing chongqing		#获取指定城市的经纬度
1) 1) "116.39999896287918091"
   2) "39.90000009167092543"
2) 1) "106.49999767541885376"
   2) "29.52999957900659211"
```

> geodist

```bash
127.0.0.1:6379> geodist china:city chongqing xian	#获取重庆 西安之间的距离
"575046.9885"
127.0.0.1:6379> geodist china:city chongqing xian km	#获取重庆 西安之间的距离 km
"575.0470"
```

> 我附近的人？（获得所有附近的人的地址，定位）通过半径来查询
>
> georadius:以给定的经纬度为中心，查询某一半径内的元素

```bash
127.0.0.1:6379> georadius china:city 110 30 1000 km			#以经纬度110 30 为中心寻找方圆1000km的元素
1) "chongqing"
2) "xian"
3) "shenzhen"
4) "hangzhou"
127.0.0.1:6379> georadius china:city 110 30 500 km
1) "chongqing"
2) "xian"
127.0.0.1:6379> georadius china:city 110 30 500 km withcoord		#把经纬度一块查出来
1) 1) "chongqing"
   2) 1) "106.49999767541885376"
      2) "29.52999957900659211"
2) 1) "xian"
   2) 1) "108.96000176668167114"
      2) "34.25999964418929977"
127.0.0.1:6379> georadius china:city 110 30 500 km withcoord count 1		#限制查询多少个
1) 1) "chongqing"
   2) 1) "106.49999767541885376"
      2) "29.52999957900659211"

```

> georadiusbymember :以给定的元素为中心，寻找

```bash
127.0.0.1:6379> georadiusbymember china:city shanghai 300 km	#找出以上海为中心，300km内的城市
1) "hangzhou"
2) "shanghai"
```

> geo 底层的实验原理其实就是zset，我们可以使用zset命令来操作geo!

```bash
127.0.0.1:6379> zrange china:city 0 -1		#查看地图中全部的元素
1) "chongqing"
2) "xian"
3) "shenzhen"
4) "hangzhou"
5) "shanghai"
6) "beijing"
127.0.0.1:6379> zrem china:city beijing		#移除特定的元素
(integer) 1
127.0.0.1:6379> zrange china:city 0 -1	
1) "chongqing"
2) "xian"
3) "shenzhen"
4) "hangzhou"
5) "shanghai"
```

---

##### Hyperloglog

> 什么是基数？

A{1,3,5,7,8,7}

B{1,3,5,7,8}

基数（不重复的元素） = 5，可以接受误差。

```bash
127.0.0.1:6379> pfadd myset a b c d e f g h i j		#创建第一组元素myset
(integer) 1	
127.0.0.1:6379> pfcount myset						#统计myset元素的基数数量
(integer) 10
127.0.0.1:6379> pfadd myset2 i j k z x c v b n m		#创建第二组元素myset2
(integer) 1
127.0.0.1:6379> pfcount myset2
(integer) 10
127.0.0.1:6379> pfmerge mykey3 myset myset2			#合并两个元素 并集
OK
127.0.0.1:6379> pfcount mykey3
(integer) 16
```

如果允许容错，可以使用hyperloglog;如果不允许容错，使用set

---

##### Bitmaps

> 位存储

统计疫情感染人数：0 1 0 1

统计用户信息，活跃，不活跃！登录，未登录！两个状态的都可以使用bitmaps

Bitmaps位图，数据结构！都是操作二进制位来进行记录，就只有0和1两个状态！

365天= 365 bit    1字节= 8 bit  46个字节左右

使用bitmap记录周一到周日的打卡！

周一：1  周二 ：0  周三 0  周四：1 .........

![image-20210604222909632](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210604222909632.png)



查看某一个是否有打卡：

```bash
127.0.0.1:6379> getbit sign 3
(integer) 1
127.0.0.1:6379> getbit sign 6
(integer) 0
```

统计操作，统计打卡的天数

```bash
127.0.0.1:6379> bitcount sign 0 3  #查看0-3天的打卡记录（可以忽略最后两个参数）
(integer) 3
```



#### 事务

要么同时成功，要么同时失败，原子性！

**redis单条命令是保证原子性的，但redis的事务是不保证原子性的！**

redis事务本质：一组命令的集合！一个事务中的所有米杠零都会被序列化，在事务执行国策和观念中，会按照顺序执行！

一次性、顺序性、排他性！

**redis没有隔离级别的概念**

所有的命令在食物中，并没有直接被执行！只有发起执行名命令的之后才会执行！Exec

redis的事务：

* 开启事务（）
* 命令入队（）
* 执行事务（）

> 正常执行事务：

```bash
127.0.0.1:6379> multi			#开启事务
OK
127.0.0.1:6379(TX)> set k1 v1	#命令入队
QUEUED
127.0.0.1:6379(TX)> set k2 v2
QUEUED
127.0.0.1:6379(TX)> get k2
QUEUED
127.0.0.1:6379(TX)> set k3 v3
QUEUED
127.0.0.1:6379(TX)> exec		#执行事务
1) OK
2) OK
3) "v2"
4) OK

```

> 放弃事务

```bash
127.0.0.1:6379> multi		#开启事务
OK
127.0.0.1:6379(TX)> set k1 v1
QUEUED
127.0.0.1:6379(TX)> set k2 v2
QUEUED
127.0.0.1:6379(TX)> set k4 v4
QUEUED
127.0.0.1:6379(TX)> discard		#放弃事务
OK
127.0.0.1:6379> get k4		#事务队列中的命令都不会被执行！
(nil)
```

> 编译型异常（代码有问题！命令有错！），事务中所有的命令都不会被执行!

```bash
127.0.0.1:6379> multi
OK
127.0.0.1:6379(TX)> set k1 v1
QUEUED
127.0.0.1:6379(TX)> set k2 v2
QUEUED
127.0.0.1:6379(TX)> set k3 v3
QUEUED	
127.0.0.1:6379(TX)> getset k3			#错误的命令
(error) ERR wrong number of arguments for 'getset' command
127.0.0.1:6379(TX)> set k4 v4
QUEUED
127.0.0.1:6379(TX)> set k5 v5
QUEUED
127.0.0.1:6379(TX)> exec		#执行事务报错
(error) EXECABORT Transaction discarded because of previous errors.
								#所有的命令都没有执行
```



> 运行时异常（1/0)，如果事务队列中存在语法性，那么执行命令的时候，其他命令是可以正常执行的，错误命令抛出异常。

```bash
127.0.0.1:6379> set k1 "v1"
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379(TX)> incr k1		#执行的时候会失败
QUEUED
127.0.0.1:6379(TX)> set k2 v2
QUEUED
127.0.0.1:6379(TX)> set k3 v3
QUEUED
127.0.0.1:6379(TX)> get k3
QUEUED
127.0.0.1:6379(TX)> exec
1) (error) ERR value is not an integer or out of range		#虽然第一条命令报错了，但是依旧执行成功了
2) OK
3) OK
4) "v3"
127.0.0.1:6379> get k2
"v2"
```

> 监控！Watch

**悲观锁：**

* 很悲观，什么时候都会出问题，无论什么时候都会加锁

**乐观锁**

* 很乐观，认为什么时候都不会出问题，所以不会上锁！更新数据的时候去判断一下，在此期间是否有人修改过这个数据，version！
* 获取version
* 更新的时候比较version

>  Redis的监视测试

正常执行成功！

```bash
127.0.0.1:6379> set money 100
OK
127.0.0.1:6379> set out 0
OK
127.0.0.1:6379> watch money		#监视money对象
OK
127.0.0.1:6379> multi			#事务正常结束，数据期间没有发生变动，这个时候就正常执行成功
OK
127.0.0.1:6379(TX)> decrby money 20
QUEUED
127.0.0.1:6379(TX)> incrby out 20
QUEUED
127.0.0.1:6379(TX)> exec
1) (integer) 80
2) (integer) 20
```

测试多线程修改值，使用watch可以当作redis的乐观锁操作。

```bash
127.0.0.1:6379(TX)> exec
1) (integer) 80
2) (integer) 20
127.0.0.1:6379> watch money			#监视 money
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379(TX)> decrby money 10
QUEUED
127.0.0.1:6379(TX)> incrby out 10
QUEUED
127.0.0.1:6379(TX)> exec			#执行之前 ，另一个线程，修改了money,这个时候，就会导致事务执行失败。
(nil)
```

---

#### Jedis

我们要使用java来操作redis

> jedis是redis官方推荐的Java连接开发工具！使用Java操作redis的中间件

1、导入对应的依赖

```xml
    <!--  导入jedis包  -->
    <!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>3.6.0</version>
        </dependency>
    <!--   fastjson     -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.76</version>
        </dependency>
```

2、编码测试：

* 连接数据库
* 操作命令
* 断开连接

---

#### Springboot整合

Springboot操作数据：spring-data  jpa  jdbc  mongodb  redis

springdata也是和springboot齐名的项目

说明：在springboot2.x后，原来使用的jedis被替换为了lettuce

jedis:采用直连，多个线程操作的话，是不安全的，如果想要避免，使用jedis pool连接池。更像BIO模式

lettuce:采用netty，实例可以在多个线程中共享，不存在不安全的情况，减少线程数量。更像NIO模式。

> 整合测试

---

#### Redis.conf详解

---

#### Redis持久化

redis是内存数据库，如果不保存到磁盘那么一旦服务器进行退出，服务器中的数据库状态也会消失，所以redis提供了持久化的功能！

##### rdb（redis database)

> 什么是rdb
>
> 在rdb中是做备份的。

![image-20210605220814251](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605220814251.png)

在指定的时间间隔内将内存中的数据集体快照写入磁盘，也就是行话说的Snapshot快照，他恢复时是将快照文件直接读到内存里

Redis会单独创建（fork）一子进程来进行持久化，会先将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件。整个过程中，主进程是不进行任何IO操作的。这就确保了极高的性能。如果需要进行大规模的数据回复，且对于数据恢复的完整性不是非常铭感，那么RDB方式要比AOF方式更加的搞笑。RDB的缺点是最后一个持久化后的数据可能丢失。我们默认的就是RDB，一般情况下不需要修改这个配置。

**rdb保存的文件就是dump.rdb**，都是再配置文件的快照中配置的。

![image-20210605215959106](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605215959106.png)

![image-20210605220113199](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605220113199.png)

> 触发机制

1.save的规则满足的情况下，会自动触发rdb规则

2.执行flushall命令，也会触发rdb规则

3.退出redis,也会产生rdb文件

备份就自动生成一个dump.rdb文件

> 如何恢复rdb文件

1.将rdb文件放在redis启动目录下，redis启动的时候就会自动检测dump.rdb文件，恢复其中的数据！

![image-20210605220725190](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605220725190.png)

优点：

1.适合大规模的数据恢复

2.对数据的完整性不高！

缺点：

1.需要一定的时间间隔进行操作。如果redis意外宕机了，这个最后一个修改的数据就没有了

2.frok进程的时候，会占用一定的内存。



##### AOF（Append Only File）

将我们的所有命令都记录下来，history,恢复的时候就把这个文件都执行一遍。

> 是什么

![image-20210605221159230](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605221159230.png)





以日志的形式来记录每个写操作，将Redis执行过的所有执行记录下来（读操作不记录），只许追加文件但不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，redis重启的话根据日志文件的内容将写质量从前到后执行一次完成数据的恢复工作。

**AOF保存的是appendonly.aof文件**

> append

![image-20210605221453938](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605221453938.png)

默认是不开启的，首要手动进行配置。只需要将图片中的no改成yes即可。

重启，redis就可以生效了

如果这个aof文件有错误，这时候，redis是启动不起来的，我们需要修复这个aof文件。

redis给提供了一个工具**redis-check-aof --fix**

![image-20210605221951633](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605221951633.png)

如果文件正常，重启就可以直接恢复

![image-20210605222035669](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605222035669.png)

> 重写的说明

aof默认就是文件的无限追加，文件会越来越大

![image-20210605222459699](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605222459699.png)

如果文件大于64mb,就会fork一个新进程，来存储。

> 优点和缺点

优点：

![image-20210605222146491](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605222146491.png)

* 每一次修改都同步，文件的完成行会更好。
* 每秒同步一次，可能会丢失一秒的数据
* 从不同步，效率最高。

缺点

* 相对于数据文件来说，aof远远大于rdb,修复的速度比rdb慢
* aof运行效率要比rdb慢，所以redis默认是rdb持久化

![image-20210605222653878](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605222653878.png)

![image-20210605222710992](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605222710992.png)

---

#### Redis发布订阅

Redis发布订阅（pub/sub）是一种消息通信模式：发送者（pub）发送消息，订阅者(sub)接收消息。微信、微博、关注系统

Redis客户端可以订阅任意数量的频道。

订阅/发布消息图：

第一个：消息发送者，第二个：i难道  第三个：消息订阅者

![image-20210605222937453](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605222937453.png)

下图展示了频道channel1,以及订阅这个批到的三个客户端    cliend2  client5  client1之间的关系：

![image-20210605223110336](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605223110336.png)

当有新消息通过PUBLISH命令发送给频道channel1是，这个消息就会被发送给订阅他的三个客户端：

![image-20210605223151668](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605223151668.png)



> 命令

![image-20210605223221923](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605223221923.png)

> 测试

![image-20210605223513325](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605223513325.png)

![image-20210605223555029](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605223555029.png)

使用场景：

* 实时消息系统
* 实时聊天（频道当作聊天室，将信息回显给所有人）
* 订阅，关注系统

稍微复杂的场景就会使用消息中间件做。

---

#### Redis主从复制

![image-20210605224004576](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605224004576.png)

![image-20210605224158394](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605224158394.png)





![image-20210605223945161](C:\Users\user\Desktop\image-20210605223945161.png)

##### 环境配置

```bash
[root@wang bin]# cd /usr/local/bin/
[root@wang bin]# redis-server myconfig/redis.conf 
[root@wang bin]# redis-cli -p 6379
127.0.0.1:6379> info replication			#查看当前库的信息
# Replication
role:master				#角色  master
connected_slaves:0		#没有从机
master_failover_state:no-failover
master_replid:2854ce507ff1f9aff323997061d3b23625d6630f
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```

复制三个配置文件，然后修改对应的信息：

* 端口
* pid名字
* log文件名字
* dump.rdb名字

修改完毕之后启动3个服务，可以通过进程查看。

![image-20210605225646549](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210605225646549.png)



##### 一主二从

**默认情况下，每台Redis服务器都是主节点；**一般情况下配置从机就好

认老大！一主（79）二从（80，81）

```bash
127.0.0.1:6380> slaveof 127.0.0.1 6379		#配置为从机
OK
127.0.0.1:6380> info replication
# Replication
role:slave			#当前角色
master_host:127.0.0.1		#主机的信息
master_port:6379
master_link_status:up
master_last_io_seconds_ago:2
master_sync_in_progress:0
slave_repl_offset:0
slave_priority:100
slave_read_only:1
replica_announced:1
connected_slaves:0
master_failover_state:no-failover
master_replid:1c57c3653aeea15421b2823d167bafeb33328320
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:0
#主机信息
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:2
slave0:ip=127.0.0.1,port=6380,state=online,offset=224,lag=0
slave1:ip=127.0.0.1,port=6381,state=online,offset=224,lag=0
master_failover_state:no-failover
master_replid:1c57c3653aeea15421b2823d167bafeb33328320
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:224
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:224
```

真实的配置应该在配置文件中进行配置。

> 细节

主机可以写，从机布恩那个写只能读。主机的所有数据，都会被从机自动保存。

![image-20210606123842636](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606123842636.png)



![image-20210606124414757](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606124414757.png)



#### 哨兵模式（自动选举老大的模式）

---

![image-20210606124522477](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606124522477.png)



![image-20210606124552896](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606124552896.png)

![image-20210606124640481](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606124640481.png)

![image-20210606124712184](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606124712184.png)

![image-20210606125014448](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606125014448.png)

![image-20210606125528056](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606125528056.png)





![image-20210606125421955](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606125421955.png)



![image-20210606125552003](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606125552003.png)

![image-20210606125658591](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606125658591.png)

![image-20210606125811111](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606125811111.png)



---

#### Redis缓存穿透和雪崩

##### 缓存穿透（查不到）

![image-20210606130057642](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606130057642.png)

![image-20210606130334389](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606130334389.png)

![](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606130406045.png)

##### 缓存击穿（量太大，缓存过期）

![image-20210606130504240](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606130504240.png)

![image-20210606130645085](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606130645085.png)

##### 缓存雪崩

![image-20210606130812505](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606130812505.png)



![image-20210606131018633](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210606131018633.png)



