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
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientGUI extends Application {
    private static String host;
    private static int destinationPort;
    public static int MIN_PORT = 11500;
    public static int MAX_PORT = 11600;

    public ClientGUI() {
    
    }

    
    public ClientGUI(String host, int destinationPort) {
        this.host = host;
        this.destinationPort = destinationPort;
    }
    public static void main(String[] args) {
        ClientGUI client = new ClientGUI("127.0.0.1", 11000);

        launch(args);

    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception { 
      primaryStage.setTitle("Tłumacz Klient-Serwer");

      Button buttonGetLanguages = new Button("Pobierz dostępne języki");
      buttonGetLanguages.setMaxSize(500, 150);
      buttonGetLanguages.setPrefHeight(45);
      
      Label labelLanguages = new Label("Przetłumacz na język:");
      
      ComboBox comboBoxLanguages = new ComboBox();
      comboBoxLanguages.setMaxSize(500, 150);
      comboBoxLanguages.setPrefHeight(45);
      
      Label labelWord = new Label("Słowo do przetłumaczenia:");

      TextField textFieldWord = new TextField();
      textFieldWord.setMaxSize(500, 150);
      textFieldWord.setPrefHeight(45);
      
      Button buttonTranslate = new Button("Przetłumacz");
      buttonTranslate.setMaxSize(500, 150);
      buttonTranslate.setPrefHeight(45);

      Label labelResult = new Label("Wynik tłumaczenia:");

      TextField textFieldResult = new TextField();
      textFieldResult.setMaxSize(500, 150);
      textFieldResult.setPrefHeight(45);
      
      VBox vbox = new VBox(buttonGetLanguages, labelLanguages, comboBoxLanguages, labelWord, textFieldWord, buttonTranslate, labelResult, textFieldResult);
      vbox.setSpacing(12);
      
      Scene scene = new Scene(vbox, 390, 360);
      primaryStage.setScene(scene);
      primaryStage.show();
      
      buttonGetLanguages.setOnAction((event) -> 
		{	
			comboBoxLanguages.getItems().clear();
			try {
				String[] languages = getAvailableLanguages();
				for(String lang : languages) {
					comboBoxLanguages.getItems().add(lang);
				}
			}catch(Exception ex) {
				System.out.println(ex);
			}
		});
      buttonTranslate.setOnAction((event) -> 
		{	
			String selectedLanguage = (String) comboBoxLanguages.getValue();
			if(selectedLanguage == null || "".equals(selectedLanguage)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Błąd");
				alert.setHeaderText("Nie wybrano języka docelowego");
				alert.setContentText("Wybierz poprawny język docelowy i spróbój ponownie.");
				alert.showAndWait();
				return;
			}
			
			String word = textFieldWord.getText();
			
			if(word == null || "".equals(word) || word.contains(" ")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Błąd");
				alert.setHeaderText("Podaj słowo do przetłumaczenia.");
				alert.setContentText("Słowo nie może być puste oraz nie może zawierać spacji.");
				alert.showAndWait();
				return;
			}
			try {
				String resultWord = translate(word,selectedLanguage);
				textFieldWord.setText("");
				textFieldResult.setText(resultWord);
			}catch(TranslationNotFoundException e) {
				textFieldWord.setText("");
				textFieldResult.setText("");

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Ostrzeżenie");
				alert.setHeaderText("Nie znaleziono tłumaczenia dla słowa: " + word + " w języku: " + selectedLanguage);
				alert.showAndWait();
			}catch(Exception ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Błąd");
				alert.setHeaderText("Wystąpił błąd podczas przetwarzania");
				alert.showAndWait();
				System.out.println(ex);
			}
			
		});
    }
    
    public Boolean establishConnectionToMainServer() {
        try {

            Socket clientSocket = new Socket(host, destinationPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("Hello");
            String result = in .readLine();

            clientSocket.close(); in .close();
            out.close();
            if ("Ok.".equals(result)) {
                return true;
            } else {
                System.out.println("Error: {" + result + "}");
                return false;
            }
        } catch (Exception ex) {
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
            String result = in .readLine();
            String[] Languages = result.split(",");

            clientSocket.close(); in .close();
            out.close();
            if ("Bad Request.".equals(result)) {
                throw new IOException("Bad Request.");
            } else if ("Internal Error.".equals(result)) {
                throw new IOException("Internal Error.");
            } else {
                return Languages;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public String translate(String wordToTranslate, String language) throws TranslationNotFoundException {
        String translatedWord = "";

        try {
            int listenPort = -1;
            for (int i = MIN_PORT; i <= MAX_PORT; i++) {
                if (available(i)) {
                    listenPort = i;
                    break;
                }
            }
            Socket clientSocket = new Socket(host, destinationPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


            ServerSocket serverSocket = new ServerSocket(listenPort);

            String query = wordToTranslate + "," + language + "," + listenPort;
            System.out.println("Sending query: {" + query + "}");
            out.println(query);
            String result = in .readLine();
            if ("Ok.".equals(result)) {
                Socket resultSocket = serverSocket.accept();
                BufferedReader resultIn = new BufferedReader(new InputStreamReader(resultSocket.getInputStream()));
                PrintWriter resultOut = new PrintWriter(resultSocket.getOutputStream(), true);

                translatedWord = resultIn.readLine();
                resultSocket.close();
                resultIn.close();
                resultOut.close();
                
                if("Translation Not Found.".equals(translatedWord)) {
                	throw new TranslationNotFoundException("Translation Not Found for: " + wordToTranslate);
                }
            } else {
                System.out.println("Error. result: {" + result + "}");
            }
            serverSocket.close();
            clientSocket.close(); in .close();
            out.close();
        } catch (IOException ex) {
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
        } catch (IOException e) {} finally {
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