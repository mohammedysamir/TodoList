package controller;

import database.DatabaseHandler;
import database.MySqlDatabase;
import handlers.CategoryListHandler;
import handlers.TodoManager;

public class TodoController {
    DatabaseHandler database = new MySqlDatabase("todo_list", "root", "P@ssw0rd"); //DB Object
    TodoManager manager = new TodoManager(database);
    CategoryListHandler categoryList = new CategoryListHandler(database);
    //Get


    //Post


    //Put


    //Delete
}
/*
 * Thinking area:
 *   1. Change controller.TodoController.java to controller.TodoController.
 *   2. TodoManager is a service now, controller.TodoController will delegate work to TodoManager.
 *   3. Methods:
 *    8  3.1. GET: selectAll, selectNearestByDate, searchByPriority, showFavorites, searchByCategory, searchByTitle, searchByEndDate, searchByStartDate
 *    1  3.2. POST: insertTodo,
 *    3  3.3. PUT: updateTodo, toggleFavorite, updateCategory
 *    1  3.4. DELETE: deleteTodo
 *   4. Response:
 *       Response.status(code).entity(object).build;
 *       -> errors: Message enum
 *      Response.status(404).entity("Todo_ not found").build;
 *   ----
 *   13 API endpoint
 *   ----
 *   param: title, endDate, startDate,priority ... -> @PathParam
 *   ex: search by title => /api/todo_/{title} => api/todo_/"first"
 * */