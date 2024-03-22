import java.util.ArrayList;
import java.util.Scanner;

public class Order {
    Restaurant currentRestaurant;
    private ArrayList<Food> orders = new ArrayList<>();
    private double orderTotal = 0;
    private int tableNumber;
    /**
     * Order constructor that creates tableNumber and currentRestaurant objects
     * @param tableNumber table number
     * @param currentRestaurant current restaurant
     */
    public Order(int tableNumber, Restaurant currentRestaurant) {
        this.tableNumber = tableNumber;
        this.currentRestaurant = currentRestaurant;
    }
    /**
     * addToOrder method that adds food to a persons order
     * @param f food
     */
    public void addToOrder(Food f) {
        orders.add(f);
        orderTotal += f.getPrice();
    }

    public ArrayList<Food> getOrderList() {
        return orders;
    }


    public int getTableNumber() {
        return tableNumber;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    /**
     * formats order data as a string
     * @return string of order data
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Food:\n");
        for (Food f : orders) {
            s.append(f);
            s.append("\n");
        }
        s.append(orderTotal);
        return s.toString();

    }
}