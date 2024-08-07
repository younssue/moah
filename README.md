
## 🍫 MOAH ( 디저트 주문 사이트 ) 

두바이 초콜릿, 베이글 등 요새 유행하는 디저트를 예약할 수 있는 사이트입니다 

<br>


## ❓ 프로젝트를 만들게 된 이유는?
- **Moah**는 **선착순 구매 로직처리를 어떻게 하면 좋을까?** 하는 고민으로 만들게 된 프로젝트입니다 

- 단순히 디저트를 판매하는 것을 넘어서 재고는 한정 되어 있는 인기 있는 상품을 구매하기 위해 많은 고객이 구매를 해야하는 상황에 구매 실패가 일어나는데 이 불편함을 어떻게 해소할 수 있을까에대한 가정하에 프로젝트를 진행했습니다

<br>

## 📐 기술 스택

- Java 17 
- Spring Boot 3.2.7
- Spring Security
- JPA / Hibernate
- MySQL 8
- Redis 6.2
- Docker / Docker Compose

<br>


## 🛢️ ERD 

![image](https://github.com/user-attachments/assets/4520ff66-b582-4210-bbbf-6d346a650e53)

<br>

## 🛠 주요 기능 

<details>
<summary>1. 유저관리 </summary>
<div dir="auto">

<br>

1. 회원가입 기능을 통해 사용자 계정을 생성합니다 
    - 이메일, 비밀번호, 이름, 생년월일, 전화번호, 주소를 저장
    - 비밀번호는 암호화 되어 저장
2. 로그인 및 로그아웃 기능을 통해 사용자는 편리하게 서비스를 이용할 수 있습니다.
    - 이메일, 비밀번호로 로그인
    - jwt 토큰을 활용한 로그인 기능
      - SpringSecurity , JWT 토큰을 활용하여 인증이 성공하면 access Token 발급
    - 사용자는 만료된 액세스 토큰 대신 유효한 리프레시 토큰을 사용하여 새로운 access Token 을 발급
    - 로그아웃 기능
3. 마이페이지를 통해 사용자는 자신의 정보를 업데이트할 수 있습니다.
    - 주소, 전화번호를 업데이트 

</div>
</details>


<details>
<summary>2. 상품관리 </summary>
<div dir="auto">

<br>

1. **상품** 

    - 상품 등록을 등록
    - 전체 상품 리스트를 조회
      - QueryDSL paging 처리 
    - 상품 상세 조회
   

        
        
2. **주문**

    - 주문내역에서는 사용자가 주문한 상품에 대한 상태를 보여주고 상품에 대한 주문 취소, 반품 기능을 제공
        - 주문 상품에 대한 상태 조회(주문 후 D+1에 배송중, D+2일에 배송완료로 변경 처리)
        - 주문 상품에 대한 취소
          - 주문 상태가 배송중이 되기 이전까지만 취소가 가능하며 취소 후에 는 상품의 재고를 복구
          - 주문 취소후 상태는 취소완료로 변경 
        - 상품에 대한 반품
          -  배송 완료 후 D+1일까지만 반품이 가능하고 그이후에는 반품이 불가능 
          -  배송 완료가 된 상품에 대해서만 반품이 가능하며 반품한 상품은 반품 신청 후 D+1에 재고에 반영
          -  재고에 반영된후 상태는 반품완료로 변경

</div>
</details>
<br>

## 📋 API 문서

[Moah PostMan API Document](https://documenter.getpostman.com/view/30861175/2sA3kUFgcG) 

<br>

## 📙 프로젝트 아키텍처 

- 배포 후 추가 예정

<br>

## 🐛 트러블 슈팅
<br>

- 낙관적락 충돌 **StaleObjectStateException** 오류
<br>

  ![image](https://github.com/user-attachments/assets/902b7b5d-8840-40bf-9121-9c9e0d55d3f1)


  ![image](https://github.com/user-attachments/assets/24e1e87b-3c08-4d8d-b8b3-824bbfba7d3b)

   
- 낙관적락 충돌 오류로 재고가 남아있는 상태에도 감소가 되지 않는 현상이 발생
      

-> [Facade 패턴을 사용하여 트랜잭션 시점을 변경하여 해결](https://velog.io/@younssue/%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85-%EB%82%99%EA%B4%80%EC%A0%81%EB%9D%BD-%EC%9E%AC%EA%B3%A0-%EA%B0%90%EC%86%8C-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0) 




<br>

## 🕶️ 성능 최적화 









