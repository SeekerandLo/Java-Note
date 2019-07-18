## Java 并发 
`线程`

### 线程的状态

- **NEW**：新建状态

    - [开启线程的方式](#开启线程的方式)

- **RUNNABLE**：就绪状态，调用 start() 之后，运行之前的状态

    - 线程的 start() 不能被多次调用，否则会抛出 IllegalStateException

- **RUNNING**：运行状态，run() 正在执行时线程的状态

    - 线程可能由于某些因素而退出 RUNNING，如时间、异常、锁、调度等 

- **BLOCKED**：阻塞状态

    - 同步阻塞：锁被其他线程占用

    - 主动阻塞：调用 Thread 的某些方法，主动让出 CPU 执行权，比如 sleep()、join() 等

    - 等待阻塞：执行了 wait()

- **DEAD**：终止状态，run() 执行结束，或因异常退出后的状态，**此状态不可逆转**

### 线程状态之间的关系

- 其中Running表示运行状态，Runnable表示就绪状态（万事俱备，只欠CPU），Blocked表示阻塞状态，阻塞状态又有多种情况，可能是因为调用wait()方法进入等待池，也可能是执行同步方法或同步代码块进入等锁池，或者是调用了sleep()方法或join()方法等待休眠或其他线程结束，或是因为发生了I/O中断

### 开启线程的方式

&emsp;&emsp;开启线程有三种方式
1. 继承 Thread 类
2. 实现 Runnable 接口 (**推荐**)
3. 实现 Callable 接口

- **继承 Thread**
    - 继承 Thread 类，重写 run 方法

    - 调用 Thread 子类的 start() 方法

    ```java
    public class TestThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println(getName() + "  " + i);
            }
        }
        public static void main(String[] args) {
            for (int i = 0; i < 100; i++) {
                if (i == 20) {
                    new TestThread().start();
                    new TestThread().start();
                }
            }
        }
    }
    ```   

- **实现 Runnable**

    - 类实现 Runnable 接口，实现其 run 方法，该方法是线程的执行方法

    - 创建 Runnbale 实现类的实例，作为 Thread 的 target 创建 Thread 对象
    ```java
    public Thread(Runnable target, String name) {
        init(null, target, name, 0);
    }
    ```
    - 调用该 Thread 的 start 方法启动线程

    ```java
    public class Test implements Runnable {
        public static void main(String[] args) {
            for (int i = 0; i < 100; i++) {
                if (i == 20) {
                    Test rtt = new Test();
                    new Thread(rtt, "新线程1").start();
                    new Thread(rtt, "新线程2").start();
                }
            }
        }
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
        }
    }
    ```



- **实现 Callable**

    - 实现 Callable 接口，实现 call 方法，该 call 方法是线程的执行体，并且有返回值

    - 创建 Callable 实现类的实例，使用 FutureTask 类来包装 Callable 对象，该 FutureTask 对象封装了该 Callable 对象的 call() 方法的返回值

    - 使用 FutureTask 对象最为 Thread 类的 target 创建线程，启动线程

    - 调用 FutureTask 对象的 get 方法获取线程的返回值
    - 
    ```java
    public class TestCall implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            int sum = 0;
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
            return sum;
        }
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            TestCall testCall = new TestCall();
            FutureTask<Integer> result = new FutureTask<>(testCall);
            new Thread(result).start();
            Integer sum = result.get();
            System.out.println(sum);
        }
    }
    ```

### 线程池

- 在面向对象编程中，创建资源和销毁资源都是很耗费时间的，因为创建一个对象要获取内存资源或更多其他资源。所以为了提交程序运行效率， 就要尽可能减少对象销毁和创建的次数，所以引入“池”的概念，线程池就是先创建若干个可执行的线程放入一个池(容器)中，需要的时候从线程池中获取，而不是自己创建，使用完毕后不需要销毁而是放回池中，从而减少创建和销毁线程对象的开销

- **需要的线程数大于线程池中的线程数量怎么办**

- **线程池类型**

### AQS

- AbstractQueuedSynchronizer 抽象队列同步器

- 
