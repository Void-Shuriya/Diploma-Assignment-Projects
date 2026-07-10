import java.util.Scanner;

public class LibrarySystem {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Book Lists
        Book[] books = {
                new Book("The Hobbit", "J.R.R. Tolkien", 4),
                new Book("Norwegian Wood", "Haruki Murakami", 3),
                new Book("Atomic Habits", "James Clear", 5),
                new Book("Dune", "Frank Herbert", 2),
                new Book("The Great Gatsby", "F. Scott Fitzgerald", 3)
        };

        Header();

        while (true) {

            Menu();

            System.out.print(Color.BLUE + "\n Enter your option: " + Color.RESET);
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:

                    viewBooks(books);
                    break;

                case 2:

                    System.out.print(Color.BLUE + "\n Enter book title to borrow: " + Color.RESET);
                    String borrowTitle = scanner.nextLine();

                    for (Book book : books) {

                        if (book.getBookTitle().equalsIgnoreCase(borrowTitle)) {

                            book.borrowBook(scanner);
                            break;
                        }
                    }

                    break;

                case 3:

                    System.out.print(Color.BLUE + "\n Enter book title to return: " + Color.RESET);
                    String returnTitle = scanner.nextLine();

                    for (Book book : books) {

                        if (book.getBookTitle().equalsIgnoreCase(returnTitle)) {

                            book.returnBook();
                            break;
                        }
                    }

                    break;

                case 4:

                    System.out.println(Color.CYAN + "\n 📚 Exiting the system...");
                    return;

                default:

                    System.out.println(Color.RED + "\n ❌ Invalid option. Please try again." + Color.RESET);
            }
        }
    }

    // Displays header
    private static void Header() {

        System.out.println("\n\n" + Color.CYAN +
                "\t\t\t╔═════════════════════════════════════╗\n" +
                "\t\t\t║" + Color.RESET + Color.GREEN + "      Library Management System      "  + Color.CYAN + "║\n" +
                "\t\t\t╠═════════════════════════════════════╣\n" +
                "\t\t\t║" + Color.RESET + Color.YELLOW + "    Created By Shuriya Jayaseelan    " + Color.CYAN + "║\n" +
                "\t\t\t╚═════════════════════════════════════╝\n" + Color.RESET);
    }

    // Displays menu
    private static void Menu() {

        System.out.print("\n\n" + Color.CYAN +
                "\t\t\t╔═════════════════════════════════════╗\n" +
                "\t\t\t║" + Color.RESET + Color.GREEN + "            Library Menu             "  + Color.CYAN + "║\n" +
                "\t\t\t╠═════════════════════════════════════╣\n" +
                "\t\t\t║" + Color.RESET + Color.YELLOW + "  1. View Books                      " + Color.CYAN + "║\n" +
                "\t\t\t║" + Color.RESET + Color.YELLOW + "  2. Borrow Book                     " + Color.CYAN + "║\n" +
                "\t\t\t║" + Color.RESET + Color.YELLOW + "  3. Return Book                     " + Color.CYAN + "║\n" +
                "\t\t\t║" + Color.RESET + Color.YELLOW + "  4. Exit                            " + Color.CYAN + "║\n" +
                "\t\t\t╚═════════════════════════════════════╝\n");
    }

    // View books
    private static void viewBooks(Book[] books) {

        System.out.println("\n");
        System.out.println(Color.YELLOW + "\t\t\t╔════════════════════════════════════════════════════════════════╗");
        System.out.println(Color.YELLOW + "\t\t\t║" + Color.GREEN + " Title                       "
                + Color.BLUE + "│ Author                " + Color.CYAN + "│ Copies" + Color.YELLOW + "   ║");
        System.out.println(Color.YELLOW + "\t\t\t╠════════════════════════════════════════════════════════════════╣");

        for (Book book : books) {

            System.out.println(Color.YELLOW + "\t\t\t║" + book.toString() + Color.YELLOW + "   ║");
        }

        System.out.println(Color.YELLOW + "\t\t\t╚════════════════════════════════════════════════════════════════╝");
    }

    // Borrow book
    private static void borrowBook(Book[] books, String title, Scanner scanner) {

        for (Book book : books) {

            if (book.getBookTitle().equalsIgnoreCase(title)) {

                book.borrowBook(scanner);
                return;
            }
        }

        System.out.println(Color.RED + "\n ❌ Book not found!" + Color.RESET);
    }

    // Return book
    private static void returnBook(Book[] books, String title) {

        for (Book book : books) {

            if (book.getBookTitle().equalsIgnoreCase(title)) {

                book.returnBook();
                return;
            }
        }

        System.out.println(Color.RED + "\n ❌ Book not found!" + Color.RESET);
    }
}