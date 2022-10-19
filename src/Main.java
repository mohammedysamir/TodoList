import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    FileHandler fileHandler = new FileHandler();
    static CategoryListHandler categoryList;
    public static void main(String[] args) throws FileNotFoundException {
        final String filePath = "todos";
        String userAnswer = "";
       categoryList= new CategoryListHandler();
        String title;
        Scanner scan = new Scanner(System.in);
        do {
            displayMenu();
            int choice = scan.nextInt();
            switch (choice) {
                case 1: //insert new item
                    parseUserInputs(scan);
                    //todo: call Manager's function to insert item into Map
                    break;
                case 2: //update existing item
                    System.out.print("Enter todo item's title to be updated: ");
                    title = scan.nextLine();
                    scan.nextLine();
                    //todo: call TaskManager's update function
                    break;
                case 3: //delete existing item
                    System.out.print("Enter todo item's title to be deleted: ");
                    title = scan.nextLine();
                    scan.nextLine();
                    //todo: call TaskManager's delete function
                    break;
                case 4: //print all todos from TaskManager's map
                    System.out.println("**Fetching all items**");
                    //todo: call selectAll() and pass it as argument to printCollection()
                    break;
                case 5: //nearest by end date
                    //todo: call selectNearest() and pass it as argument to printCollection()
                    break;
                case 6: //search by title
                    System.out.print("Enter todo item's title to search for: ");
                    title = scan.nextLine();
                    System.out.println();
                    scan.nextLine();
                    //todo: call searchByTitle and call toString to outcome
                    break;
                case 7: //search by start date
                    System.out.print("Enter start date with format 'yyyy-mm-dd': ");
                    String dateStr = scan.nextLine();
                    System.out.println();
                    LocalDate date = LocalDate.parse(dateStr);
                    //todo: call function to fetch by start date and call printCollection on outcome
                    break;
                case 8: //search by end date
                    System.out.print("Enter end date with format 'yyyy-mm-dd': ");
                    dateStr = scan.nextLine();
                    System.out.println();
                    date = LocalDate.parse(dateStr);
                    //todo: call function to fetch by end date and call printCollection on outcome
                    break;
                case 9: //search by priority
                    System.out.println("Select priority level to search for \n1:LOW\n2:MEDIUM\n3:HIGH");
                    int priorityLevel = scan.nextInt();
                    HashMap<String, TodoItem> resultMap = new HashMap<>();
                    switch (priorityLevel) {
                        case 1:
                            System.out.println("**Showing items of low priority**");
                            //todo: call function to search for low priority
                            break;
                        case 2:
                            System.out.println("**Showing items of medium priority**");
                            //todo: call function to search for medium priority
                            break;
                        case 3:
                            System.out.println("**Showing items of high priority**");
                            //todo: call function to search for high priority
                            break;
                    }
                    printCollection(resultMap);
                    break;
                case 10: //add to category
                    System.out.print("Enter todo title to update its category: ");
                    title = scan.nextLine();
                    System.out.print("\nEnter todo category: ");
                    String category = scan.nextLine();
                    System.out.println("");
                    //todo: create function to update todo category by title and call it
                    break;
                case 11: //add to favorite
                    System.out.print("Enter item's title to move it to favorite: ");
                    title = scan.nextLine();
                    System.out.println("");
                    //todo: call function on given title to toggle its favorite boolean
                    break;
                default:
                    System.out.println("Invalid choice entered");
                    break;
            }
            System.out.print("Do you wish to continue?: ");
            //take user input to terminate the app
            userAnswer = scan.nextLine();
            scan.nextLine();
        } while (userAnswer.equalsIgnoreCase("Y") ||
                userAnswer.equalsIgnoreCase("Yes"));
        //todo: don't forget to save map to file
        scan.close();
        System.out.println("Thanks for using our application");
    }

    //util functions
    public static void displayMenu() {
        System.out.println(
                "***********************************************" +
                        "\nSelect number of request:" +
                        "\n1. Add Todo item." +
                        "\n2. Update Todo item." +
                        "\n3. Delete Todo item." +
                        "\n4. Show all Todo items." +
                        "\n5. Show 5 nearest items by date." +
                        "\n6. Search by Title." +
                        "\n7. Search by Start date." +
                        "\n8. Search by End date." +
                        "\n9. Search by item's priority." +
                        "\n10. Add Todo item to Category." +
                        "\n11. Add Todo item to Favorite." +
                        "\n***********************************************");
    }

    public static TodoItem parseUserInputs(Scanner scan) {
        //simple version of input taking
        scan.nextLine();
        System.out.print("Enter todo title: ");
        String title = scan.nextLine().trim();
        System.out.print("\nEnter todo description: ");
        String description = scan.nextLine().trim();
        categoryList.viewCategories();
        System.out.print("\nEnter todo Category number: ");
        int categoryIndex = scan.nextInt();
        String category = categoryList.getCategoryAtIndex(categoryIndex);
        System.out.print("\nEnter todo Start date with format 'yyyy-mm-dd': ");
        LocalDate startDate = LocalDate.parse(scan.nextLine().trim());
        System.out.print("\nEnter todo End date with format 'yyyy-mm-dd': ");
        LocalDate endDate = LocalDate.parse(scan.nextLine().trim());
        System.out.print("\nis this todo Favorite? (Y/N): ");
        Boolean isFavorite = scan.nextLine().trim().equalsIgnoreCase("Y");
        System.out.print("\nEnter todo priority:- 1 = HIGH, 2 = MEDIUM, 3 = LOW ");
        Priorities priority = Priorities.LOW;
        switch (scan.nextInt()) {
            case 1:
                priority = Priorities.HIGH;
                break;
            case 2:
                priority = Priorities.MEDIUM;
                break;
            case 3:
                break;
        }
        //create obj from inputs
        return new TodoItem(title, description, category, startDate, endDate, isFavorite, priority);
    }

    public static void printCollection(HashMap<String, TodoItem> map) {
        for (TodoItem item : map.values())
            System.out.println(item.toString());
    }
}
/*
    Store and operate on Items: HashMap<Title, item>
    Store locally -> files.
    Fetch and operate on items using the Map.
    Before closing the application, save the map to file.
    Save items as Json
    {
        first, 'title'
        this is first, 'description'
    }
* */