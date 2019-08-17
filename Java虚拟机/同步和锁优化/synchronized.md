## synchronized

### 底层实现

- synchronized 锁是通过 monitor 监视锁来实现的，monitor 是每个对象都有的一个属性，当执行同步代码块时，JVM 会分析环境，找到目标对象的 monitor，再根据 monitor 的状态进行加锁,释放操作。 如果能成功加锁，该线程就成了目标对象 monitor 的唯一所有者，其他线程不能再获取，直到当前线程释放

- JVM 将同步代码编译成字节码后，有 monitorenter 指令和**两个** monitorexit 指令，如果使用 monitorenter 指令进入 monitor 时，monitor 为 0，则表示线程可以持有进入，将 monitor 加一，如果 monitor 不为 0，表示其他线程占有该 monitor，当前线程阻塞等待