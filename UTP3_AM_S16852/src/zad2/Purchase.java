/**
 *
 *  @author Ambroziak Mateusz S16852
 *
 */

package zad2;

import java.util.List;
import java.util.ArrayList;
public class Purchase {
	 protected static List<Purchase> customerPurchases = new ArrayList<>();

	  protected String customerId;
	  protected String fullName;
	  protected String product;
	  protected float price;
	  protected float amount;

	    Purchase(String customerId, String fullName, String product, float price, float amount) {
	        this.customerId = customerId;
	        this.fullName = fullName;
	        this.product = product;
	        this.price = price;
	        this.amount = amount;
	        customerPurchases.add(this);
	    }


	    public String getCustomerId() {
	        return customerId;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o)
	            return true;
	        if (o == null || getClass() != o.getClass())
	            return false;
	        Purchase p = (Purchase) o;
	            return  Float.compare(p.price, price) == 0 &&
	                    Float.compare(p.amount, amount) == 0 &&
	                    customerId.equals(p.customerId) &&
	                    fullName.equals(p.fullName) &&
	                    product.equals(p.product);
	    }

	    @Override
	    public String toString() {
	        return "" + this.customerId + ";" + this.fullName + ";" + this.product + ";" + this.price + ";" + this.amount;
	    }
}