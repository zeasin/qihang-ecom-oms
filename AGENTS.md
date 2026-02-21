# AGENTS.md - Agent Development Guidelines

This document provides essential information for AI coding agents working on the Qihang OMS (启航电商OMS系统) codebase.

## Project Overview

**Qihang OMS** is an open-source e-commerce Order Management System supporting multiple platforms: Taobao/Tmall, JD.com, Pinduoduo, Douyin, and WeChat Shop.

- **Language**: Java 17
- **Framework**: Spring Boot 3.0.2
- **ORM**: MyBatis-Plus 3.5.5
- **Database**: MySQL 8
- **Cache**: Redis
- **Web Server**: Undertow

---

## Build Commands

```bash
# Full build (from project root)
mvn clean package

# Build without running tests
mvn clean package -DskipTests

# Build specific module
mvn clean package -pl api -am

# Install to local repository
mvn clean install
```

## Run Commands

```bash
# Run the application (from project root)
mvn spring-boot:run -pl api

# Run with specific profile
mvn spring-boot:run -pl api -Dspring-boot.run.profiles=dev

# Run the packaged JAR
java -jar api/target/api-2.2.0.jar
```

Application starts on port **8086** by default.

## Test Commands

> **Note**: This project currently has no tests configured. Below are standard Maven test commands:

```bash
# Run all tests (when tests exist)
mvn test

# Run tests for specific module
mvn test -pl api

# Run a single test class
mvn test -Dtest=ClassNameTest

# Run a single test method
mvn test -Dtest=ClassNameTest#methodName

# Run tests matching a pattern
mvn test -Dtest=*Service*Test

# Skip tests during build
mvn package -DskipTests
```

To add tests, include `spring-boot-starter-test` dependency and create tests in `src/test/java` directories.

---

## Project Structure

```
qihang-oms/
├── api/                    # Main application (Spring Boot entry point)
│   ├── src/main/java/cn/qihangerp/api/
│   │   ├── controller/     # Internal ERP controllers
│   │   ├── tao/            # Taobao/Tmall platform handlers
│   │   ├── jd/             # JD.com platform handlers
│   │   ├── pdd/            # Pinduoduo platform handlers
│   │   ├── dou/            # Douyin platform handlers
│   │   └── wei/            # WeChat Shop platform handlers
│   └── libs/               # Local JAR dependencies
├── core/
│   ├── common/             # Common utilities, configs, exceptions
│   └── security/           # JWT authentication, Spring Security config
├── model/                  # Entity models, VOs, BOs, DTOs
├── mapper/                 # MyBatis-Plus mappers + XML files
├── service/                # Business logic services
├── vue/                    # Frontend Vue.js application
└── docs/                   # Documentation and SQL scripts
```

**Entry Point**: `api/src/main/java/cn/qihangerp/api/ApiApplication.java`

---

## Code Style Guidelines

### Indentation & Formatting

- **Indentation**: 4 spaces (no tabs)
- **Line endings**: LF
- **Encoding**: UTF-8
- **Brace style**: K&R (opening brace on same line)

```java
// Correct
public class OrderService {
    public void processOrder() {
        // implementation
    }
}
```

### Import Order

Organize imports in this order:
1. `cn.qihangerp.*` (project imports)
2. Third-party libraries (`com.baomidou.*`, `org.springframework.*`, `lombok.*`)
3. Java standard library (`java.*`)

Use wildcard imports for multiple classes from the same package when appropriate.

### Naming Conventions

| Type | Pattern | Example |
|------|---------|---------|
| Entity | PascalCase (prefix by platform: `Tao`, `Jd`, `Pdd`, `Dou`, `OmsWei`, `O`) | `TaoOrder`, `OOrder` |
| Service Interface | `<Name>Service` | `TaoOrderService` |
| Service Impl | `<Name>ServiceImpl` | `TaoOrderServiceImpl` |
| Mapper | `<Name>Mapper` | `TaoOrderMapper` |
| Controller | `<Name>Controller` | `TaoOrderController` |
| VO | `<Name>Vo` | `UserInfoVo` |
| BO | `<Name>Bo` | `OrderShipRequest` |
| Constant | SCREAMING_SNAKE_CASE | `DEFAULT_PAGE_SIZE` |
| Method | camelCase | `queryPageList()` |
| Variable | camelCase | `orderList`, `shipBo` |

**Platform Prefixes**:
- `Tao` = Taobao/Tmall
- `Jd` = JD.com
- `Pdd` = Pinduoduo
- `Dou` = Douyin
- `OmsWei` = WeChat Shop
- `O` = Internal ERP (e.g., `OOrder`, `OGoods`)

### Lombok Usage

Use Lombok annotations extensively:

```java
@Data                    // For entities and DTOs
@AllArgsConstructor      // For constructor injection
@Slf4j                   // For logging
@Builder                 // For complex object creation
```

Example entity:
```java
@TableName(value = "o_order")
@Data
public class OOrder implements Serializable {
    @TableId(type = IdType.AUTO)
    private String id;
    private String orderNo;
    // ...
}
```

### Dependency Injection

Prefer **constructor injection** with Lombok:

```java
@AllArgsConstructor
@Service
public class OOrderServiceImpl extends ServiceImpl<OOrderMapper, OOrder>
    implements OOrderService {
    
    private final OOrderMapper orderMapper;
    private final OOrderItemMapper orderItemMapper;
    // ...
}
```

### Type Declarations

- Use `var` for local variables when type is obvious
- Use explicit types for return values and fields
- Diamond operator for generics: `new ArrayList<>()`

```java
var pageList = orderService.queryPageList(bo, pageQuery);  // OK
LambdaQueryWrapper<OOrder> queryWrapper = new LambdaQueryWrapper<>();  // OK
```

---

## Error Handling

### Result Wrappers

Use appropriate result wrapper for each layer:

| Wrapper | Usage | Layer |
|---------|-------|-------|
| `ResultVo<T>` | Service layer results | Service |
| `AjaxResult` | API/Controller responses | Controller |
| `PageResult<T>` | Paginated results | Service/Controller |

```java
// Service layer
public ResultVo<Integer> createOrder(OrderBo bo) {
    if (bo == null) {
        return ResultVo.error("参数错误");
    }
    return ResultVo.success(orderId);
}

// Controller layer
@GetMapping("/list")
public AjaxResult list(PageQuery pageQuery) {
    return AjaxResult.success(data);
}
```

### Custom Exceptions

Throw `ServiceException` for business errors:

```java
if (order == null) {
    throw new ServiceException("订单不存在");
}
```

### Transaction Management

Use `@Transactional` for methods that modify data:

```java
@Transactional(rollbackFor = Exception.class)
public ResultVo<Integer> cancelOrder(Long orderId) {
    // ...
}
```

---

## MyBatis-Plus Patterns

### Query Building

Use `LambdaQueryWrapper` for type-safe queries:

```java
LambdaQueryWrapper<OOrder> queryWrapper = new LambdaQueryWrapper<OOrder>()
    .eq(bo.getShopId() != null, OOrder::getShopId, bo.getShopId())
    .eq(bo.getOrderStatus() != null, OOrder::getOrderStatus, bo.getOrderStatus())
    .ge(StringUtils.hasText(bo.getStartTime()), OOrder::getOrderTime, bo.getStartTime())
    .orderByDesc(OOrder::getCreateTime);
```

### Service Base Class

Services extend `ServiceImpl<Mapper, Entity>`:

```java
public interface OOrderService extends IService<OOrder> {
    // Custom methods
}

@AllArgsConstructor
@Service
public class OOrderServiceImpl extends ServiceImpl<OOrderMapper, OOrder>
    implements OOrderService {
    // Implementation
}
```

---

## Comments & Documentation

- **Chinese comments** are acceptable and common in this codebase
- Add Javadoc for class-level documentation
- Use inline comments for complex business logic

```java
/**
 * OMS订单表
 * @TableName o_order
 */
@Data
public class OOrder implements Serializable {
    // ...
}

// 发货状态 0 待发货 1 已分配供应商发货 2全部发货
.eq(OOrder::getShipStatus, 0)
```

---

## Important Files

| File | Purpose |
|------|---------|
| `pom.xml` | Parent POM configuration |
| `api/pom.xml` | Main application dependencies |
| `api/src/main/resources/application.yaml` | Application configuration |
| `docs/qihang-oms.sql` | Database schema |
| `vue/` | Frontend Vue.js application |

---

## Database Configuration

Default configuration in `application.yaml`:
- **Database**: `qihang-oms`
- **Port**: 3306
- **Timezone**: GMT+8

Import schema: `docs/qihang-oms.sql`

---

## Common Tasks

### Adding a New Entity

1. Create entity in `model/src/main/java/cn/qihangerp/model/entity/`
2. Create mapper in `mapper/src/main/java/cn/qihangerp/mapper/`
3. Create mapper XML in `mapper/src/main/resources/mapper/`
4. Create service interface in `service/src/main/java/cn/qihangerp/module/service/`
5. Create service impl in `service/src/main/java/cn/qihangerp/module/service/impl/`
6. Create controller in `api/src/main/java/cn/qihangerp/api/controller/`

### Adding a New Platform Integration

1. Create package under `api/src/main/java/cn/qihangerp/api/<platform>/`
2. Follow naming convention: `<Platform>Order`, `<Platform>OrderController`, etc.
3. Add platform-specific configuration if needed

---

## Do's and Don'ts

### Do
- Follow existing naming conventions
- Use Lombok annotations
- Use constructor injection with `@AllArgsConstructor`
- Return appropriate result wrapper types
- Use `@Transactional` for modifying operations
- Write Chinese comments when appropriate

### Don't
- Don't use field injection (`@Autowired`) when constructor injection is possible
- Don't return null from service methods - use `ResultVo.error()`
- Don't use `Optional` (project doesn't use it)
- Don't add Javadoc to every method - only when necessary
