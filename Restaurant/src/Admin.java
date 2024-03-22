import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

public class Admin extends Restaurant {
    /**Admin class, creates admin for admin to access and alter information
     */
    Restaurant currentRestaurant;

    public Admin(Restaurant restaurant) {
        currentRestaurant = restaurant;
    }
    /** menuForAdmin method, creates menu for admin so they can get - Restaurant id,
     Number of tables,
     Capacity,
     View the menu,
     Create new restaurant,
     Switch restaurant,
     view reservations,
     add to menu and quit
     *@throws FileNotFoundException throws filenotfoundexception
     * @throws InputMismatchException throws inputmismatchexception
     */

    public void menuForAdmin() throws FileNotFoundException, InputMismatchException {

        Scanner in = new Scanner(System.in);

        Restaurant currentRestaurant = getListOfRestaurants().get(Manager.getCurrentRestaurantIndex());

        System.out.println("Menu for Restaurant: " + currentRestaurant.getRestaurantId());
        System.out.println("Get R)estaurant ID, get N)umber of Tables, get C)apacity, V)iew Menu, Cr)eate new restaurant, S)witch restaurant, View Re)servations, A)dd to Menu, Ge)nerate Analytics Q)uit ");
        String input = in.nextLine().trim();
        /**
         switch-case to execute selected blocks of code based on admin input
         @param input admin input
         */

        switch (input.toUpperCase()) {
            case "R":
                System.out.println(currentRestaurant.getRestaurantId());
                break;
            case "N":
                System.out.println(currentRestaurant.getNumberOfTables());
                break;
            case "C":
                System.out.println(currentRestaurant.getCapacity());
                break;
            case "V":
                currentRestaurant.viewMenu();
                break;
            case "CR":
                createRestaurant();
            case "S":
                System.out.println("Choose a restaurant:");
                System.out.println(currentRestaurant.getListOfRestaurants());
                for (int i = 0; i < getListOfRestaurants().size(); i++) {
                    System.out.println((i + 1) + ".");
                    System.out.println(getListOfRestaurants().get(i).getRestaurantId());
                }
                int nextRestaurant = Integer.parseInt(in.nextLine().trim()) - 1;
                if (nextRestaurant >= 0 && nextRestaurant <= getListOfRestaurants().size()) {
                    currentRestaurant = getListOfRestaurants().get(nextRestaurant);
                    Manager.setCurrentRestaurantIndex(nextRestaurant);
                } else {
                    System.out.println("invalid Restaurant");
                    menuForAdmin();
                }
                break;
            case "RE":
                for (TableReservation s : currentRestaurant.getListOfReservations()) {
                    System.out.println(s);
                }
                break;
            case "A":
                System.out.println("Enter a time of day: (1) Morning, (2) Noon, (3) Evening");
                int timeOfDay = Integer.parseInt(in.nextLine().trim());
                System.out.println("Enter food name:");
                String foodName = in.nextLine().trim();
                System.out.println("Enter a price:");
                double price = Double.parseDouble(in.nextLine().trim());
                Food newFood = new Food(foodName, price);
                Menu newMenu = new Menu(currentRestaurant);
                newMenu.addToMenu(timeOfDay, newFood);
                currentRestaurant.setMenu(newMenu);
                break;
            case "GE":
                System.out.println("Enter first day YYYY-MM-DD");

                String[] before = in.nextLine().trim().split("-");
                int[] beforeFormat = new int[3];
                for (int i = 0; i < 3; i++) {
                    beforeFormat[i] = Integer.parseInt(before[i]);
                }

                System.out.println("Enter Last Day YYYY-MM-DD");
                String[] after = in.nextLine().trim().split("-");
                int[] afterFormat = new int[3];
                for (int i = 0; i < 3; i++) {
                    afterFormat[i] = Integer.parseInt(after[i]);
                }
                LocalDate beforeDate = LocalDate.of(beforeFormat[0], beforeFormat[1], beforeFormat[2]);
                LocalDate afterDate = LocalDate.of(afterFormat[0], afterFormat[1], afterFormat[2]);
                List<LocalDate> datesBetween = beforeDate.datesUntil(afterDate).toList();

                double totalRev = 0;
                double totalTips = 0;
                for (int i = 0; i < datesBetween.size(); i++) {
                    Scanner scanPay = new Scanner(new File("Restaurant//src//payments.csv"));
                    scanPay.nextLine();

                    double totalForDay = 0;
                    double totalTipsForDay = 0;
                    while (scanPay.hasNext()) {
                        String line = scanPay.nextLine().trim();

                        String[] dataPerLine = line.split(", ");
                        String[] date = dataPerLine[1].split("-");
                        int[] dateFormat = new int[3];
                        for (int j = 0; j < 3; j++) {
                            dateFormat[j] = Integer.parseInt(date[j]);
                        }
                        LocalDate dateOf = LocalDate.of(dateFormat[0], dateFormat[1], dateFormat[2]);
                        System.out.println(line);
                        if (dateOf.isBefore(afterDate) && dateOf.isAfter(beforeDate) && (currentRestaurant.getRestaurantId() == Integer.parseInt(dataPerLine[4].substring(0, dataPerLine[4].length()-1)))) {

                            if (dateOf.equals(datesBetween.get(i))) {
                                totalForDay += Double.parseDouble(dataPerLine[0]);
                                totalTipsForDay += Double.parseDouble(dataPerLine[2]);
                                totalRev += Double.parseDouble(dataPerLine[0]);
                                totalTips += Double.parseDouble(dataPerLine[2]);

                            }


                        }

                    }
                    System.out.println("Total For Day " + datesBetween.get(i).toString() + " : " + totalForDay + "\nTips: " + totalTipsForDay);


                }
                System.out.println("Total Revenue for " + currentRestaurant.getRestaurantId() + ": " + totalRev + "\nTips: " + totalTips);

            case "Q":
                System.out.println("Goodbye");
                currentRestaurant.run();
                break;

            default:
                System.out.println("Invalid input");
                menuForAdmin();
        }

    }
}