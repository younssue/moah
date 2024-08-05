package org.dessert.moah.order.service;

import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.jwt.JWTUtil;
import org.dessert.moah.common.service.CommonService;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.item.repository.StockRepository;
import org.dessert.moah.item.type.DessertType;
import org.dessert.moah.item.type.SaleStatus;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.order.dto.OrderRequestDto;
import org.dessert.moah.item.entity.DessertItem;
import org.dessert.moah.item.entity.Stock;
import org.dessert.moah.user.entity.Users;
import org.dessert.moah.item.repository.DessertItemRepository;
import org.dessert.moah.order.repository.OrderItemRepository;
import org.dessert.moah.order.repository.OrderRepository;
import org.dessert.moah.user.repository.UserRepository;
import org.dessert.moah.user.type.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DessertItemRepository dessertItemRepository;


    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Users user;
    private DessertItem dessertItem;
    private Stock stock;


/* @BeforeEach
    void setUp() {
        stock = Stock.builder()
                     .stockAmount(100)
                     .sellAmount(0)
                     .build();

        dessertItem = DessertItem.builder()
                                 .dessertName("Test Dessert")
                                 .contents("Delicious dessert")
                                 .price(1000)
                                 .stock(stock)
                                 .build();

        stock.setDessertItem(dessertItem);

        user = Users.builder()
                    .email("test@example.com")
                    .password("password")
                    .name("Test User")
                    .address("서울 마포구")
                    .phoneNumber("010-4567-8910")
                    .role(UserRoleEnum.USER)
                    .build();

        userRepository.save(user);
        dessertItemRepository.save(dessertItem);
        stockRepository.save(stock);
    }


    @Test
    void createOrder_OutOfStock() {
        // Arrange
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                                                         .dessertId(dessertItem.getId())
                                                         .count(20)  // 재고보다 많은 수량 주문
                                                         .deliveryAddress("서울 마포구")
                                                         .build();

        // Act & Assert
        assertThrows(OutOfStockException.class, () -> orderService.createOrder(customUserDetails, orderRequestDto));
    }

    @Test
    void testCreateOrder_Success() {
        // Arrange
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                                                         .dessertId(dessertItem.getId())
                                                         .count(2)
                                                         .deliveryAddress("서울 마포구")
                                                         .build();

        // Act
        CommonResponseDto<Object> response = orderService.createOrder(customUserDetails, orderRequestDto);

        // Assert
        assertEquals("SUCCESS", response.getStatus().toUpperCase());
        assertEquals(SuccessCode.EXAMPLE_SUCCESS.getDescription(), response.getMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        // Verify stock reduction
        Stock updatedStock = stockRepository.findByStockIdWithLock(stock.getId())
                                            .orElseThrow();
        assertEquals(8, updatedStock.getStockAmount());
    }*/

/*
    @BeforeEach
    @Transactional
    void setUp() {
        user = Users.builder()
                    .email("test@example.com")
                    .password(passwordEncoder.encode("password"))
                    .name("Test User")
                    .address("서울 마포구")
                    .phoneNumber("010-4567-8910")
                    .role(UserRoleEnum.USER)
                    .build();

        userRepository.saveAndFlush(user);

        stock = Stock.builder()
                     .stockAmount(5)
                     .sellAmount(0)
                     .build();

        stockRepository.saveAndFlush(stock); // Stock 객체를 먼저 저장

        dessertItem = DessertItem.builder()
                                 .dessertName("Test Dessert")
                                 .contents("Delicious dessert")
                                 .price(1000)
                                 .stock(stock) // Stock 객체를 참조
                                 .build();

        dessertItemRepository.saveAndFlush(dessertItem); // DessertItem 객체를 나중에 저장

        // 필요 시, Stock 객체에 다시 DessertItem 설정
        stock.setDessertItem(dessertItem);
        stockRepository.saveAndFlush(stock);


    }


    private String loginAndGetToken(String email, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return jwtUtil.createJwt("access", customUserDetails.getAuthorities().iterator().next().getAuthority(), customUserDetails.getEmail(), 600000L);
    }

    @Test
    void testConcurrentOrderProcessing() throws InterruptedException {
        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // 여기에서 스레드가 아닌 메인 스레드에서 사용자와 토큰을 확인합니다.
        String token = loginAndGetToken("test@example.com", "password");
        String email = jwtUtil.getUsername(token);
        Users mainUser = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    // 각 스레드 내에서 보안 컨텍스트 설정
                    CustomUserDetails customUserDetails = new CustomUserDetails(mainUser);

                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities())
                    );

                    OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                                                                     .dessertId(dessertItem.getId())
                                                                     .count(1)
                                                                     .deliveryAddress("서울 마포구")
                                                                     .build();
                    try {
                        CommonResponseDto<Object> response = orderService.createOrder(customUserDetails, orderRequestDto);
                        assertEquals("SUCCESS", response.getStatus().toUpperCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Stock updatedStock = stockRepository.findByStockIdWithLock(stock.getId())
                                            .orElseThrow();
        assertEquals(0, updatedStock.getStockAmount());
    }*/

    @BeforeEach
    @Transactional
    void setUp() {
        stock = Stock.builder()
                     .stockAmount(5)
                     .sellAmount(0)
                     .build();

        stockRepository.saveAndFlush(stock);

        dessertItem = DessertItem.builder()
                                 .dessertName("Test Dessert")
                                 .dessertType(DessertType.CHOCOLATE)
                                 .saleStatus(SaleStatus.ON_SALE)
                                 .contents("Delicious dessert")
                                 .price(1000)
                                 .stock(stock)
                                 .build();

        dessertItemRepository.saveAndFlush(dessertItem);

        stock.setDessertItem(dessertItem);
        stockRepository.saveAndFlush(stock);
    }

    private String createUserAndGetToken(String email) {
        Users user = Users.builder()
                          .email(email)
                          .password(passwordEncoder.encode("password"))
                          .name("Test User")
                          .address("서울 마포구")
                          .phoneNumber("010-4567-8910")
                          .role(UserRoleEnum.USER)
                          .build();

        userRepository.saveAndFlush(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, "password");
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return jwtUtil.createJwt("access", customUserDetails.getAuthorities()
                                                            .iterator()
                                                            .next()
                                                            .getAuthority(), customUserDetails.getEmail(), 600000L);
    }

    @Test
    void testConcurrentOrderProcessing() throws InterruptedException {
        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    String email = "test" + UUID.randomUUID() + "@example.com"; // UUID로 고유한 이메일 생성
                    String token = createUserAndGetToken(email);
                    String extractedEmail = jwtUtil.getUsername(token);
                    Users localUser = userRepository.findByEmail(extractedEmail)
                                                    .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
                    CustomUserDetails customUserDetails = new CustomUserDetails(localUser);

                    SecurityContextHolder.getContext()
                                         .setAuthentication(
                                                 new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities())
                                         );

                    OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                                                                     .dessertId(dessertItem.getId())
                                                                     .count(1)
                                                                     .deliveryAddress("서울 마포구")
                                                                     .build();
                    try {
                        CommonResponseDto<Object> response = orderService.createOrder(customUserDetails, orderRequestDto);
                        assertEquals("SUCCESS", response.getStatus()
                                                        .toUpperCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // 여기서 각 스레드가 종료된 후, 재고 상태를 확인
        Stock updatedStock = stockRepository.findByStockIdWithPessimisticLock(stock.getId())
                                            .orElseThrow(() -> new NotFoundException(ErrorCode.OUT_OF_STOCK));
        assertEquals(0, updatedStock.getStockAmount());
    }

}

