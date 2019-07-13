## Spring 中的 Processer
Bean 的赋值，注入组件，@Autowried，生命周期注解功能，@Async...


### BeanFactoryPostProcessor
- BeanFactory 的后置处理器，在 BeanFactory 标准初始化之后调用，所有的 Bean 定义 **已经** 保存到 BeanFactory ，但是 Bean 的实例未创建

- postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException

    > 在应用程序上下文的标准初始化之后修改内部的 Bean Factory，所有的 Bean 都将被加载，但是没有 Bean 被实例化，这里允许重写或添加属性，甚至是提前初始化 Bean 

- 原理：
    - 创建 IoC 容器
    - invokeBeanFactoryPostProcessors(beanFactory)
    - 找到所有的 BeanFactoryPostProcessor ，执行它们中的方法
    ```java
    refresh(){
        // ...    
        // Invoke factory processors registered as beans in the context.
	    invokeBeanFactoryPostProcessors(beanFactory);
        
        // ...
        
        // Instantiate all remaining (non-lazy-init) singletons.
        finishBeanFactoryInitialization(beanFactory)    
        
        // ...
    }
    ```

### BeanDefinitionRegistryPostProcessor 
- 在所有 Bean 定义信息 **将要被加载**，Bean 实例未被创建时加载，在 [BeanFactoryPostProcessor](#BeanFactoryPostProcessor) 之前加载  

- 原理：    
    - 创建 IoC 容器
    - invokeBeanFactoryPostProcessors(beanFactory)
    - 找到所有的 BeanDefinitionRegistryPostProcessor ，执行它们中的方法，在 [BeanFactoryPostProcessor](#BeanFactoryPostProcessor) 之前 

- 可以额外给容器添加组件
    ```java
    public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            System.out.println("BeanDefinitionRegistryPostProcessor--postProcessBeanDefinitionRegistry: " + registry.getBeanDefinitionCount()); // 9
            RootBeanDefinition beanDefinition = new RootBeanDefinition(Cat.class);
            registry.registerBeanDefinition("cat", beanDefinition);
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println("BeanDefinitionRegistryPostProcessor--postProcessBeanFactory  " + beanFactory.getBeanDefinitionCount()); //10
        }
    }
    ```

### InstantiationAwareBeanPostProcessor  

### MergedBeanDefinitionPostProcessor