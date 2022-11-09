package handlers;


import database.DatabaseHandler;
import model.Priorities;
import model.TodoItem;

import java.time.LocalDate;
import java.util.*;


public class TodoManager {
    DatabaseHandler databaseHandler;

    /**
     * Destructor to close database connection
     */
    @Override
    protected void finalize() {
        databaseHandler.closeConnection();
    }

    /**
     * Constructor is used to define needed resources|objects fileHandler and todoMap
     */
    public TodoManager(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
        this.databaseHandler.openConnection();
    }

    /**
     * Function to validate TodoItem's attributes are not null
     *
     * @param item item to be validated
     * @return boolean 'isValid'
     */
    public boolean validateItem(TodoItem item) {
        return item.getFavorite() != null && item.getEndDate() != null && item.getCategory() != null
                && item.getStartDate() != null && item.getPriority() != null;
    }

    /**
     * Function to insert new TodoItem to list and write changes to 'todos.txt' file
     *
     * @param item item to be written and inserted
     * @return boolean if item is inserted successfully.
     */
    public boolean insertTodo(TodoItem item) {

        return databaseHandler.insertTodoItem(item);
    }

    /**
     * Function is used to update item's data
     *
     * @param title item's old title is used to update it
     * @param item  new item's data to be replaced instead of old item's
     * @return boolean: if item is updated successfully
     */
    public boolean updateTodo(String title, TodoItem item) {
        return databaseHandler.updateTodoItem(title, item);
    }

    /**
     * Function to delete item by its title
     *
     * @param title item's title to be deleted
     * @return boolean if item's deleted successfully
     */
    public boolean deleteTodo(String title) {
        return databaseHandler.deleteTodoItem(title);
    }

    /**
     * Function is used to return all TodoItem-s
     *
     * @return HashMap<String, TodoItem>
     */
    public HashMap<String, TodoItem> selectAll() {
        return databaseHandler.getAllItems();
    }

    /**
     * Function is used to sort Map<String, TodoItem> by start date
     *
     * @return sorted HashMap
     */
    public HashMap<String, TodoItem> selectNearestByDate() {
        return databaseHandler.searchByNearestDate();
    }

    /**
     * Function is used to fetch TodoItems' with a specified priority
     *
     * @param priority priority to be searched for
     * @return HashMap<String, TodoItem>
     */
    public HashMap<String, TodoItem> searchByPriority(Priorities priority) {
        return databaseHandler.searchByPriority(priority.toString());
    }

    /**
     * Function is used to fetch all favorite items.
     *
     * @return HashMap<String, TodoItem>
     */
    public HashMap<String, TodoItem> showFavorites() {
        return databaseHandler.showFavorites();
    }

    /**
     * Function is used to search by specified category
     *
     * @param category category to be searched with
     * @return HashMap<Sting, TodoItem>
     */
    public HashMap<String, TodoItem> searchByCategory(String category) {
        return databaseHandler.searchByCategory(category);
    }

    /**
     * Function is used to search for specified title or subset of it
     *
     * @param title title to be searched with
     * @return HashMap<String, TodoItem>
     */
    public HashMap<String, TodoItem> searchByTitle(String title) {
        return databaseHandler.searchByTitle(title);
    }

    /**
     * Function is used to toggle item's favorite flag
     *
     * @param title title to toggle its favorite
     * @return boolean if item's favorite has been changed successfully.
     */
    public boolean toggleFavorite(String title) {
        return databaseHandler.toggleFavorite(title);
    }

    /**
     * Function is used to search by item's end date
     *
     * @param date date to be searched with
     * @return HashMap<String, TodoItem>
     */
    public HashMap<String, TodoItem> searchByEndDate(LocalDate date) {
        return databaseHandler.searchByEndDate(date);
    }

    /**
     * Function is used to search by item's start date
     *
     * @param date date to be searched with
     * @return HashMap<String, TodoItem>
     */
    public HashMap<String, TodoItem> searchByStartDate(LocalDate date) {
        return databaseHandler.searchByStartDate(date);
    }

    /**
     * Function is used to update category of specified item
     *
     * @param category new category to be set
     * @param item     item that will be updated
     * @return updated item if found
     */
    public TodoItem updateCategory(String category, TodoItem item) {
        return databaseHandler.updateCategory(category, item);
    }
}
