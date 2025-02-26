package org.example.asbe.service.impl;

import org.example.asbe.mapper.AuthorMapper;
import org.example.asbe.model.dto.AuthorDto;
import org.example.asbe.model.entity.Author;
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

    @Override
    public String addAuthor(Author author) {
        repository.save(author);
        return messageUtil.getMessage("success");
    }

    @Override
    public Author updateAuthor(Author author, Integer id) {
        if(repository.existsById(id)){
            Author existingAuthor = repository.findById(id).get();
            existingAuthor.setName(author.getName());
            existingAuthor.setBiography(author.getBiography());
            return repository.save(author);
        }
        return  null;
    }

    @Override
    public String deleteAuthor(Integer id) {
        if (!repository.existsById(id)) {
            throw new CustomException(messageUtil.getMessage("error.author.notFound", id));
        }
        repository.deleteById(id);
        return messageUtil.getMessage("success.author.delete", id);
    }
}
