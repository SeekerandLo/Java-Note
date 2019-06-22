## PriorityQueue

### 介绍
- 优先队列

### 添加操作-add
- [offer](#offer)

### offer
- [siftUp](#siftUp)

### siftUp(int k, E x)
- [siftUpUsingComparator](#siftUpUsingComparator)
- [siftUpComparable(int k, E x) 默认排序，小顶堆](#siftUpComparable)

### siftUpComparable
- 
```java
// k 是当前的待插入元素的 index，x 是待插入的元素
private void siftUpComparable(int k, E x) {
    // 强转 x 为可比较的 key
    Comparable<? super E> key = (Comparable<? super E>) x;
    // 包括调整堆的操作
    while (k > 0) {
        // 计算当前元素插入时的 父级位置
        int parent = (k - 1) >>> 1;
        // 获取父级位置元素
        Object e = queue[parent];
        // 判断当前待插入元素与父元素的大小，如果比父级大则退出，直接插入元素
        if (key.compareTo((E) e) >= 0)
            break;
        // 将父级元素插入到待插入位置
        queue[k] = e;
        // 把 k 移到待插入元素位置的父位置，继续迭代，判断是否还需要调整堆的结构
        k = parent;
    }
    // 最后找到 k 的位置，插入元素
    queue[k] = key;
}
```