package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class DictionaryServer extends Thread {
	private ServerSocket serverSocket;
    private Boolean working = false;
    private Map<String, String> dictionary = new HashMap<String, String>();
    private String lang;
    private String host;
    private int port;
    
    public DictionaryServer(String lang, String host, int port, Map<String, String> dictionary) {
    	this.lang = lang;
    	this.host = host;
    	this.port = port;
    	this.dictionary = dictionary;
    }
    
	public static void main(String[] args) {
		Map<String, String> englishDictionary = new HashMap<String, String>();
		englishDictionary.put("samochód", "car");
		englishDictionary.put("cześć", "hello");
		DictionaryServer englishServer = new DictionaryServer("ENG", "127.0.0.1", 12000, englishDictionary);
		
		
		Map<String, String> frenchDictionary = new HashMap<String, String>();
		frenchDictionary.put("samochód", "voiture");
		frenchDictionary.put("cześć", "partie");
		DictionaryServer frenchServer = new DictionaryServer("FR", "127.0.0.1", 12001, frenchDictionary);

		
		englishServer.start();
		frenchServer.start();
	
	}

	public void run() {
		try {
			startListening();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
    public void startListening() throws IOException {
    	ServerSocket serverSocket = new ServerSocket(port);
    	working = true;
        System.out.println("Language Server("+lang+") is waiting for requests on port: " + port);
        while(working) {
        	 Socket clientSocket = serverSocket.accept();
        	 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

             String inputLine = in.readLine();
             String[] query = inputLine.split(",");
             
             if(query.length == 2) {
            	 String polishWord = query[0];
            	 int destinationPort = Integer.parseInt(query[1]);
            	 
            	 if(dictionary.containsKey(polishWord)) {           		
            		String translatedWord = dictionary.get(polishWord);
            		       			
            		Socket languageSocket = new Socket(host, destinationPort);
            		PrintWriter languageOut = new PrintWriter(languageSocket.getOutputStream(), true);
            		BufferedReader LanguageIn = new BufferedReader(new InputStreamReader(languageSocket.getInputStream()));
            		
	            	try {
	
	            		System.out.println("Translating:{" + polishWord + "} to: {" + translatedWord + "} destPort:{" + destinationPort + "}");
	            		
	            		languageOut.println(translatedWord);
	            		String queryResult = LanguageIn.readLine();
	            		
	            		if("Ok.".equals(queryResult)) {
	               		 	out.println("Ok.");
	            		}else {
	               		 	out.println("Internal Error.");
	            		}
               		 	
            		}catch(Exception ex) {
        				ex.printStackTrace();
               		 	out.println("Internal Error.");
            		} finally {
            			languageOut.close();
            			LanguageIn.close();
            			languageSocket.close();
            		}
            		 
            	 }else {
            		 out.println("Translation Not Found.");
            	 }
            	 
             }
             else if ("Hello".equals(inputLine)) {
                out.println(this.lang);
             }
             else {
        		 out.println("Bad Request.");
             }
             
             in.close();
             out.close();
     		 clientSocket.close();
        }
        System.out.println("Closing Language Server");
        serverSocket.close();
    }
}
