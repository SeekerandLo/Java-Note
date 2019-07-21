## ConcurrentHashMap

- ConcurrentHashMap 是为了应对 HashMap 在并发环境下不安全而诞生的

- ~~ConcurrentHashMap里包含一个Segment数组，Segment的结构和HashMap类似，是一种数组和链表结构， 一个Segment里包含一个HashEntry数组，每个HashEntry是一个链表结构的元素， 每个Segment守护者一个HashEntry数组里的元素,当对HashEntry数组的数据进行修改时，必须首先获得它对应的Segment锁~~


### 源码分析（JDK1.7 和 JDK1.8 是不一样的）

#### 1.7版本(还未看过)

- 分段锁，使用 Segment，将数据分成一段一段，然后给每一段分一把锁，每一段又是一个数组，数组元素又是链表

- put 时需要两次 hash，一次分到一个 Segment，再一次分到一个元素中

- 缺点：hash 时间长

- 优点：写操作时只对元素所在的 Segment 进行加锁，不会影响到其他 Segment。在最理想的情况下，ConcurrentHashMap可以最高同时支持Segment数量大小的写操作（刚好这些写操作都非常平均地分布在所有的Segment上）

#### 1.8版本(以下的都在1.8版本的基础上分析)

### Unsafe 类

### 使用什么控制线程安全

- synchronized 和 Unsafe

- volatile

### 什么时候需要加锁