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
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-9"),2000,"Transakcija 1",
                        Type.INDIVIDUALPAYMENT, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-14"),1000,"Transakcija 2",
                        Type.REGULARPAYMENT, "Transakcija 2", 2, new SimpleDateFormat("yyyy-MM-dd").parse("2021-11-14")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-22"),500,"Transakcija 3",
                        Type.PURCHASE, "Transakcija 3"));
            } catch (ParseException e) {
                e.printStackTrace();
            }try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-16"),-100,"Transakcija 20",
                    Type.INDIVIDUALINCOME, null));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-8"),-20,"Transakcija 4",
                        Type.REGULARINCOME, null, 3, new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-02")));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-07-14"),2000,"Transakcija 5",
                        Type.INDIVIDUALPAYMENT, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-08-1"),943.5,"Transakcija 6",
                        Type.REGULARPAYMENT, "opis", 2, new SimpleDateFormat("yyyy-MM-dd").parse("2020-09-14")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-09-30"),30,"Transakcija 7",
                        Type.PURCHASE, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
            add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-16"),-200,"Transakcija 8",
                    Type.INDIVIDUALINCOME, null));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-11-5"),500,"Transakcija 9",
                        Type.REGULARINCOME, null, 3, new SimpleDateFormat("yyyy-MM-dd").parse("2020-09-14")));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-9"),70,"Transakcija 10",
                        Type.INDIVIDUALPAYMENT, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-14"),-73.4,"Transakcija 11",
                        Type.REGULARPAYMENT, "opis", 2, new SimpleDateFormat("yyyy-MM-dd").parse("2022-11-06")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-22"),134,"Transakcija 12",
                        Type.PURCHASE, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }try {
            add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-16"),-50,"Transakcija 13",
                    Type.INDIVIDUALINCOME, null));
        } catch (ParseException e) {
            e.printStackTrace();
        }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-8"),200,"Transakcija 14",
                        Type.REGULARINCOME, null, 3, new SimpleDateFormat("yyyy-MM-dd").parse("2020-09-29")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-07-14"),43,"Transakcija 15",
                        Type.INDIVIDUALPAYMENT, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-08-1"),300,"Transakcija 16",
                        Type.REGULARPAYMENT, "opis", 2, new SimpleDateFormat("yyyy-MM-dd").parse("2021-03-16")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-09-30"),55,"Transakcija 17",
                        Type.PURCHASE, "opis"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-16"),-20,"Transakcija 18",
                        Type.INDIVIDUALINCOME, null));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-11-5"),-20,"Transakcija 19",
                        Type.REGULARINCOME, null, 3, new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-14")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
}
