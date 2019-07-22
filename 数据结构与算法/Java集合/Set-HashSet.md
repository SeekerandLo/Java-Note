## HashSet

- HashSet 内部使用的是 HashMap 的方法

- HashSet 将存入 HashSet 中的值，当作键，存放到一个 HashMap 中

### 源码
```java
public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {
    
    private transient HashMap<E,Object> map;
    
    private static final Object PRESENT = new Object();

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
}
```
