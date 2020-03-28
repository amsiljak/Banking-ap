package ba.unsa.etf.rma.rma20siljakamina96;

import java.util.Date;

public interface ITransactionDetailPresenter {
    void create(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate);
    Transaction getTransaction();
    boolean checkBudget(double iznos);
}
