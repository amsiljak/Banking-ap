package ba.unsa.etf.rma.rma20siljakamina96.list;

import java.util.ArrayList;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;

public interface IFinanceView {
    void notifyTransactionListDataSetChanged();

    void setAccountData(Account account);

    void setTransactions(ArrayList<Transaction> transactions);
    void setDate();
}
