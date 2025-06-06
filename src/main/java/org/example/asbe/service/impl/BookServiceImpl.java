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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Override
    public Book getBook(Integer id) {
        Optional<Book> optionalBook = repository.findById(id);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        }
        else {
            throw new CustomException(messageUtil.getMessage("error.book.notFound", id));
        }
    }
}
