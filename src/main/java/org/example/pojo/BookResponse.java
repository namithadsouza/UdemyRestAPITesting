package org.example.pojo;

import lombok.Data;

@Data
public class BookResponse {
    private String book_name;
    private String isbn;
    private String aisle;
    private String author;
}
