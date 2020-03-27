package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Context;

import java.util.Date;

public class TransactionDetailPresenter implements ITransactionDetailPresenter {
    private Transaction transaction;
    private Context context;

    public TransactionDetailPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void create(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
        this.transaction = new Transaction(date, amount, title, type, itemDescription, transactionInterval, endDate);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
