package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.FinanceModel;

public class AccountInteractor extends AsyncTask<String, Integer, Void> implements IAccountInteractor {

    private String tmdb_api_key = "";
    private OnAccountGetDone caller;
    Account account;

    public AccountInteractor(OnAccountGetDone p) {
        caller = p;
    };
    public AccountInteractor() {}
    @Override
    public Account getAccount() {
        return FinanceModel.account;
    }

    @Override
    public void modifyAccount(double totalLimit, double monthLimit) {
        FinanceModel.account.setTotalLimit(totalLimit);
        FinanceModel.account.setMonthLimit(monthLimit);
    }
    @Override
    public void setAccountBudget(double budget) {
        FinanceModel.account.setBudget(budget);
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
    @Override
    protected Void doInBackground(String... strings) {

        try {
            URL url = null;
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10";
            try {
                url = new URL(url1);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);
            JSONObject accountDetails = new JSONObject(rezultat);
            Double budget = accountDetails.getDouble("budget");
            Double totalLimit = accountDetails.getDouble("totalLimit");

            account = new Account(budget, totalLimit, 0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public interface OnAccountGetDone{
        public void onAccountGetDone(Account account);
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onAccountGetDone(account);
    }
}
