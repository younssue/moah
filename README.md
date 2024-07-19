
## MOAH ( 디저트 주문 사이트 ) 

두바이 초콜릿, 베이글 등 요새 유행하는 디저트를 예약할 수 있는 사이트입니다 

<br>


## ❓ 프로젝트를 만들게 된 이유는?
- **Moah**는 **선착순 구매 로직처리를 어떻게 하면 좋을까?** 하는 고민으로 만들게 된 프로젝트입니다 

- 단순히 디저트를 판매하는 것을 넘어서 재고는 한정 되어 있는 인기 있는 상품을 구매하기 위해 많은 고객이 구매를 해야하는 상황에 구매 실패가 일어나는데 이 불편함을 어떻게 해소할 수 있을까에대한 가정하에 프로젝트를 진행했습니다

<br>

## 🛠 프로젝트 구조  

```java
├── java
│   │   └── org
│   │       └── dessert
│   │           └── moah
│   │               ├── common
│   │               │   ├── config
│   │               │   ├── controller
│   │               │   ├── dto
│   │               │   ├── entity
│   │               │   ├── exception
│   │               │   ├── jwt
│   │               │   ├── service
│   │               │   └── type
│   │               ├── item
│   │               │   ├── controller
│   │               │   ├── dto
│   │               │   ├── entity
│   │               │   ├── repository
│   │               │   ├── service
│   │               │   └── type
│   │               ├── order
│   │               │   ├── controller
│   │               │   ├── dto
│   │               │   ├── entity
│   │               │   ├── repository
│   │               │   ├── service
│   │               │   └── type
│   │               └── user
│   │                   ├── controller
│   │                   ├── dto
│   │                   ├── entity
│   │                   ├── repository
│   │                   ├── service
│   │                   └── type
│   └── resources
│       ├── static
│       └── templates

```


## 🛠 기능 
