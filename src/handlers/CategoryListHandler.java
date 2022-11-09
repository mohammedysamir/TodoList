package handlers;

import constants.Constants;
import database.DatabaseHandler;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class CategoryListHandler {
    DatabaseHandler databaseHandler;

    /**
     * Constructor used to define database handler object and read categories from 'categories' table
     */
    public CategoryListHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
        this.databaseHandler.openConnection();
    }

    /**
     * Used to insert new category into categories list
     *
     * @param category: category to be inserted into categories list
     */
    public void addCategory(String category) {
        databaseHandler.addCategory(category);
    }

    /**
     * Used to print categories preceded with index numbers
     */
    public void viewCategories() {
        ArrayList<String> categories = this.databaseHandler.showCategories();
        if (categories.size() == 0)
            System.out.println("You haven't previously selected categories, let's create one!");
        int index = 1;
        for (String category : categories) {
            if (!category.isEmpty()) { //to avoid empty category name
                //we have used index to avoid empty category and always have continuous numbering system
                System.out.printf("%d: %s%n", index, category);
                index++;
            }
        }
    }

    /**
     * Get category at specified index
     *
     * @param index: index of wanted category
     * @return (String) category
     */
    public String getCategoryAtIndex(int index) {
        return databaseHandler.showCategories().get(index);
    }

    /**
     * Used to determine if a category is found in the database or not
     *
     * @param category: category to be searched for
     * @return boolean
     */
    public boolean isCategoryFound(String category) {
        return databaseHandler.isCategoryFound(category);
    }
}
