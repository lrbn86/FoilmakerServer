import java.net.*;
import java.io.*;

public class GameSession extends Thread{
	private User gameLeader;
	private User gameParticipants;
	final String gameToken;
	static int minPlayers = 2;
	
	public GameSession(User gameLeader, String gameToken){
		this.gameLeader = gameLeader;
		this.gameToken = gameToken;
	}
	
	//define run() , probably should run when game leader specifies some boolean start = true;
	public void run(){
		try{
			while(true){
				gameStart();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void gameStart()throws IOException{
		
	}
}
