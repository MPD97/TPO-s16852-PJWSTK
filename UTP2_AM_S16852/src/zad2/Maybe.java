package zad2;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.NoSuchElementException;
import java.util.function.Consumer;


public class Maybe <T> {
	
	private T data;

    public Maybe() {}

    public Maybe(T in) {
    	data = in;
    }

    public static <T> Maybe<T> of(T x) {
        if (x == null)
            return new Maybe();
        else
           return new Maybe(x);
    }

    public void ifPresent(Consumer cons) {
        if (data != null)
            cons.accept(data);
    }

    public <U> Maybe<U> map(Function<T, U> func) {

        if (data != null)
            return new Maybe(func.apply(data));
        else
            return new Maybe();
    }

    public T get() {
        if (data != null)
            return data;
        else
            throw new NoSuchElementException(" maybe is empty");
    }

    public T orElse(T defVal) {
        if (data != null)
            return data;
        else
            return defVal;
    }

    public boolean isPresent() {
        if (data != null)
            return true;
        else
            return false;
    };


    public Maybe<T> filter(Predicate<T> pred) {

        if (data != null && pred.test(data))
            return this;
        else
            return new Maybe();
    };

    @Override
    public String toString() {

        if (data != null)
            return "Maybe has value " + data;
        else
            return "Maybe is empty";

    };
}
