import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Book> libraryBooks = loadBooks("books.txt");
        List<Person> libraryMembers = Arrays.asList(
                new Person("Veera"),
                new Person("Rauli")
        );

        Library alexandria = new Library(libraryBooks, libraryMembers);
        alexandria.loadBorrowedBooks();

        Person veera = libraryMembers.get(0);
        Person rauli = libraryMembers.get(1);

        alexandria.borrow(veera, "Silent Spring");
        alexandria.returnBook(veera, "Silent Spring");
        alexandria.borrow(rauli, "The Name of the Wind");

        // Add more interactions as needed

        // Adding a new book to the library
        alexandria.addBook(new Book("New Book Title", "New Book Author"));

        // Save current state of borrowed books
        alexandria.saveBorrowedBooks();
    }

    private static List<Book> loadBooks(String filename) {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                books.add(new Book(parts[1], parts[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }
}
