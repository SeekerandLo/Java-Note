## Java 基础面试

### String、StringBuilder、StringBuffer 的区别

- String 是字符串，Java 的基础类

- StringBuffer 解决大量字符串拼接时产生很多中间对象问题而提供的类，提供 append 和 add 方法，可以将字符串添加到已有字符串的末尾或指定位置，本质是一个线程安全的可修改字符序列，它所有修改字符串的方法都加上了 synchronized。但是保证了线程安全需要牺牲性能

- StringBuiler 和 StringBuffer 功能差不多，就是去掉了线程安全那部分，减少了开销
