package util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import model.*;
import handlers.*;

public class Utilities {

    /**
     * Function to display menu UI for user to select from
     */
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
                        "\n12. Toggle item's Favorite." +
                        "\n13. Exit" +
                        "\n***********************************************" +
                        "\nchoice: ");
    }

    /**
     * Used to parse user inputs and validate them.
     *
     * @param scan:         Scanner object is used to take console inputs
     * @param categoryList: CategoryListHandler object to display categories | insert new category entered by user.
     * @return _todo item instantiated from user inputs
     * @throws IOException handles creating and opening files operations
     */
    public static TodoItem parseUserInputs(Scanner scan, CategoryListHandler categoryList) throws IOException {
        //simple version of input taking
        scan.nextLine();
        System.out.print("Enter todo title: ");
        String title = scan.nextLine().trim();
        System.out.print("\nEnter todo description: ");
        String description = scan.nextLine().trim();
        String category = categoryHandling(scan, categoryList);
        System.out.print("\nEnter todo Start date with format 'yyyy-mm-dd': ");
        //handle wrong date format
        String startDateStr = scan.nextLine().trim();
        while (startDateStr.length() < 10) {
            System.out.print("\nWrong date format!");
            System.out.print("\nRe-Enter todo Start date with format 'yyyy-mm-dd': ");
            startDateStr = scan.nextLine().trim();
        }
        LocalDate startDate = LocalDate.parse(startDateStr);
        //handle end date wrong format
        System.out.print("\nEnter todo End date with format 'yyyy-mm-dd': ");
        String endDateStr = scan.nextLine().trim();
        while (endDateStr.length() < 10) {
            System.out.print("\nWrong date format!");
            System.out.print("\nRe-Enter todo Start date with format 'yyyy-mm-dd': ");
            endDateStr = scan.nextLine().trim();
        }
        LocalDate endDate = LocalDate.parse(endDateStr);
        while (endDate.isBefore(startDate)) { //validate date ranges
            System.out.println("Can't pick an end date before start date!");
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

    /**
     * Handles user selecting existing category or enter new category
     *
     * @param scan:         Scanner object is used to take console inputs
     * @param categoryList: CategoryListHandler object to display categories | insert new category entered by user.
     * @return category selected by user either by number of existing category or new category entered.
     * @throws IOException handles creating and opening files operations
     */
    public static String categoryHandling(Scanner scan, CategoryListHandler categoryList) throws IOException {
        String category;
        categoryList.viewCategories();
        System.out.print("\nEnter todo Category number, or enter new category name:\n");
        if (scan.hasNextInt()) //check if next input is an integer
        {
            //display existing categories saved in 'categories.txt'
            int categoryIndex = Integer.parseInt(scan.nextLine());
            //check if index is out of bound
            while (categoryIndex > categoryList.getCategories().size()) {
                System.out.println("Invalid category selected, re-enter category number");
                categoryIndex = Integer.parseInt(scan.nextLine());
            }
            //get category at index entered by user
            category = categoryList.getCategoryAtIndex(categoryIndex - 1); //position-index different
            scan.nextLine();
        } else {
            //entered category name
            //search if exists
            String tempCategory = scan.nextLine().trim();
            if (!categoryList.isCategoryFound(tempCategory))//new category to be added
            {
                categoryList.addCategory(tempCategory);
            }
            category = tempCategory;
        }
        return category;
    }

    /**
     * used to print HashMap<String, TodoItem> contents
     *
     * @param map:    map to be printed
     */
    public static void printCollection(HashMap<String, TodoItem> map) {
        if (map.isEmpty()) {
            System.out.println("No results found, try another predicate!");
            return;
        }
        Iterator<String> iterator = map.keySet().iterator();
        int length = map.size();
        int counter = 0;
        while (iterator.hasNext() && counter++ < length) {
            System.out.println("-----------------------" + counter + "-----------------------");
            System.out.println(map.get(iterator.next()).stringEquivalent());
        }
    }

    public static Priorities mapStringToPriority(String p) {
        if (p.equalsIgnoreCase("Low"))
            return Priorities.LOW;
        if (p.equalsIgnoreCase("Medium"))
            return Priorities.MEDIUM;
        return Priorities.HIGH;
    }
}

