package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import ba.unsa.etf.rma.rma20siljakamina96.util.AccountDBOpenHelper;

public class AccountInteractor extends AsyncTask<String, Integer, Void> implements IAccountInteractor {

    private String tmdb_api_key = "";
    private OnAccountGetDone caller;
    Account account;
    private AccountDBOpenHelper accountDBOpenHelper;
    SQLiteDatabase database;

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
            Double monthLimit = accountDetails.getDouble("monthLimit");
            account = new Account(budget, totalLimit, monthLimit);
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
    @Override
    public void insert(double budget, double totalLimit, double monthLimit, Context context) {
        accountDBOpenHelper = new AccountDBOpenHelper(context);
        database = accountDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(accountDBOpenHelper.ACCOUNT_BUDGET,budget);
        values.put(accountDBOpenHelper.ACCOUNT_TOTAL_LIMIT, totalLimit);
        values.put(accountDBOpenHelper.ACCOUNT_MONTH_LIMIT,monthLimit);

        database.insert(accountDBOpenHelper.ACCOUNT_TABLE, null, values);

        database.close();
    }


    @Override
    public Account getAccountFromDB(Context context) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = null;
        Uri adresa = Uri.parse("content://rma.provider.movies/transactions");
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cursor = cr.query(adresa,kolone,where,whereArgs,order);
        accountDBOpenHelper = new AccountDBOpenHelper(context);
//        database = accountDBOpenHelper.getWritableDatabase();

//        String query =  "SELECT *"  + " FROM "
//                    + AccountDBOpenHelper.ACCOUNT_TABLE;

//        Cursor cursor = database.rawQuery(query,null);
        Account accountFromDB = null;
        if(cursor.moveToFirst()) {
            do{
                int budgetPos = cursor.getColumnIndexOrThrow(AccountDBOpenHelper.ACCOUNT_BUDGET);
                int totalPos = cursor.getColumnIndexOrThrow(AccountDBOpenHelper.ACCOUNT_TOTAL_LIMIT);
                int monthPos = cursor.getColumnIndexOrThrow(AccountDBOpenHelper.ACCOUNT_MONTH_LIMIT);

                accountFromDB = new Account(cursor.getDouble(budgetPos),cursor.getDouble(totalPos),cursor.getDouble(monthPos));

            }while (cursor.moveToNext());
        }
        cursor.close();
//        database.close();
        return accountFromDB;
    }

    @Override
    public void deleteFromDB(Context context) {
        accountDBOpenHelper = new AccountDBOpenHelper(context);
        database = accountDBOpenHelper.getWritableDatabase();

        String where = null;
        String whereArgs [] = null;

        database.delete(accountDBOpenHelper.ACCOUNT_TABLE, where, whereArgs);
        database.close();
    }
}
