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

import java.util.ArrayList;

public class FilterSpinnerAdapter extends ArrayAdapter<String> {
    private int resource;
    public ImageView imageView;
    public TextView textView;
    public ImageView imageView2;
    public TextView textView2;


    public FilterSpinnerAdapter(@NonNull Context context, int _resource, ArrayList<String> items) {
        super(context, _resource,items);
        resource = _resource;
    }
    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
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

        String tip = getItem(position);

        textView = newView.findViewById(R.id.spinner_item_naziv);
        imageView = newView.findViewById(R.id.spinner_item_icon);

        textView.setText(tip);

        try {
            if(tip.equals("INDIVIDUALPAYMENT")) imageView.setImageResource(R.drawable.a);
            if(tip.equals("REGULARPAYMENT")) imageView.setImageResource(R.drawable.b);
            if(tip.equals("PURCHASE")) imageView.setImageResource(R.drawable.c);
            if(tip.equals("INDIVIDUALINCOME")) imageView.setImageResource(R.drawable.d);
            if(tip.equals("REGULARINCOME")) imageView.setImageResource(R.drawable.e);
        }
        catch (Exception e) {
            imageView.setImageResource(R.drawable.a);
        }

        return newView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout newView;
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().
                    getSystemService(inflater);
            li.inflate(R.layout.filter_spinner_dropdown_item, newView, true);
        } else {
            newView = (LinearLayout)convertView;
        }

        String tip = getItem(position);

        textView2 = newView.findViewById(R.id.spinner_item_naziv2);
        imageView2 = newView.findViewById(R.id.spinner_item_icon2);

        textView2.setText(tip);

        try {
            if(tip.equals("INDIVIDUALPAYMENT")) imageView2.setImageResource(R.drawable.a);
            if(tip.equals("REGULARPAYMENT")) imageView2.setImageResource(R.drawable.b);
            if(tip.equals("PURCHASE")) imageView2.setImageResource(R.drawable.c);
            if(tip.equals("INDIVIDUALINCOME")) imageView2.setImageResource(R.drawable.d);
            if(tip.equals("REGULARINCOME")) imageView2.setImageResource(R.drawable.e);

        }
        catch (Exception e) {
            imageView2.setImageResource(R.drawable.a);
        }

        return newView;
    }
}
