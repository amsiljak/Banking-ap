package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TransactionListDelete extends AsyncTask<String, Integer, Void> {
    private OnTransactionDeleteDone caller;

    public TransactionListDelete(OnTransactionDeleteDone p) {
        caller = p;
    }

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
        public void onTransactionDeleted();
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onTransactionDeleted();
    }
}
