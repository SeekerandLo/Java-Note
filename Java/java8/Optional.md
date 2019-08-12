## Optional

### 构造方法

- Optional.of(obj)   
它要求传入的 obj 不能是 null 值的, 否则还没开始进入角色就倒在了 NullPointerException 异常上了。

- Optional.ofNullable(obj)  
它以一种智能的, 宽容的方式来构造一个 Optional 实例. 来者不拒, 传 null 进到就得到 Optional.empty(), 非 null 就调用 Optional.of(obj)。

- Optional.empty()