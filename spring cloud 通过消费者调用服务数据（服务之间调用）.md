## 一、场景
#### 在使用微服务的时候不免存在服务之间的调用。
* 比如：用户模块的微服务需要获取订单模块微服务的数据，此时可以在用户模块直接通过http方式调用订单模块的接口获取数据（个人感觉so恶心），也可以采用在订单模块创建一个消费者，用户模块通过订单模块消费者调用订单模块接口数据。
### 本案例项目如下
```
base-use
--base-eureka
--use-api
--use-provide
--use-consumer
--use-consumer-pro
```
##### 模块说明：
* base-eureka：注册中心
* use-api：模块接口api定义
* use-provide：服务提供者（理解为场景下：订单模块服务提供者）
* use-consumer：服务消费者（理解为场景下：订单模块消费者）
* use-consumer-pro：消费者使用者（理解为场景下：用户模块）

## 二、项目搭建
* 本项目采用maven方式搭建
### 1、基础项目搭建
* 基础项目主要定义maven库的版本号
#### 1.1、基础项目pom配置：
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>base.use</groupId>
    <artifactId>base-use</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>base-eureka</module>
        <module>use-api</module>
        <module>use-provide</module>
        <module>use-consumer</module>
        <module>use-consumer-pro</module>
    </modules>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Dalston.RELEASE</spring-cloud.version>
    </properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
</project>
```
### 2、base-eureka模块搭建
* eureka为注册中心，创建启动类及定义配置文件即可
#### 2.1、pom配置文件
```
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
    </dependencies>
 ```
#### 2.2、创建启动类
```
package com.base;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * @author brusion
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```
#### 2.3、配置文件：application.properties
```
spring.application.name=spring-cloud-eureka
server.port=9000
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.serviceUrl.defaultZone=http://localhost:${server.port}/eureka/
```
### 3、api模块
* api模块主要是定义操作，在实际项目中用户和订单模块需要单独定义api
#### 3.1、pom配置文件
```
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
        </dependency>
    </dependencies>
 ```
##### 说明：
* spring-web：主要是在定义接口时需要使用web中的注解类

#### 3.2、定义接口类
```
/**
 * @author brusion
 * @date 2018/9/4
 */
public interface UserService {
    @RequestMapping("/index")
    String index();
}
```

### 4、服务提供者搭建
* 服务提供者主要是创建数据通过接口给外部使用
#### 4.1、pom文件
```
    <dependencies>
        <dependency>
            <groupId>base.use</groupId>
            <artifactId>use-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
    </dependencies>
 ```
 
#### 4.2、定义启动类
```
package com.use;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * @author brusion
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableEurekaServer
public class ProvideApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProvideApplication.class, args);
    }
}
```
#### 4.3、定义访问接口类

* 定义一个简单的接口用于外部使用
```
package com.use.controller;
import com.use.api.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author brusion
 * @date 2018/9/4
 */
@RestController
public class UserController implements UserService {
    @Override
    @RequestMapping("/index")
    public String index() {
        return "provide data: " + getClass().toString();
    }
}
```
#### 4.4、创建配置文件：application.properties
```
spring.application.name=use-provide
server.port=9111
eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
```

### 5、服务消费者搭建
* 服务消费者主要通过feign实现远程调用

#### 5.1、pom配置
```
    <dependencies>
        <dependency>
            <groupId>base.use</groupId>
            <artifactId>use-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
        </dependency>
    </dependencies>
 ```
 
#### 5.2、创建启动类
```
package com.use;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
/**
 * @author brusion
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
```
#### 5.3、创建远程调用类
```
package com.use.client;
import com.use.api.UserService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
/**
 * @author brusion
 * @date 2018/9/4
 */
@FeignClient(name = "use-provide", fallback = UseClient.UseHystrix.class)
public interface UseClient extends UserService {
    @Component
    public class UseHystrix implements UseClient {
        @Override
        public String index() {
            return "Hystrix data : " + getClass().toString();
        }
    }
}
```
##### 说明：
* use-provide：为指定远程调用的服务模块名称（与服务提供者名称一致）
* UseClient.UseHystrix.class：为熔断类，做服务降级使用
#### 5.4、定义访问接口类
* 直接将远程调用的数据返回给调用者
```
package com.use.controller;
import com.use.api.UserService;
import com.use.client.UseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author brusion
 * @date 2018/9/4
 */
@RestController
public class ConsumerController implements UserService {
    @Autowired
    UseClient useClient;
    @Override
    @RequestMapping("/index")
    public String index() {
        return useClient.index();
    }
}
```
#### 5.5、项目配置文件：application.properties
```
spring.application.name=use-consumer
server.port=9222
eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
```

### 6、消费者使用类
* 消费者使用类是通过引用消费者模块，通过消费者远程调用类获取到消费者中服务提供者数据

#### 6.1、pom配置
```
 <dependencies>
        <dependency>
            <groupId>base.use</groupId>
            <artifactId>use-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>base.use</groupId>
            <artifactId>use-consumer</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.netflix.hystrix</groupId>
            <artifactId>hystrix-javanica</artifactId>
            <version>1.5.10</version>
        </dependency>
    </dependencies>
 ```
#### 6.2、启动类
```
package com.use;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
/**
 * @author brusion
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@RefreshScope
@ComponentScan(basePackages = {"com.use.client","com.use.pro"})
public class ProApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProApplication.class, args);
    }
}
```
##### 说明：
* ComponentScan：通过扫描获取到消费者远程类对象

#### 6.3、访问接口类
```
package com.use.pro;
import com.use.client.UseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author brusion
 * @date 2018/9/4
 */
@RestController
public class ProController {
    @Autowired
    private UseClient useClient;
    @RequestMapping("/index")
    public String index() {
        return useClient.index();
    }
}
```
#### 6.4、配置文件：application.propertis
```
spring.application.name=use-consumer-pro
server.port=9333
eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
```

## 三、测试：
* 项目启动顺序

#### base-eureka-注册中心地址：http://localhost:9000/
#### use-provide服务提供者数据：http://localhost:9111/index
#### use-consumer服务消费者数据：http://localhost:9222/index
#### use-consumer-pro通过消费者获取服务数据：http://localhost:9333/index

## 代理地址：
