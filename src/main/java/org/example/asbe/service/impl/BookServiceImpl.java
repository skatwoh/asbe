package org.example.asbe.service.impl;

import org.example.asbe.mapper.BookMapper;
import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.dto.BookDTO;
import org.example.asbe.model.dto.CategoryDto;
import org.example.asbe.model.entity.Author;
import org.example.asbe.model.entity.Book;
import org.example.asbe.model.entity.Category;
import org.example.asbe.repository.AuthorRepository;
import org.example.asbe.repository.BookRepository;
import org.example.asbe.repository.CategoryRepository;
import org.example.asbe.service.BookService;
import org.example.asbe.util.CustomException;
import org.example.asbe.util.MessageUtil;
import org.example.asbe.util.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private BookMapper bookMapper;

//    @Override
//    public PagedResponse<BookDTO> listBook(int page, int size, String category) {
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, category.isEmpty() ? "id" : "book_id");
//        Page<Book> bookPage;
//
//        if (category.trim().isEmpty()) {
//            bookPage = repository.findAll(pageable);
//        } else {
//            bookPage = repository.findBooksByCategoryNameContaining(category, pageable);
//        }
//
//        List<BookDTO> bookDTOs = bookMapper.toDtoList(
//                bookPage.getContent().stream()
//                        .filter(book -> book.getStockQuantity() > 0)
//                        .collect(Collectors.toList())
//        );
//        return new PagedResponse<>(
//                bookDTOs,
//                page,
//                size,
//                bookPage.getTotalElements(),
//                bookPage.getTotalPages(),
//                bookPage.isLast(),
//                bookPage.getSort().toString()
//        );
//    }

    public PagedResponse<BookDTO> listBook(int page, int size, List<String> categories) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "book_id");
        Page<Book> bookPage;

        if (categories != null && !categories.isEmpty()) {
            bookPage = repository.findBooksByCategoryNameContaining(categories, pageable);
            // Filter books with stockQuantity > 0
            List<Book> filteredBooks = bookPage.getContent().stream()
                    .filter(book -> book.getStockQuantity() > 0)
                    .collect(Collectors.toList());

            // Group by category and map to DTO
            Map<String, List<BookDTO>> groupedByCategory = new HashMap<>();
            for (String category : categories) {
                List<BookDTO> dtosInCategory = filteredBooks.stream()
                        .filter(book -> book.getCategories().stream()
                                .map(Category::getName)
                                .anyMatch(catName -> catName.equalsIgnoreCase(category)))
                        .map(bookMapper::toDto)
                        .collect(Collectors.toList());
                groupedByCategory.put(category, dtosInCategory);
            }

            return new PagedResponse<>(
                    groupedByCategory,
                    page,
                    size,
                    bookPage.getTotalElements(),
                    bookPage.getTotalPages(),
                    bookPage.isLast(),
                    bookPage.getSort().toString()
            );
        } else {
            Pageable pageableNoCategory = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
            bookPage = repository.findAll(pageableNoCategory);
                    List<BookDTO> bookDTOs = bookMapper.toDtoList(
                bookPage.getContent().stream()
                        .filter(book -> book.getStockQuantity() > 0)
                        .collect(Collectors.toList())
        );
        return new PagedResponse<>(
                bookDTOs,
                page,
                size,
                bookPage.getTotalElements(),
                bookPage.getTotalPages(),
                bookPage.isLast(),
                bookPage.getSort().toString()
        );
        }


    }

    @Override
    public String addBook(BookDTO book, Set<AuthorDto> authors, Set<CategoryDto> categories) {
        if (repository.findBookByTitle(book.getTitle()).isPresent()) {
            throw new CustomException(messageUtil.getMessage("error.book.exists", book.getTitle()));
        }
        Book newBook = bookMapper.toEntity(book);

        // Chuyển Set<AuthorDto> -> Set<Long>
        Set<Long> authorIds = authors.stream().map(AuthorDto::getId).collect(Collectors.toSet());
        Set<Author> authorEntities = new HashSet<>(authorRepository.findAllById(authorIds));

        // Chuyển Set<CategoryDto> -> Set<Long>
        Set<Long> categoryIds = categories.stream().map(CategoryDto::getId).collect(Collectors.toSet());
        Set<Category> categoryEntities = new HashSet<>(categoryRepository.findAllById(categoryIds));

        newBook.setAuthors(authorEntities);
        newBook.setCategories(categoryEntities);

        repository.save(newBook);
        return messageUtil.getMessage("success");
    }

    @Override
    public Book updateBook(Book book, Integer id) {
        if (repository.existsById(id)) {
            Book existingBook = repository.findById(id).get();
            existingBook.setTitle(book.getTitle());
//            existingBook.setAuthor(book.getAuthor());
//            existingBook.setGenre(book.getGenre());
//            existingBook.setPublished(book.getPublished());
//            existingBook.setPublisher(book.getPublisher());
            existingBook.setDescription(book.getDescription());
//            existingBook.setImageUrl(book.getImageUrl());
            return repository.save(book);
        }
        return null;
    }

    @Override
    public String deleteBook(Integer id) {
        if (!repository.existsById(id)) {
            throw new CustomException(messageUtil.getMessage("error.book.notFound", id));
        }
        repository.deleteById(id);
        return messageUtil.getMessage("success.book.delete", id);
    }

    @Override
    public Book getBook(Integer id) {
        Optional<Book> optionalBook = repository.findById(id);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        } else {
            throw new CustomException(messageUtil.getMessage("error.book.notFound", id));
        }
    }
}
