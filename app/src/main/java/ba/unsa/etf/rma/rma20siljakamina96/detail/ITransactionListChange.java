package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.content.Context;

public interface ITransactionListChange {
    void save(String date, String amount, String title, String type, String itemDescription, Integer transactionInterval, String endDate, Context context);

    void update(String date, String amount, String title, String type, String itemDescription, Integer transactionInterval, String endDate, Integer id, Context context);
}
