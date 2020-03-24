package ba.unsa.etf.rma.rma20siljakamina96;

import java.util.ArrayList;

public interface IFinanceInteractor {
    Account getAccount();
    ArrayList<Transaction> getTransactions();
}
