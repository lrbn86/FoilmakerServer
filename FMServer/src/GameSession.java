import java.net.*;
import java.io.*;
import java.util.HashMap;

public class GameSession extends Thread{
	private User gameLeader;
	private Socket clientSocket;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean start;
	final String gameKey;
	static int minPlayers = 2;
	static HashMap<String, User> currentParticipants= new HashMap<>();
	
	public GameSession(User gameLeader, String gameKey, PrintWriter writer, BufferedReader reader, Socket clientSocket){
		this.gameLeader = gameLeader;
		this.gameKey = gameKey;
		this.writer = writer;
		this.reader = reader;
		this.clientSocket = clientSocket;
	}
	
	//define run() , probably should run when game leader specifies some boolean start = true;
	public void run(){
		try{
			start = true;
			while(start){
				gameStart();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void gameStart()throws IOException{
		writer.println("Waiting for participants");
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
}
