package org.example.asbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.asbe.model.entity.*;
import org.example.asbe.model.sdi.BookCartSdi;
import org.example.asbe.repository.*;
import org.example.asbe.service.CartService;
import org.example.asbe.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public String addBookToCart(List<BookCartSdi> listBookCart) {
        for(BookCartSdi bookCartSdi : listBookCart){
            Userinfo user = userRepository.findById(bookCartSdi.getUserId()).orElse(null);
            Book book = bookRepository.findById(bookCartSdi.getBookId()).orElse(null);
            // Kiểm tra xem người dùng đã thêm sách này vào giỏ chưa
            Optional<Cart> existingCartItem = cartRepository.findByUserIdAndBookId(bookCartSdi.getUserId(),  bookCartSdi.getBookId());
            Integer checkQuantity = 0;
            Cart cart;
            if (existingCartItem.isPresent()) {
                cart = existingCartItem.get();
                checkQuantity = cart.getQuantity() - bookCartSdi.getQuantity();
                cart.setQuantity(bookCartSdi.getQuantity());
                cart.setUpdatedAt(Instant.now());
            } else {
                cart = new Cart();
                checkQuantity = -bookCartSdi.getQuantity();
                cart.setUser(user);
                cart.setBook(book);
                cart.setQuantity(bookCartSdi.getQuantity());
                cart.setCreatedAt(Instant.now());
                cart.setUpdatedAt(Instant.now());
                cart.setStatus(false);
            }
            try {
                cartRepository.save(cart);
                book.setStockQuantity(book.getStockQuantity() + checkQuantity);
                bookRepository.save(book);
                return messageUtil.getMessage("success.cart.add");
            }
            catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
        return  null;
    }
}
