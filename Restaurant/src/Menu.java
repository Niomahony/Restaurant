import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    /** Menu class to list food and price for breakfast lunch and dinner
     *
     */

    Restaurant currentRestaurant;
    private ArrayList<Food> menuForMorning = new ArrayList<>();
    private ArrayList<Food> menuForAfterNoon = new ArrayList<>();
    private ArrayList<Food> menuForEvening = new ArrayList<>();

    /** Menu method lists food in an arraylist
     *
     */
    public Menu(Restaurant r) {

        currentRestaurant = r;
        menuForMorning.add(new Food("Full Irish Breakfast", 16.95));
        menuForMorning.add(new Food("Vegetarian Breakfast", 15.95));
        menuForMorning.add(new Food("Eggs Benedict", 12.50));
        menuForMorning.add(new Food("Eggs Royale", 14.50));
        menuForMorning.add(new Food("Avocado Benedict's", 11.50));
        menuForMorning.add(new Food("Hot Buttermilk Pancakes With Bacon", 13.50));
        menuForMorning.add(new Food("Scrambled Eggs And Smoked Salmon", 13.95));
        menuForMorning.add(new Food("Two Hen's Eggs", 8.95));
        menuForMorning.add(new Food("Folded Ham And Cheese Omelette", 11.95));
        menuForMorning.add(new Food("Organic Galway Bay Smoked Salmon", 14.95));
        menuForMorning.add(new Food("Crushed Avocado And Roasted Tomato", 10.95));
        menuForMorning.add(new Food("Non-Gluten Bramley Apple Granola", 6.75));
        menuForMorning.add(new Food("Poached Eggs And Crushed Avocado", 12.95));

        menuForAfterNoon.add(new Food("Smoked Mackerel", 5.00));
        menuForAfterNoon.add(new Food("Mushroom Consomme", 5.00));
        menuForAfterNoon.add(new Food("Ham Hock Croquette", 5.00));

        menuForEvening.add(new Food("Baked Salmon Fillet", 12.95));
        menuForEvening.add(new Food("Sweet Potato Keralan Curry", 12.95));
        menuForEvening.add(new Food("Chargrilled Paillard Of Chicken", 12.95));
        menuForEvening.add(new Food("McChicken Sandwich", 4.95));
        menuForEvening.add(new Food("Steak, Egg And Thick Cut Chips", 12.95));

        try {
            loadMenuFromDisk();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads menu data from csv so menu data is persistent
     * @throws FileNotFoundException
     */
    private void loadMenuFromDisk() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("Restaurant/src/menu.csv"));

        int firstLine = 0;
        while (sc.hasNext()) {
            if (firstLine == 0) {
                sc.nextLine();
                firstLine++;
                continue;
            }

            String[] data = sc.nextLine().split(",");
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].trim();
            }

            int restaurantID = Integer.parseInt(data[0]);
            String foodName = data[1];
            double price = Double.parseDouble(data[2]);
            int timeOfDay = Integer.parseInt(data[3]);

            if (restaurantID != currentRestaurant.getRestaurantId()) continue;
            Food f = new Food(foodName, price);
            ArrayList<Food> arr = (timeOfDay == 1 ? menuForMorning : timeOfDay == 2 ? menuForAfterNoon : timeOfDay == 3 ? menuForEvening : menuForMorning);

            arr.add(f);
        }
    }

    public String getMenuForAfterNoon() {
        return stringifyMenu(menuForAfterNoon);
    }

    public String getMenuForEvening() {
        return stringifyMenu(menuForEvening);
    }

    public String getMenuForMorning() {
        return stringifyMenu(menuForMorning);
    }

    /**
     * returns total menu
     * @return
     */
    public ArrayList<Food> getTotalMenu() {
        ArrayList<Food> total = new ArrayList<>();
        total.addAll(menuForEvening);
        total.addAll(menuForAfterNoon);
        total.addAll(menuForMorning);
        return total;
    }

    public ArrayList<Food> getAllMenuItemsAsArray() {
        ArrayList<Food> totalMenu = new ArrayList<>();

        totalMenu.addAll(menuForMorning);
        totalMenu.addAll(menuForAfterNoon);
        totalMenu.addAll(menuForEvening);

        return totalMenu;
    }

    /**
     * method that turns the array list of food into a string
     * @param menu menu
     * @return the menu
     */
    public String stringifyMenu(ArrayList<Food> menu) {
        String m = "";
        for(Food f : menu) {
            m += "\n" + f.getFoodName() + "\t\t:\t\t" + f.getPrice();
        }

        return m;
    }
    /**
     * toString method that returns the time of day + the menu for that time of day
     * @return menu for morning, afternoon and evening
     */
    @Override
    public String toString() {
        String morningMenu = stringifyMenu(menuForMorning);
        String afternoonMenu = stringifyMenu(menuForAfterNoon);
        String eveningMenu = stringifyMenu(menuForEvening);

        return "Morning:\n" + morningMenu + "\n\n" + "Afternoon:\n" + afternoonMenu + "\n\n" + "Evening:\n" + eveningMenu;
    }
    /**
     * addToMenu method that allows admin to add items to menu
     * @param timeOfDay takes time of day
     * @param food takes food item to be added
     */
    public void addToMenu(int timeOfDay, Food food) throws FileNotFoundException {
        if (timeOfDay == 1) {
            menuForMorning.add(food);
        } else if (timeOfDay == 2) {
            menuForAfterNoon.add(food);
        } else if (timeOfDay == 3) {
            menuForEvening.add(food);
        } else {
            System.out.println("Invalid.");
        }

        String[] values = {String.valueOf(currentRestaurant.getRestaurantId()), String.valueOf(food.getFoodName()), String.valueOf(food.getPrice()), String.valueOf(timeOfDay)};
        // restaurantID, name, price, timeOfDay
        CSV("Restaurant/src/menu.csv", values);

    }

    public void CSV(String path, String[] columnNames) throws FileNotFoundException {

        FileWriter write;
        try {
            write = new FileWriter(path, true);
            for (String s : columnNames) {
                write.write(s);
                write.write(", ");
            }
            write.write("\n");
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}