## 1.HashMap与ConcurrentHashMap

**HashMap与ConcurrentHashMap的实现原理是怎样的？ConcurrentHashMap如何保证线程安全？**

* HashMap线程是不安全的，现象
  * 多线程的put可能导致元素的丢失
  * put和get并发时，可能导致get为null
* 多线程条件环境下要使用线程安全的HashMap
  * Collections.synchronizedMap:全局锁，性能有问题
  * HashTable:全局锁，性能问题
  * ConcurrentHashMap:分段锁技术

**ConcurrentHashMap**

分段锁：锁的时候不锁整个hash表，而是只锁一部分  

ConcurrentHashMap中维护了Segment数组，每个Segment可以看作一个HashMap,他继承了ReentrantLock,就是一个锁。

Segment通过HashEntry数组维护其内部的hash表，每个HashEntry就代表了map中的一个K-V,用HashEntry可以组成一个链表结构。

```java
public class ConcurrentHashMap<K, V> extends AbstractMap<K, V>
        implements ConcurrentMap<K, V>, Serializable {

    // ... 省略 ...
    /**
     * The segments, each of which is a specialized hash table.
     */
    final Segment<K,V>[] segments;

    // ... 省略 ...

    /**
     * Segment是ConcurrentHashMap的静态内部类
     * 
     * Segments are specialized versions of hash tables.  This
     * subclasses from ReentrantLock opportunistically, just to
     * simplify some locking and avoid separate construction.
     */
    static final class Segment<K,V> extends ReentrantLock implements Serializable {
    	// ... 省略 ...
    	/**
         * The per-segment table. Elements are accessed via
         * entryAt/setEntryAt providing volatile semantics.
         */
        transient volatile HashEntry<K,V>[] table;
        // ... 省略 ...
    }
    // ... 省略 ...

    /**
     * ConcurrentHashMap list entry. Note that this is never exported
     * out as a user-visible Map.Entry.
     */
    static final class HashEntry<K,V> {
        final int hash;
        final K key;
        volatile V value;
        volatile HashEntry<K,V> next;
        // ... 省略 ...
    }
}
```

![image-20210620101756408](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620101756408.png)

由ConcurrentHashMap的结构图，只要hash值足够分散，每次put的时候就会put到不同的segment中取，而segment本身就是一个锁，put的时候，当前segment会把自己锁住，其他线程无法操作这个segment,但不会影响其他segment的操作。这就是分段锁的好处。

**put方法**:最终会调用segment的put方法，将元素put到HashEntry数组中。

```java
public V put(K key, V value) {
    Segment<K,V> s;
    if (value == null)
        throw new NullPointerException();
    int hash = hash(key);
    int j = (hash >>> segmentShift) & segmentMask;

    // 根据key的hash定位出一个segment，如果指定index的segment还没初始化，则调用ensureSegment方法初始化
    if ((s = (Segment<K,V>)UNSAFE.getObject          // nonvolatile; recheck
         (segments, (j << SSHIFT) + SBASE)) == null) //  in ensureSegment
        s = ensureSegment(j);
    // 调用segment的put方法
    return s.put(key, hash, value, false);
}
```

```java
final V put(K key, int hash, V value, boolean onlyIfAbsent) {
    // 因为segment本身就是一个锁
    // 这里调用tryLock尝试获取锁
    // 如果获取成功，那么其他线程都无法再修改这个segment
    // 如果获取失败，会调用scanAndLockForPut方法根据key和hash尝试找到这个node，如果不存在，则创建一个node并返回，如果存在则返回null
    // 查看scanAndLockForPut源码会发现他在查找的过程中会尝试获取锁，在多核CPU环境下，会尝试64次tryLock()，如果64次还没获取到，会直接调用lock()
    // 也就是说这一步一定会获取到锁
    HashEntry<K,V> node = tryLock() ? null : scanAndLockForPut(key, hash, value);
    V oldValue;
    try {
        HashEntry<K,V>[] tab = table;
        int index = (tab.length - 1) & hash;
        HashEntry<K,V> first = entryAt(tab, index);
        for (HashEntry<K,V> e = first;;) {
            if (e != null) {
                K k;
                if ((k = e.key) == key ||
                    (e.hash == hash && key.equals(k))) {
                    oldValue = e.value;
                    if (!onlyIfAbsent) {
                        e.value = value;
                        ++modCount;
                    }
                    break;
                }
                e = e.next;
            }
            else {
                if (node != null)
                    node.setNext(first);
                else
                    node = new HashEntry<K,V>(hash, key, value, first);
                int c = count + 1;
                if (c > threshold && tab.length < MAXIMUM_CAPACITY)
                    // 扩容
                    rehash(node);
                else
                    setEntryAt(tab, index, node);
                ++modCount;
                count = c;
                oldValue = null;
                break;
            }
        }
    } finally {
        // 释放锁
        unlock();
    }
    return oldValue;
}
```

![image-20210620101905054](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20210620101905054.png)

[未完][https://juejin.cn/post/6844903813892014087]

## 2.volatile关键字

**volatile（易失性）关键字解决了什么问题，实现原理是什么**

