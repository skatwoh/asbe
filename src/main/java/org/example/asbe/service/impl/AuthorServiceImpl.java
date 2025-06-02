package org.example.asbe.service.impl;

import org.example.asbe.mapper.AuthorMapper;
import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.dto.CategoryDto;
import org.example.asbe.model.entity.Author;
import org.example.asbe.model.entity.Category;
import org.example.asbe.repository.AuthorRepository;
import org.example.asbe.service.AuthorService;
import org.example.asbe.util.CustomException;
import org.example.asbe.util.MessageUtil;
import org.example.asbe.util.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository repository;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public PagedResponse<AuthorDto> listAuthor(int page, int size) {
        Pageable bookable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
        Page<Author> entities = repository.findAll(bookable);
        List<AuthorDto> listAuthorDTOS =  authorMapper.toDtoList(entities.getContent());

        return new PagedResponse<>(
                authorMapper.toDtoList(entities.getContent()),
                page,
                size,
                entities.getTotalElements(),
                entities.getTotalPages(),
                entities.isLast(),
                entities.getSort().toString()
        );
    }

    public PagedResponse<AuthorDto> filterListAuthor(String name) {
        List<Author> entities;

        if (name != null && !name.trim().isEmpty()) {
            // Tìm kiếm theo name
            entities = repository.findByNameContainingIgnoreCaseOrderById(name);
        } else {
            // Lấy tất cả nếu không có từ khóa
            entities = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        }

        List<AuthorDto> listAuthorDTOS = new ArrayList<>();

        for (Author entity : entities) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setId(entity.getId());
            authorDto.setName(entity.getName());
            authorDto.setBiography(entity.getBiography());
            listAuthorDTOS.add(authorDto);
        }

        return new PagedResponse<>(
                listAuthorDTOS,
                1, // page (bạn có thể set là 1 nếu không dùng phân trang)
                listAuthorDTOS.size(), // size (số phần tử trong list)
                listAuthorDTOS.size(), // totalElements
                1, // totalPages
                true, // isLast (vì không phân trang, luôn là trang cuối)
                Sort.by(Sort.Direction.ASC, "id").toString()
        );
    }

    @Override
    public String addAuthor(Author author) {
        if (repository.existsByName(author.getName()) && repository.existsByBiography(author.getBiography())) {
            throw new CustomException(messageUtil.getMessage("author.exist"));
        }
        repository.save(author);
        return messageUtil.getMessage("success");
    }

    @Override
    public Author updateAuthor(Author author, Integer id) {
        if(repository.existsById(Long.valueOf(id))){
            if(repository.existsByName(author.getName()) && repository.existsByBiography(author.getBiography())){
                throw new CustomException(messageUtil.getMessage("author.exist"));
            }
            Author existingAuthor = repository.findById(Long.valueOf(id)).get();
            existingAuthor.setName(author.getName());
            existingAuthor.setBiography(author.getBiography());
            return repository.save(author);
        }
        return  null;
    }

    @Override
    public String deleteAuthor(Integer id) {
        if (!repository.existsById(Long.valueOf(id))) {
            throw new CustomException(messageUtil.getMessage("error.author.notFound", id));
        }
        repository.deleteById(Long.valueOf(id));
        return messageUtil.getMessage("success.author.delete", id);
    }
}
