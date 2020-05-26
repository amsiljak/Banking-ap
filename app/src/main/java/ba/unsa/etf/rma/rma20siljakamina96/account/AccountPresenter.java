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
    private IAccountView view;
    private Account account;

    public AccountPresenter(Context context, IAccountView view) {
        this.context = context;
        this.view = view;
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
    }

    @Override
    public void onAccountGetDone(Account account) {
        this.account = account;
        view.setLimits(account.getTotalLimit(),account.getMonthLimit());
        view.setBudget(String.valueOf(account.getBudget()));
    }

    @Override
    public void onAccountChanged() {
        view.setLimits(account.getTotalLimit(),account.getMonthLimit());
        view.setBudget(String.valueOf(account.getBudget()));
    }

}
