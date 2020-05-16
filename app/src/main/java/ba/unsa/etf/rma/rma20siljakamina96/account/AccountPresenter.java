package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.content.Context;

import java.text.DecimalFormat;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.list.ITransactionInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListInteractor;


public class AccountPresenter implements IAccountPresenter{

    private Context context;
    private  IAccountInteractor accountInteractor;
    private  ITransactionInteractor transactionInteractor;

    public AccountPresenter(Context context) {
        this.context = context;
//        this.accountInteractor = new AccountInteractor();
    }
    @Override
    public Account getAccount() {
        return accountInteractor.getAccount();
    }
    @Override
    public void modifyAccount(double totalLimit, double monthLimit) {
        accountInteractor.modifyAccount(totalLimit, monthLimit);
    }
    @Override
    public String getBudget() {
        DecimalFormat df = new DecimalFormat("#.##");
        double iznos = 0;
        for(Transaction t : transactionInteractor.getTransactions()) {
            if(t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("INDIVIDUALPAYMENT")
                    || t.getType().toString().equals("REGULARPAYMENT")) iznos -= t.getAmount();
            else iznos += t.getAmount();
        }
        iznos = accountInteractor.getAccount().getBudget() + iznos;
        return df.format(iznos);
    }
    @Override
    public String getTotalLimit() {
        return String.valueOf(accountInteractor.getAccount().getTotalLimit());
    }
    @Override
    public String getMonthLimit() {
        return String.valueOf(accountInteractor.getAccount().getMonthLimit());
    }
}
