package examlpe;
import java.util.Scanner;
public class Program {

    public static void main (String[] args){

        boolean flaga = true;
        int val1,val2;
        boolean flagaScanner=true;
        Scanner scanner = new Scanner(System.in);
        String znak;

        do {
            System.out.println("Podaj pierwszą liczbę:");
            val1 = scanner.nextInt();

            System.out.println("Podaj drugą liczbę:");
            val2 = scanner.nextInt();

            if (val1 == 0 && val2 == 0)
                System.exit(0);
            else if(val1 >= 0 && val2 >=0){ 
            
                System.out.print("Podaj operacje:");
                znak = scanner.next();

                System.out.println(val1+" - "+ convertToBinary(val1));
                System.out.println(val2+" - "+ convertToBinary(val2));

                String znakDodawania = "+";
                String znakOdejmowania = "-";
                String znakMnożenia = "*";
                String znakDzielenia ="/";

                if (stringCompare(znak,znakDodawania)==1){
                    System.out.println(val1+val2+" - " + convertToBinary(add(val1,val2)));
                }
                else if (stringCompare(znak,znakOdejmowania)==1){
                    System.out.println(val1-val2+" - " + convertToBinary(subtract(val1,val2)));
                }
                else if (stringCompare(znak,znakDzielenia)==1){
                    if (val2 == 0)
                        System.out.println("Nie można dzielić przez 0!");
                    else
                        System.out.println(val1/val2+" - " + convertToBinary(divide(val1,val2)));
                }
                else if (stringCompare(znak,znakMnożenia)==1){
                    System.out.println(val1*val2+" - "+convertToBinary(multiply(val1,val2)));
                }
                else
                    System.out.println("Nie przewidziano takiej operacji. Wprowadź ponownie wartości");                              
            }
            else
                System.out.println("Obie liczby muszą należeć do zbioru liczb naturalnych!");
        }
        while (flagaScanner);



    }
    public static int add(int val1, int val2) {

        boolean flagAdding = true;
        int a = val1;
        int b = val2;
        int c,d;

        do {
            c = (a ^ b);
            d = (a & b);
            d = (d << 1);

            if (d == 0) {
                flagAdding = false;

            } else {
                a = c;
                b = d;
            }
        } while (flagAdding);

        return c;
    }

    public static int subtract(int val1, int val2){

        int a= val1;
        int b= val2;

        while (b!=0) {
            int tmp = (~a) & b;
            a = a^ b;
            b = tmp << 1;
        }
        return a;

    }

    public static int divide(int val1, int val2){

        int maxInt= 2147483647;
        boolean negative = false;

        if ((val1 & (1 << 31)) == (1 << 31)) {
            negative = !negative;
            val1 = add(~val1, 1);
        }

        if ((val2 & (1 << 31)) == (1 << 31)) {
            negative = !negative;
            val2 = add(~val2, 1);
        }

        int quotient = 0;
        long r;
        for (int i = 30; i >= 0; i = subtract(i, 1)) {
            r = (val2 << i);

            if (r < maxInt && r >= 0) {
                if (r <= val1) {
                    quotient |= (1 << i);
                    val1 = subtract(val1, (int) r);
                }
            }
        }
        if (negative) {
            quotient = add(~quotient, 1);
        }
        return quotient;
    }

    public static int multiply(int val1, int val2){

        if (val2 == 0)
            return 0;

        else{

            int result = 0;
            while (val2 > 0)
            {
                if ((val2 & 1) != 0)
                    result = result + val1;

                val1 = val1 << 1;
                val2 = val2 >> 1;
            }
            return result;
        }
    }

    public static String convertToBinary(int x){
        String binary = "";
        if (x == 0)
            return "0";

        else {
            while (x > 0) {
                int tmp = x % 2;
                binary = tmp + binary;
                x = x / 2;
            }
        }
        return binary;
    }

    public static int stringCompare(String string1, String string2)
    {
        int slowo1 = string1.length();
        int slowo2 = string2.length();

        for (int i = 0; i < slowo1 && i < slowo2; i++) {
            int str1_char = (int)string1.charAt(i);
            int str2_char = (int)string2.charAt(i);

            if (str1_char == str2_char) {
                continue;
            } else {
                return str1_char - str2_char;
            }
        }
        if (slowo1 < slowo2) {
            return slowo1 - slowo2;
        } else if (slowo1 > slowo2) {
            return slowo1 - slowo2;
        } else {
            return 1;
        }
    }

}

