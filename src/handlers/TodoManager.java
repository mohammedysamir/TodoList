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
    private FileHandler fileHandler;

    public TodoManager(HashMap<String, TodoItem> map) {
        fileHandler = new FileHandler();
        this.map = map;
    }

    public boolean validateItem(TodoItem item) {
        return item.getFavorite() != null && item.getEndDate() != null && item.getCategory() != null
                && item.getStartDate() != null && item.getPriority() != null;
    }

    //todo: update files after each operation
    //todo: document the code
    public boolean insertTodo(TodoItem item) throws IOException {
        if (this.map.containsKey(item.getTitle())) {
            return false;
        } else {
            this.map.put(item.getTitle()
                    , new TodoItem(item.getTitle(), item.getDescription(), item.getCategory(), item.getStartDate(), item.getEndDate(), item.getFavorite(), item.getPriority()));
        }
        //write to file
        fileHandler.writeToFile(map, Constants.todoFilePath);
        return true;
    }

    public boolean updateTodo(String title, TodoItem item) throws IOException {
        if (this.map.containsKey(title)) {
            this.map.put(title, new TodoItem(item.getTitle(), item.getDescription(), item.getCategory(), item.getStartDate(), item.getEndDate(), item.getFavorite(), item.getPriority()));
            fileHandler.writeToFile(map, Constants.todoFilePath);
            return true;
        }
        return false;
    }

    public boolean deleteTodo(String title) throws IOException {
        //read changes from file
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(fileHandler.readFromFile(Constants.todoFilePath).toString());
        //search for required title
        //if exist then update map and re-write it
        if (map.containsKey(title)) {
            map.remove(title);
            fileHandler.writeToFile(map, Constants.todoFilePath);
            return true;
        }
        //else return false
        return false;
    }

    public HashMap<String, TodoItem> selectAll() {
        return this.map;
    }

    public HashMap<String, TodoItem> selectNearestByDate() {
        //1. turn map into stream to apply sort.
        //2. compare|sort map based of start date.
        //3. collect the outcome stream into new HashMap.
        return map.entrySet().stream().sorted(Comparator.comparing(o -> o.getValue().getStartDate())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    public HashMap<String, TodoItem> searchByPriority(Priorities Priority) {
        HashMap<String, TodoItem> filteredMap = new HashMap<>();
        for (Map.Entry<String, TodoItem> set : this.map.entrySet()) {
            if (set.getValue().getPriority() == Priority) {
                filteredMap.put(set.getKey(), set.getValue());
            }
        }
        return filteredMap;
    }

    public HashMap<String, TodoItem> showFavorites() {
        return map.entrySet().stream().filter(todoItem -> todoItem.getValue().getFavorite()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    public HashMap<String, TodoItem> searchByCategory(String category) {
        HashMap<String, TodoItem> filteredMap = new HashMap<>();
        for (Map.Entry<String, TodoItem> set : this.map.entrySet()) {
            if (Objects.equals(set.getValue().getCategory(), category)) {
                filteredMap.put(set.getKey(), set.getValue());
            }
        }
        return filteredMap;
    }

    public HashMap<String, TodoItem> searchByTitle(String title) {
        if (map.containsKey(title)) {
            TodoItem item = map.get(title);
            new HashMap<String, TodoItem>().put(title, item);
        }
        System.out.println("Can't find item with specified title!\nBut here are list of items that contain title as substring");
        return map.entrySet().stream().filter(todoItem -> todoItem.getValue().getTitle().contains(title)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    public boolean toggleFavorite(String title) throws IOException {
        //read changes from file
        map = (HashMap<String, TodoItem>) fileHandler.parseItems(fileHandler.readFromFile(Constants.todoFilePath).toString());
        if (searchByTitle(title).size() == 1) {
            TodoItem item = searchByTitle(title).get(title);
            item.setFavorite(!item.getFavorite());
            map.put(title, item);
            //write changes to file
            fileHandler.writeToFile(map, Constants.todoFilePath);
            return true;
        }
        return false;
    }

    public HashMap<String, TodoItem> searchByEndDate(LocalDate date) {
        HashMap<String, TodoItem> filteredMap = new HashMap<>();
        for (TodoItem item : this.map.values()) {
            if (Objects.equals(item.getEndDate(), date)) {
                filteredMap.put(item.getTitle(), item);
            }
        }
        return filteredMap;
    }

    public HashMap<String, TodoItem> searchByStartDate(LocalDate date) {
        HashMap<String, TodoItem> filteredMap = new HashMap<>();
        for (TodoItem item : this.map.values()) {
            if (Objects.equals(item.getStartDate(), date)) {
                filteredMap.put(item.getTitle(), item);
            }
        }
        return filteredMap;
    }

    public TodoItem updateCategory(String category, TodoItem item) {
        if (this.map.containsKey(item.getTitle())) {
            item.setCategory(category);
            return item;
        }
        return null;
    }
}
