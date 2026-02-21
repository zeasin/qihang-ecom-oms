# AGENTS_CN.md - AI开发代理指南

本文档为AI编码代理提供Qihang OMS（启航电商OMS系统）代码库的必要信息。

---

## 项目概述

**Qihang OMS** 是一款开源的电商订单管理系统，支持多个电商平台：淘宝/天猫、京东、拼多多、抖音和微信小程序商店。

### 技术栈

| 技术 | 版本 |
|------|------|
| 编程语言 | Java 17 |
| 后端框架 | Spring Boot 3.0.2 |
| ORM框架 | MyBatis-Plus 3.5.5 |
| 数据库 | MySQL 8 |
| 缓存 | Redis |
| Web服务器 | Undertow |

---

## 构建命令

### 完整构建
```bash
# 从项目根目录完整构建
mvn clean package
```

### 跳过测试构建
```bash
# 构建但不运行测试（推荐，开发阶段使用）
mvn clean package -DskipTests
```

### 构建特定模块
```bash
# 只构建api模块，同时构建其依赖模块
mvn clean package -pl api -am
```

### 安装到本地仓库
```bash
# 安装到本地Maven仓库
mvn clean install
```

---

## 运行命令

### 使用Maven运行
```bash
# 从项目根目录运行应用
mvn spring-boot:run -pl api

# 使用特定配置 profiles 运行
mvn spring-boot:run -pl api -Dspring-boot.run.profiles=dev
```

### 运行打包后的JAR
```bash
# 运行打包好的JAR文件
java -jar api/target/api-2.2.0.jar
```

> **默认端口**: 8086

---

## 测试命令

> **注意**: 当前项目暂未配置单元测试。以下是标准Maven测试命令，供参考：

```bash
# 运行所有测试
mvn test

# 运行特定模块的测试
mvn test -pl api

# 运行单个测试类
mvn test -Dtest=ClassNameTest

# 运行单个测试方法
mvn test -Dtest=ClassNameTest#methodName

# 运行匹配模式的测试
mvn test -Dtest=*Service*Test

# 构建时跳过测试
mvn package -DskipTests
```

如需添加测试：
1. 在 `pom.xml` 中添加 `spring-boot-starter-test` 依赖
2. 在 `src/test/java` 目录下创建测试类

---

## 项目结构

```
qihang-oms/
├── api/                    # 主应用模块（Spring Boot入口）
│   ├── src/main/java/cn/qihangerp/api/
│   │   ├── controller/     # 内部ERP控制器
│   │   ├── tao/            # 淘宝/天猫平台处理器
│   │   ├── jd/             # 京东平台处理器
│   │   ├── pdd/            # 拼多多平台处理器
│   │   ├── dou/            # 抖音平台处理器
│   │   └── wei/            # 微信小商店平台处理器
│   └── libs/               # 本地JAR依赖
├── core/                   # 核心模块
│   ├── common/             # 通用工具类、配置、异常类
│   └── security/           # JWT认证、Spring Security配置
├── model/                  # 实体模型、VO、BO、DTO
├── mapper/                 # MyBatis-Plus映射器 + XML文件
├── service/                # 业务逻辑服务层
├── vue/                    # 前端Vue.js应用
└── docs/                   # 文档和SQL脚本
```

### 目录详细说明

| 目录 | 说明 |
|------|------|
| `api/` | 主应用模块，包含所有控制器和平台集成代码 |
| `core/common/` | 通用工具类、配置类、自定义异常 |
| `core/security/` | 安全认证相关（JWT、权限控制） |
| `model/` | 数据模型（实体类、视图对象、业务对象、传输对象） |
| `mapper/` | 数据访问层（MyBatis映射器接口和XML） |
| `service/` | 业务逻辑层（服务接口和实现） |
| `vue/` | 前端Vue.js项目 |
| `docs/` | 项目文档、数据库脚本 |

**入口类**: `api/src/main/java/cn/qihangerp/api/ApiApplication.java`

---

## 代码规范

### 缩进与格式化

- **缩进**: 4个空格（禁止使用Tab）
- **换行符**: LF
- **编码**: UTF-8
- **大括号风格**: K&R（开括号放在同一行）

```java
// 正确示例
public class OrderService {
    public void processOrder() {
        // 实现代码
    }
}

// 错误示例
public class OrderService
{
    public void processOrder()
    {
        // 实现代码
    }
}
```

### 导入顺序

按以下顺序组织导入：
1. `cn.qihangerp.*` （项目内部包）
2. 第三方库 (`com.baomidou.*`, `org.springframework.*`, `lombok.*`)
3. Java标准库 (`java.*`)

适当情况下，对同一包下的多个类使用通配符导入。

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| Entity实体 | PascalCase（按平台前缀：Tao、Jd、Pdd、Dou、OmsWei、O） | `TaoOrder`, `OOrder` |
| Service接口 | `<名称>Service` | `TaoOrderService` |
| Service实现 | `<名称>ServiceImpl` | `TaoOrderServiceImpl` |
| Mapper | `<名称>Mapper` | `TaoOrderMapper` |
| Controller | `<名称>Controller` | `TaoOrderController` |
| VO视图对象 | `<名称>Vo` | `UserInfoVo` |
| BO业务对象 | `<名称>Bo` | `OrderShipRequest` |
| 常量 | SCREAMING_SNAKE_CASE | `DEFAULT_PAGE_SIZE` |
| 方法 | camelCase | `queryPageList()` |
| 变量 | camelCase | `orderList`, `shipBo` |

**平台前缀说明**:
- `Tao` = 淘宝/天猫
- `Jd` = 京东
- `Pdd` = 拼多多
- `Dou` = 抖音
- `OmsWei` = 微信小商店
- `O` = 内部ERP系统（如 `OOrder`, `OGoods`）

### Lombok使用

广泛使用Lombok注解：

```java
@Data                    // 为实体类和DTO生成getter/setter
@AllArgsConstructor      // 生成全参构造器（用于构造注入）
@Slf4j                   // 生成日志对象
@Builder                 // 生成Builder模式（用于复杂对象构建）
```

实体类示例：
```java
@TableName(value = "o_order")
@Data
public class OOrder implements Serializable {
    @TableId(type = IdType.AUTO)
    private String id;
    private String orderNo;
    // 其他字段...
}
```

### 依赖注入

优先使用 **构造器注入** + Lombok：

```java
@AllArgsConstructor
@Service
public class OOrderServiceImpl extends ServiceImpl<OOrderMapper, OOrder>
    implements OOrderService {
    
    private final OOrderMapper orderMapper;
    private final OOrderItemMapper orderItemMapper;
    // 其他依赖...
}
```

### 类型声明

- 当类型明显时，使用 `var` 声明局部变量
- 返回值和字段使用显式类型
- 泛型使用钻石运算符：`new ArrayList<>()`

```java
// 推荐用法
var pageList = orderService.queryPageList(bo, pageQuery);
LambdaQueryWrapper<OOrder> queryWrapper = new LambdaQueryWrapper<>();
```

---

## 错误处理

### 返回结果封装

各层使用适当的返回结果封装：

| 封装类 | 用途 | 层级 |
|--------|------|------|
| `ResultVo<T>` | Service层返回结果 | Service |
| `AjaxResult` | API/Controller响应 | Controller |
| `PageResult<T>` | 分页结果 | Service/Controller |

```java
// Service层
public ResultVo<Integer> createOrder(OrderBo bo) {
    if (bo == null) {
        return ResultVo.error("参数错误");
    }
    return ResultVo.success(orderId);
}

// Controller层
@GetMapping("/list")
public AjaxResult list(PageQuery pageQuery) {
    return AjaxResult.success(data);
}
```

### 自定义异常

业务错误抛出 `ServiceException`：

```java
if (order == null) {
    throw new ServiceException("订单不存在");
}
```

### 事务管理

修改数据的方法使用 `@Transactional`：

```java
@Transactional(rollbackFor = Exception.class)
public ResultVo<Integer> cancelOrder(Long orderId) {
    // 业务逻辑
}
```

---

## MyBatis-Plus模式

### 查询构建

使用 `LambdaQueryWrapper` 进行类型安全的查询：

```java
LambdaQueryWrapper<OOrder> queryWrapper = new LambdaQueryWrapper<OOrder>()
    .eq(bo.getShopId() != null, OOrder::getShopId, bo.getShopId())
    .eq(bo.getOrderStatus() != null, OOrder::getOrderStatus, bo.getOrderStatus())
    .ge(StringUtils.hasText(bo.getStartTime()), OOrder::getOrderTime, bo.getStartTime())
    .orderByDesc(OOrder::getCreateTime);
```

### Service基类

Service继承 `ServiceImpl<Mapper, Entity>`：

```java
public interface OOrderService extends IService<OOrder> {
    // 自定义方法
}

@AllArgsConstructor
@Service
public class OOrderServiceImpl extends ServiceImpl<OOrderMapper, OOrder>
    implements OOrderService {
    // 实现
}
```

---

## 注释与文档

- **中文注释** 在本项目中是允许且常见的
- 类级别添加Javadoc文档
- 复杂业务逻辑使用行内注释

```java
/**
 * OMS订单表
 * @TableName o_order
 */
@Data
public class OOrder implements Serializable {
    // ...
}

// 发货状态 0 待发货 1 已分配供应商发货 2 全部发货
.eq(OOrder::getShipStatus, 0)
```

---

## 重要文件

| 文件 | 用途 |
|------|------|
| `pom.xml` | 父POM配置 |
| `api/pom.xml` | 主应用依赖配置 |
| `api/src/main/resources/application.yaml` | 应用配置文件 |
| `docs/qihang-oms.sql` | 数据库schema脚本 |
| `vue/` | 前端Vue.js应用 |

---

## 数据库配置

`application.yaml` 中的默认配置：
- **数据库名**: `qihang-oms`
- **端口**: 3306
- **时区**: GMT+8

导入数据库：`docs/qihang-oms.sql`

---

## 常见任务

### 添加新实体

1. 在 `model/src/main/java/cn/qihangerp/model/entity/` 创建实体类
2. 在 `mapper/src/main/java/cn/qihangerp/mapper/` 创建Mapper接口
3. 在 `mapper/src/main/resources/mapper/` 创建Mapper XML
4. 在 `service/src/main/java/cn/qihangerp/module/service/` 创建Service接口
5. 在 `service/src/main/java/cn/qihangerp/module/service/impl/` 创建Service实现
6. 在 `api/src/main/java/cn/qihangerp/api/controller/` 创建Controller

### 添加新平台集成

1. 在 `api/src/main/java/cn/qihangerp/api/<platform>/` 下创建包
2. 遵循命名规范：`<平台>Order`, `<平台>OrderController` 等
3. 如需要，添加平台特定配置

---

## 注意事项

### 推荐做法
- 遵循现有命名规范
- 使用Lombok注解
- 使用 `@AllArgsConstructor` 进行构造器注入
- 返回适当的封装类型
- 修改操作使用 `@Transactional`
- 适当使用中文注释

### 避免做法
- 避免使用字段注入 (`@Autowired`)，应使用构造器注入
- Service方法不要返回 null，应使用 `ResultVo.error()`
- 不使用 `Optional`（项目未使用）
- 不要为每个方法添加Javadoc，仅在必要时添加
