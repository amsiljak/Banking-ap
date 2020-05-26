package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ba.unsa.etf.rma.rma20siljakamina96.data.FinanceModel;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;

public class TransactionListInteractor extends AsyncTask<String, Integer, Void> implements ITransactionInteractor {

    private OnTransactionGetDone caller;
    public static ArrayList<Transaction> transactions;
    static Map<Integer,String> transactionTypes;

    @Override
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public TransactionListInteractor(OnTransactionGetDone p) {
        caller = p;
    };

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
    private String getQuery(String type, String sort, String month, String year) {
        String query = "";

        if(type != null && !type.equals("All")){
            for (Map.Entry<Integer,String> entry : transactionTypes.entrySet()) {
                if(type.equals(entry.getValue())) { //ako nadje ime tipa transakcije iz filter liste u query stavi id tog tipa
                    type = entry.getKey().toString();
                    break;
                }
            }
            query += "typeId=" + type;
        }
        if(query != "") query+= "&";

        if (sort != null) {
            if(sort.equals("Price - Ascending")) sort = "amount.asc";
            else if(sort.equals("Price - Descending"))  sort = "amount.desc";
            else if(sort.equals("Title - Ascending"))  sort = "title.asc";
            else if(sort.equals("Title - Descending"))  sort = "title.desc";
            else if(sort.equals("Date - Ascending"))  sort = "date.asc";
            else if(sort.equals("Date - Descending")) sort = "date.desc";
            query += "sort=" + sort;
        }
        if(query != "") query+= "&";

        if (month != null) {
            if(month.length() == 1) month = '0' + month;
            query += "month=" + month;
        }
        if(query != "") query+= "&";
        if (year != null) {
            query += "year=" + year;
        }
        return query;
    }
    @Override
    protected Void doInBackground(String... strings) {
        transactions = new ArrayList<>();
        AddTypes();
        int page = 0;

        String query = getQuery(strings[0],strings[1],strings[2],strings[3]);

        while(true) {
            String url1;
            if(query == "") {
                if (page == 0)
                    url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10/transactions";
                else
                    url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10/transactions?page=" + page;
            }
            else {
                if (page == 0)
                    url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10/transactions/filter?"+query;
                else
                    url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/a8dfa9fe-fe66-4026-9fb0-1c6abcdd0f10/transactions/filter?page=" + page+"&"+query;
            }
            try {
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = convertStreamToString(in);
                JSONObject jo = new JSONObject(result);
                JSONArray results = jo.getJSONArray("transactions");
                if(results.length() == 0) break;

                for (int i = 0; i < results.length(); i++) {

                    JSONObject transaction = results.getJSONObject(i);

                    Integer id = transaction.getInt("id");
                    Date date = DATE_FORMAT.parse(transaction.getString("date"));
                    String title = transaction.getString("title");
                    Double amount = transaction.getDouble("amount");
                    String itemDescription = transaction.getString("itemDescription");
                    int transactionTypeId = transaction.getInt("TransactionTypeId");

                    Integer transactionInterval = 0;
                    if (!transaction.isNull("transactionInterval")) {
                        transactionInterval = Integer.valueOf(transaction.getString("transactionInterval"));
                    }

                    Date endDate = null;
                    if (!transaction.isNull("endDate")) {
                        endDate = DATE_FORMAT.parse(transaction.getString("endDate"));
                    }
                    String type = "";
                    for (Map.Entry<Integer,String> entry : transactionTypes.entrySet()) {
                        if(transactionTypeId == entry.getKey()) {
                            type = entry.getValue();
                            break;
                        }
                    }
                    transactions.add(new Transaction(id, date, amount, title, Type.valueOf(type), itemDescription, transactionInterval, endDate));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            page++;
        }
        return null;
    }

    protected Void AddTypes(String... params)
    {
        transactionTypes = new HashMap<>();
        try {
            URL url = null;
            String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/transactionTypes";
            try {
                url = new URL(url1);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);
            JSONObject jo = new JSONObject(rezultat);

            JSONArray items = jo.getJSONArray("rows");
            for (int i = 0; i < items.length(); i++) {
                JSONObject type = items.getJSONObject(i);
                Integer id = type.getInt("id");
                String name = type.getString("name");

                if (!transactionTypes.containsKey(id)) {
                    transactionTypes.put(id, name.replaceAll("\\s","").toUpperCase());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public interface OnTransactionGetDone{
        public void onTransactionGetDone(ArrayList<Transaction> results);
    }
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        caller.onTransactionGetDone(transactions);
    }
}
