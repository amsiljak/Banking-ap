package ba.unsa.etf.rma.rma20siljakamina96.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.net.URL;
import java.net.URLConnection;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {


    public interface MyBroadcastListener{
        public void doSomething();
    }

    public static boolean connected;

    private MyBroadcastListener listener;

    public ConnectivityBroadcastReceiver(MyBroadcastListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);
            toast.show();
            connected = false;
        }
        else {
            Toast toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
            toast.show();

            if(connected == false) {
                connected = true;
                listener.doSomething();
            }
            else connected = true;
//            if(connected == false) {
                //ako je povezan na internet treba provjeriti da li se moze povezati na serverpublic boolean isConnectedToServer {
//                try{
//                    URL myUrl = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+key+"/transactions");
//                    URLConnection connection = myUrl.openConnection();
//                    connection.connect();
//                    connected = true;
//                    listener.doSomething();
//                } catch (Exception e) {
//                    // Handle your exceptions
//                    connected = false;
//                }
//                connected = true;
//                listener.doSomething();
//            }
//            else {
//                try{
//                    URL myUrl = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+key+"/transactions");
//                    URLConnection connection = myUrl.openConnection();
//                    connection.connect();
//                    connected = true;
//                } catch (Exception e) {
//                    // Handle your exceptions
//                    connected = false;
//                }
//                connected = true;
//            }
        }
    }
}