package com.ecom.app.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.ecom.app.dto.CartItemRequest;
import com.ecom.app.model.CartItem;
import com.ecom.app.model.Product;
import com.ecom.app.model.User;
import com.ecom.app.repositories.CartItemRepository;
import com.ecom.app.repositories.ProductRepository;
import com.ecom.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public boolean addToCart(String userId, CartItemRequest request){

        Optional<Product> productOpt = productRepo.findById(request.getProductId());

        if(productOpt.isEmpty()){
            return false;
        }

        Product existingProduct = productOpt.get();

        if(existingProduct.getStockQuantity() < request.getQuantity()){
            return false;
        }

        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));

        if(userOpt.isEmpty()){
            return false;
        }
        User existingUser = userOpt.get();

        CartItem existingItem = cartItemRepo.findByUserAndProduct(existingUser,existingProduct);

        if(existingItem != null){
            existingItem.setQuantity(existingItem.getQuantity()+ request.getQuantity());
            existingItem.setPrice(existingProduct.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            cartItemRepo.save(existingItem);
        }else{

            CartItem item = new CartItem();
            item.setUser(existingUser);
            item.setProduct(existingProduct);
            item.setQuantity(request.getQuantity());
            item.setPrice(existingProduct.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepo.save(item);
        }
        return true;
    }

    public boolean deleteFromCart(String userId, Long productId) {

        Optional<Product> productOpt = productRepo.findById(productId);
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));

        if(productOpt.isPresent() && userOpt.isPresent()){
            cartItemRepo.deleteByUserAndProduct(userOpt.get(),productOpt.get());
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId){

        return userRepo.findById(Long.valueOf(userId))
                .map(cartItemRepo::findByUser)
                .orElseGet(List::of);

    }

    public void clearCart(Long userId){
        System.out.println("Inside clear cart method");
            userRepo.findById(userId)
                    .ifPresent(cartItemRepo :: deleteByUser);
    }
}
