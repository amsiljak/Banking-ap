package ba.unsa.etf.rma.rma20siljakamina96.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Transaction implements Parcelable{
    private Date date;
    private double amount;
    private String title;
    private Type type;
    private String itemDescription;
    private Integer transactionInterval;
    private Date endDate;
    private Integer id;

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public Transaction(Date date, double amount, String title, Type type, String itemDescription, Integer transactionInterval, Date endDate) {
        this.date = date;
        this.amount = amount;
        this.title = title;
        this.type = type;
        this.itemDescription = itemDescription;
        this.transactionInterval = transactionInterval;
        this.endDate = endDate;
    }
    public Transaction(int id, Date date, double amount, String title, Type type, String itemDescription, Integer transactionInterval, Date endDate) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.title = title;
        this.type = type;
        this.itemDescription = itemDescription;
        this.transactionInterval = transactionInterval;
        this.endDate = endDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getTransactionInterval() {
        return transactionInterval;
    }

    public void setTransactionInterval(int transactionInterval) {
        this.transactionInterval = transactionInterval;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public static Comparator<Transaction> TranPriceComparatorAsc = new Comparator<Transaction>() {

        public int compare(Transaction t1, Transaction t2) {
            double amount1 = t1.getAmount();
            double amount2 = t2.getAmount();

            //ascending order
            return (int) (amount1 - amount2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
    public static Comparator<Transaction> TranPriceComparatorDesc = new Comparator<Transaction>() {

        public int compare(Transaction t1, Transaction t2) {
            double amount1 = t1.getAmount();
            double amount2 = t2.getAmount();

//            //ascending order
//            return (int) (amount1 - amount2);

            //descending order
            return (int) (amount2-amount1);
        }
    };
    public static Comparator<Transaction> TranTitleComparatorAsc = new Comparator<Transaction>() {

        public int compare(Transaction t1, Transaction t2) {
            String title1 = t1.getTitle();
            String title2 = t2.getTitle();

            return title1.compareTo(title2);
        }
    };
    public static Comparator<Transaction> TranTitleComparatorDesc = new Comparator<Transaction>() {

        public int compare(Transaction t1, Transaction t2) {
            String title1 = t1.getTitle();
            String title2 = t2.getTitle();

            return title2.compareTo(title1);
        }
    };
    public static Comparator<Transaction> TranDateComparatorAsc = new Comparator<Transaction>() {

        public int compare(Transaction t1, Transaction t2) {
            Date date1 = t1.getDate();
            Date date2 = t2.getDate();

            return date1.compareTo(date2);
        }
    };
    public static Comparator<Transaction> TranDateComparatorDesc = new Comparator<Transaction>() {

        public int compare(Transaction t1, Transaction t2) {
            Date date1 = t1.getDate();
            Date date2 = t2.getDate();

            return date2.compareTo(date1);
        }
    };
    protected Transaction(Parcel in) {

        try {
            this.date = DATE_FORMAT.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.amount = in.readDouble();
        this.title = in.readString();
        this.type = Type.valueOf(in.readString());
        this.itemDescription = in.readString();;
        this.transactionInterval = in.readInt();
        try {
            this.endDate = DATE_FORMAT.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DATE_FORMAT.format(date));
        dest.writeDouble(amount);
        dest.writeString(title);
        dest.writeString(type.toString());
        dest.writeString(itemDescription);
        dest.writeInt(transactionInterval);
        dest.writeString(DATE_FORMAT.format(endDate));
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", itemDescription='" + itemDescription + '\'' +
                ", transactionInterval=" + transactionInterval +
                ", endDate=" + endDate +
                ", id=" + id +
                ", DATE_FORMAT=" + DATE_FORMAT +
                '}';
    }

}
