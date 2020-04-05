package zad1;

import java.io.*;
import java.net.*;

public class GreetClient {
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
 
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
 
    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = "";
		resp = in.readLine();
        return resp;
    }
 
    public void stopConnection() {
	    try {
	    	
			in.close();
	        out.close();
	        clientSocket.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
