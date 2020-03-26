package zad1;

public class CurrencyNotFindException extends Exception{
	public String message = "Cannot find the currency.";
	
	CurrencyNotFindException(){}
	
	CurrencyNotFindException(String message){
		this.message = message;
	}
	@Override
	public String toString() {
		return message;
	}
}
