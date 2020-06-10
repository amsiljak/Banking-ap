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
import java.text.SimpleDateFormat;
import java.util.Map;

import ba.unsa.etf.rma.rma20siljakamina96.util.TransactionDBOpenHelper;

import static ba.unsa.etf.rma.rma20siljakamina96.detail.TransactionDetailPresenter.transactionDetailResultReceiver;
import static ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListInteractor.transactionTypes;

public class TransactionListPost extends AsyncTask<String, Integer, Void> implements ITransactionListPost {
    private int internalId = -1;
    private String key = "a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10";
    private OnTransactionPostDone caller;
    private TransactionDBOpenHelper transactionDBOpenHelper;
    SQLiteDatabase database;

    public TransactionListPost(OnTransactionPostDone p) {
        caller = p;
    }

    public TransactionListPost() {}

    public interface OnTransactionPostDone{
        public void onTransactionPosted(int id);
    }
    @Override
    protected Void doInBackground(String... strings) {
        String body = getParametersInJSON(strings);
        try {
            URL url = null;
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+key+"/transactions";
            if(strings[7] != null) internalId = Integer.valueOf(strings[7]);
            try {
                url = new URL(url1);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoInput(true);

            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input,0,input.length);
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getParametersInJSON(String... strings) {

        String body = "{";
        body += "\"date\": \""+strings[0]+"\", ";
        body += "\"title\": \""+strings[1]+"\", ";
        body += "\"amount\": "+strings[2];
        if(!(strings[3]).equals("")) {
            body += ", \"endDate\": \""+strings[3]+"\"";
        }
        if(!(strings[4]).equals("")) {
            body += ", \"itemDescription\": \""+strings[4]+"\"";
        }
        if(!(strings[5]).equals("")) {
            body += ", \"transactionInterval\": \""+strings[5]+"\"";
        }

        String typeId = "";
        for (Map.Entry<Integer,String> entry : transactionTypes.entrySet()) {
            if((strings[6]).equals( entry.getValue())) {
                typeId = entry.getKey().toString();
                break;
            }
        }
        body += ", \"TransactionTypeId\": "+typeId+"}";
        return body;
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onTransactionPosted(internalId);
    }
    @Override
    public void save(String date, Double amount, String title, String type, String itemDescription, Integer transactionInterval, String endDate, Context context) {
        transactionDBOpenHelper = new TransactionDBOpenHelper(context);
        database = transactionDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(transactionDBOpenHelper.TRANSACTION_TITLE,title);
        values.put(transactionDBOpenHelper.TRANSACTION_DATE, date);
        values.put(transactionDBOpenHelper.TRANSACTION_INTERVAL,transactionInterval);
        values.put(transactionDBOpenHelper.TRANSACTION_AMOUNT, amount);
        values.put(transactionDBOpenHelper.TRANSACTION_DESCRIPTION,itemDescription);
        values.put(transactionDBOpenHelper.TRANSACTION_TYPE, type);
        values.put(transactionDBOpenHelper.TRANSACTION_ENDDATE, endDate);
        values.put(transactionDBOpenHelper.TRANSACTION_CHANGE, "add");
        database.insert(transactionDBOpenHelper.TRANSACTION_TABLE, null, values);

        database.close();
    }
}
