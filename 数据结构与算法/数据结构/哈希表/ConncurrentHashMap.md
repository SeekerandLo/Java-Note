## ConcurrentHashMap

- 只锁定当前链表或红黑树的首节点，这样只要 hash 不冲突，就不会出现并发问题