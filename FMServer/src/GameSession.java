import java.net.*;
import java.io.*;
import java.util.HashMap;

public class GameSession{
	public User gameLeader;
	
	private Socket clientSocket;
	private BufferedReader reader;
	private PrintWriter writer;
	private static BufferedReader inWordleDeck = null;
	private boolean start;
	final String gameKey;
	static int minPlayers = 2;
	private int numPlayers = 0;
	private HashMap<String, User> currentParticipants= new HashMap<>(); //Username is key
	
	public GameSession(User user, String gameKey, Socket clientSocket, PrintWriter writer, BufferedReader reader){
		this.gameKey = gameKey;
		this.currentParticipants.put(user.getUsername(), user);
		this.clientSocket = clientSocket;
		this.writer = writer;
		this.reader = reader;
		this.numPlayers++;
	}
	
	public void setGameLeader(User gameLeader){
		this.gameLeader = gameLeader;
	}
	
	public HashMap<String, User> getCurrentParticipants(){
		return this.currentParticipants;
	}
	
	public void incNumPlayers(){
		this.numPlayers++;
	}
	
	public void writePlayer(String message){
		this.writer.println(message);
	}
	
	
	
}
