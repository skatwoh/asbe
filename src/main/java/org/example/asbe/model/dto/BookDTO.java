package org.example.asbe.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookDTO {
    private String title;
    private String author;
    private String genre;
    private Date published;
    private String publisher;
    private String description;
    private String imageUrl;
}
