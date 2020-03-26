package zad1;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class XList<X> extends ArrayList<X> {

    public XList(Collection<X> parameters) {
        this.addAll(parameters);
    }
    @SafeVarargs
    public XList(X... parameters) {
        this.addAll(Arrays.asList(parameters));
    }

    public static <X> XList<X> of(X... x) {
        XList<X> result = new XList(x);
        return result;
    }

    public static XList of(Collection coll) {
        XList result = new XList(coll);
        return result;
    }

    public static XList<String> charsOf(String line) {
        XList<String> result = new XList<>(line.split(""));
        return result;
    }

    public static XList<String> tokensOf(String line, String separator) {
        XList<String> result = new XList<>(line.split(separator));
        return result;
    }

    public static XList<String> tokensOf(String line) {
        XList<String> result = tokensOf(line, " ");
        return result;
    }

    public XList<X> union(Collection<X> coll) {
        XList<X> result = Stream.concat(this.stream(), coll.stream()).collect(Collectors.toCollection(XList::new));
        return result;
    }

    public XList<X> union(X... parameters) {
        XList<X> result = Stream.concat(this.stream(), Arrays.stream(parameters)).collect(Collectors.toCollection(XList::new));
        return result;
    }

    public XList<X> diff(Collection<X> coll) {
        XList<X> result = new XList<>();
        result.addAll(this);
        result.removeAll(coll);
        return result;
    }

    public XList<X> unique() {
        XList<X> result = this.stream().distinct().collect(Collectors.toCollection(XList::new));
        return result;
    }

    public XList<XList<String>> combine() {

        XList<List<X>> source = (XList<List<X>>) this;
        List<String> result = (List<String>) source.get(0);

        for (int i = 0; i < source.size(); i++) {
            List<String> nextList = (List<String>) source.get(i);
            result = result.stream().flatMap(s1 -> nextList.stream().map(s2 -> s1 + s2))
                    .collect(Collectors.toCollection(XList::new));
        }
        XList<XList<String>> combineResult = result.stream()
                .map(Collections::singletonList)
                .map(XList::new)
                .collect(Collectors.toCollection(XList::new));
        return combineResult;
    }
    
    public void forEachWithIndex(BiConsumer<X, Integer> biConsumer) {
        for (int i = 0; i < this.size(); i++)
            biConsumer.accept(this.get(i), i);
    }


}