public class Food {
    private String foodName;
    private double price;
    private String timeOfDay;
    /**
     * Food constructor that initializes food name and price
     * @param foodName name of food
     * @param price price of food
     */
    public Food(String foodName, double price) {
        this.foodName = foodName;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getFoodName() {
        return foodName;
    }
    /**
     * toString method for foodName and price which also formats them
     * @return returns food name and price
     */
    public String toString() {
        String s = "";
        s += foodName + " ";
        s += price + "\n";
        return s;
    }


}

