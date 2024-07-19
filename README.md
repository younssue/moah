
## MOAH ( 디저트 주문 사이트 ) 

두바이 초콜릿, 베이글 등 요새 유행하는 디저트를 예약할 수 있는 사이트입니다 

<br>


## ❓ 프로젝트를 만들게 된 이유는?
- **Moah**는 **선착순 구매 로직처리를 어떻게 하면 좋을까?** 하는 고민으로 만들게 된 프로젝트입니다 

- 단순히 디저트를 판매하는 것을 넘어서 재고는 한정 되어 있는 인기 있는 상품을 구매하기 위해 많은 고객이 구매를 해야하는 상황에 구매 실패가 일어나는데 이 불편함을 어떻게 해소할 수 있을까에대한 가정하에 프로젝트를 진행했습니다

<br>

## 🛠 프로젝트 구조  

```java
── src
    ├── main
    │   ├── generated
    │   │   └── org
    │   │       └── dessert
    │   │           └── moah
    │   │               ├── common
    │   │               │   └── entity
    │   │               │       └── QBaseTime.java
    │   │               ├── item
    │   │               │   └── entity
    │   │               │       ├── QDessertItem.java
    │   │               │       ├── QDessertItemImage.java
    │   │               │       └── QStock.java
    │   │               ├── order
    │   │               │   └── entity
    │   │               │       ├── QOrderItem.java
    │   │               │       └── QOrders.java
    │   │               └── user
    │   │                   └── entity
    │   │                       ├── QSession.java
    │   │                       └── QUsers.java
    │   ├── java
    │   │   └── org
    │   │       └── dessert
    │   │           └── moah
    │   │               ├── MoahApplication.java
    │   │               ├── common
    │   │               │   ├── config
    │   │               │   │   ├── CacheConfig.java
    │   │               │   │   ├── QueryDslConfig.java
    │   │               │   │   ├── RedisConfig.java
    │   │               │   │   └── SecurityConfig.java
    │   │               │   ├── controller
    │   │               │   │   ├── ExampleController.java
    │   │               │   │   └── MainController.java
    │   │               │   ├── dto
    │   │               │   │   ├── CommonResponseDto.java
    │   │               │   │   ├── ExampleDto.java
    │   │               │   │   ├── PaginationDto.java
    │   │               │   │   └── ResultDto.java
    │   │               │   ├── entity
    │   │               │   │   └── BaseTime.java
    │   │               │   ├── exception
    │   │               │   │   ├── BadRequestException.java
    │   │               │   │   ├── GlobalExceptionHandler.java
    │   │               │   │   ├── NotFoundException.java
    │   │               │   │   └── OutOfStockException.java
    │   │               │   ├── jwt
    │   │               │   │   ├── CustomLogoutFilter.java
    │   │               │   │   ├── JWTFilter.java
    │   │               │   │   ├── JWTUtil.java
    │   │               │   │   └── LoginFilter.java
    │   │               │   ├── service
    │   │               │   │   ├── CommonService.java
    │   │               │   │   └── ExampleService.java
    │   │               │   └── type
    │   │               │       ├── ErrorCode.java
    │   │               │       ├── ResponseStatus.java
    │   │               │       └── SuccessCode.java
    │   │               ├── item
    │   │               │   ├── controller
    │   │               │   │   └── ItemController.java
    │   │               │   ├── dto
    │   │               │   │   ├── ItemResponseDto.java
    │   │               │   │   ├── ItemResponseListDto.java
    │   │               │   │   ├── RemainStockDto.java
    │   │               │   │   └── StockDto.java
    │   │               │   ├── entity
    │   │               │   │   ├── DessertItem.java
    │   │               │   │   ├── DessertItemImage.java
    │   │               │   │   └── Stock.java
    │   │               │   ├── repository
    │   │               │   │   ├── DessertItemImageRepository.java
    │   │               │   │   ├── DessertItemRepository.java
    │   │               │   │   ├── DessertItemRepositoryCustom.java
    │   │               │   │   ├── DessertItemRepositoryImpl.java
    │   │               │   │   ├── StockRepository.java
    │   │               │   │   ├── StockRepositoryCustom.java
    │   │               │   │   └── StockRepositoryImpl.java
    │   │               │   ├── service
    │   │               │   │   └── ItemService.java
    │   │               │   └── type
    │   │               │       ├── DessertType.java
    │   │               │       └── SaleStatus.java
    │   │               ├── order
    │   │               │   ├── controller
    │   │               │   │   └── OrderController.java
    │   │               │   ├── dto
    │   │               │   │   ├── OrderItemDto.java
    │   │               │   │   ├── OrderRequestDto.java
    │   │               │   │   ├── OrderResponseDto.java
    │   │               │   │   └── OrderResponseList.java
    │   │               │   ├── entity
    │   │               │   │   ├── OrderItem.java
    │   │               │   │   └── Orders.java
    │   │               │   ├── repository
    │   │               │   │   ├── OrderItemRepository.java
    │   │               │   │   ├── OrderRepository.java
    │   │               │   │   ├── OrderRepositoryCustom.java
    │   │               │   │   └── OrderRepositoryImpl.java
    │   │               │   ├── service
    │   │               │   │   ├── OrderLockService.java
    │   │               │   │   ├── OrderService.java
    │   │               │   │   ├── StockLockService.java
    │   │               │   │   ├── StockRedisAndLockService.java
    │   │               │   │   └── StockService.java
    │   │               │   └── type
    │   │               │       └── OrderStatus.java
    │   │               └── user
    │   │                   ├── controller
    │   │                   │   ├── ReissueController.java
    │   │                   │   └── UserController.java
    │   │                   ├── dto
    │   │                   │   ├── CustomUserDetails.java
    │   │                   │   ├── LoginRequestDto.java
    │   │                   │   ├── LoginResponseDto.java
    │   │                   │   ├── SignupRequestDto.java
    │   │                   │   └── UpdateUserInfoRequestDto.java
    │   │                   ├── entity
    │   │                   │   ├── Session.java
    │   │                   │   └── Users.java
    │   │                   ├── repository
    │   │                   │   ├── SessionRepository.java
    │   │                   │   └── UserRepository.java
    │   │                   ├── service
    │   │                   │   ├── CustomUserDetailsService.java
    │   │                   │   ├── ReissueService.java
    │   │                   │   └── UserService.java
    │   │                   └── type
    │   │                       └── UserRoleEnum.java
    │   └── resources
    │       ├── application-dev.yaml
    │       ├── application-test.yaml
    │       ├── application.yaml
    │       ├── static
    │       └── templates

```


## 🛠 기능 
