package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.content.Context;

public interface ITransactionListChange {
    void save(String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate, Context context);

    void update(String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate, String id, Context context);
}
