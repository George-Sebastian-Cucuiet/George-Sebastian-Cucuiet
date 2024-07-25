import java.io.*;
import java.util.*;

public class Library {
    private List<Book> books;
    private List<Person> members;
    private Map<Person, List<Book>> borrowedBooks;

    public Library(List<Book> books, List<Person> members) {
        this.books = books;
        this.members = members;
        this.borrowedBooks = new HashMap<>();
    }

    public boolean borrow(Person person, String title) {
        // Check if the person is a member
        if (!members.contains(person)) {
            System.err.println("Member not found!");
            return false;
        }

        // Check if the person has already borrowed 3 books
        if (borrowedBooks.getOrDefault(person, new ArrayList<>()).size() >= 3) {
            System.err.println("Cannot borrow more than 3 books!");
            return false;
        }

        // Find the book
        Book target = null;
        for (Book book : books) {
            if (book.getTitle().equals(title) && !isBookBorrowed(book)) {
                target = book;
                break;
            }
        }

        if (target == null) {
            System.err.println("Book not found or already borrowed!");
            return false;
        }

        // Borrow the book
        borrowedBooks.putIfAbsent(person, new ArrayList<>());
        borrowedBooks.get(person).add(target);
        System.out.println("Book borrowed successfully!");

        saveBorrowedBooks();
        return true;
    }

    public boolean returnBook(Person person, String title) {
        if (!borrowedBooks.containsKey(person)) {
            System.err.println("No books borrowed by this member!");
            return false;
        }

        List<Book> borrowed = borrowedBooks.get(person);
        for (Book book : borrowed) {
            if (book.getTitle().equals(title)) {
                borrowed.remove(book);
                System.out.println("Book returned successfully!");
                saveBorrowedBooks();
                return true;
            }
        }

        System.err.println("Book not found!");
        return false;
    }

    private boolean isBookBorrowed(Book book) {
        for (List<Book> books : borrowedBooks.values()) {
            if (books.contains(book)) {
                return true;
            }
        }
        return false;
    }

    public void saveBorrowedBooks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("borrowed_books.txt"))) {
            for (Map.Entry<Person, List<Book>> entry : borrowedBooks.entrySet()) {
                for (Book book : entry.getValue()) {
                    writer.println(entry.getKey().getName() + "|" + book.getTitle() + "|" + book.getAuthor());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBorrowedBooks() {
        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed_books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                Person person = new Person(parts[0]);
                Book book = new Book(parts[1], parts[2]);

                borrowedBooks.putIfAbsent(person, new ArrayList<>());
                borrowedBooks.get(person).add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added successfully!");
    }
}
