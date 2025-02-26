import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


interface Borrowable {
    void borrowBook(String userId);
    void returnBook(String userId);
}


class Book implements Borrowable {
    private String title;
    private String author;
    private String category;
    private boolean isBorrowed;
    private String borrowedBy;

    public Book(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.isBorrowed = false;
        this.borrowedBy = null;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isBorrowed() { return isBorrowed; }

   
    public void borrowBook(String userId) {
        if (!isBorrowed) {
            isBorrowed = true;
            borrowedBy = userId;
            System.out.println(userId + " borrowed " + title);
        } else {
            System.out.println("Sorry, this book is already borrowed by " + borrowedBy);
        }
    }

    
    public void returnBook(String userId) {
        if (isBorrowed && borrowedBy.equals(userId)) {
            isBorrowed = false;
            borrowedBy = null;
            System.out.println(userId + " returned " + title);
        } else {
            System.out.println("You cannot return a book because you didn't borrow.");
        }
    }


    public String toString() {
        return title + "," + author + "," + category + "," + isBorrowed + "," + (borrowedBy == null ? "None" : borrowedBy);
    }
}


class Library {
    private static Library instance;
    private List<Book> books;
    private final String FILE_NAME = "C:\\Users\\KARTHIK YADAV\\Desktop\\mini project\\books.txt";

    private Library() {
        books = new ArrayList<>();
        loadBooksFromFile(); // Load existing books from file
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    private void loadBooksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing book records found.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 5) {
                    Book book = new Book(details[0].trim(), details[1].trim(), details[2].trim());
                    if (Boolean.parseBoolean(details[3].trim())) {
                        book.borrowBook(details[4].trim());
                    }
                    books.add(book);
                }
            }
            System.out.println("Books loaded successfully from file.");
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    public void saveBooksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                writer.println(book.toString());
            }
            System.out.println("Books saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile(); // Save changes to file
        System.out.println("Book added successfully!");
    }

    public void removeBook(String title) {
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
        saveBooksToFile(); // Save changes to file
        System.out.println("Book " + title + " removed.");
    }

    public void borrowBook(String title, String userId) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                book.borrowBook(userId);
                saveBooksToFile();
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void returnBook(String title, String userId) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                book.returnBook(userId);
                saveBooksToFile();
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void listBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        for (Book book : books) {
            System.out.println(book.getTitle() + " by " + book.getAuthor() + " [" + book.getCategory() + "] " +
                    (book.isBorrowed() ? "(Borrowed by " + book.toString().split(",")[4] + ")" : "(Available)"));
        }
    }
}


public class LibraryManagement {
    public static void main(String[] args) {
        Library library = Library.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("File at: ");
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add a Book");
            System.out.println("2. Remove a Book");
            System.out.println("3. List Books");
            System.out.println("4. Borrow a Book");
            System.out.println("5. Return a Book");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();
                    library.addBook(new Book(title, author, category));
                    break;
                case 2:
                    System.out.print("Enter Book Title to Remove: ");
                    String removeTitle = scanner.nextLine();
                    library.removeBook(removeTitle);
                    break;
                case 3:
                    library.listBooks();
                    break;
                case 4:
                    System.out.print("Enter Book Title to Borrow: ");
                    String borrowTitle = scanner.nextLine();
                    System.out.print("Enter Your User ID: ");
                    String userId = scanner.nextLine();
                    library.borrowBook(borrowTitle, userId);
                    break;
                case 5:
                    System.out.print("Enter Book Title to Return: ");
                    String returnTitle = scanner.nextLine();
                    System.out.print("Enter Your User ID: ");
                    String returnUserId = scanner.nextLine();
                    library.returnBook(returnTitle, returnUserId);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
