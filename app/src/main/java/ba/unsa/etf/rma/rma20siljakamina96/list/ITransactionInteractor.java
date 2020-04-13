package ba.unsa.etf.rma.rma20siljakamina96.list;

import java.util.ArrayList;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;

public interface ITransactionInteractor {
    ArrayList<Transaction> getTransactions();
}
