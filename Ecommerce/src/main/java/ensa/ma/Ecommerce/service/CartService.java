package ensa.ma.Ecommerce.service;

import ensa.ma.Ecommerce.model.Cart;
import ensa.ma.Ecommerce.model.Product;
import ensa.ma.Ecommerce.model.User;
import ensa.ma.Ecommerce.model.dataTransferObjects.AddToCartDto;
import ensa.ma.Ecommerce.model.dataTransferObjects.CartDto;
import ensa.ma.Ecommerce.model.dataTransferObjects.CartItemDto;
import ensa.ma.Ecommerce.repos.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {

    private CartRepository cartRepository;



    public void addToCart(AddToCartDto addToCartDto, Product product, User user){
        Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
        cartRepository.save(cart);
    }


    public CartDto listCartItems(Optional<User> user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        for (Cart cart:cartList){
            CartItemDto cartItemDto = getDtoFromCart(cart);
            cartItems.add(cartItemDto);
        }
        double totalCost = 0;
        for (CartItemDto cartItemDto :cartItems){
            totalCost += (cartItemDto.getProduct().getPrice()* cartItemDto.getQuantity());
        }
        return new CartDto(cartItems,totalCost);
    }


    public static CartItemDto getDtoFromCart(Cart cart) {
        return new CartItemDto(cart);
    }


    public void updateCartItem(AddToCartDto cartDto, Optional<User> user, Product product){
        Optional<Cart> cart1 = cartRepository.findById(cartDto.getId());
        Cart cart = cart1.get();
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
    }

    public void deleteCartItem(Long id,Long userId) throws IOException {
        if (!cartRepository.existsById(id))
            throw new IOException();
        cartRepository.deleteById(id);

    }

    public void deleteCartItems(Integer userId) {
        cartRepository.deleteAll();
    }


    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }

}
