package ba.unsa.etf.rma.rma20siljakamina96.account;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.FinanceModel;

public class AccountInteractor implements IAccountInteractor {
    @Override
    public Account getAccount() {
        return FinanceModel.account;
    }

    @Override
    public void modifyAccount(double budget, double totalLimit, double monthLimit) {
        FinanceModel.account.setBudget(budget);
        FinanceModel.account.setTotalLimit(totalLimit);
        FinanceModel.account.setMonthLimit(monthLimit);
    }
}
