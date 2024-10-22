package hexlet.jdbc;

import lombok.AllArgsConstructor;

import java.util.Date;
@AllArgsConstructor
public class Book {
    private String title;
    private String author;
    private Date publishedDate;
    private String isbn;

}
