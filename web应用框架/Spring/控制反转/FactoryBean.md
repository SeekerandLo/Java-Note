## FactoryBean

&emsp;&emsp;是一个接口，为了方便创建 Bean 提供的一种方式，有三个方法
```java
public interface FactoryBean<T> {

	@Nullable
	T getObject() throws Exception;


	@Nullable
	Class<?> getObjectType();


	default boolean isSingleton() {
		return true;
	}
}
```
&emsp;&emsp;可以重写 getObject 返回一个想创建的 Bean，这是在加载过程