# redis 发布订阅功能 参考网址：https://blog.csdn.net/qq_42511550/article/details/113698857
具体是消息发布者
redisTemplate.convertAndSend(String channel, Object message)
发布到通道

消息订阅者
定义一个redis消息监听器容器 加载factory和消息监听器
去执行监听方法得到该通道上的消息