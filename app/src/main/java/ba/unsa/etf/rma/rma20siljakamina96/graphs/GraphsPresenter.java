package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import android.content.Context;

import java.util.ArrayList;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.list.IFinanceView;
import ba.unsa.etf.rma.rma20siljakamina96.list.ITransactionInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionInteractor;

public class GraphsPresenter {
    private Context context;

    private static ITransactionInteractor financeInteractor;
    private IAccountInteractor accountInteractor;
    private IFinanceView view;
    private static ArrayList<Transaction> transactions;

    public GraphsPresenter(IFinanceView view, Context context) {
        this.context = context;
        this.financeInteractor = new TransactionInteractor();
        this.accountInteractor = new AccountInteractor();
        this.view = view;
        transactions = financeInteractor.getTransactions();
    }
}
