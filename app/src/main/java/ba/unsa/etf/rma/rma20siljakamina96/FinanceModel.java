package ba.unsa.etf.rma.rma20siljakamina96;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FinanceModel {
    public static Account account = new Account(50000, 40000, 1000);
    public static Calendar cal = Calendar.getInstance();
    public static ArrayList<Transaction> transactions = new ArrayList<Transaction>() {
        {
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-9"),2000,"naziv",
                        Type.INDIVIDUALPAYMENT, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-14"),1000,"a",
                        Type.REGULARPAYMENT, "opis", 2, new SimpleDateFormat("yyyy-MM-dd").parse("2020-09-14")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-22"),500,"b",
                        Type.PURCHASE, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }try {
            add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-16"),-100,"naziv",
                    Type.INDIVIDUALINCOME, null));
        } catch (ParseException e) {
            e.printStackTrace();
        }try {
            add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-8"),-20,"naziv",
                    Type.REGULARINCOME, null, 3, new SimpleDateFormat("yyyy-MM-dd").parse("2020-09-14")));
        } catch (ParseException e) {
            e.printStackTrace();
        }




//
//            try {
//            add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14"),2000,"naziv",
//                    Type.INDIVIDUALPAYMENT, "opis", new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14")));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//            try {
//                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14"),2000,"naziv",
//                        Type.INDIVIDUALPAYMENT, "opis", new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14")));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            try {
//                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14"),2000,"naziv",
//                        Type.INDIVIDUALPAYMENT, "opis", new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14")));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            try {
//                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14"),2000,"naziv",
//                        Type.INDIVIDUALPAYMENT, "opis", new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14")));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            try {
//                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14"),2000,"naziv",
//                        Type.INDIVIDUALPAYMENT, "opis", new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14")));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            try {
//                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14"),2000,"naziv",
//                        Type.INDIVIDUALPAYMENT, "opis", new SimpleDateFormat("yyyy-MM-dd").parse("2014-02-14")));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }




        }
    };
}
