import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Customer extends Restaurant {

    int customerId;
    Restaurant currentRestaurant;
    Login login;

    /**
     * Constructor that assigns a customer object to a restaurant
     * @param restaurant current restaurant the customer object has been created in
     */
    public Customer(Restaurant restaurant) {
        currentRestaurant = restaurant;
    }

    /**
     * login method that lets customers login to new or existing account for the restaurant and cutomer class
     * @throws FileNotFoundException throws FileNotFoundException
     * @throws InputMismatchException throws InputMismatchException
     */
    public void login() throws FileNotFoundException, InputMismatchException {
        System.out.println("C)reate customer, S)ign in as Customer Q)uit");
        Scanner in = new Scanner(System.in);
        String choice = in.nextLine().trim();

        switch (choice.toUpperCase()) {
            case "C":
                int newCustomerId = (int) ((Math.random() * 89999999) + 10000000);
                System.out.println("Enter name");
                String username = in.nextLine().trim();

                System.out.println("Enter pass");
                String password = in.nextLine().trim();
                Login l = new Login(username, password, newCustomerId);
                currentRestaurant.addToListOfCustomers(l, false);
                this.login = l;
                break;
            case "S":
                System.out.println("Enter username:");
                String user = in.nextLine().trim();
                System.out.println("Enter password:");
                String pass = in.nextLine().trim();
                for (Login login : Restaurant.getListOfCustomers()) {

                    if (login.getUsername().equalsIgnoreCase(user) && login.getPassword().equals(pass)) {
                        this.login = login;
                        this.customerId = login.getCustomerid();

                        menuForCustomers();
                    }
                }
                break;
            case "Q":
                currentRestaurant.run();
                break;
            default:
                System.out.println("Invalid input");
                login();
        }

    }

    /**
     * menuForCustomer method that lets customer -
     * make/cancel reservations
     * view menu
     * switch the restaurant
     * pay
     * lookup available tables
     * see reservations
     * see reminders
     * quit
     * @throws FileNotFoundException throws FileNotFoundException
     */
    public void menuForCustomers() throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.println("Customer Menu for Restaurant: " + currentRestaurant.getRestaurantId());
        System.out.println("Welcome " + login.getUsername());
        System.out.println("M)ake a reservation, W)alk-in Reservation,V)iew Menu, C)ancel Reservation, S)witch Restaurant, P)ay, L)ookup available tables, Se)e Reservations, See Re)minders Q)uit");
        String input = in.nextLine().trim();

        if (input.equalsIgnoreCase("M")) {
            makeReservation();
            menuForCustomers();
        } else if (input.equalsIgnoreCase("W")) {
            makeWalkInReservation();
            menuForCustomers();
        } else if (input.equalsIgnoreCase("V")) {
            currentRestaurant.viewMenu();
            menuForCustomers();
        } else if (input.equalsIgnoreCase("C")) {
            System.out.println("Choose a reservation to cancel");
            int i = 1;
            TableReservation delete = new TableReservation();
            for (TableReservation t : currentRestaurant.getListOfReservations()) {
                if(t.getCustomerId() == customerId) {
                    delete = t;
                    System.out.println(i);
                    System.out.println(t);
                    i++;
                }
            }
            int index = Integer.parseInt(in.nextLine().trim()) - 1;
            if (index >= 0 && index < currentRestaurant.getListOfReservations().size()) {
                delete.cancels();

                currentRestaurant.getListOfReservations().remove(delete);
            } else {
                System.out.println("invalid reservation");
                menuForCustomers();
            }
            menuForCustomers();
        } else if (input.equalsIgnoreCase("P")) {
            System.out.println("Choose an Order");
            for (int i = 0; i < currentRestaurant.getPaymentPendingOrders().size(); i++) {
                System.out.println((i + 1) + ".");
                System.out.println(currentRestaurant.getPaymentPendingOrders().get(i).toString());
            }
            int index = Integer.parseInt(in.nextLine().trim()) - 1;
            System.out.println("Enter Date: YYYY-MM-DD");
            String[] dateFormat = in.nextLine().trim().split("-");
            int[] intFormat = new int[3];
            for (int i = 0; i < 3; i++) {
                intFormat[i] = Integer.parseInt(dateFormat[i]);
            }
            LocalDate theDay = LocalDate.of(intFormat[0], intFormat[1], intFormat[2]);

            System.out.println("Cash, Card, or Cheque?");
            String payMethod = in.nextLine().trim();
            System.out.println("How much was paid?");
            double pay = Double.parseDouble(in.nextLine().trim());
            if (pay < currentRestaurant.getPaymentPendingOrders().get(index).getOrderTotal()) {
                System.out.println("Payment insufficient.");
                menuForCustomers();
            } else if (pay > currentRestaurant.getPaymentPendingOrders().get(index).getOrderTotal()) {
                double change = pay - currentRestaurant.getPaymentPendingOrders().get(index).getOrderTotal();
                System.out.printf("Change due: %.2f.%n", change);
            }
            System.out.println("Enter Tip Amount: (0)%, (5)%, (8)%, (10)%, (15)%, (20)%, (Custom)%");
            double tip = Double.parseDouble(in.nextLine().trim());
            double tipActual = currentRestaurant.getPaymentPendingOrders().get(index).getOrderTotal() * (tip / 100);
            Payment newPayment = new Payment(currentRestaurant.getPaymentPendingOrders().get(index).getOrderTotal(), LocalDate.now(), tipActual);
            String[] data = {
                    String.valueOf(currentRestaurant.getPaymentPendingOrders().get(index).getOrderTotal()),
                    String.valueOf(theDay),
                    String.valueOf(tipActual),
                    payMethod,
                    String.valueOf(currentRestaurant.getRestaurantId())
            };
            currentRestaurant.getPaymentPendingOrders().remove(index);
            super.CSV("Restaurant/src/payments.csv", data);
            System.out.println("Payment Processed. Thank you.");
            menuForCustomers();
        } else if (input.equalsIgnoreCase("S")) {
            System.out.println("Choose a restaurant ");
            System.out.println(currentRestaurant.getListOfRestaurants());
            for (int i = 0; i < currentRestaurant.getListOfRestaurants().size(); i++) {
                System.out.println((i + 1) + ". " + currentRestaurant.getListOfRestaurants().get(i));
            }
            int newIndex = Integer.parseInt(in.nextLine().trim()) - 1;
            currentRestaurant = currentRestaurant.getListOfRestaurants().get(newIndex);
            menuForCustomers();
        } else if (input.equalsIgnoreCase("L")) {
            System.out.println("Input a date YYYY-MM-DD");
            String s = in.nextLine().trim();

            System.out.println("Input a time HH:MM");
            String t = in.nextLine().trim();
            String[] timeArray = t.split(":");
            LocalTime a = LocalTime.of(Integer.parseInt(timeArray[0]),Integer.parseInt(timeArray[1]));
            int counter = currentRestaurant.getNumberOfTables();
            ArrayList<TableReservation> currentRes = new ArrayList<>();
            for (TableReservation r : currentRestaurant.getListOfReservations()) {
                System.out.println(r.getTime().getHour());
                System.out.println(timeArray[0]);
                if (r.getDate().toString().equals(s)) {
                    System.out.println("equal date");
                    if (r.getTime().getHour() + 3 >= a.getHour()) {
                        System.out.println("Equal time");
                        counter--;
                        currentRes.add(r);
                    }
                }
            }

            System.out.println("There are " + counter + " tables available at the given date and time. Table(s)");
            for (TableReservation r : currentRes) {
                System.out.println(r.getTableNumber());
            }
            System.out.println("are taken.");

            menuForCustomers();
        } else if (input.equalsIgnoreCase("Se")) {
            for (TableReservation r : currentRestaurant.getListOfReservations()) {

                if (r.getCustomerId() == customerId) {
                    System.out.println(r);
                }
            }
            menuForCustomers();

        } else if (input.equalsIgnoreCase("re")) {
            for (TableReservation r : currentRestaurant.getListOfReservations()) {

                if (r.getCustomerId() == customerId && r.getPhoneNumber() != 0) {
                    System.out.println("Reminder! Reservation upcoming: ");
                    System.out.println(r);
                }
            }
        } else if (input.equalsIgnoreCase("q")) {
            currentRestaurant.run();
        } else {
            System.out.println("Invalid input");
            menuForCustomers();
        }

    }

    /**
     * makeReservation method takes -
     * time and date of reservation,
     * name of customer
     * phone number of customer
     * number of people at the table,
     * and the table number and then reserves those details for the customer
     * @throws FileNotFoundException throws FileNotFoundException
     * @throws InputMismatchException throws InputMismatchException
     */
    public void makeReservation() throws FileNotFoundException, InputMismatchException {
        System.out.println("Enter date 'YYYY-MM-DD, time 'HH:MM', full name, phone number, number of people, table number");
        try {
            Scanner in = new Scanner(System.in);
            String[] resData = in.nextLine().split(",");
            for (int i = 0; i < resData.length; i++) {
                resData[i] = resData[i].trim();
            }

            if (Integer.parseInt(resData[5]) > currentRestaurant.getNumberOfTables()) {
                System.out.println("Not a valid table number. Number of tables is: " + currentRestaurant.getNumberOfTables());
                menuForCustomers();

            } else {
                //format the date string as a LocalDate
                String[] dateAsArray = resData[0].split("-");
                int[] dateAsInts = new int[3];
                for (int i = 0; i < dateAsArray.length; i++) {
                    dateAsInts[i] = Integer.parseInt(dateAsArray[i]);
                }
                LocalDate b = LocalDate.now();
                try {
                    b = LocalDate.of(dateAsInts[0], dateAsInts[1], dateAsInts[2]);
                } catch (DateTimeException e) {
                    System.out.println("Incorrect date format");
                }
                //format the time string as a LocalTime
                String[] timeArray = resData[1].split(":");
                int[] timeAsInts = new int[2];
                for (int i = 0; i < timeArray.length; i++) {
                    timeAsInts[i] = Integer.parseInt(timeArray[i]);
                }
                LocalTime c = LocalTime.now();
                try {
                    c = LocalTime.of(timeAsInts[0], timeAsInts[1]);
                } catch (DateTimeException e) {
                    System.out.println("Incorrect Time Format");
                }
                int counter = currentRestaurant.getNumberOfTables();
                ArrayList<TableReservation> currentRes = new ArrayList<>();
                for (TableReservation r : currentRestaurant.getListOfReservations()) {
                    if (r.getDate().equals(b)) {
                        if (r.getTime().getHour() + 3 >= c.getHour()) {
                            counter--;
                            currentRes.add(r);
                        }
                    }
                }
                if (currentRestaurant.getListOfReservations().isEmpty()) {
                    TableReservation a = new TableReservation(b, c, resData[2], Integer.parseInt(resData[3]), Integer.parseInt(resData[4]), currentRestaurant.getRestaurantId(), Integer.parseInt(resData[5]), currentRestaurant, customerId, false, false);
                    currentRestaurant.getListOfReservations().add(a);
                } else {
                    for (TableReservation r : currentRestaurant.getListOfReservations()) {
                        if (r.getDate().getDayOfYear() == b.getDayOfYear()) {
                            if (r.getTime().getHour() + 3 >= c.getHour()) {
                                if (r.getTableNumber() == Integer.parseInt(resData[5])) {
                                    System.out.println("Table is booked at this time.");
                                    menuForCustomers();
                                }
                            }
                        }
                    }
                    TableReservation a = new TableReservation(b, c, resData[2], Integer.parseInt(resData[3]), Integer.parseInt(resData[4]), currentRestaurant.getRestaurantId(), Integer.parseInt(resData[5]), currentRestaurant, customerId, false,false);
                    if(!a.getBooked()) {
                        currentRestaurant.getListOfReservations().add(a);
                    }

                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter all requested fields");
            makeReservation();
        }
    }

    /**
     * makeWalkInReservation takes number of people and assigns an available table
     * @throws FileNotFoundException
     */
    public void makeWalkInReservation() throws FileNotFoundException {
        System.out.println("Enter number of people. Table Number is Assigned Randomly");

        try {
            Scanner in = new Scanner(System.in);
            int noOfPeople = Integer.parseInt(in.nextLine().trim());
            if (noOfPeople <= currentRestaurant.getCapacity()) {
                TableReservation a = new TableReservation(LocalDate.now(), LocalTime.now(), noOfPeople, currentRestaurant.getRestaurantId(), currentRestaurant, customerId, false);
                currentRestaurant.getListOfReservations().add(a);
            } else {
                System.out.println(noOfPeople + " people exceeds capacity of " + currentRestaurant.getCapacity() + "\n" + "Please edit reservation");
                makeWalkInReservation();
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
            makeWalkInReservation();
        }
    }

    /**
     * cancelReservation removes a reservation from the index
     * @param a a
     * @param index index
     */
    public void cancelReservation(ArrayList<TableReservation> a, int index) {
        System.out.println("Table reservation:\n " + a.get(index).toString() + "\n has been cancelled.");
        a.remove(index);

    }
}