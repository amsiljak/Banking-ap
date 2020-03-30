package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Context;

import java.util.Date;

public class TransactionDetailPresenter implements ITransactionDetailPresenter {
    private Transaction transaction;
    private Context context;
    private IFinanceInteractor interactor;
    public TransactionDetailPresenter(Context context) {
        this.context = context;
        this.interactor = new FinanceInteractor();
    }

    public boolean checkBudget(double iznos) {
        if(iznos > interactor.getAccount().getBudget() || iznos > interactor.getAccount().getMonthLimit()) return true;
        return false;
    }

    @Override
    public void create(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
        this.transaction = new Transaction(date, amount, title, type, itemDescription, transactionInterval, endDate);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

}
