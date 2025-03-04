package org.example.asbe.service.impl;

import org.example.asbe.mapper.BookMapper;
import org.example.asbe.model.dto.BookDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public PagedResponse<BookDTO> listBook(int page, int size) {
        Pageable bookable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
        Page<Book> entities = repository.findAll(bookable);
        List<BookDTO> listBookDTOS =  bookMapper.toDtoList(entities.getContent());

        return new PagedResponse<>(
                bookMapper.toDtoList(entities.getContent()),
                page,
                size,
                entities.getTotalElements(),
                entities.getTotalPages(),
                entities.isLast(),
                entities.getSort().toString()
        );
    }

    @Override
    public String addBook(BookDTO book, Set<Long> authorIds, Set<Long> categoryIds) {
        if(repository.findBookByTitle(book.getTitle()).isPresent()) {
//            return messageUtil.getMessage("error.book.exists", book.getTitle());
            throw new CustomException(messageUtil.getMessage("error.book.exists", book.getTitle()));
        }
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setDescription(book.getDescription());
        newBook.setPrice(book.getPrice());
        newBook.setStockQuantity(book.getStockQuantity());
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(authorIds));
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));

        newBook.setAuthors(authors);
        newBook.setCategories(categories);
        repository.save(newBook);
        return messageUtil.getMessage("success");
    }

    @Override
    public Book updateBook(Book book, Integer id) {
        if(repository.existsById(id)){
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
        return  null;
    }

    @Override
    public String deleteBook(Integer id) {
        if (!repository.existsById(id)) {
            throw new CustomException(messageUtil.getMessage("error.book.notFound", id));
        }
        repository.deleteById(id);
        return messageUtil.getMessage("success.book.delete", id);
    }
}
