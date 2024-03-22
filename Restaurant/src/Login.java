public class Login {
    private String username;
    private String password;
    private int customerid;

    /**
     * Constructor for a login that assigns a login a username, a passowrd and a customer id
     * @param username name of user
     * @param password password of user
     * @param customerid unique customer id
     */
    public Login(String username, String password, int customerid){
        this.username = username;
        this.password = password;
        this.customerid = customerid;
    }
    public int getCustomerid() {
        return customerid;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
