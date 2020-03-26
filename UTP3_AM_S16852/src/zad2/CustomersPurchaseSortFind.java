/**
 *
 *  @author Ambroziak Mateusz S16852
 *
 */

package zad2;

import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class CustomersPurchaseSortFind {
	
	String [] customersArrayed =  null;
    Scanner readFile = null;

   public void readFile(String firstName)  {

        try {
            readFile = new Scanner(new File(firstName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(readFile.hasNextLine()) {
            customersArrayed = readFile.nextLine().split(";");
            new Purchase(customersArrayed[0], customersArrayed[1], customersArrayed[2],
               Float.parseFloat(customersArrayed[3]), Float.parseFloat(customersArrayed[4]));
        }
    }

        public void showSortedBy (String by){
                System.out.println(by);

           if (by.equals("Nazwiska")){
               Comparator<Purchase> compare = Comparator.comparing(purchase -> purchase.fullName);
               compare = compare.thenComparing(Comparator.comparing(purchase -> purchase.customerId));
               Purchase.customerPurchases.stream()
                       .sorted(compare)
                       .forEach(System.out::println);
               
           } else if (by.equals("Koszty")) {
               Comparator<Purchase> compare1 = (item1, item2) -> Float.compare(item2.price * item2.amount, item1.price * item1.amount);
               compare1 = compare1.thenComparing(Comparator.comparing(purchase -> purchase.customerId));
               Purchase.customerPurchases.stream()
                       .sorted(compare1)
                       .forEach(items -> System.out.println(items + " (koszt: " + items.amount * items.price + ")"));
               
           } 
           else 
           {
               System.out.println("Wrong type of sorting parameter!!");
           }

                System.out.println();
           }

    public void showPurchaseFor(String id) {
        System.out.println("Klient " + id);
        Purchase.customerPurchases.stream()
                .filter(purchase -> purchase.getCustomerId().equals(id))
                .forEach(System.out::println);
        System.out.println();
    }
}