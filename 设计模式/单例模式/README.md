## 单例模式

### 线程安全的饿汉模式
```java
public class Single {

    private static volatile Single instance;


    private Single() {

    }

    public static Single getInstance() {
        if (instance == null) {
            synchronized (Single.class) {
                if (instance == null) {
                    instance = new Single();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        Single single = Single.getInstance();
        Single single2 = Single.getInstance();
        System.out.println(single);
        System.out.println(single2);
    }
}
```
### 为什么使用 volatile 修饰变量

&emsp;&emsp;使用者在调用 getInstance 时，可能获得初始化未完成的对象，**这和虚拟机的编译优化有关**，对于 Java 编译器而言，初始化 instance 和将对象地址写到 instance 字段并非原子操作。
&emsp;&emsp;假设某个线程执行 new Single 时，构造方法还未被调用，编译器仅仅为该对象**分配了内存空间并设置为默认值(类的加载阶段)**，此时，若另一线程调用 getInstance 方法，由于 instance != null，就会返回一个错误的 instance 实例