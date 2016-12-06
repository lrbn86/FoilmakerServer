import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameSession{
    public User gameLeader;

    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private static BufferedReader inWordleDeck = null;
    private boolean start;
    final String gameKey;
    static AtomicInteger numPlayers = new AtomicInteger();
    private HashMap<String, User> currentParticipants= new HashMap<>(); //Username is key

    public GameSession(User user, String gameKey, Socket clientSocket, PrintWriter writer, BufferedReader reader){
        this.gameKey = gameKey;
        this.currentParticipants.put(user.getUsername(), user);
        this.clientSocket = clientSocket;
        this.writer = writer;
        this.reader = reader;
    }

    public void setGameLeader(User gameLeader){
        this.gameLeader = gameLeader;
    }

    public HashMap<String, User> getCurrentParticipants(){
        return this.currentParticipants;
    }

    public int incNumPlayers() {
        return numPlayers.incrementAndGet();
    }

    public void writePlayer(String message){
        this.writer.println(message);
    }

}
