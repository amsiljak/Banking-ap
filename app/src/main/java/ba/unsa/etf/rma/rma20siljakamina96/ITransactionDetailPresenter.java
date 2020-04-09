package ba.unsa.etf.rma.rma20siljakamina96;

import android.os.Parcelable;

import java.util.Date;

public interface ITransactionDetailPresenter {
    void create(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate);
    Transaction getTransaction();
    Account getAccount();
    void create(String title, double amount, Type type, String itemDescription, Date date);

    void setTransaction(Parcelable movie);
}
