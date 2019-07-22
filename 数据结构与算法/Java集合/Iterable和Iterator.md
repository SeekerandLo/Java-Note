## Iterable

- 实现这个接口允许一个对象成为“for each loop”语句的目标。

- 集合类都实现了这个接口，所以他们可以使用 `for each` 语法

- 只有一个方法，Iterator<T> iterator(); 获取到集合的迭代器

## Iterator

- Iterator 就是迭代器，可以使用它来遍历集合

- 具有方法 hasNext，next，remove

## ListIterator

- 继承了 Iterator，扩展了它的功能，不过只是对于 List 的集合来说，可以双向遍历

### Iterator 和 ListIterator 的区别

- ListIterator 继承了 Iterator，扩展了它的功能，可以双向遍历，还可以使用 `set`、`add` 方法给集合修改和添加元素，add 为在当前元素之后添加元素，set 将当前元素修改为其他值