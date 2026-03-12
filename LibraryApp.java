import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LibraryApp {

    // ==================== BOOK CLASS ====================
    static class Book {
        private String bookId;
        private String title;
        private String author;
        private String isbn;
        private String category;
        private int year;
        private boolean isAvailable;
        private String issuedTo;
        private String issueDate;
        private String dueDate;

        public Book(String bookId, String title, String author, String isbn, String category, int year) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.category = category;
            this.year = year;
            this.isAvailable = true;
        }

        public String getBookId() { return bookId; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getIsbn() { return isbn; }
        public String getCategory() { return category; }
        public int getYear() { return year; }
        public boolean isAvailable() { return isAvailable; }
        public String getIssuedTo() { return issuedTo; }
        public String getIssueDate() { return issueDate; }
        public String getDueDate() { return dueDate; }

        public void setAvailable(boolean available) { isAvailable = available; }
        public void setIssuedTo(String userId) { this.issuedTo = userId; }
        public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }

        @Override
        public String toString() {
            return String.format("%-8s | %-30s | %-20s | %-15s | %-12s | %4d | %s",
                    bookId, title, author, category, isbn, year,
                    isAvailable ? "Available" : "Issued");
        }

        public String getDetailedInfo() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                        BOOK DETAILS                            ║\n");
            sb.append("╠════════════════════════════════════════════════════════════════╣\n");
            sb.append(String.format("║ Book ID      : %-48s║\n", bookId));
            sb.append(String.format("║ Title        : %-48s║\n", title));
            sb.append(String.format("║ Author       : %-48s║\n", author));
            sb.append(String.format("║ ISBN         : %-48s║\n", isbn));
            sb.append(String.format("║ Category     : %-48s║\n", category));
            sb.append(String.format("║ Year         : %-48d║\n", year));
            sb.append(String.format("║ Status       : %-48s║\n", isAvailable ? "Available" : "Issued"));
            if (!isAvailable) {
                sb.append(String.format("║ Issued To    : %-48s║\n", issuedTo));
                sb.append(String.format("║ Issue Date   : %-48s║\n", issueDate));
                sb.append(String.format("║ Due Date     : %-48s║\n", dueDate));
            }
            sb.append("╚════════════════════════════════════════════════════════════════╝\n");
            return sb.toString();
        }
    }

    // ==================== USER CLASS ====================
    static class User {
        private String userId;
        private String name;
        private String email;
        private String phone;
        private String password;
        private List<String> issuedBooks;
        private static final int MAX_BOOKS = 5;

        public User(String userId, String name, String email, String phone, String password) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.password = password;
            this.issuedBooks = new ArrayList<>();
        }

        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getPassword() { return password; }
        public List<String> getIssuedBooks() { return issuedBooks; }

        public boolean canIssueBook() { return issuedBooks.size() < MAX_BOOKS; }
        public void issueBook(String bookId) { if (canIssueBook()) issuedBooks.add(bookId); }
        public void returnBook(String bookId) { issuedBooks.remove(bookId); }

        public String getDetailedInfo() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                        USER DETAILS                            ║\n");
            sb.append("╠════════════════════════════════════════════════════════════════╣\n");
            sb.append(String.format("║ User ID      : %-48s║\n", userId));
            sb.append(String.format("║ Name         : %-48s║\n", name));
            sb.append(String.format("║ Email        : %-48s║\n", email));
            sb.append(String.format("║ Phone        : %-48s║\n", phone));
            sb.append(String.format("║ Books Issued : %d / %-44d║\n", issuedBooks.size(), MAX_BOOKS));
            if (!issuedBooks.isEmpty()) {
                sb.append(String.format("║ Issued Books : %-48s║\n", ""));
                for (String bookId : issuedBooks) {
                    sb.append(String.format("║   - %-56s║\n", bookId));
                }
            }
            sb.append("╚════════════════════════════════════════════════════════════════╝\n");
            return sb.toString();
        }
    }

    // ==================== ISSUE RECORD CLASS ====================
    static class IssueRecord {
        private String issueId;
        private String bookId;
        private String userId;
        private String bookTitle;
        private String userName;
        private LocalDate issueDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private boolean isReturned;
        private static int issueCounter = 1;

        public IssueRecord(String bookId, String userId, String bookTitle, String userName) {
            this.issueId = String.format("ISS%03d", issueCounter++);
            this.bookId = bookId;
            this.userId = userId;
            this.bookTitle = bookTitle;
            this.userName = userName;
            this.issueDate = LocalDate.now();
            this.dueDate = issueDate.plusDays(14);
            this.isReturned = false;
        }

        public String getIssueId() { return issueId; }
        public String getBookId() { return bookId; }
        public String getUserId() { return userId; }
        public LocalDate getIssueDate() { return issueDate; }
        public LocalDate getDueDate() { return dueDate; }
        public boolean isReturned() { return isReturned; }

        public boolean isOverdue() {
            if (isReturned) return returnDate.isAfter(dueDate);
            return LocalDate.now().isAfter(dueDate);
        }

        public long getDaysOverdue() {
            if (!isOverdue()) return 0;
            LocalDate compareDate = isReturned ? returnDate : LocalDate.now();
            return ChronoUnit.DAYS.between(dueDate, compareDate);
        }

        public double calculateFine() { return getDaysOverdue() * 10.0; }

        public long getDaysLeft() {
            if (isReturned) return 0;
            long days = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
            return days < 0 ? 0 : days;
        }

        public void processReturn() {
            this.returnDate = LocalDate.now();
            this.isReturned = true;
        }

        @Override
        public String toString() {
            String status = isReturned ? "Returned" : (isOverdue() ? "OVERDUE" : "Active");
            long daysInfo = isReturned ? 0 : getDaysLeft();
            return String.format("%-8s | %-25s | %-20s | %s | %s | %-6d days | %-10s",
                    issueId, bookTitle, userName, issueDate, dueDate, daysInfo, status);
        }

        public String getDetailedInfo() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║                      ISSUE RECORD DETAILS                      ║\n");
            sb.append("╠════════════════════════════════════════════════════════════════╣\n");
            sb.append(String.format("║ Issue ID     : %-48s║\n", issueId));
            sb.append(String.format("║ Book ID      : %-48s║\n", bookId));
            sb.append(String.format("║ Book Title   : %-48s║\n", bookTitle));
            sb.append(String.format("║ User Name    : %-48s║\n", userName));
            sb.append(String.format("║ Issue Date   : %-48s║\n", issueDate));
            sb.append(String.format("║ Due Date     : %-48s║\n", dueDate));
            sb.append(String.format("║ Status       : %-48s║\n", isReturned ? "Returned" : "Active"));
            if (isReturned) {
                sb.append(String.format("║ Return Date  : %-48s║\n", returnDate));
                if (isOverdue()) {
                    sb.append(String.format("║ Days Overdue : %-48d║\n", getDaysOverdue()));
                    sb.append(String.format("║ Fine Amount  : Rs.%-44.2f║\n", calculateFine()));
                }
            } else {
                if (isOverdue()) {
                    sb.append(String.format("║ Days Overdue : %-48d║\n", getDaysOverdue()));
                    sb.append(String.format("║ Fine Amount  : Rs.%-44.2f║\n", calculateFine()));
                } else {
                    sb.append(String.format("║ Days Left    : %-48d║\n", getDaysLeft()));
                }
            }
            sb.append("╚════════════════════════════════════════════════════════════════╝\n");
            return sb.toString();
        }
    }

    // ==================== LIBRARY MANAGEMENT SYSTEM CLASS ====================
    static class LibraryManagementSystem {
        private HashMap<String, Book> books;
        private HashMap<String, User> users;
        private ArrayList<IssueRecord> issueRecords;
        private HashMap<String, String> credentials;
        private User currentUser;

        public LibraryManagementSystem() {
            books = new HashMap<>();
            users = new HashMap<>();
            issueRecords = new ArrayList<>();
            credentials = new HashMap<>();
            initializeSampleData();
        }

        private void initializeSampleData() {
            addBook(new Book("BK001", "The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", "Fiction", 1925));
            addBook(new Book("BK002", "To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", "Fiction", 1960));
            addBook(new Book("BK003", "A Brief History of Time", "Stephen Hawking", "978-0-553-10953-5", "Science", 1988));
            addBook(new Book("BK004", "Sapiens", "Yuval Noah Harari", "978-0-06-231609-7", "History", 2011));
            addBook(new Book("BK005", "Clean Code", "Robert C. Martin", "978-0-13-235088-4", "Technology", 2008));
            addBook(new Book("BK006", "1984", "George Orwell", "978-0-452-28423-4", "Fiction", 1949));
            addBook(new Book("BK007", "Educated", "Tara Westover", "978-0-399-59050-4", "Non-Fiction", 2018));
            addBook(new Book("BK008", "The Code Breaker", "Walter Isaacson", "978-1-982-115-85-6", "Science", 2021));

            addUser(new User("U001", "John Doe", "john@email.com", "+91 98765 43210", "password123"));
            addUser(new User("U002", "Jane Smith", "jane@email.com", "+91 98765 43211", "password123"));
            addUser(new User("U003", "Michael Johnson", "michael@email.com", "+91 98765 43212", "password123"));
            addUser(new User("U004", "Emily Davis", "emily@email.com", "+91 98765 43213", "password123"));
            addUser(new User("U005", "David Wilson", "david@email.com", "+91 98765 43214", "password123"));
        }

        public void addBook(Book book) { books.put(book.getBookId(), book); }
        public void addUser(User user) {
            users.put(user.getUserId(), user);
            credentials.put(user.getEmail(), user.getPassword());
        }

        public boolean login(String email, String password) {
            if (credentials.containsKey(email) && credentials.get(email).equals(password)) {
                for (User user : users.values()) {
                    if (user.getEmail().equals(email)) {
                        currentUser = user;
                        return true;
                    }
                }
            }
            return false;
        }

        public void logout() { currentUser = null; }
        public User getCurrentUser() { return currentUser; }

        public List<Book> searchBooks(String query) {
            query = query.toLowerCase();
            List<Book> results = new ArrayList<>();
            for (Book book : books.values()) {
                if (book.getBookId().toLowerCase().contains(query) ||
                    book.getTitle().toLowerCase().contains(query) ||
                    book.getAuthor().toLowerCase().contains(query) ||
                    book.getCategory().toLowerCase().contains(query)) {
                    results.add(book);
                }
            }
            return results;
        }

        public List<Book> filterByAvailability(boolean available) {
            return books.values().stream().filter(b -> b.isAvailable() == available).collect(Collectors.toList());
        }

        public List<Book> sortByTitle(boolean ascending) {
            List<Book> sorted = new ArrayList<>(books.values());
            sorted.sort((b1, b2) -> ascending ? b1.getTitle().compareTo(b2.getTitle()) : b2.getTitle().compareTo(b1.getTitle()));
            return sorted;
        }

        public List<Book> sortByAuthor() {
            List<Book> sorted = new ArrayList<>(books.values());
            sorted.sort(Comparator.comparing(Book::getAuthor));
            return sorted;
        }

        public List<Book> sortByYear(boolean newestFirst) {
            List<Book> sorted = new ArrayList<>(books.values());
            sorted.sort((b1, b2) -> newestFirst ? Integer.compare(b2.getYear(), b1.getYear()) : Integer.compare(b1.getYear(), b2.getYear()));
            return sorted;
        }

        public List<Book> getAllBooks() { return new ArrayList<>(books.values()); }
        public Book getBookById(String bookId) { return books.get(bookId); }

        public boolean issueBook(String bookId, String userId) {
            Book book = books.get(bookId);
            User user = users.get(userId);
            if (book == null || user == null || !book.isAvailable() || !user.canIssueBook()) return false;

            IssueRecord record = new IssueRecord(bookId, userId, book.getTitle(), user.getName());
            issueRecords.add(record);

            book.setAvailable(false);
            book.setIssuedTo(userId);
            book.setIssueDate(record.getIssueDate().toString());
            book.setDueDate(record.getDueDate().toString());
            user.issueBook(bookId);
            return true;
        }

        public IssueRecord returnBook(String bookId) {
            Book book = books.get(bookId);
            if (book == null || book.isAvailable()) return null;

            IssueRecord activeRecord = null;
            for (IssueRecord record : issueRecords) {
                if (record.getBookId().equals(bookId) && !record.isReturned()) {
                    activeRecord = record;
                    break;
                }
            }
            if (activeRecord == null) return null;

            activeRecord.processReturn();
            book.setAvailable(true);
            String userId = activeRecord.getUserId();
            book.setIssuedTo(null);
            book.setIssueDate(null);
            book.setDueDate(null);

            User user = users.get(userId);
            if (user != null) user.returnBook(bookId);
            return activeRecord;
        }

        public List<IssueRecord> getActiveIssueRecords() {
            return issueRecords.stream().filter(r -> !r.isReturned()).collect(Collectors.toList());
        }
        public List<IssueRecord> getAllIssueRecords() { return new ArrayList<>(issueRecords); }
        public int getTotalBooks() { return books.size(); }
        public int getAvailableBooks() { return (int) books.values().stream().filter(Book::isAvailable).count(); }
        public int getIssuedBooks() { return (int) books.values().stream().filter(b -> !b.isAvailable()).count(); }
        public int getNewArrivals() {
            int currentYear = java.time.Year.now().getValue();
            return (int) books.values().stream().filter(b -> b.getYear() >= currentYear - 2).count();
        }
    }

    // ==================== MAIN APPLICATION ====================
    private static LibraryManagementSystem lms = new LibraryManagementSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayWelcome();
        while (true) {
            if (lms.getCurrentUser() == null) loginMenu();
            else mainMenu();
        }
    }

    private static void displayWelcome() {
        clearScreen();
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║          📚 LIBRARY BOOK MANAGEMENT SYSTEM 📚                    ║");
        System.out.println("║     This system helps users and librarians efficiently manage,   ║");
        System.out.println("║     search, sort, and issue library books with ease.             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝\n");
    }

    private static void loginMenu() {
        System.out.println("\n╔═════════════════════════════════════╗");
        System.out.println("║         LOGIN / SIGN UP             ║");
        System.out.println("╠═════════════════════════════════════╣");
        System.out.println("║ 1. Login                            ║");
        System.out.println("║ 2. View as Guest                    ║");
        System.out.println("║ 3. Exit                             ║");
        System.out.println("╚═════════════════════════════════════╝");
        System.out.print("\nEnter your choice: ");

        int choice = getIntInput();
        switch (choice) {
            case 1: login(); break;
            case 2: guestMenu(); break;
            case 3:
                System.out.println("\n✓ Thank you for using Library Management System!");
                System.exit(0);
            default:
                System.out.println("\n✗ Invalid choice. Please try again.");
                pause();
        }
    }

    private static void login() {
        clearScreen();
        System.out.println("\n╔═════════════════════════════════════╗");
        System.out.println("║            USER LOGIN               ║");
        System.out.println("╚═════════════════════════════════════╝");
        System.out.print("\nEmail: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (lms.login(email, password)) {
            System.out.println("\n✓ Login successful! Welcome " + lms.getCurrentUser().getName() + "!");
            pause();
        } else {
            System.out.println("\n✗ Invalid credentials. Please try again.");
            System.out.println("\nSample: Email: john@email.com, Password: password123");
            pause();
        }
    }

    private static void mainMenu() {
        clearScreen();
        displayHeader();
        displayStats();

        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                      MAIN MENU                              ║");
        System.out.println("╠═════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. 📖 Browse Book List                                     ║");
        System.out.println("║  2. 🔍 Search Books                                         ║");
        System.out.println("║  3. 🔄 Sort Books                                           ║");
        System.out.println("║  4. 📤 Issue Book                                           ║");
        System.out.println("║  5. 📥 Return Book                                          ║");
        System.out.println("║  6. 📋 View Issue Records                                   ║");
        System.out.println("║  7. 👤 View My Profile                                      ║");
        System.out.println("║  8. 🚪 Logout                                               ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
        System.out.print("\nEnter your choice: ");

        int choice = getIntInput();
        switch (choice) {
            case 1: browseBooks(); break;
            case 2: searchBooks(); break;
            case 3: sortBooks(); break;
            case 4: issueBook(); break;
            case 5: returnBook(); break;
            case 6: viewIssueRecords(); break;
            case 7: viewProfile(); break;
            case 8:
                lms.logout();
                System.out.println("\n✓ Logged out successfully!");
                pause();
                break;
            default:
                System.out.println("\n✗ Invalid choice.");
                pause();
        }
    }

    private static void guestMenu() {
        while (true) {
            clearScreen();
            System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
            System.out.println("║                    GUEST MENU                               ║");
            System.out.println("╠═════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. 📖 Browse Book List                                     ║");
            System.out.println("║  2. 🔍 Search Books                                         ║");
            System.out.println("║  3. 🔙 Back to Login                                        ║");
            System.out.println("╚═════════════════════════════════════════════════════════════╝");
            System.out.print("\nEnter your choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1: browseBooks(); break;
                case 2: searchBooks(); break;
                case 3: return;
                default: System.out.println("\n✗ Invalid choice."); pause();
            }
        }
    }

    private static void displayHeader() {
        User user = lms.getCurrentUser();
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║       📚 LIBRARY MANAGEMENT SYSTEM 📚                       ║");
        System.out.println(String.format("║ Logged in as: %-46s║", user.getName()));
        System.out.println(String.format("║ Email: %-51s║", user.getEmail()));
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
    }

    private static void displayStats() {
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println(String.format("║  Total: %-4d  Available: %-4d  Issued: %-4d  New: %-8d║",
                lms.getTotalBooks(), lms.getAvailableBooks(), lms.getIssuedBooks(), lms.getNewArrivals()));
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
    }

    private static void browseBooks() {
        clearScreen();
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                    📖 BOOK LIST                             ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝\n");
        displayBookList(lms.getAllBooks());
        System.out.print("\nEnter Book ID for details (or Enter to go back): ");
        String bookId = scanner.nextLine().trim();
        if (!bookId.isEmpty()) {
            Book book = lms.getBookById(bookId);
            if (book != null) { System.out.println(book.getDetailedInfo()); pause(); }
            else { System.out.println("\n✗ Book not found!"); pause(); }
        }
    }

    private static void searchBooks() {
        clearScreen();
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║              🔍 SEARCH BOOKS                                ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝\n");
        System.out.print("Enter search query (title, author, category): ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) { System.out.println("\n✗ Empty query!"); pause(); return; }

        List<Book> results = lms.searchBooks(query);
        if (results.isEmpty()) System.out.println("\n✗ No books found.");
        else { System.out.println("\n✓ Found " + results.size() + " book(s):\n"); displayBookList(results); }
        pause();
    }

    private static void sortBooks() {
        clearScreen();
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                🔄 SORT BOOKS                                ║");
        System.out.println("╠═════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Title (A-Z)    2. Title (Z-A)    3. Author              ║");
        System.out.println("║  4. Year (Newest)  5. Year (Oldest)                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
        System.out.print("\nChoice: ");

        int choice = getIntInput();
        List<Book> sorted = null;
        switch (choice) {
            case 1: sorted = lms.sortByTitle(true); break;
            case 2: sorted = lms.sortByTitle(false); break;
            case 3: sorted = lms.sortByAuthor(); break;
            case 4: sorted = lms.sortByYear(true); break;
            case 5: sorted = lms.sortByYear(false); break;
            default: System.out.println("\n✗ Invalid!"); pause(); return;
        }
        clearScreen();
        System.out.println("\n✓ Books sorted!\n");
        displayBookList(sorted);
        pause();
    }

    private static void issueBook() {
        clearScreen();
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                  📤 ISSUE BOOK                              ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝\n");

        User user = lms.getCurrentUser();
        if (!user.canIssueBook()) {
            System.out.println("✗ Maximum limit reached (5 books).");
            pause();
            return;
        }

        System.out.println("Available Books:\n");
        displayBookList(lms.filterByAvailability(true));
        System.out.print("\nEnter Book ID to issue: ");
        String bookId = scanner.nextLine().trim();

        Book book = lms.getBookById(bookId);
        if (book == null) { System.out.println("\n✗ Book not found!"); pause(); return; }
        if (!book.isAvailable()) { System.out.println("\n✗ Book already issued."); pause(); return; }

        if (lms.issueBook(bookId, user.getUserId())) {
            System.out.println("\n✓ Book issued successfully!");
            System.out.println("Book: " + book.getTitle());
            System.out.println("Due Date: " + book.getDueDate());
        } else System.out.println("\n✗ Failed to issue!");
        pause();
    }

    private static void returnBook() {
        clearScreen();
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                  📥 RETURN BOOK                             ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝\n");

        List<IssueRecord> active = lms.getActiveIssueRecords().stream()
                .filter(r -> r.getUserId().equals(lms.getCurrentUser().getUserId()))
                .collect(Collectors.toList());
        if (active.isEmpty()) { System.out.println("✗ No books issued."); pause(); return; }

        System.out.println("Currently Issued:\n");
        displayIssueRecords(active);
        System.out.print("\nEnter Book ID to return: ");
        String bookId = scanner.nextLine().trim();

        Book book = lms.getBookById(bookId);
        if (book == null) { System.out.println("\n✗ Book not found!"); pause(); return; }
        if (book.isAvailable()) { System.out.println("\n✗ Book not issued."); pause(); return; }

        IssueRecord record = lms.returnBook(bookId);
        if (record != null) {
            System.out.println("\n✓ Book returned!");
            System.out.println(record.getDetailedInfo());
            if (record.isOverdue()) {
                System.out.println("\n⚠️  OVERDUE NOTICE:");
                System.out.println("Days Overdue: " + record.getDaysOverdue());
                System.out.println("Fine: Rs." + record.calculateFine());
            }
        } else System.out.println("\n✗ Return failed!");
        pause();
    }

    private static void viewIssueRecords() {
        clearScreen();
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║              📋 ISSUE RECORDS                               ║");
        System.out.println("╠═════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Active Issues    2. All Issues    3. Back               ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
        System.out.print("\nChoice: ");

        int choice = getIntInput();
        List<IssueRecord> records = null;
        switch (choice) {
            case 1: records = lms.getActiveIssueRecords(); clearScreen(); System.out.println("\n✓ Active:\n"); break;
            case 2: records = lms.getAllIssueRecords(); clearScreen(); System.out.println("\n✓ All:\n"); break;
            case 3: return;
            default: System.out.println("\n✗ Invalid!"); pause(); return;
        }
        if (records.isEmpty()) System.out.println("No records.");
        else displayIssueRecords(records);
        pause();
    }

    private static void viewProfile() {
        clearScreen();
        User user = lms.getCurrentUser();
        System.out.println(user.getDetailedInfo());
        if (!user.getIssuedBooks().isEmpty()) {
            System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
            System.out.println("║              YOUR CURRENTLY ISSUED BOOKS                    ║");
            System.out.println("╚═════════════════════════════════════════════════════════════╝\n");
            for (String bookId : user.getIssuedBooks()) {
                Book book = lms.getBookById(bookId);
                if (book != null) System.out.println("• " + book.getTitle() + " (Due: " + book.getDueDate() + ")");
            }
        }
        pause();
    }

    private static void displayBookList(List<Book> books) {
        if (books.isEmpty()) { System.out.println("No books."); return; }
        System.out.println("─".repeat(140));
        System.out.printf("%-8s | %-30s | %-20s | %-15s | %-12s | %4s | %s%n",
                "Book ID", "Title", "Author", "Category", "ISBN", "Year", "Status");
        System.out.println("─".repeat(140));
        for (Book book : books) System.out.println(book);
        System.out.println("─".repeat(140));
        System.out.println("Total: " + books.size());
    }

    private static void displayIssueRecords(List<IssueRecord> records) {
        if (records.isEmpty()) { System.out.println("No records."); return; }
        System.out.println("─".repeat(120));
        System.out.printf("%-8s | %-25s | %-20s | %10s | %10s | %-10s | %-10s%n",
                "Issue ID", "Book Title", "User Name", "Issue Date", "Due Date", "Days Left", "Status");
        System.out.println("─".repeat(120));
        for (IssueRecord r : records) System.out.println(r);
        System.out.println("─".repeat(120));
        System.out.println("Total: " + records.size());
    }

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else { System.out.print("\033[H\033[2J"); System.out.flush(); }
        } catch (Exception e) { for (int i = 0; i < 50; i++) System.out.println(); }
    }

    private static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static int getIntInput() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
