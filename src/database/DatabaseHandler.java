package database;

import model.TodoItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

//Supports strategy pattern
public interface DatabaseHandler {
    void openConnection();

    void closeConnection();

    boolean insertTodoItem(TodoItem item);

    boolean updateTodoItem(String title, TodoItem item);

    boolean deleteTodoItem(String title);

    HashMap<String, TodoItem> getAllItems();

    HashMap<String, TodoItem> searchByTitle(String title);

    HashMap<String, TodoItem> searchByNearestDate(); //order by start date and use LIMIT to 5

    HashMap<String, TodoItem> showFavorites();

    HashMap<String, TodoItem> searchByPriority(String priority);

    HashMap<String, TodoItem> searchByCategory(String category);

    HashMap<String, TodoItem> searchByStartDate(LocalDate date);

    HashMap<String, TodoItem> searchByEndDate(LocalDate date);

    boolean toggleFavorite(String title);

    TodoItem updateCategory(String category, TodoItem item);

    ArrayList<String> showCategories();

    void addCategory(String category);

    boolean isCategoryFound(String category);

}
