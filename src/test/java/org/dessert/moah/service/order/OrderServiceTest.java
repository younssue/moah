package org.dessert.moah.service.order;

import org.dessert.moah.dto.CustomUserDetails;
import org.dessert.moah.dto.order.OrderRequestDto;
import org.dessert.moah.entity.Item.DessertItem;
import org.dessert.moah.entity.Item.Stock;
import org.dessert.moah.entity.order.OrderItem;
import org.dessert.moah.entity.order.Orders;
import org.dessert.moah.entity.type.OrderStatus;
import org.dessert.moah.entity.user.Users;
import org.dessert.moah.exception.NotFoundException;
import org.dessert.moah.exception.OutOfStockException;
import org.dessert.moah.repository.item.DessertItemRepository;
import org.dessert.moah.repository.order.OrderItemRepository;
import org.dessert.moah.repository.order.OrderRepository;
import org.dessert.moah.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DessertItemRepository dessertItemRepository;

    @InjectMocks
    private OrderService orderService;

    private CustomUserDetails customUserDetails;
    private OrderRequestDto orderRequestDto;
    private Users user;
    private DessertItem dessertItem;
    private Stock stock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = Users.builder()
                    .email("test@example.com")
                    .build();

        customUserDetails = new CustomUserDetails(user);

        stock = Stock.builder()
                     .stockAmount(10)
                     .build();

        dessertItem = DessertItem.builder()
                                 .price(100)
                                 .stock(stock)
                                 .build();

        orderRequestDto = OrderRequestDto.builder()
                                         .dessertId(1L)
                                         .count(2)
                                         .build();
    }

//    @Test
//    void createOrder_Success() {
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
//        when(dessertItemRepository.findById(anyLong())).thenReturn(Optional.of(dessertItem));
//
//        orderService.createOrder(customUserDetails, orderRequestDto);
//
//        verify(userRepository, times(1)).findByEmail(anyString());
//        verify(dessertItemRepository, times(1)).findById(anyLong());
//        verify(orderRepository, times(1)).save(any(Orders.class));
//    }

    @Test
    void createOrder_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.createOrder(customUserDetails, orderRequestDto));

        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void createOrder_ItemNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(dessertItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.createOrder(customUserDetails, orderRequestDto));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(dessertItemRepository, times(1)).findById(anyLong());
    }

    @Test
    void createOrder_OutOfStock() {
        stock = Stock.builder()
                     .stockAmount(1)
                     .build();

        dessertItem = DessertItem.builder()
                                 .price(100)
                                 .stock(stock)
                                 .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(dessertItemRepository.findById(anyLong())).thenReturn(Optional.of(dessertItem));

        assertThrows(OutOfStockException.class, () -> orderService.createOrder(customUserDetails, orderRequestDto));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(dessertItemRepository, times(1)).findById(anyLong());
    }
}
