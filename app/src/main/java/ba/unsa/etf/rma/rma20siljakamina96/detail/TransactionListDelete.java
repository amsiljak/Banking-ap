package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ba.unsa.etf.rma.rma20siljakamina96.util.TransactionDBOpenHelper;

import static ba.unsa.etf.rma.rma20siljakamina96.util.TransactionDBOpenHelper.TRANSACTION_ID;

public class TransactionListDelete extends AsyncTask<String, Integer, Void> implements ITransactionListDelete{
    private OnTransactionDeleteDone caller;
    private int id;

    private TransactionDBOpenHelper transactionDBOpenHelper;
    SQLiteDatabase database;

    public TransactionListDelete(OnTransactionDeleteDone p) {
        caller = p;
    }
    public TransactionListDelete() {}

    @Override
    protected Void doInBackground(String... strings) {
        try {
            URL url = null;
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10/transactions/"+strings[0];
            try {
                url = new URL(url1);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            id = Integer.valueOf(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" );
            urlConnection.setRequestMethod("DELETE");
            urlConnection.connect();
            String s = String.valueOf(urlConnection.getResponseCode());
        } catch (MalformedURLException e)    {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public interface OnTransactionDeleteDone{
        public void onTransactionDeleted(int id);
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onTransactionDeleted(id);
    }
    @Override
    public void delete(String date, Double amount, String title, String type, String itemDescription, Integer transactionInterval, String endDate, Integer id, Context context) {
        transactionDBOpenHelper = new TransactionDBOpenHelper(context);
        database = transactionDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_ID, id);
        values.put(transactionDBOpenHelper.TRANSACTION_TITLE,title);
        values.put(transactionDBOpenHelper.TRANSACTION_DATE, date);
        values.put(transactionDBOpenHelper.TRANSACTION_INTERVAL,transactionInterval);
        values.put(transactionDBOpenHelper.TRANSACTION_AMOUNT, amount);
        values.put(transactionDBOpenHelper.TRANSACTION_DESCRIPTION,itemDescription);
        values.put(transactionDBOpenHelper.TRANSACTION_TYPE, type);
        values.put(transactionDBOpenHelper.TRANSACTION_ENDDATE, endDate);
        values.put(transactionDBOpenHelper.TRANSACTION_CHANGE, "delete");

        database.insert(transactionDBOpenHelper.TRANSACTION_TABLE, null, values);
        database.close();
    }

}
