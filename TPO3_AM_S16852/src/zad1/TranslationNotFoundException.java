package zad1;

public class TranslationNotFoundException extends Exception {

	private String msg = "";
	
	public TranslationNotFoundException() {
	}
	
	public TranslationNotFoundException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return this.msg;
	}
}
