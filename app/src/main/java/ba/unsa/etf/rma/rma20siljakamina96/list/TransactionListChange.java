package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListInteractor.transactionTypes;

public class TransactionListChange extends AsyncTask<String, Integer, Void> {
    private OnTransactionPostDone caller;

    public TransactionListChange(OnTransactionPostDone p) {
        caller = p;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String body = getParametersInJSON(strings);
        try {
            URL url = null;
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10/transactions";
            if((strings[7]) != null) url1+= "/"+strings[7];
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getParametersInJSON(String... strings) {
        SimpleDateFormat DATE_FORMAT_SET= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat DATE_FORMAT_GET= new SimpleDateFormat("dd-MM-yyyy");
        String body = "{";
//        "{"name": "Upendra", "job": "Programmer"}"
//        ”date”, ”title”, ”amount”, ”endDate”, ”itemDescription”, ”transactionInterval”, ”typeId”
        body += "\"date\": \""+strings[0]+"\", ";
        body += "\"title\": \""+strings[1]+"\", ";
        body += "\"amount\": "+strings[2];
        if(!(strings[3]).equals("")) {
            body += "\"endDate\": \""+strings[3]+"\"";
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
        body += ", \"typeId\": "+typeId+"}";
        return body;
    }

    public interface OnTransactionPostDone{
        public void onTransactionPosted();
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onTransactionPosted();
    }
}
