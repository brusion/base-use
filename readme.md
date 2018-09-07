# spring cloud 通过消费者调用服务数据（服务之间调用）
## 项目说明：
* base-eureka：eureka注册中心
* base-api：模块接口api
* use-provide：服务提供者
* use-consumer：服务消费者
* use-consumer-pro：服务消费者使用者（通过use-consumer访问use-provide的数据）

## 项目启动顺序：
* base-eureka-注册中心地址：http://localhost:9000/
* use-provide服务提供者数据：http://localhost:9111/index
* use-consumer服务消费者数据：http://localhost:9222/index
* use-consumer-pro通过消费者获取服务数据：http://localhost:9333/index

