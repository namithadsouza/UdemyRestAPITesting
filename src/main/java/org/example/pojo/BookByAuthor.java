package org.example.pojo;


import lombok.Data;

@Data
public class BookByAuthor {
    private String book_name;
    private String isbn;
    private String aisle;
}