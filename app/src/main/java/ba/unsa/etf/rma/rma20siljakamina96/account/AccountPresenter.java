package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.content.Context;

import java.net.URL;
import java.net.URLConnection;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.list.ITransactionInteractor;

import static ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver.connected;


public class AccountPresenter implements IAccountPresenter, AccountInteractor.OnAccountGetDone, AccountChange.OnAccountChange {

    private Context context;
    private  IAccountInteractor accountInteractor;
    private  ITransactionInteractor transactionInteractor;
    private IAccountView view;
    private static Account account;


    public AccountPresenter(Context context, IAccountView view) {
        this.context = context;
        this.view = view;
        accountInteractor = new AccountInteractor();
    }

    @Override
    public Account getAccount() {
        return accountInteractor.getAccount();
    }

    @Override
    public void modifyAccount(double budget, double totalLimit, double monthLimit) {
        if(!connected) {
            accountInteractor.deleteFromDB(context.getApplicationContext());
            accountInteractor.insert(budget,totalLimit,monthLimit, context.getApplicationContext());
            view.setOfflineText("Offline izmjena");
            account.setBudget(budget);
            account.setTotalLimit(totalLimit);
            account.setMonthLimit(monthLimit);
        }
        else new AccountChange((AccountChange.OnAccountChange)
                this).execute(String.valueOf(budget),String.valueOf(totalLimit),String.valueOf(monthLimit));
    }

    @Override
    public void setAccountData() {
        if(connected) new AccountInteractor((AccountInteractor.OnAccountGetDone)
                this).execute("account");
        if(!connected) {
            Account account = accountInteractor.getAccountFromDB(context.getApplicationContext());
            if(account != null) {
                this.account = account;
                view.setLimits(this.account.getTotalLimit(),this.account.getMonthLimit());
                view.setBudget(String.valueOf(account.getBudget()));
            }
        }
        //ako nije connected i nije dobijen account sa servera ne treba nista prikazati
    }

    @Override
    public void uploadToServis() {
        Account account = accountInteractor.getAccountFromDB(context.getApplicationContext());
        if(account != null) {
            new AccountChange((AccountChange.OnAccountChange)
                    this).execute(String.valueOf(account.getBudget()), String.valueOf(account.getTotalLimit()), String.valueOf(account.getMonthLimit()));
        }
    }

    @Override
    public void onAccountGetDone(Account account) {
        this.account = account;
        view.setLimits(this.account.getTotalLimit(),this.account.getMonthLimit());
        view.setBudget(String.valueOf(this.account.getBudget()));
    }

    @Override
    public void onAccountChanged() {
        if(accountChanged()) {
            accountInteractor.deleteFromDB(context.getApplicationContext());
            view.setOfflineText("");
        }
//        view.setLimits(account.getTotalLimit(),account.getMonthLimit());
//        view.setBudget(String.valueOf(account.getBudget()));
    }

    @Override
    public boolean accountChanged() {
        Account account = accountInteractor.getAccountFromDB(context.getApplicationContext());
        if(account != null) return true;
        else return false;
    }
}
