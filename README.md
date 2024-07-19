
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

<details>
<summary>1. 유저관리 </summary>
<div dir="auto">

<br>

    - 회원가입 기능을 통해 사용자 계정을 생성합니다 
      - 이메일, 비밀번호, 이름, 생년월일, 전화번호, 주소를 저장
      - 비밀번호는 암호화 되어 저장
    - 로그인 및 로그아웃 기능을 통해 사용자는 편리하게 서비스를 이용할 수 있습니다.
      - 이메일, 비밀번호로 로그인
      - jwt 토큰을 활용한 로그인 기능
        - SpringSecurity , JWT 토큰을 활용하여 인증이 성공하면 access Token 발급
      - 사용자는 만료된 액세스 토큰 대신 유효한 리프레시 토큰을 사용하여 새로운 access Token 을 발급
      - 로그아웃 기능
    - 마이페이지를 통해 사용자는 자신의 정보를 업데이트할 수 있습니다.
      - 주소, 전화번호를 업데이트 

</div>
</details>
