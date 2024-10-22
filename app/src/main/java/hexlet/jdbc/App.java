package hexlet.jdbc;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class App {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        try {
            // Подключение к базе данных
            dbManager.connect();

            // Добавление книг
            int bookId1 = dbManager.addBook("1984", "George Orwell", new Date(), "1234567890");
            int bookId2 = dbManager.addBook("To Kill a Mockingbird", "Harper Lee", new Date(), "0987654321");

            // Добавление читателей
            int readerId1 = dbManager.addReader("John Doe", "john.doe@example.com");
            int readerId2 = dbManager.addReader("Jane Smith", "jane.smith@example.com");

            // Получение всех книг
            List<Book> allBooks = dbManager.getAllBooks();
            System.out.println("All Books:");
            for (Book book : allBooks) {
                System.out.println(book);
            }

            // Получение всех читателей
            List<Reader> allReaders = dbManager.getAllReaders();
            System.out.println("All Readers:");
            for (Reader reader : allReaders) {
                System.out.println(reader);
            }

            // Поиск книг по названию
            List<Book> booksByTitle = dbManager.findBooksByTitle("1984");
            System.out.println("Books by Title '1984':");
            for (Book book : booksByTitle) {
                System.out.println(book);
            }

            // Поиск читателя по email
            Reader readerByEmail = dbManager.findReaderByEmail("john.doe@example.com");
            System.out.println("Reader by Email 'john.doe@example.com':");
            System.out.println(readerByEmail);

            // Удаление книги по ID
            dbManager.deleteBook(bookId1);
            System.out.println("Book with ID " + bookId1 + " deleted.");

            // Удаление читателя по ID
            dbManager.deleteReader(readerId1);
            System.out.println("Reader with ID " + readerId1 + " deleted.");

            // Получение всех книг после удаления
            allBooks = dbManager.getAllBooks();
            System.out.println("All Books after Deletion:");
            for (Book book : allBooks) {
                System.out.println(book);
            }

            // Получение всех читателей после удаления
            allReaders = dbManager.getAllReaders();
            System.out.println("All Readers after Deletion:");
            for (Reader reader : allReaders) {
                System.out.println(reader);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Закрытие соединения
                dbManager.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
