## 几个点需要注意 

1. 在 BeanFactory 的创建过程中如何处理几种 BeanPostProcessor?

    1. [applyMergedBeanDefinitionPostProcessors](#applyMergedBeanDefinitionPostProcessors)

    2. []()



2. 容器的概念，BeanFactory，ApplicationContext

3. 有一些类的方法是为子类准备的，哪些是？
    
    1. [initPropertySources](#刷新前的预处理)

    2. [postProcessBeanFactory](#开放子类对BeanFactory的修改)

    3. [onRefresh()](#初始化特定上下文子类中的其他特殊-Bean)


## 将 BeanFactory 的创建过程分为以下几个部分

在此对 refresh() 方法进行分析，梳理 BeanFactory 的创建过程  

**注**：以下方法未写参数

1. **【BeanFactory 的准备工作】**
    - 【[刷新前的预处理](#刷新前的预处理)】prepareRefresh()
        - 【环境变量准备】

    - 【[获取 BeanFactory](#获取-BeanFactory)】obtainFreshBeanFactory()

        - 【刷新 BeanFactory】refreshBeanFactory()
        
        - 【获取 BeanFactory】getBeanFactory()

    - 【[配置 BeanFactory](#配置-BeanFactory)】prepareBeanFactory()

    - 【[为子类提供添加后置处理器逻辑的方法](#开放子类对BeanFactory的修改)】postProcessBeanFactory()

    - 【[将 BeanFactoryPostProcessor 注册到上下文](#将-BeanFactoryPostProcessor-注册到上下文)】invokeBeanFactoryPostProcessors()

2. **【Bean 的准备工作】**

    - 【[注册 BeanPostProcessor](#注册-BeanPostProcessor)】registerBeanPostProcessors()

    - 【[为上下文初始化信息源](#初始化信息源)】initMessageSource()

    - 【[为上下文初始化事件派发器](#初始化事件派发器)】initApplicationEventMulticaster()

3. **【注册 Bean】**
    - 【[初始化特定上下文子类中的其他特殊 Bean](#初始化特定上下文子类中的其他特殊-Bean)】onRefresh()
    
    - 【[检查监听器 Bean 并注册](#注册监听器)】registerListeners()

    - 【[实例化剩下的单实例 Bean](#完成-BeanFactory-的初始化)】finishBeanFactoryInitialization()

    - 【[发布相应事件]()】finishRefresh()

## BeanFactory 的准备工作     

&emsp;&emsp;此阶段是 **创建 BeanFactory**，**配置 BeanFactory**，**实例化** 实现了 **BeanFactoryPostProcessor** 接口的 Bean ，**调用** 各 **BeanFactoryPostProcessor** 的方法 **完善** BeanFactory 的过程

### 刷新前的预处理
&emsp;&emsp;通过调用 prepareRefresh() 方法对容器进行刷新，包括初始化属性源，验证标记为必需的属性是否可解析，存储预刷新应用监听器，收集早期的应用事件，一旦多播可用将发布。

&emsp;&emsp;以下是主要方法：

- **initPropertySources()**

    **此方法留给子类实现，从注释上看是初始化属性源**
    ```java
    // Initialize any placeholder property sources in the context environment.
	initPropertySources();
    ```

- **getEnvironment().validateRequiredProperties()**

    **验证所有标记为“必需”的属性是否可解析**
    ```java
    // Validate that all properties marked as required are resolvable:
    // see ConfigurablePropertyResolver#setRequiredProperties
    getEnvironment().validateRequiredProperties();
    ```

- **this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners)**

    **存储预刷新的应用监听器**
    ```java
    // Store pre-refresh ApplicationListeners...
    if (this.earlyApplicationListeners == null) {
        this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
    }
    else {
        // Reset local application listeners to pre-refresh state.
        this.applicationListeners.clear();
        this.applicationListeners.addAll(this.earlyApplicationListeners);
    }
    ```

- **this.earlyApplicationEvents = new LinkedHashSet<>()**

    **收集早期的应用事件，当派发器可用的时候发布它们**
    ```java
    // Allow for the collection of early ApplicationEvents,
    // to be published once the multicaster is available...
    this.earlyApplicationEvents = new LinkedHashSet<>();
    ```
### 获取 BeanFactory
&emsp;&emsp;obtainFreshBeanFactory() 方法中有两个方法，一个是刷新，一个是获取
- **refreshBeanFactory()**

  **不能重复刷新BeanFactory，为 BeanFactory 设置序列化Id**

    ```java
    @Override
    protected final void refreshBeanFactory() throws IllegalStateException {
        if (!this.refreshed.compareAndSet(false, true)) {
            throw new IllegalStateException(
                    "GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
        }
        this.beanFactory.setSerializationId(getId());
    }
    ```

### 配置 BeanFactory
&emsp;&emsp;配置上下文的标准特征，比如类加载器和部分后置处理器

- **告诉内部 BeanFactory 使用类加载器等**
    ```java
    // Tell the internal bean factory to use the context's class loader etc.
    beanFactory.setBeanClassLoader(getClassLoader());
    beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
    beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
    ```

- **使用上下文回调配置 BeanFactory**
    ```java
    // Configure the bean factory with context callbacks.
    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
    beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
    beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
    beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
    ```

- **BeanFactory接口未在普通工厂中注册为可解析类型**  
  **MessageSource 作为Bean注册到容器中，可以使用@AutoWired使用**    
    ```java
    // BeanFactory interface not registered as resolvable type in a plain factory.
    // MessageSource registered (and found for autowiring) as a bean.
    beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
    beanFactory.registerResolvableDependency(ResourceLoader.class, this);
    beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
    beanFactory.registerResolvableDependency(ApplicationContext.class, this);
    ```
- **将早期的后置处理器注册为 applicationListener，以检测内部的 Bean**
    ```java
    // Register early post-processor for detecting inner beans as ApplicationListeners.
    beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
    ```   

- **如果能找到 LoadTimeWeaver，设置到 BeanPostProcessor**
    [👉与AOP有关]()
    ```java
    // Detect a LoadTimeWeaver and prepare for weaving, if found.
    if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        // Set a temporary ClassLoader for type matching.
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }
    ```

- **注册默认的环境 Bean**
    ```java
    // Register default environment beans.
    if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
    }
    if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
    }
    if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
    }
    ```

### 开放子类对BeanFactory的修改

&emsp;&emsp;为子类提供的修改 BeanFactory 的方法，此方法运行在 应用上下文内部的 BeanFactory 标准初始化之后，所有的 Bean 定义 **将被加载**，但是没有 Bean 被实例化，允许注册一些特殊的 BeanPostProcessor 在当前的上下文实现中

- **postProcessBeanFactory()**
    ```java
    /**
	 * Modify the application context's internal bean factory after its standard
	 * initialization. All bean definitions will have been loaded, but no beans
	 * will have been instantiated yet. This allows for registering special
	 * BeanPostProcessors etc in certain ApplicationContext implementations.
	 * @param beanFactory the bean factory used by the application context
	 */
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
	}
    ```

### 将 BeanFactoryPostProcessor 注册到上下文
&emsp;&emsp;将实现了各种 BeanFactoryPostProcessor 接口或其子类的类注册成 Bean 到上下文中

- **PostProcessorRegistrationDelegate.[invokeBeanFactoryPostProcessors](#invokeBeanFactoryPostProcessors)(beanFactory, getBeanFactoryPostProcessors())**

    有两种 BeanFactoryPostProcessor，一个是 BeanFactoryPostProcessor，另一个是 BeanDefinitionRegistryPostProcessor，BeanDefinitionRegistryPostProcessor 继承自 BeanFactoryPostProcessor

- **如果同时发现 LoadTimeWeaver，准备编织**  

  例如，通过configurationClassPostProcessor注册的@bean方法 
    ```java
    // Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
    // (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
    if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }
    ```
#### invokeBeanFactoryPostProcessors
&emsp;&emsp;此方法对当前容器中的 BeanFactoryProcessor 进行遍历，按照一定的次序执行它们中的方法

- **首先获取实现了 BeanDefinitionRegistryPostProcessor 接口的 postProcessor**  

    **按照 PriorityOrdered -> Ordered -> 其他的 顺序调用它们的 [postProcessBeanDefinitionRegistry]()()**

- **然后获取实现了 BeanFactoryPostProcessor 接口的 postProcessor**

    **按照 PriorityOrdered -> Ordered -> 其他的 顺序调用它们的 [postProcessBeanFactory]()()**    

- **最后清除缓存的合并 Bean 定义，因为后置处理器可能修改了它们**  
    例如替换值中的占位符
    ```java
    // Clear cached merged bean definitions since the post-processors might have
    // modified the original metadata, e.g. replacing placeholders in values...
    beanFactory.clearMetadataCache();
    ``` 

## Bean 的准备工作

&emsp;&emsp;此阶段为 Bean 的实例化作准备，注册 BeanPostProcessor，初始化消息源，初始化应用事件派发器，注册监听器等

### 注册 BeanPostProcessor
&emsp;&emsp;调用 registerBeanPostProcessors(beanFactory) 注册 BeanPostProcessor 拦截 Bean 的创建

- **PostProcessorRegistrationDelegate.[registerBeanPostProcessors](#registerBeanPostProcessors)(beanFactory, this)👇**  
    
    以下的全部是 [registerBeanPostProcessors()](#registerBeanPostProcessors) 中的内容
    
#### registerBeanPostProcessors 
&emsp;&emsp;此方法将容器中的 BeanPostProcessor 按照一定的次序获取并执行其中的方法

- **首先获取容器中的 BeanPostProcessor**

- **注册 BeanPostProcessorChecker，当在 BeanPostProcessor 实例化期间创建 Bean 时记录日志信息。例如：当一个 Bean 不能被所有 BeanPostProcessor 处理时**  
    [上述话有问题☝]()

- **按照 PriorityOrdered -> Ordered -> 其他的 顺序注册它们到容器中**   

    此时只是注册，还没有调用其中的方法

- **将检测内部 Bean 的后置处理器重新注册为应用监听器，将这个处理器移到处理器链的最后**
    
    用于提取代理等

### 初始化信息源

&emsp;&emsp;为上下文初始化信息源，有实现国际化等功能

- **首先判断容器中是否存在 id 为 messageSource 的 Bean**

- **如果不存在则创建一个 DelegatingMessageSource**

### 初始化事件派发器

&emsp;&emsp;初始化事件派发器，如果在上下文中没有定义，则使用 SimpleApplicationEventMulticaster

- **获取 BeanFactory**

- **判断容器中是否存在 id 为 applicationEventMulticaster 的 Bean**
    - [ ] 完善
- **如果不存在则创建一个 SimpleApplicationEventMulticaster**

### 初始化特定上下文子类中的其他特殊 Bean
&emsp;&emsp;提供一个模板方法，可添加上下文特定的刷新工作，在实例化之前调用特殊 Bean 初始化

- **onRefresh()**

### 注册监听器
&emsp;&emsp;调用 registerListeners()，检查监听器，并注册它们。添加实现了 ApplicationListener 的监听器 Bean，不影响其他的不使用 Bean 添加的监听器，

- **首先注册静态指定的监听器**

    ```java
    // Register statically specified listeners first.
    for (ApplicationListener<?> listener : getApplicationListeners()) {
        getApplicationEventMulticaster().addApplicationListener(listener);
    }
    ```

- **不再这里初始化 FactoryBean，我们需要保留常规 Bean，不再这里初始化，以允许 BeanPostProcessor 应用于它们**
    ```java
    // Do not initialize FactoryBeans here: We need to leave all regular beans
    // uninitialized to let post-processors apply to them!
    String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
    for (String listenerBeanName : listenerBeanNames) {
        getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
    }
    ```
- **发布早期的应用事件，现在我们终于有了一个派发器**  
    ```java
    // Publish early application events now that we finally have a multicaster...
    Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
    this.earlyApplicationEvents = null;
    if (earlyEventsToProcess != null) {
        for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
            getApplicationEventMulticaster().multicastEvent(earlyEvent);
        }
    }
    ```  

### 完成 BeanFactory 的初始化
&emsp;&emsp;调用 finishBeanFactoryInitialization(beanFactory) 实例化剩下的不是懒加载的单实例 Bean

- **为上下文初始化 ConversionService**
    ```java
    // Initialize conversion service for this context.
    if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
            beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
        beanFactory.setConversionService(
                beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
    }
    ```

- **如果之前没有注册 Bean 的后置处理器，则注册一个默认的值解析器**
    **(例如 PropertyPlaceholderConfigurer)：**
    **此时主要用于注释中值的解析**
    ```java
    // Register a default embedded value resolver if no bean post-processor
    // (such as a PropertyPlaceholderConfigurer bean) registered any before:
    // at this point, primarily for resolution in annotation attribute values.
    if (!beanFactory.hasEmbeddedValueResolver()) {
        beanFactory.addEmbeddedValueResolver(strVal -> getEnvironment().resolvePlaceholders(strVal));
    }   
    ```

- **尽早初始化 Loadtimeweaveraware Bean 以允许尽早注册其转换器**
    ```java
    // Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
    String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
    for (String weaverAwareName : weaverAwareNames) {
        getBean(weaverAwareName);
    }
    ```

- **停止使用暂时的类加载器进行类型匹配**
    ```java
    // Stop using the temporary ClassLoader for type matching.
    beanFactory.setTempClassLoader(null);
    ```

- **允许缓存所有的 Bean 定义元数据，不需进一步更改**
    ```java
    // Allow for caching all bean definition metadata, not expecting further changes.
    beanFactory.freezeConfiguration();
    ```

- **beanFactory.[preInstantiateSingletons](#preInstantiateSingletons)()**

    **实例化所有的不是懒加载的单实例的 Bean**

#### preInstantiateSingletons
&emsp;&emsp;确保所有的不是懒加载的单实例的 Bean 被实例化，也考虑 **[FactoryBean]()**，如果需要，通常在 BeanFactory 设置结束后调用此方法，如果有单实例 Bean 不能被创建则抛出 BeansException。  
```java
/**
 * Ensure that all non-lazy-init singletons are instantiated, also considering
 * {@link org.springframework.beans.factory.FactoryBean FactoryBeans}.
 * Typically invoked at the end of factory setup, if desired.
 * @throws BeansException if one of the singleton beans could not be created.
 * Note: This may have left the factory with some beans already initialized!
 * Call {@link #destroySingletons()} for full cleanup in this case.
 * @see #destroySingletons()
 */
void preInstantiateSingletons() throws BeansException;
```

- **迭代一个副本以允许 init 方法，该方法反过来注册成新的 Bean 定义**  
    **虽然这可能不是一个常规工厂引导的一部分，但它在其他方法是正常的**
    ```java
    // Iterate over a copy to allow for init methods which in turn register new bean definitions.
    // While this may not be part of the regular factory bootstrap, it does otherwise work fine.
    List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
    ```

- **触发所有不是懒加载的单实例 Bean 的初始化**

    **遍历 beanNames，开始初始化 Bean**

    - 判断是否是 FactoryBean，FactoryBean 与普通 Bean 的初始化稍有不同

    - [ ] FactoryBean 的创建

    - **[getBean(beanName)](#getBean)** 普通 Bean 的创建 

    **遍历 beanNames，获取单实例 Bean**

- **[返回上级方法](#完成-BeanFactory-的初始化)**

#### getBean
&emsp;&emsp;调用的 **[doGetBean](#doGetBean)** 方法
```java
@Override
public Object getBean(String name) throws BeansException {
    return doGetBean(name, null, null, false);
}
```
- **[返回上级方法](#preInstantiateSingletons)**

#### doGetBean
&emsp;&emsp;返回可以共享的或独立的一个实例，或者一个特殊的 Bean
- **Object sharedInstance = [getSingleton](#getSingleton)(beanName)**  
    
    **期望在缓存中获取单实例 Bean**
    ```java
    // Eagerly check singleton cache for manually registered singletons.
	Object sharedInstance = getSingleton(beanName);
    ```

- **如果能获得到 sharedInstance**

    **bean = [getObjectForBeanInstance](#getObjectForBeanInstance)(sharedInstance, name, beanName, null)**

    - [ ] 其他操作

    **【返回 Bean】**  

- **如果不能获得到 sharedInstance**

    - **如果已经创建了 Bean 实例，循环引用是不可想象的**
        ```java
        // Fail if we're already creating this bean instance:
        // We're assumably within a circular reference.
        if (isPrototypeCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        }
        ```
    - **[markBeanAsCreated(beanName)]()**

        **将 Bean 标记为已经创建了**

    - **获取 Bean 的依赖，如果有依赖先获取依赖 Bean**

    - **sharedInstance = getSingleton(beanName, () -> {**  
      &emsp;&emsp;**try {**  
      &emsp;&emsp;&emsp;&emsp;**return [createBean](#createBean)(beanName, mbd, args);**  
      &emsp;&emsp;**}**  
      &emsp;&emsp;**catch (BeansException ex) {**  
      &emsp;&emsp;&emsp;&emsp;**destroySingleton(beanName);**    
      &emsp;&emsp;&emsp;&emsp;**throw ex;**   
      &emsp;&emsp;**}**       
      **});**  
      **bean = [getObjectForBeanInstance](#getObjectForBeanInstance)(sharedInstance, name,   beanName, mbd)**
    - 

- ****



#### getSingleton
&emsp;&emsp;返回以给定的名称注册的单实例 Bean。检查 **已经实例化** 的单实例 Bean，也允许当前创建的单实例 Bean 的引用(解析循环引用)

- **Object singletonObject = this.singletonObjects.get(beanName)**

    **从当前缓存中获取 Bean，如果不为 null，直接返回**

- **如果 singletonObject 并且不在正在创建中**

    ```java
    if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
        synchronized (this.singletonObjects) {
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
    }
    ```

- **[返回上级方法](#doGetBean)**

#### getObjectForBeanInstance
- [ ] 完善

#### getObjectForBeanInstance
- [ ] 完善

#### createBean
&emsp;&emsp;创建 Bean 的实例，填充这个 Bean 实例，应用 Bean 后置处理器等

- **RootBeanDefinition mbdToUse = mbd**
    **获取 Bean定义**

- **Class<?> resolvedClass = resolveBeanClass(mbd, beanName)**
    **确保bean类在此时被实际解析，并在动态解析的类不能存储在共享合并bean定义中的情况下克隆bean定义**

- **Object beanInstance = [doCreateBean](#doCreateBean)(beanName, mbdToUse, args)**

- ****

- **[返回上级方法](#doGetBean)**   

#### doCreateBean
&emsp;&emsp;实际创建指定的 Bean，此时已进行预创建处理，例如，检查 postProcessBeforeInstantiation 调用。区分默认 Bean 实例化、工厂方法的使用和自动连接构造函数

- **BeanWrapper instanceWrapper = null**
    
    **实例化这个 Bean**

- **instanceWrapper = [createBeanInstance](createBeanInstance)(beanName, mbd, args)**

    ****

- **[applyMergedBeanDefinitionPostProcessors](#applyMergedBeanDefinitionPostProcessors)(mbd, beanType, beanName)**  
    **允许后置处理器修改合并 Bean 的定义**

- **[populateBean](#populateBean)(beanName, mbd, instanceWrapper)**

- 

#### createBeanInstance
&emsp;&emsp;为指定 Bean 创建新实例，**使用适当的实例化策略**：工厂方法，构造器或者简单实例化

- **Class<?> beanClass = resolveBeanClass(mbd, beanName)**
    **确定这个 Bean 的类能实际被解析**

- **if (mbd.getFactoryMethodName() != null) {**  
    &emsp;&emsp;**return [instantiateUsingFactoryMethod](#instantiateUsingFactoryMethod)(beanName, mbd, args);**  
    **}**


- **重新创建相同bean时的快捷方式**
    ```java
    boolean resolved = false;
    boolean autowireNecessary = false;
    if (args == null) {
        synchronized (mbd.constructorArgumentLock) {
            if (mbd.resolvedConstructorOrFactoryMethod != null){
                resolved = true;
                autowireNecessary = mbd.constructorArgumentsResolved;
            }
        }
    }
    if (resolved) {
        if (autowireNecessary) {
            return autowireConstructor(beanName, mbd, null, null);
        }
        else {
            return instantiateBean(beanName, mbd);
        }
    }
    ```
-     

#### instantiateUsingFactoryMethod
&emsp;&emsp;

#### applyMergedBeanDefinitionPostProcessors
&emsp;&emsp;执行 MergedBeanDefinitionPostProcessor
```java
protected void applyMergedBeanDefinitionPostProcessors(RootBeanDefinition mbd, Class<?> beanType, String beanName) {
    for (BeanPostProcessor bp : getBeanPostProcessors()) {
        if (bp instanceof MergedBeanDefinitionPostProcessor) {
            MergedBeanDefinitionPostProcessor bdp = (MergedBeanDefinitionPostProcessor) bp;
            bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);
        }
    }
}
```