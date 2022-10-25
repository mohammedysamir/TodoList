package util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import model.*;
import handlers.*;

public class Utilities {

    //util functions
    public static void displayMenu() {
        System.out.print(
                "***********************************************" +
                        "\nSelect number of request:" +
                        "\n1. Add Todo item." +
                        "\n2. Update Todo item." +
                        "\n3. Delete Todo item." +
                        "\n4. Show all Todo items." +
                        "\n5. Show 5 nearest items by start date." +
                        "\n6. Search by Title." +
                        "\n7. Search by Start date." +
                        "\n8. Search by End date." +
                        "\n9. Search by item's priority." +
                        "\n10. Add Todo item to Category." +
                        "\n11. Show Favorites" +
                        "\n12. Add Todo item to Favorite." +
                        "\n13. Exit" +
                        "\n***********************************************" +
                        "\nchoice: ");
    }

    public static TodoItem parseUserInputs(Scanner scan, CategoryListHandler categoryList) throws IOException {
        //simple version of input taking
        scan.nextLine();
        System.out.print("Enter todo title: ");
        String title = scan.nextLine().trim();
        System.out.print("\nEnter todo description: ");
        String description = scan.nextLine().trim();
        String category = categoryHandling(scan, categoryList);
        System.out.print("\nEnter todo Start date with format 'yyyy-mm-dd': ");
        LocalDate startDate = LocalDate.parse(scan.nextLine().trim());
        System.out.print("\nEnter todo End date with format 'yyyy-mm-dd': ");
        LocalDate endDate = LocalDate.parse(scan.nextLine().trim());
        while (endDate.isBefore(startDate)) { //validate date ranges
            System.out.println("Can't pick an end date before start date!");
            System.out.print("\nRe-enter todo End date with format 'yyyy-mm-dd': ");
            endDate = LocalDate.parse(scan.nextLine().trim());
        }
        System.out.print("\nis this todo Favorite? (Y/N): ");
        Boolean isFavorite = scan.nextLine().trim().equalsIgnoreCase("Y");
        System.out.print("\nEnter todo priority:- 1 = HIGH, 2 = MEDIUM, 3 = LOW ");
        Priorities priority = Priorities.LOW;
        switch (scan.nextInt()) {
            case 1:
                priority = Priorities.HIGH;
                break;
            case 2:
                priority = Priorities.MEDIUM;
                break;
            case 3:
                break;
        }
        //create obj from inputs
        return new TodoItem(title, description, category, startDate, endDate, isFavorite, priority);
    }

    public static String categoryHandling(Scanner scan, CategoryListHandler categoryList) throws IOException {
        String category;
        categoryList.viewCategories();
        System.out.print("\nEnter todo Category number, or enter new category name:\n");
        if (scan.hasNextInt()) //check if next input is an integer
        {
            int categoryIndex = Integer.parseInt(scan.nextLine());
            //check if index is out of bound
            while (categoryIndex > categoryList.getCategories().size()) {
                System.out.println("Invalid category selected, re-enter category number");
                categoryIndex = Integer.parseInt(scan.nextLine());
            }
            category = categoryList.getCategoryAtIndex(categoryIndex - 1); //position-index different
            scan.nextLine();
        } else {
            //entered category name
            //search if exists
            String tempCategory = scan.nextLine().trim();
            if (!categoryList.isCategoryFound(tempCategory))//new category to be added
            {
                categoryList.addCategory(tempCategory);
                categoryList.writeCategoriesToFile(); //write to file
            }
            category = tempCategory;
        }
        return category;
    }

    public static void printCollection(HashMap<String, TodoItem> map, int length) {
        if (map.isEmpty())
            System.out.println("No results found, try another predicate!");
        Iterator<String> iterator = map.keySet().iterator();
        int counter = 0;
        while (iterator.hasNext() && counter++ < length)
            System.out.println(map.get(iterator.next()).toString());
    }
}

