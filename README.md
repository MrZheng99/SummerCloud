# ZRPC
- base    基础中心，
- register   提供注册中心: 为caller提供一个可以服务的callee，负责订阅和发布服务
- monitor   监控中心：负责监视各个服务是否可用
1.如果不可用，则通知注册中心，对已经订阅该不可用的服务通知或者选择可用的服务
- service    服务处理中心，处理请求

#### 需要处理的地方

1. 数据传输使用netty
2. 数据序列化使用Kryo

![image-20210402225416789](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210402225416789.png)

