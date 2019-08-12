## 流操作

流一般包括：一个数据源，中间操作链，终端操作

### 中间操作
操作 | 类型 | 返回类型 | 操作参数 | 函数描述符
--|--|--|--|--
filter|中间|Stream|Predicate|T -> boolean
map|中间|Stream|Function<T,R>|T->R
limit|中间|Stream		
sorted|中间|Stream|Comparator|(T,T)->int
distinct|中间|Stream		

### 终端操作
操作 | 类型 | 目的
-- | -- | --
forEach | 终端 | 消费流中的每个元素并对其应用Lambda.这一操作返回void
count | 终端 | 返回流中元素的个数,这一操作返回long
collect | 终端 | 把流归约成一个集合,比如List,Map甚至是Integer