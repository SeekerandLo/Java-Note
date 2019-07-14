## fail-fast 和 fail-save

1. [fail-fast](#fail-fast)

2. [modCount](#modCount)

3. [fail-safe](#fail-safe)
 
## fail-fast
> The iterators returned by all of this class's "collection view methods" are <i> **fail-fast** </i>: if the map is structurally modified at any time after the iterator is created, in any way except through the **iterator's own remove method**, the iterator will throw a **{@link ConcurrentModificationException}**.  Thus, in the face of concurrent modification, the iterator fails quickly and cleanly, rather than risking arbitrary, non-deterministic behavior at an undetermined time in the future.

&emsp;&emsp;当Iterator这个迭代器被创建后，除了迭代器本身的方法(remove)可以改变集合的结构外，其他的因素如若改变了集合的结构，都被抛出 ConcurrentModificationException 异常。

>Note that the fail-fast behavior of an iterator cannot be guaranteed as it is, generally speaking, impossible to make any hard guarantees in the presence of unsynchronized concurrent modification.  Fail-fast iterators throw <tt>ConcurrentModificationException</tt> on a best-effort basis. Therefore, it would be wrong to write a program that depended on this exception for its correctness: <i>**the fail-fast behavior of iterators should be used only to detect bugs.**</i>

&emsp;&emsp;迭代器的 fail-fast 行为不能得到保证，因为一般来说，在存在非同步并发修改的情况下，不可能做出任何硬保证。fail fast迭代器在尽最大努力的基础上抛出ConcurrentModificationException 。因此，编写一个依赖于这个异常的程序来保证其正确性是错误的：迭代器的fail-fast行为应该只用于检测错误。(来自百度翻译)


- fail-fast 机制是 Java 集合的一种错误机制，当多个线程对一个集合的内容进行操作时，会导致 fail-fast 事件。例如：当某一个线程A通过iterator去遍历某集合的过程中，若该集合的内容被其他线程所改变了；那么线程A访问集合时，就会抛出ConcurrentModificationException异常，产生fail-fast事件。(来自百度百科)

### modCount

- 之前没有注意到 modCount 原来在这里派上了大用场，以 HashMap 为例

- HashMap 的迭代器，请欣赏源码。expectedModCount 是创建迭代器时的 modCount，在获取下一个节点时，都会先判断 modCount 和 expectedModCount 是否相等，如果不想等，则有东西修改了当前集合，抛出异常。modCount 在 HashMap 的 **put、remove、merge、compute、computeIfAbsent** 五个方法中修改
    ```java
    // HashMap 的迭代器
    abstract class HashIterator {
        Node<K,V> next;        // next entry to return
        Node<K,V> current;     // current entry
        int expectedModCount;  // for fast-fail
        int index;             // current slot

        HashIterator() {
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // advance to first entry
                do {} while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Node<K,V> nextNode() {
            Node<K,V>[] t;
            Node<K,V> e = next;
            // 判断抛出异常
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            if ((next = (current = e).next) == null && (t = table) != null) {
                do {} while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }

        public final void remove() {
            Node<K,V> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
            expectedModCount = modCount;
        }
    }
    ```
### 如何处理 fail-fast


## fail-safe

&emsp;&emsp;当集合被改变时，复制一份新的集合数据，在新的数据上遍历
