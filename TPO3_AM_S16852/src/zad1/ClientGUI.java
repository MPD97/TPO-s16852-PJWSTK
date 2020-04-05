package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientGUI {
    private String host;
    private int destinationPort;
    
    public ClientGUI(String host, int destinationPort) {
    	this.host = host;
    	this.destinationPort = destinationPort;
    }
	public static void main(String[] args) {
		ClientGUI client = new ClientGUI("127.0.0.1",11000);
		if(client.establishConnectionToMainServer()) {
			System.out.println("Connection Established.");
		}
		String[] languages = client.getAvailableLanguages();
		for(String lang : languages){
			System.out.println("Available language: {"+lang+"}");
		}
	}
	public Boolean establishConnectionToMainServer() {
		try {
			
			Socket clientSocket = new Socket(host, destinationPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			out.println("Hello");
			String result = in.readLine();
			
			clientSocket.close();
			in.close();
			out.close();
			if("Ok.".equals(result)) {
				 return true;
			}else {
				System.out.println("Error: {"+result+"}");
				return false;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;

	}
	public String[] getAvailableLanguages() {
		try {
			
			Socket clientSocket = new Socket(host, destinationPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			out.println("Get Languages");
			String result = in.readLine();
			String[] Languages = result.split(",");
			
			clientSocket.close();
			in.close();
			out.close();
			if("Bad Request.".equals(result)) {
				 throw new IOException("Bad Request.");
			}else if("Internal Error.".equals(result)) {
				 throw new IOException("Internal Error.");
			}else {
				return Languages;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
}
