import java.io.FileNotFoundException;
import java.util.Scanner;

public class Waiter extends Restaurant {

    private Restaurant currentRestaurant;

    public Waiter(Restaurant currentRestaurant) {
        this.currentRestaurant = currentRestaurant;
    }

    /**
     * displays the menu for a waiter
     * @throws FileNotFoundException
     */
    public void menuForWaiters() throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        Restaurant currentRestaurant = getListOfRestaurants().get(manager.getCurrentRestaurantIndex());

        System.out.println("Menu for restaurant: " + currentRestaurant.getRestaurantId());
        System.out.println("Get R)estaurant ID, get N)umber of Tables, get C)apacity, Cr)eate order, V)iew Menu, View Re)servations, Q)uit ");
        String input = in.nextLine().trim();
        if (input.equalsIgnoreCase("R")) {
            System.out.println(currentRestaurant.getRestaurantId());
        } else if (input.equalsIgnoreCase("N")) {
            System.out.println(currentRestaurant.getNumberOfTables());
        } else if (input.equalsIgnoreCase("C")) {
            System.out.println(currentRestaurant.getCapacity());
        } else if (input.equalsIgnoreCase("V")) {
            currentRestaurant.viewMenu();
        } else if (input.equalsIgnoreCase("Cr")) {
            createOrder();
        } else if (input.equalsIgnoreCase("Re")) {
            for (TableReservation s : currentRestaurant.getListOfReservations()) {
                System.out.println(s);
            }
        } else if (input.equalsIgnoreCase("Q")) {
            System.out.println("Goodbye");
            currentRestaurant.run();
            return;
        }
    }

    /**
     * creates an order and prompts user to enter food for order
     * @throws FileNotFoundException
     */

    public void createOrder() throws FileNotFoundException {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter table number");
        int tableNumber = Integer.parseInt(s.nextLine());
        Order order = new Order(tableNumber, currentRestaurant);
        System.out.println(currentRestaurant.getMenu().getTotalMenu());

        System.out.println("\nEnter each food one by one:");
        String food = s.nextLine().toUpperCase();
        while(!food.equalsIgnoreCase("q")){
            for(Food f: currentRestaurant.getMenu().getTotalMenu()){
                if(f.getFoodName().toUpperCase().equalsIgnoreCase(food)){
                    order.addToOrder(f);
                    System.out.println("Food Added");
                }
            }
            food = s.nextLine();
        }
        currentRestaurant.addToActiveOrders(order);
        menuForWaiters();
    }
}
