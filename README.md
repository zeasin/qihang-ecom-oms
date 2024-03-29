# 启航电商OMS订单处理系统

## 一、介绍
启航电商OMS订单处理系统是一套为中小电商企业构建的一套简单实用的第三方平台订单处理系统，本项目后端采用SpringCloudAlibaba 微服务架构，前端采用Vue2开发。

支持多平台店铺，目前支持：淘宝、京东、拼多多、抖店。后续计划支持快手、视频号、小红书等。

主要功能包括：订单管理及发货、售后处理、商品管理等。

后续计划推出订单打单（电子面单打印）功能。

<img src="preview.png" />

## 二、主体功能

### 1、订单管理
+ 从平台同步订单
+ 订单发货
  + 获取订单收货地址信息
  + 推送物流信息到平台
+ 订单备注修改

### 2、售后管理
+ 从平台同步售后列表
+ 售后处理（同意、备注）

### 3、商品管理
+ 从平台同步商品信息
+ 更新销量
+ 同步库存

### 4、店铺管理

**支持多店铺管理**

+ 店铺参数设置（appkey等）
+ 基础管理
  + 发货地址库
  + 发货物流公司库

## 三、软件架构
### 1、开发环境级组件
#### 1.1 开发环境
+ Jdk：17
+ Nodejs：v16.20.0

#### 1.2 项目组件
##### 后端核心组件
+ SpringBoot：3.0.2
  + spring-boot-starter-security
+ SpringCloudAlibaba：2022.0.0.0
  + Nacos
  + SpringCloud Gateway
  + spring-cloud-starter-loadbalancer

##### 前端框架及组件
+ vue2
+ element

#### 1.3、存储及中间件

+ MySQL8
+ Redis：7.x
  + 缓存：在线用户
  + 消息队列
+ Nacos：2.2.0（配置中心、注册中心）
+ Sentinel（分布式流量治理组件）

  `java -Dserver.port=8888 -Dcsp.sentinel.dashboard.server=localhost:8888 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar`


### 2、项目结构
#### 2.1 core
项目公共模块包括：

+ `common`:公共类型

+ `security`:公共权限验证模块

#### 2.2 api
网关项目，负责微服务接口转发，前端统一通过网关调用其他微服务接口；

采用`gateway`进行api分发，引入Sentinel进行流量治理。

#### 2.3 sys-api
项目系统微服务，主要功能包括：

+ 用户
+ 菜单

#### 2.4 oms-api
oms主功能微服务，主要功能包括：

+ 队列消息处理（订单消息、退款消息）
+ 订单接口
+ 退款接口
+ 店铺接口


#### 2.5 tao-api
淘宝开放平台接口api

#### 2.6 jd-api
京东开放平台接口api

#### 2.7 dou-api
抖店开放平台接口api

#### 2.8 pdd-api
拼多多开放平台接口api

#### 2.9 wei-api
微信视频号小店开放平台接口api

### 3、运行说明
#### 3.1、启动环境

1. 启动MySQL8
2. 启动Redis7
3. 启动Sentinel1.8.7控制台
4. 启动Nacos2.2.0

#### 3.2、启动服务(项目)
1.  启动业务微服务（tao-api、jd-api、dou-api、pdd-api等平台接口api微服务）
2.  启动sys-api、oms-api微服务
3.  启动微服务网关（api）

#### 3.3、运行前端
+ Nodejs版本：v16.20.0
+ 进入`vue`文件夹
+ 运行`npm install` 
+ 运行`npm run dev`
+ 浏览网页`http://localhost`

### 4、项目部署

#### 4.1 打包

##### 后端打包
`mvn clean package`

##### 前端打包
`pnpm run build:prod`


#### 4.2 Nginx配置
```
# 上传文件至远程服务器
将打包生成在 `dist` 目录下的文件拷贝至 `/usr/share/nginx/html` 目录

# nginx.cofig 配置
server {
	listen     80;
	server_name  localhost;
	location / {
			root /usr/share/nginx/html;
			index index.html index.htm;
	}
	# 反向代理配置
	location /prod-api/ {
			proxy_pass http://127.0.0.1:8080/; # 替换成你的后端网关API地址
	}
}
```


## 交个朋友

💖 如果觉得有用记得点 Star⭐

#### 关注公众号

作者微信公众号：qihangerp168

欢迎一起交流！


#### 捐献支持

作者为兼职做开源,平时还需要工作,如果帮到了您可以请作者吃个盒饭(目前还希望接收大家的捐献可以搭建一个演示环境！)


<img src="./weixinzhifu.jpg" width="300px" />
<img src="./zhifubao.jpg" width="300px" />

