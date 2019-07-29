## 生产者消费者

## wait/notify
```java
// 消费者
public class Customer extends Thread {

    private final LinkedList<Object> list;

    private Integer max;

    Customer(LinkedList<Object> list, Integer max) {
        this.list = list;
        this.max = max;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (list) {
                try {
                    if (list.size() == 0) {
                        System.out.println("缓冲区没了，等待生产");
                        list.wait();
                    }
                    list.poll();
                    System.out.println(Thread.currentThread().getName() + ": 消费者消费一个元素  " + list.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 等待
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 通知
                list.notifyAll();
            }
        }
    }
}
```
```java
// 生产者
public class Producer extends Thread {

    private final LinkedList<Object> list;

    private Integer max;

    Producer(LinkedList<Object> list, Integer max) {
        this.list = list;
        this.max = max;
    }


    @Override
    public void run() {
        while (true) {
            synchronized (list) {
                try {
                    if (list.size() == max) {
                        System.out.println("缓冲区满了,等待消费");
                        list.wait();
                    }
                    list.add(new Object());
                    System.out.println(Thread.currentThread().getName() + ": 生产者生产一个元素   " + list.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 等待
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 通知
                list.notifyAll();
            }
        }
    }
}
```
```java
// 运行
public class Run {
    public static class PersonFactory implements ThreadFactory {
        private AtomicInteger atomicInteger = new AtomicInteger(1);

        private String name;

        PersonFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            int seq = atomicInteger.getAndIncrement();
            String name = this.name + " " + seq + " ";
            System.out.println(name);
            return new Thread(null, r, name, 0);
        }
    }

    public static void main(String[] args) {
        // 消费者线程工厂    
        PersonFactory customFactory = new PersonFactory("消费者");
        // 生产者线程工厂
        PersonFactory produceFactory = new PersonFactory("生产者");

        // 线程队列
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>(1);
        
        // 创建消费者线程池
        ThreadPoolExecutor customThreadPoolExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, blockingQueue, customFactory);

        // 创建生产者线程池
        ThreadPoolExecutor produceThreadPoolExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, blockingQueue, produceFactory);

        // 仓库
        LinkedList<Object> list = new LinkedList<>();

        // 生产者
        Runnable producer = new Producer(list, 10);
        
        // 消费者
        Runnable customer = new Customer(list, 10);

        // go
        customThreadPoolExecutor.execute(customer);
        produceThreadPoolExecutor.execute(producer);
    }
}
```