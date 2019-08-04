## ThreadLoal


&emsp;&emsp;ThreadLocal类用于创建一个线程本地变量。在Thread中有一个成员变量ThreadLocals，该变量的类型是ThreadLocalMap,也就是一个Map，它的键是threadLocal，值就是变量的副本，ThreadLocal为每一个使用该变量的线程都提供了一个变量值的副本，每一个线程都可以独立地改变自己的副本，是线程隔离的。通过ThreadLocal的get()方法可以获取该线程变量的本地副本，在get方法之前要先set,否则就要重写initialValue()方法。
    ThreadLocal不是用来解决对象共享访问问题的，而主要是提供了保持对象的方法和避免参数传递的方便的对象访问方式。一般情况下，通过ThreadLocal.set() 到线程中的对象是该线程自己使用的对象，其他线程是不需要访问的，也访问不到的。各个线程中访问的是不同的对象。