package me.pleasemurder.thebutton.pickmeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Cheese on 9/21/14.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("log_tag", "onReceive");
        Intent startServiceIntent = new Intent(context, MyService.class);
        context.startService(startServiceIntent);
    }
}