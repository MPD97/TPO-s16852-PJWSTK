package zad1;

import java.io.*;
import java.net.*;
import java.util.*;

public class MainServer extends Thread{
	private ServerSocket serverSocket;
    private Boolean working = false;
    private Map<String, Integer> dictionary = new HashMap<String, Integer>();
    private String host;
    private int port;
    
    public MainServer(String host, int port) {
    	this.host = host;
    	this.port = port;
    }
	public static void main(String[] args) {
		MainServer mainServer = new MainServer("127.0.0.1",11000);
		mainServer.setDictionaries(12000,12001);
		mainServer.run();
		
	}
	public void run() {
		try {
			startListening();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
    private void startListening() throws IOException {
    	ServerSocket serverSocket = new ServerSocket(port);
    	working = true;
        System.out.println("Server is waiting for requests on port: " + port);
        while(working) {
        	 Socket clientSocket = serverSocket.accept();
        	 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

             String inputLine = in.readLine();
             String[] query = inputLine.split(",");
             
             if(query.length == 3) {
            	 String polishWord = query[0];
            	 String languageCode = query[1];
            	 String destinationPort = query[2];
            	 
            	 if(dictionary.containsKey(languageCode)) {           		
            		int LanguageServerPort = dictionary.get(languageCode);
            		       			
            		Socket languageSocket = new Socket(host, LanguageServerPort);
            		PrintWriter languageOut = new PrintWriter(languageSocket.getOutputStream(), true);
            		BufferedReader LanguageIn = new BufferedReader(new InputStreamReader(languageSocket.getInputStream()));
            		
	            	try {
	            		String outputQuery = String.join(",", polishWord, destinationPort);
	            		System.out.println("Forwarding Message: {" + outputQuery + "}");
	            		
	            		languageOut.println(outputQuery);
	            		out.println("Ok.");
            		}catch(Exception ex) {
        				ex.printStackTrace();
               		 	out.println("Internal Error.");
            		}
        			languageSocket.close();
        			languageOut.close();
        			LanguageIn.close();
            		
            	 }else {
            		 out.println("Language Not Supported.");
            	 }
            	 
             }
             else if ("Get Languages".equals(inputLine)) {
           		String[] languages = new String[dictionary.size()];
           		int i = 0;
           		
            	for (String language : dictionary.keySet()) {
            		 languages[i++] = language;
            	}
            	
            	String joinedLanguages = String.join(",", languages);
                out.println(joinedLanguages);
             }
             else if ("Hello".equals(inputLine)) { 
                 out.println("Ok.");
              }
             else {
        		 out.println("Bad Request.");
             }
             
             in.close();
             out.close();
     		 clientSocket.close();
        }
        System.out.println("Closing Server");
        serverSocket.close();
    }
    
    public void setDictionaries(int firstPort, int lastPort) {
	     Socket clientSocket;
	     PrintWriter out;
	     BufferedReader in;
	     Map<String, Integer> newDictionary = new HashMap<String, Integer>();
	     
	     for(int i = firstPort; i <= lastPort; i++) 
	     {
	    	 try {
	    		 clientSocket = new Socket(host, i);
	    		 out = new PrintWriter(clientSocket.getOutputStream(), true);
	    		 in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    		 
	    		 out.println("Hello");
	    		 String language = in.readLine();
				 System.out.println("{"+language + "} Dictionary found on port: {" + i + "}");
	    		 newDictionary.put(language, i);
	    	 }
			 catch (IOException e) {
				System.out.println("Dictionary Server not found on port: {" + i + "}");
			 }     
	     }    	
	     dictionary = newDictionary;
    }
}
