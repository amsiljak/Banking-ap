package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.content.Context;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.list.ITransactionInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListInteractor;


public class AccountPresenter implements IAccountPresenter, AccountInteractor.OnAccountGetDone, AccountChange.OnAccountChange {

    private Context context;
    private  IAccountInteractor accountInteractor;
    private  ITransactionInteractor transactionInteractor;
    private IAccountView view;
    private Account account;

    public AccountPresenter(Context context, IAccountView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public Account getAccount() {
        return accountInteractor.getAccount();
    }

    @Override
    public void modifyAccount(double budget, double totalLimit, double monthLimit) {
        new AccountChange((AccountChange.OnAccountChange)
                this).execute(String.valueOf(budget),String.valueOf(totalLimit),String.valueOf(monthLimit));
        account.setBudget(budget);
        account.setTotalLimit(totalLimit);
        account.setMonthLimit(monthLimit);
    }

    @Override
    public void setAccountData() {
        new AccountInteractor((AccountInteractor.OnAccountGetDone)
                this).execute("account");
//        new TransactionListInteractor((TransactionListInteractor.OnTransactionGetDone)
//                this).execute("transactions");

    }

    @Override
    public void onAccountGetDone(Account account) {
        this.account = account;
        view.setLimits(account.getTotalLimit(),account.getMonthLimit());
        view.setBudget(String.valueOf(account.getBudget()));
    }

//    @Override
//    public void onTransactionGetDone(ArrayList<Transaction> results) {
//        DecimalFormat df = new DecimalFormat("#.##");
//        double iznos = 0;
//        for(Transaction t : results) {
//            if(t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("INDIVIDUALPAYMENT")
//                    || t.getType().toString().equals("REGULARPAYMENT")) iznos -= t.getAmount();
//            else iznos += t.getAmount();
//        }
//        iznos = account.getBudget() + iznos;
//        view.setBudget(df.format(iznos));
//    }

    @Override
    public void onAccountChanged() {
        view.setLimits(account.getTotalLimit(),account.getMonthLimit());
        view.setBudget(String.valueOf(account.getBudget()));
    }
}
