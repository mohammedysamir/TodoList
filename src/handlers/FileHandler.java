package handlers;

import model.Priorities;
import model.TodoItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileHandler {

    public StringBuilder readFromFile(String fileLocation) throws IOException {

        StringBuilder fileResult = new StringBuilder();
        File file = new File(fileLocation);
        if (!file.exists())
            file.createNewFile();
        Scanner scannerObj = new Scanner(file);

        while (scannerObj.hasNext()) {

            fileResult.append(scannerObj.nextLine());

        }
        scannerObj.close();

        return fileResult;
    }

    public void writeToFile(HashMap<String, TodoItem> listItem, String fileLocation) throws IOException {
        StringBuilder result = new StringBuilder();
        for (TodoItem todo : listItem.values()) {
            String json = todo.toString();
            result.append(json);
        }
        Files.writeString(Path.of(fileLocation), result.toString());
    }


    public Map<String, TodoItem> parseItems(String fileLocation) throws IOException {
        Map<String, TodoItem> todos = new HashMap<>();

        StringBuilder result = readFromFile(fileLocation);
        if (!result.toString().isEmpty()) {
            String[] items = result.toString().split("}");//match using closing-curly bracket

            //iterate through each item
            for (String item : items) {
                item = item.substring(1);//to remove open curly bracket.
                String[] itemDetails = item.split(",");
                Priorities priorities = Priorities.LOW;

                switch (itemDetails[6]) {
                    case "HIGH":
                        priorities = Priorities.HIGH;
                        break;
                    case "MEDIUM":
                        priorities = Priorities.MEDIUM;
                        break;
                }

                TodoItem objTodoItem = new TodoItem(itemDetails[0],
                        itemDetails[1],
                        itemDetails[2],
                        LocalDate.parse(itemDetails[3]),
                        LocalDate.parse(itemDetails[4]),
                        itemDetails[5].equalsIgnoreCase("true"),
                        priorities);
                //add items to map
                todos.put(objTodoItem.getTitle(), objTodoItem);
            }
        }
        return todos;
    }

}
