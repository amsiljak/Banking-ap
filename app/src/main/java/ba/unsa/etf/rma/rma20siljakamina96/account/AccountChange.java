package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;

public class AccountChange extends AsyncTask<String, Integer, Void> {
    private OnAccountChange caller;

    public AccountChange(OnAccountChange p) {
        caller = p;
    }
    @Override
    protected Void doInBackground(String... strings) {
        String body = getParametersInJSON(strings);
        try {
            URL url = null;
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10";

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

    public interface OnAccountChange{
        public void onAccountChanged();
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onAccountChanged();
    }
    private String getParametersInJSON(String... strings) {
        String body = "{";
        body += "\"budget\": \""+strings[0]+"\", ";
        body += "\"totalLimit\": \""+strings[1]+"\", ";
        body += "\"monthLimit\": \""+strings[2] + "\"}";
        return body;
    }

}
