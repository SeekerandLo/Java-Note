## Spring Security

###  

***

## 问题集
1. ~~Spring Security自定义登录页面时使用访问页面404错误，为什么？配置如下~~
```java
protected void configure(HttpSecurity http) throws Exception {
    http.formLogin()
            .loginPage("/login.html")
            .and()
            .authorizeRequests()
            .antMatchers("/login.html").permitAll()
            .anyRequest()
            .authenticated();
}
```
&emsp;&emsp;没有开启 Spring Security 时，静态资源路径为以下几个，可以直接通过 url 访问 
1. `classpath:/resource` 项目资源目录
2. `classpath:/static` 项目静态文件目录
3. `classpath:/public`  
4. `classpath:/META-INF/resource`  

&emsp;&emsp;开启Spring Security 后，通过配置上面配置中 `loginPage()` 及使用`antMatchers()`对目标路径进行放行，还能访问到的有  
1. `classpath:/resource`
2. `classpath:/static`
3. `classpath:/public`  
4. `classpath:/META-INF/resource`  

&emsp;&emsp;Spring Security 并没有干扰到资源路径........经过测试之后，再使用发现，自定义登录页面成功，可能是缓存还是其他问题导致当时没有生效

***
2. loginProcessingUrl() 没有生效  
&emsp;&emsp;loginProcessingUrl() 是为了让 UsernamePasswordAuthenticationFilter 处理该请求  
  
***
3. 配置完成后 loginPage() 总是重定向，或者是 formLogin() 配置失败


***
4. 第一次配置 loginPage() 404错误，但是删除 Spring Security 的依赖后再导入一次，就能找到页面