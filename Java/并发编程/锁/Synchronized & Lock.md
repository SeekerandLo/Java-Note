## synchronized 和 Lock 的区别


### 用法的区别

- synchronized 是 Java 虚拟机提供的锁，可以加在方法上，代码块上

- Lock 是 Java 中的一个类，使用在代码中，需要指定起始位置和结束位置

### 性能的区别

- synchronized 是一个悲观锁，性能较低，适用于逻辑简单的场景

- Lock 是一个乐观锁，性能高，适用于逻辑复杂的场景
