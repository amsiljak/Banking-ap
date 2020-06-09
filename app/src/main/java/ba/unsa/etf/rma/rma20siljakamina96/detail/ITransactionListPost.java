package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.content.Context;

interface ITransactionListPost {
    void save(String date, Double amount, String title, String type, String itemDescription, Integer transactionInterval, String endDate, Context context);
}
