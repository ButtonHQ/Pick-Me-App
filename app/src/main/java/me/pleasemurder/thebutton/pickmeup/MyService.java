package me.pleasemurder.thebutton.pickmeup;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cheese on 9/20/14.
 */
public class MyService extends Service {
    Context context = this;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created...", Toast.LENGTH_LONG).show();
        SmsReader content = new SmsReader(new Handler(), this);
        // REGISTER ContetObserver
        this.getContentResolver().
                registerContentObserver(Uri.parse("content://sms/"), true, content);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand...", Toast.LENGTH_LONG).show();
        if(intent.getStringExtra("body") != null) {
            String body = intent.getStringExtra("body");
            String number = intent.getStringExtra("number");
            Toast.makeText(this, body + number, Toast.LENGTH_LONG).show();
            new postText().execute(body, number);
        }
        return 1;
    }

    public class postText extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedReader in;
            String line = "";
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://pickmeupapp.me");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("body", params[0]));
                nameValuePairs.add(new BasicNameValuePair("number", params[1]));
                nameValuePairs.add(new BasicNameValuePair("zipcode", Integer.toString(30313)));
                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response =   httpclient.execute(request);

                in = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                line = in.readLine();
                Log.e("log_tag", line);
            } catch(Exception e){
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            return line;
        }

        @Override
        protected void onPostExecute(String result) {
           if(result != null){
               PackageManager pm = context.getPackageManager();
               try {
                   pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                   // Do something awesome - the app is installed! Launch App.
                   startActivity(new Intent("com.ubercap", Uri.parse(result)));
               } catch (PackageManager.NameNotFoundException e) {
                   // No Uber app! Open Mobile Website.
               }
           }
        }
    }
}
