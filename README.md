# SummerCloud
- base提供公共基础
- register提供注册中心
- summer-boot提供请求处理

#### 需要处理的地方

1. 将数据传输交给netty框架
2. 加入调用缓存
3. 序列化方式改变

#### 注解
@ZRegistry 标识为一个注册中心注册交给spring容器管理
@ZApplication 标识为一个应用（发布服务）
@ZRPCUtil   netty框架数据传输工具



