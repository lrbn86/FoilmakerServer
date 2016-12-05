import java.net.*;
import java.io.*;
import java.util.HashMap;

public class GameSession extends Thread{
	public User gameLeader;
	private Socket clientSocket;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean start;
	final String gameKey;
	static int minPlayers = 2;
	private int numPlayers = 0;
	private HashMap<String, User> currentParticipants= new HashMap<>(); //Username is key
	
	public GameSession(User user, String gameKey){
		this.gameKey = gameKey;
		this.currentParticipants.put(user.getUsername(), user);
		this.numPlayers++;
	}
	
	public void setGameLeader(User gameLeader){
		this.gameLeader = gameLeader;
	}
	
	public void run(){
		
		
		try{
			writer = new PrintWriter(FMServer.runningSockets.get(gameLeader.getID()).getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(FMServer.runningSockets.get(gameLeader.getID()).getInputStream()));
			
			if(minPlayers <= numPlayers)
				writer.println("NEWPARTICIPANTS--");
				
			while(minPlayers <= numPlayers){
				gameStart();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void gameStart()throws IOException{
		try{
			String inputLine;
			while((inputLine = reader.readLine()) != null){
				
				
			}
			
		}catch(IOException e){
			System.out.println("Unexpected interruption");
			clientSocket.close();
			start = false;
		}finally{
			if(writer != null){
				writer.close();
			}
			if(reader != null){
				reader.close();
			}
			clientSocket.close();
			start = false;
		}
		
	}
	
	public HashMap<String, User> getCurrentParticipants(){
		return this.currentParticipants;
	}
	
	public void incNumPlayers(){
		this.numPlayers++;
	}
	

}
