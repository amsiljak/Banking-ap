package ba.unsa.etf.rma.rma20siljakamina96;

import java.util.ArrayList;

public class FinanceInteractor implements IFinanceInteractor {
    @Override
    public Account getAccount() {
        return FinanceModel.account;
    }
    @Override
    public ArrayList<Transaction> getTransactions() {
        return FinanceModel.transactions;
    }
}
