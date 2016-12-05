/**
 * User.java
 *
 * Keeps details of user
 *
 * @author Brandon Nguyen & Daniel Acevedo, nguye299@purdue.edu & acevedd@purdue.edu, Lab Section G06
 *
 * @version November 22, 2016
 *
 */
public class User {

    private String username;
    private String password;
    private int clientID;	//dynamic ID that allows us to access client socket

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
    
    public int getID(){
    	return this.clientID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setID(int clientID){
    	this.clientID = clientID;
    }
}
