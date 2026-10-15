package com.ecom.app.repositories;

import com.ecom.app.model.CartItem;
import com.ecom.app.model.Product;
import com.ecom.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user,Product product);

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);
}
