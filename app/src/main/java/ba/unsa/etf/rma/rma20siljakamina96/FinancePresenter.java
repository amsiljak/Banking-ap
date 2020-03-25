package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FinancePresenter implements IFinancePresenter {
    private Context context;
    private IFinanceInteractor financeInteractor;
    private IFinanceView view;

    public FinancePresenter(IFinanceView view, Context context) {
        this.context = context;
        this.financeInteractor = new FinanceInteractor();
        this.view = view;
    }

    @Override
    public void refresh() {
        view.setAccountData(String.valueOf(financeInteractor.getAccount().getBudget()),String.valueOf(financeInteractor.getAccount().getTotalLimit()));
        view.setTransactions(financeInteractor.getTransactions());
        view.notifyTransactionDataSetChanged();
        view.setDate(financeInteractor.getCalendar());
    }
}
