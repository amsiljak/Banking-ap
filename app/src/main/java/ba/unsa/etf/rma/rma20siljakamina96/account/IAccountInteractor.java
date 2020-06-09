package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.content.Context;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;

public interface IAccountInteractor {
    public Account getAccount();
    public void modifyAccount(double totalLimit, double monthLimit);

    void setAccountBudget(double budget);

    void insert(double budget, double totalLimit, double monthLimit, Context context);

    Account getAccountFromDB(Context context);

    void deleteFromDB(Context context);
}
