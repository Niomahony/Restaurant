import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Restaurant {
    public Restaurant() {

    }

    private int restaurantId;
    private int capacity;
    private int numberOfTables;
    private final ArrayList<TableReservation> listOfReservations = new ArrayList<>();
    private Menu menu;
    static boolean quit = false;
    private final ArrayList<Order> currentOrders = new ArrayList<>();
    private final ArrayList<Order> completedOrder = new ArrayList<>();
    private final ArrayList<Order> paymentPendingOrders = new ArrayList<>();
    private static final ArrayList<Login> listOfCustomers = new ArrayList<>();
    public Manager manager;

    /**
     * constructor for restaurant that creates a restaurant
     * writes data to a CSV file
     *
     * @param restaurantId   id of restaurant
     * @param capacity       number of people it can hold
     * @param numberOfTables the number of tables in restaurant
     * @throws FileNotFoundException if file not found by Scanner
     */
    public Restaurant(Manager m, int restaurantId, int capacity, int numberOfTables, boolean exists) throws FileNotFoundException {
        this.restaurantId = restaurantId;
        this.capacity = capacity;
        this.numberOfTables = numberOfTables;
        this.manager = m;
        menu = new Menu(this);
        String[] data = {String.valueOf(restaurantId), String.valueOf(capacity), String.valueOf(numberOfTables)};
        if (!exists) {
            CSV("Restaurant/src/restaurant.csv", data);
        } else {
            loadReservationsFromDisk();
            loadLoginsFromDisk();
        }
    }

    public ArrayList<Order> getPaymentPendingOrders() {
        return paymentPendingOrders;
    }

    public static ArrayList<Login> getListOfCustomers() {
        return listOfCustomers;
    }

    /**
     * adds a login object to the list of customers. This ensures a login can only see reservations they've made
     *
     * @param l the login object
     */
    public void addToListOfCustomers(Login l, boolean exists) {
        listOfCustomers.add(l);
        String[] loginData = {String.valueOf(this.getRestaurantId()), String.valueOf(l.getCustomerid()), String.valueOf(l.getUsername()), String.valueOf(l.getPassword())};
        if(!exists) {
            this.CSV("Restaurant/src/logins.csv", loginData);
        }
    }

    /**
     * loads the reservations from CSVs to allow for persistent storage
     */
    private void loadReservationsFromDisk() {

        try {
            Scanner sc = new Scanner(new File("Restaurant/src/data.csv"));
            int firstLine = 0;
            while (sc.hasNext()) {
                if (firstLine == 0) {
                    sc.nextLine();
                    firstLine++;
                    continue;
                }

                String[] data = sc.nextLine().split(",");
                // name, reservationID, Table, date, time, restaurantID, phone number, customer ID
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }

                String name = data[0];
                int reservationID = Integer.parseInt(data[1]);
                int table = Integer.parseInt(data[2]);
                String[] dateSplit = data[3].split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]));
                String[] timeSplit = data[4].split(":");
                LocalTime time = LocalTime.of(Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]));
                int restaurantID = Integer.parseInt(data[5]);
                int phoneNumber = Integer.parseInt(data[6]);
                int customerID = Integer.parseInt(data[7]);
                int numberOfPeople = Integer.parseInt(data[8]);
                boolean cancel = Boolean.parseBoolean(data[9]);

                if (restaurantID != this.getRestaurantId()) continue;

                TableReservation tableReservation = new TableReservation(date, time, name, phoneNumber, numberOfPeople, restaurantID, table, this, customerID, true, cancel);
                this.listOfReservations.add(tableReservation);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * loads login details from csv for persistent storage
     */
    private void loadLoginsFromDisk() {
        try {
            Scanner sc = new Scanner(new File("Restaurant/src/logins.csv"));
            int firstLine = 0;

            while(sc.hasNext()) {
                if(firstLine == 0) {
                    firstLine++;
                    sc.nextLine();
                    continue;
                }

                String[] data = sc.nextLine().split(",");

                for(int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }

                int restaurantID = Integer.parseInt(data[0]);
                int customerID = Integer.parseInt(data[1]);
                String username = data[2];
                String password = data[3];

                Login l = new Login(username, password, customerID);

                this.addToListOfCustomers(l, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumberOfTables() {
        return numberOfTables;
    }

    public ArrayList<TableReservation> getListOfReservations() {
        return listOfReservations;
    }

    public ArrayList<Restaurant> getListOfRestaurants() {
        return Manager.getListOfRestaurants();
    }

    /**
     * toString that returns the restaurant ID
     *
     * @return string of restaurant ID
     */
    @Override
    public String toString() {
        return "" + restaurantId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    /**
     * creates a new restaurant
     * @throws FileNotFoundException
     */
    public void createRestaurant() throws FileNotFoundException {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Restaurant ID");
        try {
            int id = Integer.parseInt(s.nextLine());
            System.out.println("Enter Number of Tables");
            int numberOfTables = Integer.parseInt(s.nextLine());
            System.out.println("Enter Capacity");
            int capacity = Integer.parseInt(s.nextLine());
            if (capacity <= 0 || numberOfTables <= 0 || id <= 0) {
                System.out.println("Invalid input, no negative numbers please.");
                createRestaurant();
            } else {
                Restaurant r = new Restaurant(this.manager, id, capacity, numberOfTables, false);
                Manager.addRestaurant(r);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
            createRestaurant();
        }
    }

    /**
     * a method that writes data to a csv file
     *
     * @param path        the path to the csv file
     * @param columnNames the actual data
     */
    public void CSV(String path, String[] columnNames) {

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

    public ArrayList<Order> getCurrentOrders() {
        return currentOrders;
    }

    public ArrayList<Order> getCompletedOrder() {
        return completedOrder;
    }

    /**
     * displays the main menu
     * @throws FileNotFoundException
     */
    public void run() throws FileNotFoundException {
        if (getListOfRestaurants().size() == 0) {
            createRestaurant();
        }
        Scanner in = new Scanner(System.in);
        System.out.println("Menu for Restaurant: " + getListOfRestaurants().get(Manager.getCurrentRestaurantIndex()).getRestaurantId());
        System.out.println("C)ustomer or W)aiter or Ch)ef or A)dministration.\nQ)uit");
        String role = in.nextLine().toLowerCase().trim();
        if (role.equalsIgnoreCase("C")) {

            while (!quit) {
                Customer c = new Customer(Manager.getListOfRestaurants().get(Manager.getCurrentRestaurantIndex()));
                c.login();
            }

        } else if (role.equalsIgnoreCase("W")) {
            Waiter w = new Waiter(Manager.getListOfRestaurants().get(Manager.getCurrentRestaurantIndex()));
            while (!quit) {
                w.menuForWaiters();
            }
        } else if (role.equalsIgnoreCase("Ch")) {
            Chef chef = new Chef(Manager.getListOfRestaurants().get(Manager.getCurrentRestaurantIndex()));
            while (!quit) {
                chef.menuForChef();
            }
        } else if (role.equalsIgnoreCase("A")) {
            while (!quit) {
                Admin a = new Admin(Manager.getListOfRestaurants().get(Manager.getCurrentRestaurantIndex()));
                a.menuForAdmin();
            }
        } else if (role.equalsIgnoreCase("Q")) {
            System.out.println("Goodbye");
            System.exit(0);
        } else {
            System.out.println("Invalid input");
            run();
        }


    }

    /**
     * displays the menu for the current restaurant
     * @throws InputMismatchException
     */
    public void viewMenu() throws InputMismatchException {
        System.out.println("Enter time of day: (1)Morning, (2)Afternoon, (3)Evening");
        Scanner in = new Scanner(System.in);
        int timeOfDay = Integer.parseInt(in.nextLine().trim());
        if (timeOfDay == 1) {
            System.out.println(menu.getMenuForMorning());
        } else if (timeOfDay == 2) {
            System.out.println(menu.getMenuForAfterNoon());
        } else if (timeOfDay == 3) {
            System.out.println(menu.getMenuForEvening());
        } else {
            System.out.println("Invalid input");
        }
    }

    public void addToActiveOrders(Order o) {
        currentOrders.add(o);
    }


}
