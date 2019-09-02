## Comparable和Comparator

### Comparable

&emsp;&emsp;Comparable 是一个接口，里面的方法只有一个，是 `compareTo`，这个比较方法相当于写在类的内部，当一个类实现了这个接口后，这个类就是可以排序的了，如使用 `Collection.sort()` 排序。

### Comparator

&emsp;&emsp;Comarator 是比较器，使用方法是写一个比较器，然后比较某个类，可以是使用泛型，接口中方法很多，常用的是 `compare(T o1, T o2)`，使用时使用 `sort(List<T> list, Comparator<? super T> c)`。
