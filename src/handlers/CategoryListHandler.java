package handlers;

import constants.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class CategoryListHandler {
    ArrayList<String> categories = new ArrayList<>();

    /**
     * Getter for categories attribute
     *
     * @return list of categories
     */
    public ArrayList<String> getCategories() {
        return categories;
    }

    /**
     * Constructor used to define fileHandler object and read categories from 'categories.txt' file
     *
     * @throws IOException: handles file's operations
     */
    public CategoryListHandler() throws IOException {
        //open category file
        FileHandler fileHandler = new FileHandler();
        //parse categories into Array and set it
        categories = parseCategories(fileHandler.readFromFile(Constants.categoryFilePath).toString());
    }

    /**
     * Used to insert new category into categories list
     *
     * @param category: category to be inserted into categories list
     */
    public void addCategory(String category) {
        categories.add(category);
    }

    /**
     * Used to print categories preceded with index numbers
     */
    public void viewCategories() {
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
        return categories.get(index);
    }

    /**
     * Used to determine if a category is found in the list or not
     *
     * @param category: category to be searched for
     * @return boolean
     */
    public boolean isCategoryFound(String category) {
        return categories.contains(category);
    }

    /**
     * Used to parse category list using ',' as delimiter
     *
     * @param categoriesString: string represents categories read from the file
     * @return List of strings (categories)
     */
    private ArrayList<String> parseCategories(String categoriesString) {
        return new ArrayList<>(Arrays.asList(categoriesString.split(",")));
    }

    /**
     * Used to write categories list into 'categories.txt' file
     *
     * @throws IOException handles file's operations
     */
    public void writeCategoriesToFile() throws IOException {
        StringBuilder result = new StringBuilder();
        for (String category : categories)
            result.append(category + ",\n");
        Files.writeString(Path.of(Constants.categoryFilePath), result.toString());
    }

}
