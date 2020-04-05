package zad1;

import java.io.*;
import java.net.*;

public class GreetServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
 
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String greeting = in.readLine();
            if ("hello server".equals(greeting)) {
                out.println("hello client");
            }
            else {
                out.println("unrecognised greeting");
            }
    }
 
    public void stop() {
	  try {
		  
    	in.close();
        out.close();
		clientSocket.close();
        serverSocket.close();
        
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
    }
  
}
