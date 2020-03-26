package zad1;

import java.util.function.Function;

public class InputConverter<T> {
	
	public T data;
	
	public InputConverter(T in) {
		this.data = in;
	}
	
	public <T> T convertBy (Function...fn) {
		
		Object result = data;
		
		for(Function f : fn) {
			result = f.apply(result);
		}
		return (T) result;
		
	}
}
