## WebSocket

## 基于 STOMP 协议的 WebSocket

- 以 `/app` 开头的消息都会被路由到带有 `@MessageMapping` 或 `@SubscribeMapping` 注解的方法中

- 以 `/topic` 或 `/queue` 开头的消息都会发送到 STOMP 代理中

### @MessageMapping
```java
@MessageMapping("/topic/activity")
@SendTo("/topic/tracker")
public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
    activityDTO.setUserLogin(principal.getName());
    activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
    activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
    activityDTO.setTime(Instant.now());
    log.debug("Sending user tracking data {}", activityDTO);
    return activityDTO;
}
```
- 隐含开头 /app ?


### 思路
当打开视频时，创建客户端与服务器的 websocket 连接，服务器初始化本次连接行为，并记录客户端上的媒体缓存，播放，暂停，互动，停止播放，等行为，并记录相应行为的时间戳


第一步：java如何获取到数据（是读取c保存的录像文件还是c直接跟java通讯，发送视频流。

第二步：java如何将数据发送到前台。这个用的是websocket协议，在java服务端这边写一个 websocket 的服务端，然后前台那边是 websocket 的客户端，建立连接实时发送音频流。

第三步：前段每次收到获取到音频流用web audio api给解析成音频可播放格式并存储到全局队列中。在另外写一个播放的循环，去循环检测队列进行播放，