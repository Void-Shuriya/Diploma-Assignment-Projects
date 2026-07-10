import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Book {

    private String bookTitle;
    private String bookAuthor;
    private int bookCopy;
    private LocalDate dueDate;

    public Book(String title, String author, int copy) {

        this.bookTitle = title;
        this.bookAuthor = author;
        this.bookCopy = copy;
    }

    public String getBookTitle() {

        return bookTitle;
    }

    public void borrowBook(Scanner scanner) {

        if (bookCopy > 0) {

            System.out.print(Color.YELLOW + "\n Enter return date (DD/MM/YYYY): " + Color.RESET);
            String dateInput = scanner.nextLine();

            this.dueDate = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            this.bookCopy--;

            System.out.println(Color.BLUE + "\n Book borrowed successfully! Due Date: " + this.dueDate);
        }

        else {

            System.out.println(Color.RED + "\n Book is not available!");
        }
    }

    public void returnBook() {

        if (dueDate != null) {

            long daysLate = ChronoUnit.DAYS.between(dueDate, LocalDate.now());

            System.out.println(daysLate > 0
                    ? Color.RED + "\n Book is returned late! Fine: RM " + daysLate
                    : Color.GREEN + "\n Book returned on time. No fine.");

            this.bookCopy++;
            this.dueDate = null;
        }

        else {

            System.out.println(Color.RED + "\n This book was not borrowed.");
        }
    }

    public String toString() {

        return String.format(Color.GREEN + " %-28s" + Color.BLUE + "│ %-22s" + Color.CYAN + "│ %-5d ",
               bookTitle, bookAuthor, bookCopy);
    }
}