package database;

import model.TodoItem;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class MySqlDatabase implements databaseHandler {
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
            connection = DriverManager.getConnection(connectionString);
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
        String query = "INSERT INTO todo_item VALUES(?,?,?,?,?,?,?,?);";
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
        String updateQuery = "UPDATE todo_item SET title = ?, description = ?" +
                "category = ?, start_date = ?, end_date = ?, priority = ?" +
                "is_favorite = ? WHERE title = ? "; //update data for the given title -old-
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
    public boolean deleteTodoItem(TodoItem item) {
        return false;
    }

    @Override
    public HashMap<String, TodoItem> getAllItems() {
        return null;
    }

    @Override
    public HashMap<String, TodoItem> searchByTitle(String title) {
        return null;
    }

    @Override
    public HashMap<String, TodoItem> searchByNearestDate() {
        return null;
    }

    @Override
    public HashMap<String, TodoItem> showFavorites() {
        return null;
    }

    @Override
    public HashMap<String, TodoItem> searchByPriority(String priority) {
        return null;
    }

    @Override
    public HashMap<String, TodoItem> searchByCategory(String category) {
        return null;
    }

    @Override
    public HashMap<String, TodoItem> searchByStartDate(LocalDate date) {
        return null;
    }

    @Override
    public HashMap<String, TodoItem> searchByEndDate(LocalDate date) {
        return null;
    }

    @Override
    public void ToggleFavorite() {

    }

    @Override
    public TodoItem updateCategory(String category, TodoItem item) {
        return null;
    }
}
