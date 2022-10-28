package handlers;

import constants.Constants;
import model.Priorities;
import model.TodoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class TodoManager {
    private HashMap<String, TodoItem> map;
    private final FileHandler fileHandler;

    /**
     * Constructor is used to define needed resources|objects fileHandler and todoMap
     */
    public TodoManager() {
        fileHandler = new FileHandler();
        map = new HashMap<>();
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
     * @throws IOException handles file's operations
     */
    public boolean insertTodo(TodoItem item) throws IOException {
        //putIfAbsent -> added the item to the map iff the key if absent.
        //returns null if the key is absent otherwise returns the associated value (old)
        //and prevent the addition
        boolean isInserted = map.putIfAbsent(item.getTitle(), item) == null;
        //write to file
        fileHandler.writeToFile(map, Constants.todoFilePath);
        return isInserted; //item is added successfully
    }

    /**
     * Function is used to update item's data
     *
     * @param title item's old title is used to update it
     * @param item  new item's data to be replaced instead of old item's
     * @return boolean: if item is updated successfully
     * @throws IOException handles file's operations
     */
    public boolean updateTodo(String title, TodoItem item) throws IOException {
        //if map contains this title then update, else return false
        if (map.containsKey(title)) {
            //update item in the map
            map.put(title, new TodoItem(item.getTitle(), item.getDescription(), item.getCategory(), item.getStartDate(), item.getEndDate(), item.getFavorite(), item.getPriority()));
            //write changes to the file
            fileHandler.writeToFile(map, Constants.todoFilePath);
            return true;
        }
        return false; //item is not exist
    }

    /**
     * Function to delete item by its title
     *
     * @param title item's title to be deleted
     * @return boolean if item's deleted successfully
     * @throws IOException handles file's operations
     */
    public boolean deleteTodo(String title) throws IOException {
        //read changes from file
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        //search for required title
        //if exist then delete from the map and write changes into file
        if (map.containsKey(title)) {
            map.remove(title);
            fileHandler.writeToFile(map, Constants.todoFilePath);
            return true;
        }
        //else return false
        return false;
    }

    /**
     * Function is used to return all TodoItem-s
     *
     * @return HashMap<String, TodoItem>
     * @throws IOException handles file's operations
     */
    public HashMap<String, TodoItem> selectAll() throws IOException {
        //update map by reading from the file
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        return map;
    }

    /**
     * Function is used to sort Map<String, TodoItem> by start date
     *
     * @return sorted HashMap
     * @throws IOException handles file's operations
     */
    public HashMap<String, TodoItem> selectNearestByDate() throws IOException {
        //update the map
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        //1. turn map into stream to apply sort.
        //2. added filter to filter only starting dates that are after today's date
        //3. compare|sort map based of start date.
        //4. collect the outcome stream into new HashMap.
        return map.entrySet().stream().filter(t -> t.getValue().getStartDate().isAfter(LocalDate.now())).sorted(Comparator.comparing(o -> o.getValue().getStartDate())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    /**
     * Function is used to fetch TodoItems' with a specified priority
     *
     * @param priority priority to be searched for
     * @return HashMap<String, TodoItem>
     * @throws IOException handles file's operations
     */
    public HashMap<String, TodoItem> searchByPriority(Priorities priority) throws IOException {
        //call parseItems to update the map
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        //filter map by priority
        return map.entrySet().stream().filter(todoItem -> todoItem.getValue().getPriority().equals(priority)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    /**
     * Function is used to fetch all favorite items.
     *
     * @return HashMap<String, TodoItem>
     * @throws IOException handles file's operations
     */
    public HashMap<String, TodoItem> showFavorites() throws IOException {
        //update the map by reading from the file
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        //filter the map using favorite flag
        return map.entrySet().stream().filter(todoItem -> todoItem.getValue().getFavorite()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    /**
     * Function is used to search by specified category
     *
     * @param category category to be searched with
     * @return HashMap<Sting, TodoItem>
     * @throws IOException handles file's operations
     */
    public HashMap<String, TodoItem> searchByCategory(String category) throws IOException {
        //call parseItems to update the map
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        //filter by passed category
        return map.entrySet().stream().filter(todoItem -> todoItem.getValue().getCategory().equalsIgnoreCase(category)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    /**
     * Function is used to search for specified title or subset of it
     *
     * @param title title to be searched with
     * @return HashMap<String, TodoItem>
     * @throws IOException handles file's operations
     */
    public HashMap<String, TodoItem> searchByTitle(String title) throws IOException {
        //call parseItems to update the map
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        if (map.containsKey(title)) {
            //returns only if a title can be found without substring search
            TodoItem item = map.get(title);
            HashMap<String, TodoItem> resultMap = new HashMap<String, TodoItem>();
            resultMap.put(title, item);
            return resultMap;
        }
        //support substring search
        System.out.println("Can't find item with specified title!\nBut here are list of items that contain title as substring");
        return map.entrySet().stream().filter(todoItem -> todoItem.getValue().getTitle().contains(title)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    /**
     * Function is used to toggle item's favorite flag
     *
     * @param title title to toggle its favorite
     * @return boolean if item's favorite has been changed successfully.
     * @throws IOException handles file's operations
     */
    public boolean toggleFavorite(String title) throws IOException {
        //read changes from file
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        HashMap<String, TodoItem> resultMap = searchByTitle(title);
        //condition to check if the search only returns one item -> to prevent toggling favorite for substring's search result
        if (resultMap.size() == 1) {
            TodoItem item = resultMap.get(title);
            //toggle favorite flag
            item.setFavorite(!item.getFavorite());
            //update map
            map.put(title, item);
            //write changes to file
            fileHandler.writeToFile(map, Constants.todoFilePath);
            return true;
        }
        return false; //title can't be found
    }

    /**
     * Function is used to search by item's end date
     *
     * @param date date to be searched with
     * @return HashMap<String, TodoItem>
     * @throws IOException handles file's operations
     */
    public HashMap<String, TodoItem> searchByEndDate(LocalDate date) throws IOException {
        //call parseItems to update the map
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        //filter the map based on end date
        return map.entrySet().stream().filter(todoItem -> todoItem.getValue().getEndDate().equals(date)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    /**
     * Function is used to search by item's start date
     *
     * @param date date to be searched with
     * @return HashMap<String, TodoItem>
     * @throws IOException handles file's operations
     */
    public HashMap<String, TodoItem> searchByStartDate(LocalDate date) throws IOException {
        //update the map
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        //filter the map based on start date
        return map.entrySet().stream().filter(todoItem -> todoItem.getValue().getStartDate().equals(date)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    /**
     * Function is used to update category of specified item
     *
     * @param category new category to be set
     * @param item     item that will be updated
     * @return updated item if found
     * @throws IOException handles file's operations
     */
    public TodoItem updateCategory(String category, TodoItem item) throws IOException {
        //call parseItems to update the map
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(Constants.todoFilePath);
        if (map.containsKey(item.getTitle())) {
            item.setCategory(category);
            map.put(item.getTitle(), item);
            fileHandler.writeToFile(map, Constants.todoFilePath);
            return item;
        }
        return null;
    }
}
