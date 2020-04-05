package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientGUI {
    private String host;
    private int destinationPort;
    public static int MIN_PORT = 11500; 
    public static int MAX_PORT = 11600; 

    
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
		System.out.println(client.translate("samochód","FR"));
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
	public String translate(String wordToTranslate, String language) {
		String translatedWord = "";

		try {
			int listenPort = -1;
			for	(int i = MIN_PORT; i <= MAX_PORT; i++) {
				if(available(i)) {
					listenPort = i;
					break;
				}
			}
			Socket clientSocket = new Socket(host, destinationPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			
	    	ServerSocket serverSocket = new ServerSocket(listenPort);

			String query = wordToTranslate + "," + language + "," + listenPort;
			System.out.println("Sending query: {" + query +"}");
			out.println(query);
			String result = in.readLine();
			if("Ok.".equals(result)) {
				Socket resultSocket = serverSocket.accept();
	       	 	BufferedReader resultIn = new BufferedReader(new InputStreamReader(resultSocket.getInputStream()));
	            PrintWriter resultOut = new PrintWriter(resultSocket.getOutputStream(), true);
	            
	            translatedWord = resultIn.readLine();
	            
	            resultSocket.close();
	            resultIn.close();
	            resultOut.close();
			}else {
				System.out.println("Error. result: {" + result + "}");
			}
			serverSocket.close();
			clientSocket.close();
			in.close();
			out.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return translatedWord;
	}
	private static boolean available(int port) {
	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}
	
}
