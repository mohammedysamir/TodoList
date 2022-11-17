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

package controller;

import database.DatabaseHandler;
import database.MySqlDatabase;
import handlers.CategoryListHandler;
import handlers.TodoManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.TodoItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Path("/todo")
public class TodoController {
    DatabaseHandler database = new MySqlDatabase("todo_list", "root", "P@ssw0rd"); //DB Object
    TodoManager manager = new TodoManager(database);
    CategoryListHandler categoryList = new CategoryListHandler(database);

    //Get
    @GET
    @Path("/favorites")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showFavorite() {
        return Response.status(200).entity(manager.showFavorites()).build();
    }

    @GET
    @Path("/search/start-date/{date}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response searchByStartDate(@PathParam("date") String startDate) { //use yyyy-mm-dd format
        if (startDate.length() < 10) { //handle
            return Response.status(400).entity("Bad date format, use yyyy-mm-dd").build();
        }
        Response dateValidationResponse = manager.validateDate(startDate);
        if (dateValidationResponse.getStatus() == 200) { //validate that date has valid response
            LocalDate date = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            HashMap<String, TodoItem> todoItemHashMap = manager.searchByStartDate(date);
            if (todoItemHashMap.size() > 0)
                return Response.status(200).entity(todoItemHashMap).build();
            else
                return Response.status(404).entity("Date not found").build();
        }
        return dateValidationResponse;
    }

    @GET
    @Path("/search/end-date/{date}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response searchByEndDate(@PathParam("date") String endDate) { //use yyyy-mm-dd format
        if (endDate.length() < 10) { //handle
            return Response.status(400).entity("Bad date format, use yyyy-mm-dd").build();
        }
        Response dateValidationResponse = manager.validateDate(endDate);
        if (dateValidationResponse.getStatus() == 200) { //validate that date has valid response
            LocalDate date = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            HashMap<String, TodoItem> todoItemHashMap = manager.searchByEndDate(date);
            if (todoItemHashMap.size() > 0)
                return Response.status(200).entity(todoItemHashMap).build();
            else
                return Response.status(404).entity("Date not found").build();
        }
        return dateValidationResponse;
    }

    @GET
    @Path("/search/nearest-date")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByNearestDate() {

        return Response.status(200).entity(manager.selectNearestByDate()).build();

    }

    @GET
    @Path("/search/category/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByCategory(@PathParam("category") String category) {
        HashMap<String,TodoItem> todoItemHashMap = manager.selectNearestByDate();
        if(todoItemHashMap.size() > 0)
            return Response.status(200).entity(manager.searchByCategory(category)).build();
        else
            return Response.status(404).entity("Category is not found").build();
    }

    @GET
    @Path("/search/title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByTitle(@PathParam("title") String title) {

        HashMap<String,TodoItem> todoItemHashMap = manager.selectNearestByDate();
        if(todoItemHashMap.size() > 0)
            return Response.status(200).entity(manager.searchByTitle(title)).build();
        else
            return Response.status(404).entity("Title is not found").build();
    }
    //Post


    //Put
    @PUT
    @Path("/{title}/favorite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toggleFavorite(@PathParam("title") String title) {
        if (manager.toggleFavorite(title)) //toggled successfully
            return Response.status(200).entity("Favorite flag is toggled successfully").build();
        else
            return Response.status(404).entity("Todo item is not found or can't be toggled").build();
    }

    @PUT
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateToDo(@PathParam("title") String title, TodoItem item) {
        if (manager.updateTodo(title, item))
            return Response.status(200).entity("Item is updated successfully").build();
        else
            return Response.status(404).entity("Item is not found to be changed").build();
    }

    //Delete
    @DELETE
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTodo(@PathParam("title") String title) {
        if (manager.deleteTodo(title))
            return Response.status(200).entity("Todo item is deleted successfully!").build();
        else
            return Response.status(404).entity("Todo item is not found or can't be deleted").build();
    }
}
