
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

![image](https://velog.velcdn.com/images/younssue/post/e30cc8f5-ae47-42b6-b45d-9cee7ecfb300/image.png)

<br>

## 🐛 트러블 슈팅
<br>

1. **재고 감소에 따른 동시성 문제 해결**

- 프로젝트의 핵심 기능 중 하나인 재고 관리 시스템에서, 동시에 여러 주문이 발생할 때 재고가 정확히 감소하지 않는 **동시성 문제**와 **데이터 일관성 문제**가 발생
- 초기 테스트에서 100개의 재고에 대해 100건의 주문을 처리하는 동안 일부 재고가 남는 현상이 발견됨 (예상 남은 재고: 0, 실제 남은 재고: 2)

- **문제점:** 연관된 엔티티를 조회할 때 두 번의 ```SELECT``` 쿼리가 발생하면서 데이터베이스에서 트랜잭션 격리 수준을 제대로 관리하지 못해,
  동시에 여러 트랜잭션이 동일한 데이터를 수정하려고 하거나, 업데이트가 잘못 처리되는 상황이 발생 -> 여러 스레드가 동시에 동일한 재고 정보를 조회하고 수정하는 과정에서 **데이터 일관성** 문제가 발생

```java
Hibernate: 
    select
        s1_0.stock_id,
        di1_0.dessert_id,
        di1_0.contents,
        di1_0.created_at,
        di1_0.deleted_at,
        di1_0.dessert_name,
        di1_0.dessert_type,
        di1_0.price,
        di1_0.sale_status,
        di1_0.updated_at,
        s1_0.sell_amount,
        s1_0.stock_amount 
    from
        stock s1_0 
    left join
        dessert_item di1_0 
            on di1_0.dessert_id=s1_0.dessert_id 
    where
        s1_0.dessert_id=?
Hibernate: 
    select
        s1_0.stock_id,
        s1_0.dessert_id,
        s1_0.sell_amount,
        s1_0.stock_amount 
    from
        stock s1_0 
    where
        s1_0.stock_id=? for update
```
<br>
<br>


- **해결:** 재고와 상품 정보를 한 번에 조회하는 **통합 쿼리**를 작성하고, **비관적 락(Pessimistic Lock)** 을 적용하여 동시성 문제를 해결
```java
@Override
    public Optional<DessertDto> findDessertItemByPessimisticLock(Long dessertId) {
        QDessertItem qDessertItem = QDessertItem.dessertItem;
        QStock qstock = QStock.stock;


        DessertDto result = queryFactory.select(Projections.constructor(DessertDto.class,
                                                qDessertItem, qstock))
                                        .from(qDessertItem)
                                        .join(qDessertItem.stock, qstock).fetchJoin()
                                        .where(qDessertItem.id.eq(dessertId))
                                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                        .fetchOne();

        return Optional.ofNullable(result);


    }
```
<br>
- 통합 쿼리를 사용해 재고 정보를 ```fetch join``` 으로 조회하는 동시에 비관적 락을 걸어, 여러 스레드가 동일한 재고 항목에 동시에 접근하지 못하도록 방지
- 이를 통해 재고 감소 처리의 데이터 일관성을 확보하고, 동시에 발생하는 다수의 주문에 대해 정확한 재고 관리를 구현
- 통합 쿼리 적용 후 테스트 결과, 100개의 재고에 대해 100건의 주문이 들어왔을 때 남은 재고가 0개로 정확히 처리됨



<br>

## 🕶️ 성능 최적화 

1. **Redisson의 Pub/Sub 분산락 도입으로 CPU 사용량 감소**
- **문제점:** 비관적 락 사용 시 데이터베이스 CPU 사용량이 높아지는 성능 저하 발생
- **해결:** 비관적 락의 성능 문제를 해결하기 위해 **Redisson의 Pub/Sub 분산락**을 도입
    - Redisson을 사용하여 락을 획득하고 트랜잭션을 처리한 후 락을 해제 , 데이터베이스 부하를 줄임
    - 성능 테스트 결과, 데이터베이스 CPU 사용량이 비관적 락에 비해 절반으로 감소
        - 비관적 락 사용 시 데이터베이스 CPU 사용량: 40.89%
        - Redisson 분산락 사용 시 데이터베이스 CPU 사용량: 23.55% ⇒ 비관적락보다 대략 **17% 감소**


2. **비동기 이미지 업로드를 통한 속도 개선**
- **문제점:**  동기 방식의 이미지 업로드로 인한 응답 지연
    - 이미지 업로드를 동기 방식으로 처리하면서 서버의 응답 속도가 느려졌고, 특히 다중 이미지 업로드 시 전체 트랜잭션의 완료 시간이 길어지는 문제가 발생 , 평균적으로 이미지 업로드 처리에 1447ms로 느린 응답 속도
![image](https://velog.velcdn.com/images/younssue/post/8ac32990-df92-44a6-a013-a2ec22774749/image.png)

- 해결: **비동기 이미지 업로드 처리 도입**
    - Spring Boot의 `@Async`와 `CompletableFuture`를 활용하여 이미지 업로드를 비동기 방식으로 전환 →  이미지 업로드 시간이 **1447ms에서 47ms**로 **약 30배** 단축되어, 전체 트랜잭션 속도가 크게 개선됨

![image](https://velog.velcdn.com/images/younssue/post/662099b4-39d4-4131-80f5-89759b2dd19a/image.png)




