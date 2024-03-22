import java.io.FileNotFoundException;

public class Driver {
    /**
     * Runs the program
     */
    public static void main(String[] args) throws FileNotFoundException {

        Restaurant r = new Restaurant();
        r.manager = new Manager();
        r.manager.startup();
        try {
            Restaurant existing = Manager.getListOfRestaurants().get(0);
            Manager.setCurrentRestaurantIndex(0);
            existing.run();

        } catch (Exception e) {
            r.run();
        }
    }
}
