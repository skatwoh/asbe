package org.example.asbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.asbe.mapper.BookMapper;
import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.dto.CartDTO;
import org.example.asbe.model.dto.UserDTO;
import org.example.asbe.model.entity.*;
import org.example.asbe.model.sdi.BookCartSdi;
import org.example.asbe.repository.*;
import org.example.asbe.service.CartService;
import org.example.asbe.util.CustomException;
import org.example.asbe.util.MessageUtil;
import org.example.asbe.util.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

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

    @Autowired
    private BookMapper bookMapper;

//    @Override
//    public String addBookToCart(List<BookCartSdi> listBookCart) {
//        for(BookCartSdi bookCartSdi : listBookCart){
//            Userinfo user = userRepository.findById(bookCartSdi.getUserId()).orElse(null);
//            Book book = bookRepository.findById(bookCartSdi.getBookId()).orElse(null);
//            // Kiểm tra xem người dùng đã thêm sách này vào giỏ chưa
//            Optional<Cart> existingCartItem = cartRepository.findByUserIdAndBookId(bookCartSdi.getUserId(),  bookCartSdi.getBookId());
//            Integer checkQuantity = 0;
//            Cart cart;
//            if (existingCartItem.isPresent()) {
//                cart = existingCartItem.get();
//                checkQuantity = cart.getQuantity() - bookCartSdi.getQuantity();
//                cart.setQuantity(bookCartSdi.getQuantity());
//                cart.setUpdatedAt(Instant.now());
//            } else {
//                cart = new Cart();
//                checkQuantity = -bookCartSdi.getQuantity();
//                cart.setUser(user);
//                cart.setBook(book);
//                cart.setQuantity(bookCartSdi.getQuantity());
//                cart.setCreatedAt(Instant.now());
//                cart.setUpdatedAt(Instant.now());
//                cart.setStatus(false);
//            }
//            try {
//                cartRepository.save(cart);
//                book.setStockQuantity(book.getStockQuantity() + checkQuantity);
//                bookRepository.save(book);
//                return messageUtil.getMessage("success.cart.add");
//            }
//            catch (Exception e) {
//                log.error(e.getMessage(),e);
//            }
//        }
//        return  null;
//    }

    @Override
    public PagedResponse<CartDTO> listCart(int page, int size) {
        Pageable cartable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
        Page<Cart> entities = cartRepository.findAll(cartable);
        List<CartDTO> listCartDTOS = new ArrayList<>();
        for (Cart entity : entities) {
            if(!Boolean.TRUE.equals(entity.getStatus())){
                UserDTO user = userRepository.getUserinfoById(entity.getUser().getId()).orElse(null);
                Book book = bookRepository.findById(entity.getBook().getId()).orElse(null);
                CartDTO cartDTO = new CartDTO();
                cartDTO.setId(entity.getId());
                cartDTO.setUsername(user.getUsername());
                cartDTO.setUser(user);
                cartDTO.setUserId(entity.getUser().getId());
                cartDTO.setBookName(book.getTitle());
                cartDTO.setBook(bookMapper.toDto(book));
                cartDTO.setBookId(entity.getBook().getId());
                cartDTO.setQuantity(entity.getQuantity());
                listCartDTOS.add(cartDTO);
            }
        }

        return new PagedResponse<>(
                listCartDTOS,
                page,
                size,
                entities.getTotalElements(),
                entities.getTotalPages(),
                entities.isLast(),
                entities.getSort().toString()
        );
    }

    @Override
    public String addBookToCart(BookCartSdi bookCartSdi) {
        Userinfo user = userRepository.findById(bookCartSdi.getUserId()).orElse(null);
        Book book = bookRepository.findById(bookCartSdi.getBookId()).orElse(null);
        // Kiểm tra xem người dùng đã thêm sách này vào giỏ chưa
        Optional<Cart> existingCartItem = cartRepository.findByUserIdAndBookIdAndStatus(bookCartSdi.getUserId(), bookCartSdi.getBookId(), bookCartSdi.getStatus());
        Integer checkQuantity = 0;
        Cart cart;
        if (existingCartItem.isPresent() && !Boolean.TRUE.equals(existingCartItem.get().getStatus())) {
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
//        bookRepository.save(book);
            return messageUtil.getMessage("success.cart.add");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String deleteBookFromCart(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new CustomException(messageUtil.getMessage("error.book.notFound", id));
        }
        cartRepository.deleteById(id);
        return messageUtil.getMessage("success.book.delete", id);
    }
}


