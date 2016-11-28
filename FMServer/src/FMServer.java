import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * FMServer.java
 *
 * Foilmaker Server 
 *
 * @author Brandon Nguyen & Daniel Acevedo, nguye299@purdue.edu & acevedd@purdue.edu, Lab Section G06
 *
 * @version November 22, 2016
 *
 */
public class FMServer {


    // Server Connection Data
    private Socket socket = null;
    private String serverName = null;
    private int serverPortNumber = 0;
    private ServerSocket listener = null;
    private BufferedReader inFromServer = null;
    private PrintWriter outToServer = null;

    static HashMap<String, User> userHashMap = new HashMap<>();

    // TODO: Initialize Server
    /*
    *
    * Use HashMap to store user information.
    *
    * */
    public void intitializeServer() throws IOException {

        // Listen on port 9090
        listener = new ServerSocket(9090);

        try {

            while (true) {

                // Wait for next client connection
                socket = listener.accept();

                // Create data reader
                inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Create data writer
                outToServer = new PrintWriter(socket.getOutputStream(), true);

                // Read client request
                String clientMessage = inFromServer.readLine();
                System.out.println(parseClientMessage(clientMessage));

                // Send reply to client
                // out.println(clientMessage);
            }

        } finally {
            // Close server socket eventually
            listener.close();
        }

    }

    // TODO: Register New User
    /*
    *
    * Username:
    * - Cannot be empty
    * - Length has to be less than 10 characters
    * - Can only have alphanumeric characters or underscores
    * */

    /*
    *
    * Password:
    * - Cannot be empty
    * - Length has to be less than 10 characters
    * - Can only have alphanumeric characters, #, &, $ or *
    * - Should have at least one uppercase letter
    * - Should have at least one digit
    * */

    /*
    *
    * Server receives "CREATENEWUSER" command and handle by:
    *
    * INVALIDMESSAGEFORMAT - Request does not comply with the format given above
    * INVALIDUSERNAME - Invalid username
    * INVALIDUSERPASSWORD - Invalid password
    * USERALREADYEXISTS - User already exists in the user store
    * SUCCESS - User created in the user store successfully
    * */


    private String[] parseClientMessage(String message) {

        if (message == null)
            return null;

        return message.split(FoilMakerNetworkProtocol.SEPARATOR);
    }

    /**
     * Program's Main Entry Point
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) throws IOException {

        FMServer server = new FMServer();

        server.intitializeServer();

    }

}
