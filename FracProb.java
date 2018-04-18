import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Caroline on 11/1/17.
 */
public class FracProb{
    static int N = 0;
    static double frac = 0.0;
    public static void main(String[] args) throws IOException{

        // takes command line arguments for frac and N
        N = Integer.parseInt(args[1]);
        frac = Double.parseDouble(args[0]);

        gen(frac, N);
        //System.out.println(outvec);
        writeTable();

    }
    static ArrayList<Integer> outvec = new ArrayList<>();

    static void gen(double fract, int n) {
        int[] temp = new int[n];
        int[] p = perm(n, temp);
        for (int i = 0; i < p.length; i++) {
            //System.out.println(p[i]);
            outvec.add(p[i]);
        }
        // initialize quantPrice before getting fractal distribution since each symbol is represented only once
        initQuantPrice();

        // make rest of outvec
        makeOutvec(fract, p);
        System.out.println("outvec size = " + outvec.size());
    }

    static int[] perm (int n, int[] perms) {
//        if (n < 1) {
//            return perms;
//        }
//        else {
//
//            boolean added = false;
//            while(!added) {
//                int idx = (int)(Math.random() * N);
//                if (perms[idx] == 0) {
//                    perms[idx] = n;
//                    added = true;
//                }
//            }
//
//            return perm(n - 1, perms);
//        }
        if (n < 1) {
            return perms;
        }
        else {
            int max = n;
            for (int i = 0; i < n; i++) {
                boolean added = false;
                while(!added) {
                    int idx = (int)(Math.random() * N);
                    if (perms[idx] == 0) {
                        perms[idx] = max;
                        added = true;
                    }
                }
                max -= 1;
            }
            return perms;
        }
    }

    static void makeOutvec(double fract, int[] perms) {
        if (perms.length <= 1) {
            return;
        }
        int idx = (int)(fract * perms.length);
        int[] temp = new int[idx];
        for (int i = 0; i < idx; i++) {
            outvec.add(perms[i]);
            temp[i] = perms[i];
        }
        makeOutvec(fract, temp);
    }

    static HashMap<Integer, int[]> quantPrice = new HashMap<>();
    static void writeTable() throws IOException {
        PrintWriter tablewriter = new PrintWriter(new FileWriter("table.csv"));

        // write header row
        tablewriter.println("\"" + "stocksymbol" + "\"" + "," + "\"" + "time" + "\"" + "," +
                "\"" + "quantity" + "\"" + "," +  "\"" + "price" + "\"");

        // get time, quantity, and price for each symbol in outvec
        int linenum = 0;
        for (int i = 0; i < 10000000; i++) {
            linenum++;
            int randFrac = (int)(Math.random() * outvec.size());
            genQP(outvec.get(randFrac));
            tablewriter.println("\"" + outvec.get(randFrac) + "\"" + "," + "\"" + i + "\"" + "," + "\"" +
                    quantPrice.get(outvec.get(randFrac))[0] + "\"" + "," + "\"" +
                    quantPrice.get(outvec.get(randFrac))[1] + "\"");
        }
        tablewriter.close();
        System.out.println(linenum);
    }

    static void initQuantPrice() {
        for (int i = 0; i < outvec.size(); i++) {
            quantPrice.put(outvec.get(i), new int[2]);
        }
    }
    static int[] priceInterval = {-5, -4, -3, -2, -1, 1, 2, 3, 4, 5};
    static void genQP(int symbol){
        //quantPrice.get(outvec.get(symbol))[0] = 100 + ((int)(Math.random() * 9901));
        int quant = 100 + ((int)(Math.random() * 9901));
        int price = 0;

        // need to account for price = 0, 50 (can only add 1-5)
        if (quantPrice.get(symbol)[1] > 0) {
            boolean inrange = false;
            while(!inrange) {
                int idx = (int)(Math.random() * 10);
                if ((quantPrice.get(symbol)[1] + priceInterval[idx]) >= 50 &&
                        (quantPrice.get(symbol)[1] + priceInterval[idx]) <= 500) {
                    price = quantPrice.get(symbol)[1] + priceInterval[idx];
                    inrange = true;
                }
            }
        }
        else {
            price = quantPrice.get(symbol)[1] + 50 + ((int)(Math.random() * 451));
        }
        int[] newval = {quant, price};
        quantPrice.put(symbol, newval);

    }
}
