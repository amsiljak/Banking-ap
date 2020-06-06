package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.content.Context;

public interface ITransactionListDelete {
    void delete(String date, String amount, String title, String type, String  itemDescription, Integer transactionInterval, String endDate, Integer id, Context context);
//    void delete(String id, Context context);
}
