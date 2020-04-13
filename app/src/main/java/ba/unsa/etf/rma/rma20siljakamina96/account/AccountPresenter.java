package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.content.Context;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;


public class AccountPresenter implements IAccountPresenter{

    private Context context;
    private static IAccountInteractor accountInteractor;
    private IAccountView view;

    public AccountPresenter(IAccountView view, Context context) {
        this.context = context;
        this.accountInteractor = new AccountInteractor();
        this.view = view;
    }
    @Override
    public Account getAccount() {
        return accountInteractor.getAccount();
    }
    @Override
    public void modifyAccount(double budget, double totalLimit, double monthLimit) {
        accountInteractor.modifyAccount(budget, totalLimit, monthLimit);
    }
}
