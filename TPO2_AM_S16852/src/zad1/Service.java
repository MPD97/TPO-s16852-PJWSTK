/**
 *
 *  @author Ambroziak Mateusz S16852
 *
 */

package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Service {
	
	private String _country;
	
	private String _wheatherApiKey = "6903e279ebedda8a657b19322c04b458";
	private String _NBPA = "http://www.nbp.pl/kursy/kursya.html";
	private String _NBPB = "http://www.nbp.pl/kursy/kursyb.html";
	
    private Map<String, String[]> _currencies;
	
	public Service (String country)
	{
		_country = country.toLowerCase();
		_currencies = getAvailableCurrencies();
	}
	
	public String getWeather(String city) throws IOException
	{
		String resultJson = "";
		
		String ccode="";
		try {
			ccode = GetCountryCodeFromCountry(_country);
		} catch (CurrencyNotFindException e) {
	
			e.printStackTrace();
		}
		
		URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+ city +",,"+ ccode +"&appid="+ _wheatherApiKey);
   		System.out.println(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
       
		String line;
 
		while ((line = in.readLine()) != null) {
			resultJson += line;
		}
	    in.close();


		return resultJson;
	}
	
	public double getRateFor(String currency) throws GetRateForException
	{
		currency = currency.toUpperCase();
		
		String resultJson = "";
	
		URL url;
		
		String countryCurrency;
		try {
			countryCurrency = GetCurrencyFromCountry(_country);
		} catch (CurrencyNotFindException e1) {
			e1.printStackTrace();
			throw new GetRateForException("Cannot complete the task due to error in getting currency.");
		}
		try {
			url = new URL("https://api.exchangeratesapi.io/latest?base=" + countryCurrency);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new GetRateForException("Exception occured when trying to get URL.");
		}
   			
        BufferedReader in;
        
		try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new GetRateForException("Exception occured when trying to get stream from URL.");
		}
       
		String line;
 
		try {
			
			while ((line = in.readLine()) != null) {
				resultJson += line;
			}
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new GetRateForException("Exception occured when trying to read lines in input stream.");
		}
		
		

		double rate = new JSONObject(resultJson).getJSONObject("rates").getDouble(currency);
		if(rate == 0.0) {
			throw new GetRateForException("Cannot find rate for: " + currency);
		}
		return rate;
	}

	private double GetCurrencyFromNBP(String currency, String url)
			throws CurrencyNotFindException, IOException {
		
		String rowsSelector = "body > table:nth-child(2) > tbody > tr > td > center > table.pad5 > tbody > tr";
		String currencyCodeSelector = "td";
		

		Document doc = Jsoup.connect(url).get();
		Elements trs = doc.select(rowsSelector);
		for (Element tr : trs) 
		{
			Elements tds = tr.select(currencyCodeSelector);
			
			String currencyCode = tds.eq(1).text().toUpperCase();
			if (currencyCode.contains(currency)) {
				String currencyRatio = tds.eq(2).text();
				//System.out.println("Currency for: " + currencyCode + " is: " + currencyRatio );
				return Double.parseDouble(currencyRatio.replace(',', '.'));
			}
		}

		throw new CurrencyNotFindException("Currency: "+ currency +" was not find in site: " + url);
	}
	
	public double getNBPRate() throws GetNBPRateException{

		String currency;
		try {
			currency = GetCurrencyFromCountry(_country);
		} catch (CurrencyNotFindException e1) {
			e1.printStackTrace();
			throw new GetNBPRateException("Cannot complete the task due to error in getting currency.");
		}
		
		try {
			try {
				double tyleToPLN = GetCurrencyFromNBP(currency, _NBPA);
				double jedenObcyTo = tyleToPLN;
				return jedenObcyTo;
			} catch (CurrencyNotFindException e) {
				try {
					double tyleToPLN =  GetCurrencyFromNBP(currency, _NBPB);
					double jedenObcyTo = tyleToPLN;
					return jedenObcyTo;
				} catch (CurrencyNotFindException e1) {
					if(currency.toUpperCase().equals("PLN")) {
						return 1.0;
					}
					e1.printStackTrace();
					throw new GetNBPRateException("Cannot find the currency: "+currency+" in all available resources");
				}
			} 
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new GetNBPRateException("Cannot complete the task due to IOException when getting currency from NBP.");
		}	
	}

	public String GetCurrencyFromCountry(String country) throws CurrencyNotFindException {
		String[] result = _currencies.get(country);
		if(result.length == 2 && result[0] != null) {
			return result[0].toUpperCase();
		}
		throw new CurrencyNotFindException("Currency for country: " + country + " was not find in Locales");
	}
	public String GetCountryCodeFromCountry(String country) throws CurrencyNotFindException {
		String[] result = _currencies.get(country);
		if(result.length == 2 && result[1] != null) {
			return result[1].toUpperCase();
		}
		throw new CurrencyNotFindException("Currency for country: " + country + " was not find in Locales");
	}
	private Map<String, String[]> getAvailableCurrencies() {        
        Map<String, String[]> currencies = new TreeMap<>();

        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            String cc = "NULL";
            try {
            	cc = Currency.getInstance(l).getCurrencyCode();
            } catch(NullPointerException e) {
            	
            }
            currencies.put(l.getDisplayCountry().toLowerCase(),new String[] {cc, iso});  
            //System.out.println(l.getDisplayCountry() +" : " + iso +" : " + cc);
        }
        return currencies;
    }

	public String GetWikiStringUrl(String city) {
		return "https://pl.wikipedia.org/wiki/" + city;
	}
	public String GetCountry() {
		return this._country;
	}
}  
