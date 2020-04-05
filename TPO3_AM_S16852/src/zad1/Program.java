package zad1;

import java.io.IOException;

public class Program {

	public static void main(String[] args) {
        GreetServer server=new GreetServer();
        try {
        	
			server.start(6666);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        GreetClient client = new GreetClient();
        try {
			client.startConnection("127.0.0.1", 6666);
			String response = client.sendMessage("hello server");
			
			System.out.println(response);
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
}
