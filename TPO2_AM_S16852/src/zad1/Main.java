/**
 *
 *  @author Ambroziak Mateusz S16852
 *
 */

package zad1;

import java.io.IOException;
import org.json.JSONObject;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application{
	
	private static Service s;
	
	private static String city;
	private static String currency;
	
	private static String weatherJson;
	private static Double rate1;
	private static Double rate2;
	private static String wikiAddress;
	
	private Scene mainScene; 
	private BorderPane mainContainer;

	public static void main(String[] args) throws IOException, GetRateForException, GetNBPRateException{	
		
		
		s = new Service("Polska");
		
		city = "Warszawa";
		weatherJson = s.getWeather(city);
		
		currency = "USD";
		rate1 = s.getRateFor(currency);
		
		rate2 = s.getNBPRate();
		wikiAddress = s.GetWikiStringUrl(city);
		
		launch(args);    
	}
	
	@Override
	public void start(Stage primaryStage) throws CurrencyNotFindException {   
		prepareScene(primaryStage);
		      
		primaryStage.setTitle("Graphic User Interface - " + s.GetCountry());
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}


	private void prepareScene(Stage primaryStage) throws CurrencyNotFindException {    
  
		WebView browser = new WebView();
		
		mainContainer = new BorderPane();
		mainContainer.setPadding(new Insets(15, 15, 15, 15)); 
		  
		TextArea textArea = new TextArea();
		mainContainer.setCenter(textArea);
		  
		BorderPane navContainer = new BorderPane();
		navContainer.setPadding(new Insets(0, 0, 10, 0));  
		  
		HBox buttonsContainer = new HBox(10);
		  
		Button getWeatherButton = new Button("Weather for " + city);
		Button getRateButton = new Button("Rate for " + currency);
		Button getNBPRateButton = new Button("NBP Rate for " + s.GetCurrencyFromCountry(s.GetCountry()));
		Button getWikiButton = new Button("Wikipedia for " + city);
		buttonsContainer.getChildren().addAll(getWeatherButton,
				  getRateButton, getNBPRateButton, getWikiButton);
		  
		getWeatherButton.setOnAction((event) -> 
		{	
			textArea.setText(formatWeather(weatherJson));
			mainContainer.setCenter(textArea);
		});
		  
		getRateButton.setOnAction((event) -> 
		{
			textArea.setText(rate1.toString());
			mainContainer.setCenter(textArea);
		});
		  
		getNBPRateButton.setOnAction((event) -> 	    
		{
			textArea.setText(rate2.toString());
			mainContainer.setCenter(textArea);
		});
		  
		getWikiButton.setOnAction((event) -> 
		{		  
			browser.getEngine().load(wikiAddress);  
			mainContainer.setCenter(browser);
		});
		   
		navContainer.setCenter(buttonsContainer);
		mainContainer.setTop(navContainer);
		        
		mainScene = new Scene(mainContainer, 800, 600);
	}

	public String formatWeather(String rawText) 
	{
		String result = "";;
		String MapUrl = "https://www.google.com/maps/place/@";
		try 
		{
			JSONObject jsonObj = new JSONObject(rawText);
			
			if (!jsonObj.isNull("coord")) 
			{
				JSONObject section = jsonObj.getJSONObject("coord");
				
				MapUrl += section.get("lat").toString() + ",";
				MapUrl += section.get("lon").toString()+ ",13z";
				
				result += "Location: " + MapUrl + "\n";
			}
			
			if (!jsonObj.isNull("main")) 
			{
				JSONObject section = jsonObj.getJSONObject("main");

				result += "Temperature: " + KelvinToCelcius(section.get("temp")) + " celcius \n";
				result += "Minimum temperature: " + KelvinToCelcius(section.get("temp_min")) + " celcius \n";
				result += "Maximum temperature: " + KelvinToCelcius(section.get("temp_max")) + " celcius \n";
				result += "Humidity: " + section.get("humidity") + "%\n";
				result += "Pressure: " + section.get("pressure") + " hPa";
			}
			
	  	} catch (Exception e) {
	  		e.printStackTrace();
		}
  		return result;
	}
	private String KelvinToCelcius(Object object) {
		float kelvin = Float.parseFloat(object.toString());
		float celsius = kelvin - 273.15F;
		return String.format("%.2f",celsius);
	}
}





































