## HashMap 

- HashMap 采用数组和链表的数据结构，

## 源码分析

### 内部存储结构

- **Node<K,V>**: 实现了 **Map.Entry<K,V>**

- **EntrySet**：是 **Map.Entry<K,V>** 的集合

### 初始化过程

- 默认的加载因子是 **0.75f**

###  get

- 通过 key 的 hash 值在 Node 数组中寻找目标的位置，先寻找所在位置的第一个，如果第一个元素的 key 和 key 想等，且节点的 hash == key 的 hash，则第一个节点就是要找的节点，如果不是就迭代遍历，直到寻找到那个节点
```java
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```


### Hash碰撞


### 红黑树