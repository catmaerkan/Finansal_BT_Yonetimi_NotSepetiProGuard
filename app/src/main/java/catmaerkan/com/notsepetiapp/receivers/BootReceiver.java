package catmaerkan.com.notsepetiapp.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import catmaerkan.com.notsepetiapp.services.BildirimServisi;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager manager= (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent2=new Intent(context, BildirimServisi.class);
        PendingIntent pendingIntent=PendingIntent.getService(context, 100, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 360000, pendingIntent);

    }
}
