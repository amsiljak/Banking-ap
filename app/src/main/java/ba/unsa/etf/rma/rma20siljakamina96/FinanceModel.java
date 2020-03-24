package ba.unsa.etf.rma.rma20siljakamina96;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class FinanceModel {
    public static Account account = new Account(50000, 40000, 1000);
    public static ArrayList<Transaction> transactions = new ArrayList<Transaction>() {
        {
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14"),2000,"naziv",
                        Type.INDIVIDUALPAYMENT, "opis", new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
}
