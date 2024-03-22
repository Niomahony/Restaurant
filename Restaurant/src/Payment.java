import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Payment extends TableReservation {
    private double amountDue;
    private LocalDate date;
    private double tip;
    /**
     * payment constructor that creates the amountDue object, the date object and the tip object
     * @param amountDue amount due
     * @param date date
     * @param tip tip
     * @throws FileNotFoundException throws FileNotFoundException
     */
    public Payment(double amountDue, LocalDate date, double tip) throws FileNotFoundException {
        this.amountDue = amountDue;
        this.date = date;
        this.tip = tip;
    }
    /**
     * writeToFile method that writes to the csv file
     * @param data
     * @throws FileNotFoundException
     */
    private void writeToFile(String[] data) throws FileNotFoundException {

        CSV("Restaurant/src/payments.csv",data);
            /*FileWriter f = new FileWriter("Restaurant/src/payments.csv", true);
            for (String s : data) {
                f.write(s + ", ");
            }
            f.write("\n");*/

    }

    public void takePayment() throws FileNotFoundException {
        String[] data = { String.valueOf(this.amountDue), String.valueOf(this.date), String.valueOf(this.tip)};
        writeToFile(data);
    }


}