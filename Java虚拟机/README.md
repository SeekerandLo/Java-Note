## JVM

### JDK、JRE、JVM的关系
- JDK > JRE > JVM

- JDK: Java Development Kit

- JRE：Java Runtime Environment

- JVM：Java Virtual Machine

### JVM 监控工具

- 

### 内存

- 线程共享区

    - 方法区

    - Java 堆

- 线程独占区

    - 虚拟机栈

    - 本地方法栈

    - [程序计数器](#程序计数器)

- 堆内存
    - Eden

    - Survivor


### 程序计数器

- 程序计数器是一块较小的内存空间，可以看作当前线程所执行的字节码的行号指示器

- 程序计数器处于线程独占区

- 如果线程执行的是 Java 方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址，如果正在执行的是 native 方法，这个计数器的值为 undefined

- 此区域是唯一一个在 Java 虚拟机规范中没有规定任何 OutOfMemoryError 情况的区域



## 虚拟机命令

- 
