package zad1;

public class GetRateForException extends Exception{
	public String message = "Exception occurs when trying to get rate.";
	
	GetRateForException(){}
	
	GetRateForException(String message){
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
