package hexlet.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {

    private static Connection conn;

    //подключение к моей локальной базе данных
    public void connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/library_db";
        String user = "postgres";
        String password = "postgres";
        conn = DriverManager.getConnection(url, user, password);
    }

    //закрытие соединение
    public void closeConnection() throws SQLException {
        conn.close();
    }

    //я назвал поле с годом выпуска в базе данных published_date
    //добавление книги
    //с помощью try-with-resources мой statement автоматически закрывается в каждом методе
    //и память не утекает
    public int addBook(String title, String author, Date publishedDate, String isbn) throws SQLException {
        String sqlQuery = "INSERT INTO books (title, author, published_date, isbn) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setDate(3, new java.sql.Date(publishedDate.getTime()));
            statement.setString(4, isbn);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Failed to add book.");
                }
            }
        }
    }

    //получение списка всех книг
    public List<Book> getAllBooks() throws SQLException {
        List<Book> result = new ArrayList<>();
        String sqlQuery = "SELECT * FROM books";
        try (var statement = conn.createStatement();
             var resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                Date publishedDate = resultSet.getDate("published_date");
                String isbn = resultSet.getString("isbn");
                var book = new Book(title, author, publishedDate, isbn);
                result.add(book);
            }
        }
        return result;
    }
    //поиск книг по названию
    public List<Book> findBooksByTitle(String title) throws SQLException {
        List<Book> result = new ArrayList<>();
        String sqlQuery = "SELECT * FROM books WHERE title = ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String bookTitle = resultSet.getString("title");
                    String bookAuthor = resultSet.getString("author");
                    Date bookPublishedDate = resultSet.getDate("published_date");
                    String bookIsbn = resultSet.getString("isbn");
                    var book = new Book(bookTitle, bookAuthor, bookPublishedDate, bookIsbn);
                    result.add(book);
                }
            }
        }
        return result;
    }
    //удаление книги, возвращает id удаленной книги, или выбрасывает ошибку в случае неудачи
    public int deleteBook(int id) throws SQLException {
        String sqlQuery = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return id;
            } else {
                throw new SQLException("Failed to delete the book.");
            }
        }
    }
    //добавление читателя
    public int addReader(String name, String email) throws SQLException {
        String sqlQuery = "INSERT INTO readers (name, email) VALUES (?, ?) RETURNING id";
        try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, name);
            statement.setString(2, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Failed to add reader.");
                }
            }
        }
    }
    // получаения списка всех читателей
    public List<Reader> getAllReaders() throws SQLException {
        List<Reader> result = new ArrayList<>();
        String sqlQuery = "SELECT * FROM readers";
        try (var statement = conn.createStatement();
             var resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                var reader = new Reader(name, email);
                result.add(reader);
            }
        }
        return result;
    }
    //поиск читателя по имейл, так как он email уникальный, он всего один такой будет
    public Reader findReaderByEmail(String email) throws SQLException {
        String sqlQuery = "SELECT * FROM readers WHERE email = ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String readerName = resultSet.getString("name");
                    String readerEmail = resultSet.getString("email");
                    var reader = new Reader(readerName, readerEmail);
                    return reader;
                } else {
                    throw new SQLException("Reader with the specified email not found.");
                }
            }
        }
    }
    //удаление читателя
    public int deleteReader(int id) throws SQLException {
        String sqlQuery = "DELETE FROM readers WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return id;
            } else {
                throw new SQLException("Failed to delete the reader.");
            }
        }
    }

}
