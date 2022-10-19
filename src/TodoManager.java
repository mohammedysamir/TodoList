import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TodoManager {
    private final HashMap<String, TodoItem> map;
    public TodoManager(HashMap<String, TodoItem> map){
        this.map=map;
    }
    public boolean validateItem(TodoItem item){
            if(item.isFavorite instanceof Boolean ||
                    item.endDate instanceof LocalDate ||
                    item.category  instanceof String ||
                    item.startDate  instanceof LocalDate ||
                    item.priority  instanceof Priorities){
                return item.isFavorite != null && item.endDate != null && item.category != null
                        && item.startDate !=null && item.priority!=null;
            }else {
                return false;
            }
    }
    public boolean insertTodo(TodoItem item){
        if(this.map.containsKey("title")){
            return false;
        }else {
            this.map.put(item.getTitle()
                    , new TodoItem(item.title, item.description,item.category, item.startDate,item.endDate,item.isFavorite,item.priority));
        }
        return true;
    }
    public boolean updateTodo(TodoItem item){
        if(this.map.containsKey(item.getTitle())){
            this.map.put("title", new TodoItem(item.title, item.description,item.category, item.startDate,item.endDate,item.isFavorite,item.priority));
            return true;
        }
        return false;
    }
    public boolean deleteTodo(String title){
        return this.map.remove(title) != null;
    }
    public HashMap<String, TodoItem> selectAll(){
        return this.map;
    }
    public String[] selectNearestbyDate(){
        String[] result = new String[5];
        HashMap<String, Long> tempMap = new HashMap<>();
        LocalDate date = LocalDate.now();
        for (Map.Entry<String, TodoItem> set : this.map.entrySet()) {
                tempMap.put(set.getKey(), date.toEpochDay() - set.getValue().startDate.toEpochDay());
        }
        int i=0;
            for (Map.Entry<String, Long> set : tempMap.entrySet()) {
                Long min = Collections.min(tempMap.values());
                if(i==5)break;
                if(set.getValue()==min){
                    result[i]=set.getKey();
                    tempMap.remove(set.getKey());
                    i++;
                }
            }
        return result;
    }

    public HashMap<String, TodoItem> searchByPriority(Priorities Priority){
        HashMap<String, TodoItem> filteredMap = new HashMap<>();
        for (Map.Entry<String, TodoItem> set : this.map.entrySet()) {
            if(set.getValue().priority == Priority){
                filteredMap.put(set.getKey(), set.getValue());
            }
        }
        return filteredMap;
    }
    public HashMap<String, TodoItem> searchByCategory(String category){
        HashMap<String, TodoItem> filteredMap = new HashMap<>();
        for (Map.Entry<String, TodoItem> set : this.map.entrySet()) {
            if(Objects.equals(set.getValue().category, category)){
                filteredMap.put(set.getKey(), set.getValue());
            }
        }
        return filteredMap;
    }
    public TodoItem  searchByTitle(String title){
        return this.map.get(title);
    }
    public boolean addToFavorite(String title){
        if(map.containsKey(title)) {
            TodoItem item = searchByTitle(title);
            item.setFavorite(true);
            map.put(title,item);
        }else{
            return false;
        }
        return true;
    }

}
