package ba.unsa.etf.rma.rma20siljakamina96.list;

import java.util.ArrayList;

import ba.unsa.etf.rma.rma20siljakamina96.data.FinanceModel;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;

public class TransactionInteractor implements ITransactionInteractor {

    @Override
    public ArrayList<Transaction> getTransactions() {
        return FinanceModel.transactions;
    }

}
