import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Chef extends Restaurant {

    private Restaurant currentRestaurant;

    public Chef(Restaurant a) {
        this.currentRestaurant = a;
    }
    /**
     * menuforChef method that allows chef to-
     * view current orders,
     * update the status of an order
     * see the completed orders
     * quits the program
     * @throws FileNotFoundException throws FileNotFoundExceptions
     * @throws InputMismatchException throws InputMismatchExceptions
     */


    public void menuForChef() throws FileNotFoundException, InputMismatchException {
        System.out.println("Menu for restaurant: " + currentRestaurant.getRestaurantId());
        System.out.println("V)iew current orders, U)pdate status, S)ee completed orders, Q)uit");
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine().trim();

        switch (input.toUpperCase()) {
            case "V":
                seeCurrentOrders();
                break;
            case "U":
                if (currentRestaurant.getCurrentOrders().isEmpty()) {
                    System.out.println("No Current Orders");
                    menuForChef();
                } else {
                    System.out.println("Choose an order to update");
                    for (int i = 0; i < currentRestaurant.getCurrentOrders().size(); i++) {
                        System.out.println((i + 1) + " " + currentRestaurant.getCurrentOrders().get(i));
                    }
                    Scanner in = new Scanner(System.in);
                    int index = Integer.parseInt(in.nextLine().trim()) - 1;;
                    if (index >= 0 && index <= currentRestaurant.getCurrentOrders().size()) {
                        currentRestaurant.getCompletedOrder().add(currentRestaurant.getCurrentOrders().get(index));
                        currentRestaurant.getPaymentPendingOrders().add(currentRestaurant.getCurrentOrders().get(index));
                        currentRestaurant.getCurrentOrders().remove(index);
                    }
                }
                break;
            case "S":
                for (Order o : currentRestaurant.getCompletedOrder()) {
                    System.out.println(o);
                }
                break;
            case "Q":
                System.out.println("Goodbye");
                currentRestaurant.run();
        }
    }

    /**
     * Prints current orders that haven't been completed.
     */

    public void seeCurrentOrders() {
        for (Order o : currentRestaurant.getCurrentOrders()) {
            System.out.println(o);
        }
    }
}