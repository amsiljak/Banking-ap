package ba.unsa.etf.rma.rma20siljakamina96;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public interface IFinanceView {

    void notifyTransactionDataSetChanged();

    void setAccountData(String globalAmount, String limit);
    void setTransactions(ArrayList<Transaction> transactions);
    void setDate(Calendar cal);
}
