import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class CategoryListHandler {
    ArrayList<String> categories = new ArrayList<>();
    private static final String categoriesPath = "categories";

    public CategoryListHandler() throws IOException {
        //open category file
        FileHandler fileHandler = new FileHandler();
        //parse categories into Array and set it
        categories = parseCategories(fileHandler.readFromFile(categoriesPath).toString());
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public void viewCategories() {
        if (categories.size() == 0)
            System.out.println("You haven't previously selected categories, let's create one!");
        for (int i = 0; i < categories.size(); i++)
            System.out.printf("%d: %s%n", i + 1, categories.get(i));
    }

    public String getCategoryAtIndex(int index) {
        return categories.get(index);
    }

    public boolean isCategoryFound(String category) {
        return categories.contains(category);
    }

    private ArrayList<String> parseCategories(String categoriesString) {
        return new ArrayList<>(Arrays.asList(categoriesString.split(",")));
    }

    public void writeCategoriesToFile() throws IOException {
        StringBuilder result = new StringBuilder();
        for (String category : categories)
            result.append(category + ",\n");
        Files.writeString(Path.of(categoriesPath), result.toString());
    }

}
