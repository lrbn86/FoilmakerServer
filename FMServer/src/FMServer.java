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
    
    private String serverName = null;
    private static PrintWriter outToClient = null;
    private static BufferedReader inFromClient = null;
    private static BufferedWriter toUserDB = null;
	private static BufferedWriter toWordleDeck = null;
    
    static HashMap<String, User> userHashMap = new HashMap<>();	//List of all users in the database
    static HashMap<String, User> currentUserHash = new HashMap<>(); //List of users logged onto server
    static HashMap<Integer, Socket> runningSockets = new HashMap<>();	//Sockets connected to clients, key is clientID (int)
    static HashMap<Integer, ClientHandler> runningHandlers = new HashMap<>(); //Request handlers, key is also clientID
    static int clientID = 0;
    
    /**
     * Program's Main Entry Point
     *
     * @param args command-line arguments
     * @throws IOException 
     */
    public static void main(String[] args){
    	int portNumber;
    	
    	//portNumber = Integer.parseInt(args[0]);
    	portNumber = 9999;
    	
    	try{
    		initializeServer(portNumber);
    	}catch(BindException a){
    		System.out.println("Port is already in use");
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	
    }
    
    /*
    *
    * Use HashMap to store user information.
    *
    * */
    public static void initializeServer(int portNumber) throws IOException {

      //Open user database and wordle deck to read and write to
        BufferedReader inUserDB = new BufferedReader(new FileReader("C://Users//Daniel//workspace//Project 4//UserDatabase.txt"));
		BufferedReader inWordleDeck = new BufferedReader(new FileReader("C://Users//Daniel//workspace//Project 4//WordleDeck.txt"));
		toUserDB = new BufferedWriter(new FileWriter("C://Users//Daniel//workspace//Project 4//UserDatabase.txt",true));
		
		//Adds users in database to hashmap
		String line;
    	String[] users;
    
    	while((line = inUserDB.readLine()) != null){
			if(line.isEmpty()){
				continue;
			}
			users = line.split(":");
			User player = new User();
			player.setUsername(users[0]);
			player.setPassword(users[1]);
			userHashMap.put(player.getUsername(), player);
		}
		
		// Listen on portNumber
		
        ServerSocket listener = null;
		
		try {
			System.out.println("Creating socket");
			listener = new ServerSocket(portNumber);   
			
			System.out.println("Listening");
            while (true) {
            	
                // Wait for next client connection, add to hashmap
                runningSockets.put(clientID, listener.accept());
                System.out.println("Client connected: " + runningSockets.get(clientID).getPort());
                runningHandlers.put(clientID, new ClientHandler(runningSockets.get(clientID), clientID));
                runningHandlers.get(clientID).start();
                clientID++;
            }
           
            
        }catch(MalformedURLException e){
        	e.getMessage();
        }finally {
            // Close server socket eventually
        	if(listener != null){
        		listener.close();
        	}
        	for(Socket s: runningSockets.values())
        		if(s != null && !s.isClosed()){
        			s.close();
        		}
        }
    }

    public static String registerUser(String message) throws IOException{
    	String reply = " ";
    	String[] request = parseClientMessage(message);
    	
    	String parameter = request[0];
    	String username = request[1];
    	String password = request[2];
    	
    	String usernameTemp = username.replace('_', 'u');
    	String passwordTemp = password.replace('#', 'h');
    	passwordTemp = passwordTemp.replace('&', 'a');
    	passwordTemp = passwordTemp.replace('$', 'd');
    	passwordTemp = passwordTemp.replace('*', 's');
    	
    	if( message.equals(parameter + "--" + username + "--" + password) == false || parameter.equals("CREATENEWUSER") == false){
    		reply = "RESPONSE--CREATENEWUSER--INVALIDMESSAGEFORMAT";
    	}else if(usernameTemp.isEmpty() == true || usernameTemp.length() >= 10 || isAlphanumeric(usernameTemp) == false){
    		reply = "RESPONSE--CREATENEWUSER--INVALIDUSERNAME";
    	}else if(passwordTemp.isEmpty() == true || passwordTemp.length() >= 10 || isAlphanumeric(passwordTemp) == false || isAllLowerCase(passwordTemp) == true || containsAnyNum(passwordTemp) == false){
    		reply = "RESPONSE--CREATENEWUSER--INVALIDUSERPASSWORD";
    	}else if(userHashMap.containsKey(username) == true){
    		reply = "RESPONSE--CREATENEWUSER--USERALREADYEXISTS";
    	}else{
    		User player = new User();
    		player.setUsername(username);
    		player.setPassword(password);
    		userHashMap.put(username, player);
    		toUserDB.newLine();
    		toUserDB.write("\n" + username + ":" + password + ":" + 0 + ":" + 0 + ":" + 0);
    		toUserDB.close();
    		reply = "RESPONSE--CREATENEWUSER--SUCCESS";
    	}
    	return reply;
    }
    
    //User login
    public static String loginUser(String message) throws IOException{
    	
    	String reply = "";
    	String[] request = parseClientMessage(message);
    	
    	String parameter = request[0];
    	String username = request[1];
    	String password = request[2];
    	
    	if( message.equals(parameter + "--" + username + "--" + password) == false || parameter.equals("LOGIN") == false){
    		reply = "RESPONSE--LOGIN--INVALIDMESSAGEFORMAT";
    	}else if(userHashMap.containsKey(username) == false){
    		reply = "RESPONSE--LOGIN--UNKNOWNUSER";
    	}else if(userHashMap.get(username).getPassword().equals(password) == false){
    		reply = "RESPONSE--LOGIN--INVALIDUSERPASSWORD";
    	}else if(currentUserHash.containsKey(username)){
    		reply = "RESPONSE--LOGIN--USERALREADYLOGGEDIN";
    	}else{
    		//generate token
        	String gameToken = "";
        	String token = "";
    		String charBase[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","!","@","#","$","%","^","&","*","(",")","_","+","-","=","[","]"};
    		Random rand = new Random();
    		
    		for(int i = 1; i < charBase.length; i++){
    			String randomizedBase = charBase[rand.nextInt(i)];
    			gameToken = gameToken.concat(randomizedBase);
    		}
    		token = gameToken.substring(gameToken.length()-10, gameToken.length());
    		
    		//Add to current user hash map
    		User player = new User();
    		player.setUsername(username);
    		player.setPassword(password);
    		currentUserHash.put(username, player);
    		reply = "RESPONSE--LOGIN--SUCCESS--" + token;
    	}
    	return reply;
    }
    
    //Checks to see if string is alphanumeric
    public static boolean isAlphanumeric(String args){
    	boolean isAlphanumeric = false;
    	int alphNumCount = 0;
    	
    	char[] alphanum = new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    	for(int i = 0; i < args.length(); i ++){
    		for(int j = 0; j < alphanum.length; j++){
    			if(args.charAt(i) == alphanum[j]){
    				alphNumCount++;
    			}
    		}
    	}
    	
    	if(alphNumCount == args.length()){
    		isAlphanumeric = true;
    	}
    	
    	return isAlphanumeric;
    }
    
    //Checks to see if string is in lower case
    public static boolean isAllLowerCase(String str){
    	boolean isLowerCase = false;
    	int numLowers = 0;
    	
    	char[] lower = new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    	
    	for(int i = 0; i < str.length(); i++){
    		for(int j = 0; j < lower.length; j++){
    			if(str.charAt(i) == lower[j]){
    				numLowers++;
    			}
    		}
    	}
    	
    	if(numLowers == str.length()){
    		isLowerCase = true;
    	}
    	
    	return isLowerCase;
    }
    
    //Checks for numbers in string
    public static boolean containsAnyNum(String str){
    	boolean hasNum = false;
    	char[] arr = new char[]{'0','1','2','3','4','5','6','7','8','9'};
    	int numNums = 0;
    	
    	for(int i = 0; i < str.length(); i++){
    		for(int j = 0; j < arr.length; j++){
    			if(str.charAt(i) == arr[j]){
    				numNums++;
    			}
    		}
    	}
    	
    	if( 1 <= numNums){
    		hasNum = true;
    	}
    	
    	return hasNum;
    }
    
    private static String[] parseClientMessage(String message) {

        if (message == null)
            return null;
        
        return message.split(FoilMakerNetworkProtocol.SEPARATOR);
    }
    
}
