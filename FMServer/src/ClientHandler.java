import java.net.*;
import java.io.*;


public class ClientHandler extends Thread{
	private Socket clientSocket;
	private int clientID;
	private String gameToken;
	private boolean running;
	
	public ClientHandler(Socket clientSocket, int clientID){
		this.clientSocket = clientSocket;
		this.clientID = clientID;
	}
	
	public void run(){
		try{
			running = true;
			while(running){	
				clientInteraction();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void clientInteraction()throws IOException{
		PrintWriter outToClient = null;
		BufferedReader inFromClient = null;
		String inputLine, outputLine = null;
		
		try{
			outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
			inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			//implement while loop that runs while client is connected, this will handle requests
			while((inputLine = inFromClient.readLine()) != null){
				System.out.println("Request from client: " + inputLine);
				String[] arr = inputLine.split("--");
				
				if(arr[0].equals("CREATENEWUSER")){
					outputLine = FMServer.registerUser(inputLine);
					outToClient.println(outputLine);
					System.out.println("Sent to client: " + outputLine);
				}else if(arr[0].equals("LOGIN")){
					outputLine = FMServer.loginUser(inputLine, this.clientID);
					outToClient.println(outputLine);
					String[] arr2 = outputLine.split("--");
					this.gameToken = arr2[3];
					System.out.println("Sent to client: " + outputLine);
				}else if(arr[0].equals("STARTNEWGAME")){
					outputLine = FMServer.startNewGame(inputLine, outToClient, inFromClient, this.clientSocket);
					outToClient.println(outputLine);
				}else if(arr[0].equals("JOINGAME")){
					outputLine = FMServer.joinGame(inputLine, outToClient, inFromClient, this.clientSocket);
					outToClient.println(outputLine);
				}else if(arr[0].equals("ALLPARTICIPANTSHAVEJOINED")){
					FMServer.sessionStart(inputLine);
					
				}
			}
			
		}catch(IOException e){
			System.out.println("Unexpected interruption");
			FMServer.currentUserHash.remove(this.gameToken);
			closeSocket();
		}finally{
			if(outToClient != null)
				outToClient.close();
			if(inFromClient != null)
				inFromClient.close();
				System.out.println("User " + clientID + " disconnected");
			clientSocket.close();
			FMServer.currentUserHash.remove(this.gameToken);
			closeSocket();
			}
		}
	
	public void closeSocket(){
		running = false;
	}
}
