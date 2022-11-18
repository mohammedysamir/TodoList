package database;

import model.Priorities;
import model.TodoItem;
import util.Utilities;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class MySqlDatabase implements DatabaseHandler {
    String databaseName;
    String userName;
    String password;

    static Statement statement;

    static Connection connection;

    public MySqlDatabase(String databaseName, String userName, String password) {
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public void openConnection() {
        String connectionString = String.format("jdbc:mysql://localhost:3306/%s", databaseName);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString, userName, password);//added username and password for DB
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void closeConnection() {

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertTodoItem(TodoItem item) {
        //title, description, category,start_date,end_date,priority.toString,is_favorite (bit)
        String query = "INSERT INTO todo_item VALUES(?,?,?,?,?,?,?);";
        int rowAffected;
        try {
            //prepare mysql to insert
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, item.getTitle());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setString(3, item.getCategory());
            preparedStatement.setDate(4, Date.valueOf(item.getStartDate()));
            preparedStatement.setDate(5, Date.valueOf(item.getEndDate()));
            preparedStatement.setString(6, item.getPriority().toString());
            preparedStatement.setBoolean(7, item.getFavorite());
            //execute
            rowAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowAffected > 0; //return true if rowAffected > 0 -> inserted
    }

    @Override
    public boolean updateTodoItem(String title, TodoItem item) {
        int rowAffected;
        String updateQuery = "UPDATE todo_item SET title = ?, description = ?," +
                "category = ?, start_date = ?, end_date = ?, priority = ?," +
                "is_favorite = ? WHERE title = ? ;"; //update data for the given title -old-
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            //updated data -> Set Clause
            preparedStatement.setString(1, item.getTitle());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setString(3, item.getCategory());
            preparedStatement.setDate(4, Date.valueOf(item.getStartDate()));
            preparedStatement.setDate(5, Date.valueOf(item.getEndDate()));
            preparedStatement.setString(6, item.getPriority().toString());
            preparedStatement.setBoolean(7, item.getFavorite());
            //Where Clause
            preparedStatement.setString(8, title);
            rowAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowAffected > 0;
    }

    @Override
    public boolean deleteTodoItem(String title) {
        int rowAffected = 0;
        String query = "DELETE FROM todo_item WHERE title=?; ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //updated data -> Set Clause
            preparedStatement.setString(1, title);
            rowAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowAffected > 0;
    }

    @Override
    public HashMap<String, TodoItem> getAllItems() {
        HashMap<String, TodoItem> map = new HashMap<>();
        String query = "select * FROM todo_item; ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                TodoItem item = parseRowToTodo(result);
                map.put(item.getTitle(), item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public HashMap<String, TodoItem> searchByTitle(String title) {
        HashMap<String, TodoItem> map = new HashMap<>();
        String query = "select * FROM todo_item where title =?; ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, title);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                TodoItem item = parseRowToTodo(result);
                map.put(item.getTitle(), item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public HashMap<String, TodoItem> searchByNearestDate() {
        //select * and order by start_date -> Limit 5
        String query = "select * from (select * from todo_item order by  start_date asc) todo_item limit 5";
        HashMap<String, TodoItem> items = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                TodoItem item = parseRowToTodo(result);
                items.put(item.getTitle(), item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public HashMap<String, TodoItem> showFavorites() {
        String query = "Select * from todo_item where is_favorite = 1;";
        HashMap<String, TodoItem> items = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                TodoItem item = parseRowToTodo(result);
                items.put(item.getTitle(), item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;

    }

    @Override
    public HashMap<String, TodoItem> searchByPriority(String priority) {
        HashMap<String, TodoItem> items = new HashMap<>();
        String query = "select * FROM todo_item where priority =?; ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, priority);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                TodoItem item = parseRowToTodo(result);
                items.put(item.getTitle(), item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public HashMap<String, TodoItem> searchByCategory(String category) {
        HashMap<String, TodoItem> items = new HashMap<>();
        String query = "select * FROM todo_item where category =?; ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                TodoItem item = parseRowToTodo(result);
                items.put(item.getTitle(), item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public HashMap<String, TodoItem> searchByStartDate(LocalDate date) {
        HashMap<String, TodoItem> items = new HashMap<>();
        String query = "select * FROM todo_item where start_date =?; ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                TodoItem item = parseRowToTodo(result);
                items.put(item.getTitle(), item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public HashMap<String, TodoItem> searchByEndDate(LocalDate date) {
        HashMap<String, TodoItem> items = new HashMap<>();
        String query = "select * FROM todo_item where end_date =?; ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                TodoItem item = parseRowToTodo(result);
                items.put(item.getTitle(), item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public boolean toggleFavorite(String title) {
        String query = "update todo_item set is_favorite = ? where title= ? ;";
        int rowAffected = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, !searchByTitle(title).get(title).getFavorite());
            preparedStatement.setString(2, title);
            rowAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rowAffected > 0;
    }

    @Override
    public TodoItem updateCategory(String category, TodoItem item) {
        String query = "update todo_item set category = ? where title= ? ;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category);
            preparedStatement.setString(2, item.getTitle());
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                item.setCategory(category);
                System.out.println("Category is updated successfully!");
            } else {
                System.out.println("Category can't be updated!");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    @Override
    public ArrayList<String> showCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                String categoryName = result.getString(2);
                categories.add(categoryName);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public void addCategory(String category) {
        String query = "INSERT INTO categories (category_name) VALUES(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category);
            if (preparedStatement.executeUpdate() > 0)
                System.out.println("New category has been inserted!");
            else
                System.err.println("Error: Couldn't insert new category");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isCategoryFound(String category) {
        String query = "SELECT * FROM categories WHERE category_name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) return true; //result returned with data

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private TodoItem parseRowToTodo(ResultSet result) {
        String title, description, category;
        LocalDate start_date, end_date;
        Priorities priority;
        boolean is_favorite;
        try {
            title = result.getString(1);
            description = result.getString(2);
            category = result.getString(3);
            start_date = result.getDate(4).toLocalDate();
            end_date = result.getDate(5).toLocalDate();
            priority = Utilities.mapStringToPriority(result.getString(6));
            is_favorite = result.getBoolean(7);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new TodoItem(title, description, category, start_date, end_date, is_favorite, priority);
    }
}
