import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static FileHandler fileHandler = new FileHandler();
    static CategoryListHandler categoryList;

    public static void main(String[] args) throws IOException {
        final String filePath = "todos";
        String userAnswer = "";
        TodoManager manager = new TodoManager((HashMap<String, TodoItem>) fileHandler.parseItems(filePath));
        categoryList = new CategoryListHandler();
        String title;
        Scanner scan = new Scanner(System.in);
        do {
            displayMenu();
            int choice = scan.nextInt();
            switch (choice) {
                case 1: //insert new item
                    manager.insertTodo(parseUserInputs(scan));
                    break;
                case 2: //update existing item
                    System.out.print("Enter todo item's title to be updated: ");
                    title = scan.nextLine();
                    scan.nextLine();
                    manager.updateTodo(manager.searchByTitle(title));
                    break;
                case 3: //delete existing item
                    System.out.print("Enter todo item's title to be deleted: ");
                    title = scan.nextLine();
                    scan.nextLine();
                    manager.deleteTodo(title);
                    break;
                case 4: //print all todos from TaskManager's map
                    System.out.println("**Fetching all items**");
                    printCollection(manager.selectAll());
                    break;
                case 5: //nearest by end date
                    //todo: call selectNearest() and pass it as argument to printCollection()
                    break;
                case 6: //search by title
                    System.out.print("Enter todo item's title to search for: ");
                    title = scan.nextLine();
                    System.out.println();
                    scan.nextLine();
                    System.out.println(manager.searchByTitle(title).toString());
                    break;
                case 7: //search by start date
                    System.out.print("Enter start date with format 'yyyy-mm-dd': ");
                    String dateStr = scan.nextLine();
                    System.out.println();
                    LocalDate date = LocalDate.parse(dateStr);
                    printCollection(manager.searchByStartDate(date));
                    break;
                case 8: //search by end date
                    System.out.print("Enter end date with format 'yyyy-mm-dd': ");
                    dateStr = scan.nextLine();
                    System.out.println();
                    date = LocalDate.parse(dateStr);
                    printCollection(manager.searchByEndDate(date));
                    break;
                case 9: //search by priority
                    System.out.println("Select priority level to search for \n1:LOW\n2:MEDIUM\n3:HIGH");
                    int priorityLevel = scan.nextInt();
                    HashMap<String, TodoItem> resultMap = new HashMap<>();
                    switch (priorityLevel) {
                        case 1:
                            System.out.println("**Showing items of low priority**");
                            resultMap = manager.searchByPriority(Priorities.LOW);
                            break;
                        case 2:
                            System.out.println("**Showing items of medium priority**");
                            resultMap = manager.searchByPriority(Priorities.MEDIUM);
                            break;
                        case 3:
                            System.out.println("**Showing items of high priority**");
                            resultMap = manager.searchByPriority(Priorities.HIGH);
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
                    manager.updateCategory(category, manager.searchByTitle(title));
                    break;
                case 11: //add to favorite
                    System.out.print("Enter item's title to move it to favorite: ");
                    title = scan.nextLine();
                    System.out.println("");
                    manager.addToFavorite(title);
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
        scan.close();
        fileHandler.writeToFile(manager.selectAll(), filePath); //write final map to file
        System.out.println("Thanks for using our application");
    }

    //util functions
    public static void displayMenu() {
        System.out.print(
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
                        "\n***********************************************" +
                        "\nchoice: ");
    }

    public static TodoItem parseUserInputs(Scanner scan) throws IOException {
        //simple version of input taking
        scan.nextLine();
        System.out.print("Enter todo title: ");
        String title = scan.nextLine().trim();
        System.out.print("\nEnter todo description: ");
        String description = scan.nextLine().trim();
        categoryList.viewCategories();
        System.out.print("\nEnter todo Category number, or enter new category name: ");
        String category = "";
        if (scan.hasNextInt()) //check if next input is an integer
        {
            int categoryIndex = Integer.parseInt(scan.nextLine());
            //check if index is out of bound
            while (categoryIndex > categoryList.categories.size()) {
                System.out.println("Invalid category selected, re-enter category number");
                categoryIndex = Integer.parseInt(scan.nextLine());
            }
            category = categoryList.getCategoryAtIndex(categoryIndex - 1); //position-index different
            scan.nextLine();
        } else {
            //entered category name
            //search if exists
            String tempCategory = scan.nextLine().trim();
            if (!categoryList.isCategoryFound(tempCategory))//new category to be added
            {
                categoryList.addCategory(tempCategory);
                categoryList.writeCategoriesToFile(); //write to file
            }
            category = tempCategory;
        }

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