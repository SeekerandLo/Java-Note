## Object 类

### hashCode

- 如果不重写hashcode()方法的话就会在数组不同下标的链表下存在相同的对象(equals相等) 如在存储散列集合时（如Set类），将会存储了两个值一样的对象，导致混淆，因此，就也需要重写hashcode()

### equals

**SUN官方的文档中规定如果重定义equals方法，就必须重定义hashCode方法,以便用户可以将对象插入到散列(哈希)表中**