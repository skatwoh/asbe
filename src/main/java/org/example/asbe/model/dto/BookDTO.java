package org.example.asbe.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookDTO {
    private String title;
    private String author;
    private String genre;
    private Date published;
    private String publisher;
    private String description;
    private String imageUrl;
}
