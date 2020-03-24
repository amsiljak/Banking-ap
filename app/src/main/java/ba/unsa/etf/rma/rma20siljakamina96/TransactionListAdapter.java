package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {
    public int resource;
    public TextView titleView;
    public TextView amountView;
    public ImageView imageView;

    public TransactionListAdapter(@NonNull Context context, int _resource, ArrayList<Transaction> items) {
        super(context, _resource,items);
        resource = _resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout newView;
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().
                    getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout)convertView;
        }

        Transaction transaction = getItem(position);

        titleView = newView.findViewById(R.id.title);
        amountView = newView.findViewById(R.id.iznos);
        imageView = newView.findViewById(R.id.ikonica);

        titleView.setText(transaction.getTitle());

        String type = transaction.getType().toString();
        try {
            if(type.equals("INDIVIDUALPAYMENT")) imageView.setImageResource(R.drawable.a);
            if(type.equals("REGULARPAYMENT")) imageView.setImageResource(R.drawable.b);
            if(type.equals("PURCHASE")) imageView.setImageResource(R.drawable.c);
            if(type.equals("INDIVIDUALINCOME")) imageView.setImageResource(R.drawable.d);
            if(type.equals("REGULARINCOME")) imageView.setImageResource(R.drawable.e);

        }
        catch (Exception e) {
            imageView.setImageResource(R.drawable.a);
        }

        return newView;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.addAll(transactions);
    }
}
