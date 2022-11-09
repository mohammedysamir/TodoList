import database.DatabaseHandler;
import database.MySqlDatabase;
import handlers.CategoryListHandler;
import handlers.TodoManager;
import util.Utilities;
import model.Priorities;
import model.TodoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static CategoryListHandler categoryList;

    public static void main(String[] args) throws IOException {
        String userAnswer = "";
        DatabaseHandler database = new MySqlDatabase("todo_list", "root", "P@ssw0rd"); //DB Object
        TodoManager manager = new TodoManager(database);
        categoryList = new CategoryListHandler(database);
        String title;
        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        do {
            Utilities.displayMenu();
            int choice = scan.nextInt();
            switch (choice) {
                case 1: //insert new item
                    if (manager.insertTodo(Utilities.parseUserInputs(scan,categoryList)))
                        System.out.println("Todo item has been inserted successfully!");
                    else
                        System.out.println("Todo item had been inserted before, operation rejected!\n**try to update existing todo**");
                    break;
                case 2: //update existing item
                    System.out.print("Enter todo item's title to be updated: ");
                    scan.nextLine();
                    title = scan.nextLine();
                    TodoItem updatedItem = Utilities.parseUserInputs(scan, categoryList);
                    if (manager.updateTodo(title, updatedItem))
                        System.out.println("Todo item has been updated successfully!");
                    else
                        System.out.println("Todo item isn't exist, operation rejected!\n**try to insert it instead**");
                    break;
                case 3: //delete existing item
                    System.out.print("Enter todo item's title to be deleted: ");
                    scan.nextLine();
                    title = scan.nextLine();
                    if (manager.deleteTodo(title))
                        System.out.println("Todo item has been deleted successfully!");
                    else
                        System.out.println("There's no todo item with specified title, operation rejected!");
                    break;
                case 4: //print all todos from TaskManager's map
                    System.out.println("**Fetching all items**");
                    HashMap<String, TodoItem> allTodos = manager.selectAll();
                    Utilities.printCollection(allTodos, allTodos.size());
                    break;
                case 5: //nearest by start date
                    System.out.println("**Fetching items based on Nearest date**");
                    HashMap<String, TodoItem> nearestMap = manager.selectNearestByDate();
                    Utilities.printCollection(nearestMap, 5);
                    break;
                case 6: //search by title
                    System.out.print("Enter todo item's title to search for: ");
                    scan.nextLine();
                    title = scan.nextLine();
                    System.out.println();
                    HashMap<String, TodoItem> titlesMap = manager.searchByTitle(title);
                    Utilities.printCollection(titlesMap, titlesMap.size());
                    break;
                case 7: //search by start date
                    System.out.print("Enter start date with format 'yyyy-mm-dd': ");
                    scan.nextLine();
                    String dateStr = scan.nextLine();
                    //validate date length (full digits)
                    while (dateStr.length() < 10) {
                        System.out.println("You have entered the date in a wrong format!");
                        System.out.print("Please enter in the following format yyyy-mm-dd, enter day and month with 2 digits each: ");
                        dateStr = scan.nextLine();
                    }
                    System.out.println();
                    LocalDate date = LocalDate.parse(dateStr);
                    HashMap<String, TodoItem> startDateMap = manager.searchByStartDate(date);
                    Utilities.printCollection(startDateMap, startDateMap.size());
                    break;
                case 8: //search by end date
                    System.out.print("Enter end date with format 'yyyy-mm-dd': ");
                    scan.nextLine();
                    dateStr = scan.nextLine();
                    while (dateStr.length() < 10) {
                        System.out.println("You have entered the date in a wrong format!");
                        System.out.print("Please enter in the following format yyyy-mm-dd, enter day and month with 2 digits each: ");
                        dateStr = scan.nextLine();
                    }
                    System.out.println();
                    date = LocalDate.parse(dateStr);
                    HashMap<String, TodoItem> endDateMap = manager.searchByEndDate(date);
                    Utilities.printCollection(endDateMap, endDateMap.size());
                    break;
                case 9: //search by priority
                    System.out.println("Select priority level to search for \n1:LOW\n2:MEDIUM\n3:HIGH");
                    int priorityLevel = scan.nextInt();
                    HashMap<String, TodoItem> resultMap = new HashMap<>();
                    switch (priorityLevel) {
                        case 1:
                            System.out.println("**Showing items of low priority**");
                            resultMap = manager.searchByPriority(Priorities.LOW);//should we take it as Enum or String
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
                    Utilities.printCollection(resultMap, resultMap.size());
                    break;
                case 10: //add to category
                    scan.nextLine();
                    System.out.print("Enter todo title to update its category: ");
                    title = scan.nextLine();
                    String category = Utilities.categoryHandling(scan, categoryList);
                    System.out.println();
                    TodoItem updatedCategoryItem = manager.updateCategory(category, manager.searchByTitle(title).get(title));
                    if (updatedCategoryItem == null) {
                        System.out.println("No item found with this title");
                        break;
                    }
                    System.out.println("**update todo**\n" + updatedCategoryItem.stringEquivalent());
                    break;
                case 11:
                    System.out.println("Fetching Favorite todo items");
                    HashMap<String, TodoItem> favorites = manager.showFavorites();
                    Utilities.printCollection(favorites, favorites.size());
                    break;
                case 12: //add to favorite what about this one
                    scan.nextLine();
                    System.out.print("Enter item's title to move it to favorite: ");
                    title = scan.nextLine();
                    System.out.println();
                    if (manager.toggleFavorite(title))
                        System.out.println("Todo item's **Favorite** has been toggled successfully!");
                    else
                        System.out.println("There's no todo item with specified title, operation rejected!");
                    break;
                case 13:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice entered");
                    break;
            }
            scan.nextLine();
            if (!exit) {
                System.out.print("Do you wish to continue?: ");
                //take user input to terminate the app
                userAnswer = scan.nextLine();
            }
        } while ((userAnswer.equalsIgnoreCase("Y") ||
                userAnswer.equalsIgnoreCase("Yes")) && !exit);
        scan.close();
        System.out.println("Thanks for using our application");
    }
}