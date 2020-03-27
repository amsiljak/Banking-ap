package ba.unsa.etf.rma.rma20siljakamina96;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TransactionDetailActivity extends AppCompatActivity implements ITransactionDetailActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
    }

}
