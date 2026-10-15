package com.ecom.app.service;

import com.ecom.app.dto.OrderItemDTO;
import com.ecom.app.dto.OrderResponse;
import com.ecom.app.dto.UserResponse;
import com.ecom.app.model.*;
import com.ecom.app.repositories.OrderRepository;
import com.ecom.app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartService cartService;
    private final UserRepository userRepo;

    public Optional<OrderResponse> createOrder(String userId){

        //Validate cart items
        List<CartItem> cartItems = cartService.getCart(userId);

        if(cartItems.isEmpty()){
            return Optional.empty();
        }

        // validate user with given userId
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));

        if(userOpt.isEmpty()){
            return Optional.empty();
        }

        User user = userOpt.get();

        //calculate total amount of cart items
        BigDecimal totalAmount  = cartItems.stream()
                .map(CartItem :: getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        //create an order

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem-> new OrderItem(
                        null,
                        cartItem.getProduct(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order)
                ).toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepo.save(order);

        //clear cart items
        System.out.println("Clear cart method execution starts");
        cartService.clearCart(Long.valueOf(userId));
        System.out.println("After clear cart method execution");
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemDTO> orderItems = order.getItems().stream()
                    .map(orderItem->new OrderItemDTO(
                            orderItem.getId(),
                            orderItem.getProduct().getId(),
                            orderItem.getQuantity(),
                            orderItem.getPrice(),
                            orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                    )).toList();

        response.setItems(orderItems);
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}
