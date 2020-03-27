package ba.unsa.etf.rma.rma20siljakamina96;

import java.util.ArrayList;

public interface IFinanceView {

    void notifyTransactionDataSetChanged();

    void setAccountData(String globalAmount, String limit);
    void setTransactions(ArrayList<Transaction> transactions);
    void setDate();
}
