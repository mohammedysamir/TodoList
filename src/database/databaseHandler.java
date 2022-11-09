package database;

import model.TodoItem;

import java.time.LocalDate;
import java.util.HashMap;

//Supports strategy pattern
public interface databaseHandler {
    void openConnection();

    void closeConnection();

    boolean insertTodoItem(TodoItem item);

    boolean updateTodoItem(String title, TodoItem item);

    boolean deleteTodoItem(TodoItem item);

    HashMap<String, TodoItem> getAllItems();

    HashMap<String, TodoItem> searchByTitle(String title);

    HashMap<String, TodoItem> searchByNearestDate(); //order by start date and use LIMIT to 5

    HashMap<String, TodoItem> showFavorites();

    HashMap<String, TodoItem> searchByPriority(String priority);

    HashMap<String, TodoItem> searchByCategory(String category);

    HashMap<String, TodoItem> searchByStartDate(LocalDate date);

    HashMap<String, TodoItem> searchByEndDate(LocalDate date);

    void ToggleFavorite();

    TodoItem updateCategory(String category, TodoItem item);
}
