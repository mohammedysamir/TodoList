import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class TodoManager {
    private HashMap<String, TodoItem> map;

    public TodoManager(HashMap<String, TodoItem> map) {
        this.map = map;
    }

    public boolean validateItem(TodoItem item) {
        return item.isFavorite != null && item.endDate != null && item.category != null
                && item.startDate != null && item.priority != null;
    }

    public boolean insertTodo(TodoItem item) {
        if (this.map.containsKey(item.getTitle())) {
            return false;
        } else {
            this.map.put(item.getTitle()
                    , new TodoItem(item.title, item.description, item.category, item.startDate, item.endDate, item.isFavorite, item.priority));
        }
        return true;
    }

    public boolean updateTodo(String title, TodoItem item) {
        if (this.map.containsKey(title)) {
            this.map.put(title, new TodoItem(item.title, item.description, item.category, item.startDate, item.endDate, item.isFavorite, item.priority));
            return true;
        }
        return false;
    }

    public boolean deleteTodo(String title) {
        return this.map.remove(title) != null;
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
            if (set.getValue().priority == Priority) {
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
            if (Objects.equals(set.getValue().category, category)) {
                filteredMap.put(set.getKey(), set.getValue());
            }
        }
        return filteredMap;
    }

    public TodoItem searchByTitle(String title) {
        return this.map.get(title);
    }

    public boolean addToFavorite(String title) {
        if (map.containsKey(title)) {
            TodoItem item = searchByTitle(title);
            item.setFavorite(true);
            map.put(title, item);
        } else {
            return false;
        }
        return true;
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
        if (this.map.containsKey(item.title)) {
            item.setCategory(category);
            return item;
        }
        return null;
    }
}
