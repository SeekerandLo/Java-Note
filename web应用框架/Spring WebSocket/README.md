## 概念

### WebSocket 发送消息和接收消息的底层协议

- WebSocket：全双工通信协议，一次握手，多次传输数据


- SockJS：WebSocket 之上的 API

- STOMP(面向消息的简单文本协议)：基于 SockJS 的高级API

### STOMP

- 帧格式，
```java
SEND                            //命令
destination:/topic/activity     //头信息，目的地
content-length:29               //负载内容大小
                                //空行
{"page":"/admin/jhi-tracker"}   //发送的内容
```

```java
MESSAGE                                         //命令
destination:/topic/tracker                      //头信息，目的地
content-type:application/json;charset=UTF-8     //文件类型
subscription:sub-1565677719600-561              //订阅的用户
message-id:mshvuu4y-3                           //消息id
content-length:175                              //

{
  "sessionId" : "mshvuu4y",
  "userLogin" : "admin",
  "ipAddress" : "/127.0.0.1:51720",
  "page" : "/admin/jhi-tracker",
  "time" : "2019-08-13T06:28:39.602276300Z"
}
```

### 订阅

- 客户端与服务器进行 HTTP 握手连接，连接点 EndPoint 通过 WebSocketMessageBroker 设置

- 客户端通过 subscribe 向服务器订阅消息主题（/topic/demo1/greetings）

- 客户端可通过 send 向服务器发送消息，消息通过路径 /app/demo1/hello/10086 达到服务端，服务端将其转发到对应的Controller（根据Controller配置的 @MessageMapping(“/demo1/hello/{typeId}”) 信息）

- 服务器一旦有消息发出，将被推送到订阅了相关主题的客户端（Controller中的@SendTo(“/topic/demo1/greetings”)表示将方法中 return 的信息推送到 /topic/demo1/greetings 主题）


### 发布：发布消息到客户端

- 作为处理消息，或处理订阅消息的附带结果

- 使用消息模板，如果你想要在接收消息的时候，在响应中发送一条消息，修改方法签名 不是void 类型即可，如下

    返回的对象将会进行转换（通过消息转换器） 并放到 STOMP 帧的负载中，然后发送给消息代理（消息代理分为 STOMP代理中继 和 内存消息代理）

    默认情况下：帧所发往的目的地会与 触发 处理器方法的目的地相同。所以返回的对象 会写入到 STOMP 帧的负载中，并发布到 "/topic/stomp" 目的地。不过，可以通过 **@SendTo 注解，重载目的地**；

    - 😱问题：消息是发送到 MessageMapping 还是发送到 SendTo 中的路径呢    

```java
@MessageMapping("/app/topic/activity")
@SendTo("/topic/tracker")
public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
    activityDTO.setUserLogin(principal.getName());
    activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
    activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
    activityDTO.setTime(Instant.now());
    log.debug("Sending user tracking data {}", activityDTO);
    System.out.println("这里是发布消息给订阅用户");
    return activityDTO;
}
```



### 类

- WebSocketMessageBrokerConfigurer

    - registerStompEndpoints(StompEndpointRegistry registry)
    ```java
    registry.addEndpoint("/websocket/tracker")
            .setHandshakeHandler(defaultHandshakeHandler())
            .setAllowedOrigins(allowedOrigins)
            .withSockJS()
            .setInterceptors(httpSessionHandshakeInterceptor());
    ```  
    将 `/websocket/tracker` 注册为 Stomp 端点，客户端在订阅和发布消息到达目的地址之前，要先连接端点，~~即用户发送请求 `/topic/tracker` 与 Stomp 进行连接，之后再转发到订阅的 url 上~~

    - configureMessageBroker(MessageBrokerRegistry config)
    配置了一个 简单的消息代理。如果不重载，默认case下，会自动配置一个简单的 内存消息代理，用来处理 "/topic" 为前缀的消息。

        - Stomp 代理前缀的意义    
        registry.enableStompBrokerRelay("/queue", "/topic")
        这样设置后，Spring 就知道所有目的地为 /topic /queue 的消息全部发送到 STOMP 代理上

        -  Stomp 应用程序前缀  
        所有目的地以 /app 打头的消息(发送消息url 不是连接url)，都会路由到带有 @MessageMapping 注解的方法中，而不是发布到代理队列中
         

- SimpMessageSendingOperations

    - messagingTemplate.convertAndSend("/topic/tracker", activityDTO);  
    转换和发送，就是把一些消息，发送给订阅此的用户
