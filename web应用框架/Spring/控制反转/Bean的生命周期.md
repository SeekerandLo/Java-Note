## Bean 的生命周期

### ApplicationContext 中 Bean 的生命周期

### 总结

- 在调用 getBean 时开始，对 Bean 进行实例化，大致分为以下几大步

```java
sharedInstance = getSingleton(beanName, () -> {
    try {
        return createBean(beanName, mbd, args);
    }
    catch (BeansException ex) {
        // Explicitly remove instance from singleton cache: It might have been put there
        // eagerly by the creation process, to allow for circular reference resolution.
        // Also remove any beans that received a temporary reference to the bean.
        destroySingleton(beanName);
        throw ex;
    }
});
```

- getSingleton 在这个方法中完成了 **创建 Bean**，**填充属性**，**实例化**，**对Bean的完善**，**注册销毁方法等**，这是所有完成的，getSingle 的参数是 beanName 和一个函数接口，即 ObjectFactory<?> singletonFactory，下面的记录是创建此参数的过程，直接创建了一个 Object 类

- createBean **创建Bean**

    - doCreateBean **创建Bean**

        - BeanWrapper instanceWrapper = null **创建 Bean 包装类**  
            instanceWrapper = createBeanInstance(beanName, mbd, args)

            - instantiateUsingFactoryMethod(beanName, mbd, args) **执行配置类中的方法。实例化Bean**

                - bw.setBeanInstance(instantiate(beanName, mbd, factoryBean, uniqueCandidate, EMPTY_ARGS)); **创建实例，及将实例设置进入包装类，此时已经创建实例**

                    - Object result = factoryMethod.invoke(factoryBean, args); **✨创建实例的真正方法**

        - populateBean(beanName, mbd, instanceWrapper) **填充 Bean**

            - PropertyValues pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null); **获取 PropertyValues**

            - **执行 InstantiationAwareBeanPostProcessor 的 postProcessAfterInstantiation**

            - **执行 InstantiationAwareBeanPostProcessor 的 postProcessPropertyValues**

            - applyPropertyValues(beanName, mbd, bw, pvs) **应用 PropertyValues**

        - exposedObject = initializeBean(beanName, exposedObject, mbd) **初始化**

            - invokeAwareMethods(beanName, bean) **调用 Aware 接口子类的方法，如 BeanFactoryAware，执行其 setBeanFactory 方法**

            - wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName) **调用 BeanPostProcessors 的 postProcessBeforeInitialization 方法**

            - invokeInitMethods(beanName, wrappedBean, mbd) **调用指定的 init 方法**

            - wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName) **调用 BeanPostProcessors 的 postProcessAfterInitialization 方法**

        - registerDisposableBeanIfNecessary(beanName, bean, mbd) **注册销毁的方法**

- 最后创建了一个单实例的 Bean


### 较详细的记录

- **前提**：器启动，有对容器的一些准备

- 加载容器后，调用 getBean 方法获取一个 Bean 实例，
    - FactoryBean 和普通 Bean 的不一样，FactoryBean 有后续处理
- 以普通 Bean 为例，调用 getBean 后的流程

    - getSingleton 获取单实例 Bean，

        - createBean，开始创建一个 Bean

        - instanceWrapper = createBeanInstance(beanName, mbd, args);

        - instantiateUsingFactoryMethod()，创建一个 BeanWrapperImpl，这是个 Bean 的包装器，进入之后有获取这个普通 Bean 的 factoryBeanName，显示的是它外面的配置类，我这里是 BeanConfig 类，然后从容器中获取到了这个之前创建 **配置类**

        - 获取配置类后有获取这个配置类中定义的方法，在这里寻找实例化 Bean 的方法，
        ```java
        Method[] rawCandidates = getCandidateMethods(factoryClass, mbd);
        ```
        获取到的方法就是配置类中的方法们，其中有自己定义的获取 Bean 的方法。对这个数据进行遍历，寻找目标方法，最后获取到一个。

        - 获取到这个唯一的方法后，开始准备实例化，
        ```java
        bw.setBeanInstance(instantiate(beanName, mbd, factoryBean, uniqueCandidate, EMPTY_ARGS));
        ```

        - instantiate，这个方法里面是创建对象的过程，使用了反射，factoryBean 就是 BeanConfig 类，factoryMethod 是我定义的 getPerson 方法。参数这里是没有的
        ```java
        Object result = factoryMethod.invoke(factoryBean, args);
        ```
        继续调用来到了动态代理的一部分，enhancedConfigInstance **是 BeanConfig 的动态代理类**，
        ```java
        cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
        ```
        invokeSuper 调用了我配置类中返回 Bean 的方法 `getPerson(){return new Person()}；`
        ```java
        public Object invokeSuper(Object obj, Object[] args) throws Throwable {
            try {
                init();
                FastClassInfo fci = fastClassInfo;
                return fci.f2.invoke(fci.i2, obj, args);
            }
            catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
	    }
        ```
        调用完上述后，就创建了一个 Person 对象，此时回到了
        ```java
        Object result = factoryMethod.invoke(factoryBean, args);
        ```
        - 然后回到了，创建 Bean 包装器的时候，将创建的对象设置到包装器身上，
        ```java
        bw.setBeanInstance(instantiate(beanName, mbd, factoryBean, uniqueCandidate, EMPTY_ARGS));
        // ---
        public void setBeanInstance(Object object) {
            this.wrappedObject = object; // Person@3367
            this.rootObject = object; // Person@3367
            this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
            setIntrospectionClass(object.getClass());
	    }
        ```

        - 回到了 `instantiateUsingFactoryMethod`，再返回到了 `doCreateBean` 方法，然后获取到创建的对象
        ```java
        final Object bean = instanceWrapper.getWrappedInstance();
        ```

        - 一些处理 `MergedBeanDefinitionPostProcessor`

        - **填充Bean：populateBean()**，调用此方法对 Bean 进行处理，使用了 `InstantiationAwareBeanPostProcessor` 的 `postProcessAfterInstantiation` 方法，`InstantiationAwareBeanPostProcessor`是一个接口，它的实现类是对 Bean 的实例进行操作，可是这里全是 `return true;`..

        - 获取属性值，`这里有关于 AutoWired 的操作`，先不看，继续执行。
        ```java
        PropertyValues pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null);
        if (mbd.getResolvedAutowireMode() == AUTOWIRE_BY_NAME || mbd.getResolvedAutowireMode() == AUTOWIRE_BY_TYPE) {
            MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
			// Add property values based on autowire by name if applicable.
			if (mbd.getResolvedAutowireMode() == AUTOWIRE_BY_NAME) {
				autowireByName(beanName, mbd, bw, newPvs);
			}
			// Add property values based on autowire by type if applicable.
			if (mbd.getResolvedAutowireMode() == AUTOWIRE_BY_TYPE) {
				autowireByType(beanName, mbd, bw, newPvs);
			}
			pvs = newPvs;
        }
        ```

        - 又有 `InstantiationAwareBeanPostProcessor` 的操作，这次调用的是 `postProcessProperties` 的操作。最后调用下面的方法，pvs 就是属性列表，这个方法可以细分。
        ```java
        applyPropertyValues(beanName, mbd, bw, pvs);
        ```
        
        - **实例化Bean：exposedObject = initializeBean(beanName, exposedObject, mbd);**，调用
        ```java
        invokeAwareMethods(beanName, bean);
        ```
        对几种 Aware 接口进行操作，有以下操作，**不过这次的 Bean 实例化没有**。
        ```java
        private void invokeAwareMethods(final String beanName, final Object bean) {
            if (bean instanceof Aware) {
                if (bean instanceof BeanNameAware) {
                    ((BeanNameAware) bean).setBeanName(beanName);
                }
                if (bean instanceof BeanClassLoaderAware) {
                    ClassLoader bcl = getBeanClassLoader();
                    if (bcl != null) {
                        ((BeanClassLoaderAware) bean).setBeanClassLoader(bcl);
                    }
                }
                if (bean instanceof BeanFactoryAware) {
                    ((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
                }
            }
        }
        ```

        - 在实例化的过程中还有使用后置处理器对 Bean 的操作，多种 `BeanPostProcessors` 都会调用  
        
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);

        ```java
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        // ---
        public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException {

            Object result = existingBean;
            for (BeanPostProcessor processor : getBeanPostProcessors()) {
                Object current = processor.postProcessBeforeInitialization(result, beanName);
                if (current == null) {
                    return result;
                }
                result = current;
            }
            return result;
        }
        ```
        - 调用初始化方法，获取 Bean 定义中定义的 init 方法，这里没有定义
        ```java
        invokeInitMethods(beanName, wrappedBean, mbd);
        ```

        - wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        实例化后置操作，最后执行完毕，**返回实例化后的 Bean**

        - **注册销毁方法： registerDisposableBeanIfNecessary(beanName, bean, mbd);**

        - 此时 beanInstance 创建完毕，返回到
        ```java
        Object beanInstance = doCreateBean(beanName, mbdToUse, args);
        ```
        开始后续工作

        - 然后就是返回了，getSingleton 执行完毕
    
    - bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);  
    最后 return bean     

    - **完毕**

### BeanFactory 中 Bean 的生命周期

- 

- 

- 



