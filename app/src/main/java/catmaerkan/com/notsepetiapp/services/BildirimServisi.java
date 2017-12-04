package catmaerkan.com.notsepetiapp.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import br.com.goncalves.pugnotification.notification.PugNotification;
import catmaerkan.com.notsepetiapp.ActivityMain;
import catmaerkan.com.notsepetiapp.R;
import catmaerkan.com.notsepetiapp.data.Notlar;
import catmaerkan.com.notsepetiapp.data.NotlarProvider;

public class BildirimServisi extends IntentService {

    public static final String TAG=Thread.currentThread().getName();
    public static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;
    ArrayList<Notlar> tamamlanmayanNotlar=new ArrayList<>();

    public BildirimServisi() {
        super("BildirimServisi");
        Log.d(TAG, "BildirimServisi: constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "BildirimServisi: onhandleıntent");
        tamamlanmayanNotlar=tamamlanmayanNotlariGetir();

        for(Notlar geciciNot : tamamlanmayanNotlar){


            if(bildirimGerekli(geciciNot.getNotEklenmetarihi(), geciciNot.getNotTarih())){

              bildirimYolla(geciciNot.getNotIcerik());

            }

        }

    }

    private void bildirimYolla(String mesaj) {

        PugNotification.with(this)
                .load()
                .title("GÖREVİ TAMAMLA : " + mesaj)
                .message("%90'dan az bir zaman kaldı, Görevi tamamlamak ister misin?")
                .bigTextStyle("Hadi gidip görevi tamamlayalım")
                .smallIcon(R.drawable.ic_action_balloon)
                .largeIcon(R.drawable.ic_action_balloon)
                .flags(Notification.DEFAULT_ALL)
                .click(ActivityMain.class)
                .autoCancel(true)
                .simple()
                .build();
    }

    private boolean bildirimGerekli(long notEklenmetarihi, long notTarih) {

    long now=System.currentTimeMillis();

        if(now > notTarih){
            return false;
        }else {
            long yuzde90 =(long) (0.9 * (notTarih - notEklenmetarihi));
            Log.e("catma","yuzde90: "+yuzde90+" eklenme:"+notEklenmetarihi+" nottarih:"+notTarih);
            return (now > (notEklenmetarihi + yuzde90)) ? true : false;
        }
    }

    private ArrayList<Notlar> tamamlanmayanNotlariGetir(){

        ArrayList<Notlar> tamamlanmayanlar=new ArrayList<>();

        Cursor cursor=getContentResolver().query(CONTENT_URI, new String[]{"id", "notIcerik", "notEklenmeTarih", "notTarih", "tamamlandi"}, "tamamlandi=?", new String[]{"0"}, null);


        if(cursor != null){

            while(cursor.moveToNext()){
                Notlar geciciNot=new Notlar();
                geciciNot.setId(cursor.getInt(cursor.getColumnIndex("id")));
                geciciNot.setNotIcerik(cursor.getString(cursor.getColumnIndex("notIcerik")));
                geciciNot.setNotEklenmetarihi(cursor.getLong(cursor.getColumnIndex("notEklenmeTarih")));
                geciciNot.setNotTarih(cursor.getLong(cursor.getColumnIndex("notTarih")));
                geciciNot.setTamamlandi(cursor.getInt(cursor.getColumnIndex("tamamlandi")));
                tamamlanmayanlar.add(geciciNot);

            }
        }


        return  tamamlanmayanlar;

    }

}
