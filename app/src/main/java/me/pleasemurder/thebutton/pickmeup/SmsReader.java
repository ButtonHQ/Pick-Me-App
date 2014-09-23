package me.pleasemurder.thebutton.pickmeup;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Cheese on 9/20/14.
 */

public class SmsReader extends ContentObserver {

    private Context context;
    public SmsReader(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.e("log_tag", "onChange");

        Uri uriSMSURI = Uri.parse("content://sms");
        Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);
        cur.moveToNext();

        String body = cur.getString(cur.getColumnIndex("body"));
        String number = cur.getString(cur.getColumnIndex("address"));
        Log.e("log_tag", body);
        Log.e("log_tag", number);
        Intent msgIntent = new Intent(context, MyService.class);
        msgIntent.putExtra("body", body);
        msgIntent.putExtra("number", number);
        context.startService(msgIntent);
    }
}