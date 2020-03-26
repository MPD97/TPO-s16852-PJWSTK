package zad1;

public class GetNBPRateException extends Exception{
	public String message = "Exception occurs when trying to get NBP rate.";
	
	GetNBPRateException(){}
	
	GetNBPRateException(String message){
		this.message = message;
	}
	@Override
	public String toString() {
		return message;
	}
}