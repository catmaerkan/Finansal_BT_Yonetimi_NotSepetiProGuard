package catmaerkan.com.notsepetiapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import catmaerkan.com.notsepetiapp.adapter.AdapterNotlarListesi;
import catmaerkan.com.notsepetiapp.adapter.Divider;
import catmaerkan.com.notsepetiapp.adapter.Filtreler;
import catmaerkan.com.notsepetiapp.adapter.SimpleTouchCallback;
import catmaerkan.com.notsepetiapp.data.Notlar;
import catmaerkan.com.notsepetiapp.data.NotlarProvider;
import catmaerkan.com.notsepetiapp.services.BildirimServisi;

public class ActivityMain extends AppCompatActivity{

    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;
    static final String SIRALAMA_ONEMSIZ="SIRALAMA ONEMSIZ";
    static final String TAMAMLANMA_ONEMSIZ="TAMAMLANMA ONEMSIZ";

    View bosListe;
    Toolbar mToolbar;
    Button mButtonYeninot;
    NotlarRecyclerView mRecyclerViewNotlar;
    AdapterNotlarListesi mAdapterNotlarListesi;
    ArrayList<Notlar> tumNotlar=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bosListe=findViewById(R.id.bos_liste);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mButtonYeninot= (Button) findViewById(R.id.btn_sepete_not_ekle);
        mButtonYeninot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notekleDialogGoster();
            }
        });




        mRecyclerViewNotlar= (NotlarRecyclerView) findViewById(R.id.rv_not_listesi);
        mRecyclerViewNotlar.addItemDecoration(new Divider(this,LinearLayoutManager.VERTICAL));
        mRecyclerViewNotlar.egerElemanYoksaSaklanacaklar(mToolbar);
        mRecyclerViewNotlar.egerElemanYoksaGosterilecekler(bosListe);
        LinearLayoutManager mLayoutmanger=new LinearLayoutManager(this);
        mRecyclerViewNotlar.setLayoutManager(mLayoutmanger);
        mAdapterNotlarListesi=new AdapterNotlarListesi(this, tumNotlar);

        mAdapterNotlarListesi.setHasStableIds(true);

        mRecyclerViewNotlar.setAdapter(mAdapterNotlarListesi);


//swipe işlemi için yapılanlar
        SimpleTouchCallback callback=new SimpleTouchCallback();
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerViewNotlar);


        int secilenFiltre= NotSepetiApp.sharedOku(this);

        switch (secilenFiltre){

            case 0:
                dataGuncelle(SIRALAMA_ONEMSIZ, TAMAMLANMA_ONEMSIZ);
                break;

            case 1:
                NotSepetiApp.sharedYaz(this,Filtreler.COK_VAKIT_VAR);
                dataGuncelle("notTarih DESC", TAMAMLANMA_ONEMSIZ);
                break;

            case 2:
                NotSepetiApp.sharedYaz(this,Filtreler.AZ_VAKIT_KALDI);
                dataGuncelle("notTarih ASC", TAMAMLANMA_ONEMSIZ);
                break;

            case 3:
                NotSepetiApp.sharedYaz(this,Filtreler.TAMAMLANANLAR);
                dataGuncelle(SIRALAMA_ONEMSIZ, "1");
                break;


            case 4:
                NotSepetiApp.sharedYaz(this,Filtreler.TAMAMLANMAYANLAR);
                dataGuncelle(SIRALAMA_ONEMSIZ, "0");
                break;
        }

        backgrounResminiYerlestir();

        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(this, BildirimServisi.class);
        PendingIntent pendingIntent=PendingIntent.getService(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 360000, pendingIntent);


    }

    public void dataGuncelle(String siralama, String tamamlanma) {
        tumNotlar.clear();
        tumNotlar=tumNotlariGetir(siralama, tamamlanma);
        mAdapterNotlarListesi.update(tumNotlar);
    }

    private void notekleDialogGoster() {
        FragmentDialogYeniNot dialog=new FragmentDialogYeniNot();
        dialog.show(getSupportFragmentManager(), "DialogYeniNot");

    }

    private void notTamamlaDialogGoster(int position) {

        EventBus.getDefault().postSticky(new DataEvent.TamamlanacakNotPosition(position));
        FragmentDialogTamamla dialog=new FragmentDialogTamamla();
        dialog.show(getSupportFragmentManager(), "DialogNotTamamla");

    }



    private ArrayList<Notlar> tumNotlariGetir(String siralama, String tamamlanma){

        String siralamaSorgusu=siralama;
        String selection="tamamlandi=?";
        String[] tamamlanmaSorgusu={tamamlanma};

        if(siralama.equals(SIRALAMA_ONEMSIZ)){
            siralamaSorgusu=null;
        }
        if(tamamlanma.equals(TAMAMLANMA_ONEMSIZ)){
            selection=null;
            tamamlanmaSorgusu=null;
        }



        Cursor cursor=getContentResolver().query(CONTENT_URI, new String[]{"id", "notIcerik", "notTarih", "tamamlandi"}, selection, tamamlanmaSorgusu, siralamaSorgusu);


        if(cursor != null){

            while(cursor.moveToNext()){
                Notlar geciciNot=new Notlar();
                geciciNot.setId(cursor.getInt(cursor.getColumnIndex("id")));
                geciciNot.setNotIcerik(cursor.getString(cursor.getColumnIndex("notIcerik")));
                geciciNot.setNotTarih(cursor.getLong(cursor.getColumnIndex("notTarih")));
                geciciNot.setTamamlandi(cursor.getInt(cursor.getColumnIndex("tamamlandi")));
                tumNotlar.add(geciciNot);

            }
        }


    return  tumNotlar;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        boolean sonuc=true;

        switch (id){

            case R.id.menu_yeninot:
                notekleDialogGoster();
                break;

            case R.id.no_filter:
                NotSepetiApp.sharedYaz(this,Filtreler.NOFILTER);
                dataGuncelle(SIRALAMA_ONEMSIZ, TAMAMLANMA_ONEMSIZ);
                break;

            case R.id.menu_cokvakit:
                NotSepetiApp.sharedYaz(this,Filtreler.COK_VAKIT_VAR);
                dataGuncelle("notTarih DESC", TAMAMLANMA_ONEMSIZ);
                break;

            case R.id.menu_azvakit:
                NotSepetiApp.sharedYaz(this,Filtreler.AZ_VAKIT_KALDI);
                dataGuncelle("notTarih ASC", TAMAMLANMA_ONEMSIZ);
                break;

            case R.id.menu_tamamlananlar:
                NotSepetiApp.sharedYaz(this,Filtreler.TAMAMLANANLAR);
                dataGuncelle(SIRALAMA_ONEMSIZ, "1");
                break;


            case R.id.menu_tamamlanmayanlar:
                NotSepetiApp.sharedYaz(this,Filtreler.TAMAMLANMAYANLAR);
                dataGuncelle(SIRALAMA_ONEMSIZ, "0");
                break;

            default:
                sonuc=false;
                NotSepetiApp.sharedYaz(this,Filtreler.NOFILTER);
                break;

        }

        return sonuc;
    }

    private void backgrounResminiYerlestir() {
        ImageView background= (ImageView) findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .apply(new RequestOptions().centerCrop())
                .into(background);

    }

    @Subscribe
    public void onDialogNotTamamla(DataEvent.DialogTamamlaNotPosition event){

        notTamamlaDialogGoster(event.getPosition());

    }

    @Subscribe
    public void onNotEkleDialogGoster(DataEvent.NotEkleDialogGoster event){
        if(event.getTetikle()==1){
            notekleDialogGoster();
        }
    }

    @Subscribe
    public void onDataGuncelleMethoduTetikle(DataEvent.DataGuncelleMethoduTetikle event){
        if(event.getTetikle()==1){
           dataGuncelle(SIRALAMA_ONEMSIZ, TAMAMLANMA_ONEMSIZ);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
