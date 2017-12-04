package catmaerkan.com.notsepetiapp.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import catmaerkan.com.notsepetiapp.DataEvent;
import catmaerkan.com.notsepetiapp.NotSepetiApp;
import catmaerkan.com.notsepetiapp.R;
import catmaerkan.com.notsepetiapp.data.Notlar;
import catmaerkan.com.notsepetiapp.data.NotlarProvider;

/**
 * Created by Erkan ÇATMA on 12.10.2017.
 */

public class AdapterNotlarListesi extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM = 0;
    public static final int BOS_FILTRE = 1;
    public static final int FOOTER = 2;

    public static final int FOOTER_EKLE = 1;
    public static final int BOS_FILTRE_EKLE = 1;


    static final Uri CONTENT_URI = NotlarProvider.CONTENT_URI;

    LayoutInflater mInflater;
    ArrayList<Notlar> tumNotlar;
    private ContentResolver resolver;
    static  Context mContext;
    private int mFiltre;



    public AdapterNotlarListesi(Context context, ArrayList<Notlar> notlar){
        mContext=context;
        resolver=context.getContentResolver();
        mInflater=LayoutInflater.from(context);
        tumNotlar=notlar;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == ITEM){
            View view=mInflater.inflate(R.layout.tek_satir_not, parent, false);
            NotHolder holder=new NotHolder(view);
            return  holder;
        }else if(viewType == BOS_FILTRE){

            View view=mInflater.inflate(R.layout.bos_filtre, parent, false);
            BosFiltreHolder holder=new BosFiltreHolder(view);
            return holder;

        }else{

            View view=mInflater.inflate(R.layout.footer, parent, false);
            FooterHolder footerHolder=new FooterHolder(view);
            return footerHolder;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NotHolder){
            NotHolder notHolder= (NotHolder) holder;
            Notlar geciciNot=tumNotlar.get(position);
            notHolder.mTextNotIcerik.setText(geciciNot.getNotIcerik());
            notHolder.mTextNotTarih.setText(""+geciciNot.getNotTarih());
            notHolder.setBackgroundRenk(geciciNot.getTamamlandi());
            notHolder.setTarih(geciciNot.getNotTarih());
        }


    }

    @Override
    public int getItemCount() {

        if(!tumNotlar.isEmpty()){

            return tumNotlar.size() + FOOTER_EKLE;
        }else{

            if(mFiltre==Filtreler.AZ_VAKIT_KALDI || mFiltre==Filtreler.COK_VAKIT_VAR || mFiltre==Filtreler.NOFILTER){
                return 0;
            }else{

                return FOOTER_EKLE + BOS_FILTRE_EKLE;

            }


        }

    }

    @Override
    public long getItemId(int position) {

        if(position < tumNotlar.size()){

            return tumNotlar.get(position).getId();

        }

        return RecyclerView.NO_ID;


    }

    @Override
    public int getItemViewType(int position) {

        if(!tumNotlar.isEmpty()){

            if(position < tumNotlar.size()){
                return ITEM;
            }else{

                return FOOTER;
            }

        }else {

            if(mFiltre == Filtreler.TAMAMLANANLAR || mFiltre==Filtreler.TAMAMLANMAYANLAR){

                if (position == 0){
                    return BOS_FILTRE;
                }else {

                    return FOOTER;
                }

            }else{

                return ITEM;
            }




        }

    }


    public void update(ArrayList<Notlar> tumNotlar) {
        this.tumNotlar=tumNotlar;
        mFiltre= NotSepetiApp.sharedOku(mContext);
        notifyDataSetChanged();
    }

    @Subscribe
    public void onNotTamamlaPosition(DataEvent.NotTamamlaPosition event){

        int position=event.getPosition();
        if(position<tumNotlar.size()){

            Notlar tamamlanacakNot=tumNotlar.get(position);
            String tamamlanacakNotID=String.valueOf(tamamlanacakNot.getId());

            ContentValues values=new ContentValues();
            values.put("tamamlandi", 1);

            int etkilenenSatirSayisi=resolver.update(CONTENT_URI, values, "id=?", new String[]{tamamlanacakNotID});
            if(etkilenenSatirSayisi!=0){
                tamamlanacakNot.setTamamlandi(1);
                tumNotlar.set(position, tamamlanacakNot);
                Log.e("TAMAMLANDI ID", tamamlanacakNotID);
                notifyDataSetChanged();
            }

        }

    }

   @Subscribe
    public void onSwipe(DataEvent.KaydirilanNotunPozisyonu event) {
       int position=event.getPosition();
        if(position < tumNotlar.size()){
            Notlar silinecekNot=tumNotlar.get(position);
            String silinecekNotID=String.valueOf(silinecekNot.getId());

            int etkilenenSatirSayisi=resolver.delete(CONTENT_URI,"id=?", new String[]{silinecekNotID});
            if(etkilenenSatirSayisi != 0){
                tumNotlar.remove(silinecekNot);

                if(tumNotlar.isEmpty() && (mFiltre == Filtreler.TAMAMLANANLAR || mFiltre == Filtreler.TAMAMLANMAYANLAR)){
                    NotSepetiApp.sharedYaz(mContext, Filtreler.NOFILTER);
                    EventBus.getDefault().post(new DataEvent.DataGuncelleMethoduTetikle(1));
                }
                update(tumNotlar);

            }

        }

    }


    public static class NotHolder extends RecyclerView.ViewHolder{

        TextView mTextNotIcerik;
        TextView mTextNotTarih;
        View mItemView;

        public NotHolder(View itemView) {
            super(itemView);
            mItemView=itemView;
            mTextNotIcerik= (TextView) itemView.findViewById(R.id.tv_not_icerik);
            mTextNotTarih= (TextView) itemView.findViewById(R.id.tv_not_tarih);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DataEvent.DialogTamamlaNotPosition(getAdapterPosition()));
                }
            });

        }

        public void setBackgroundRenk(int tamamlandi) {

            Drawable backgroundDrawable;

            if(tamamlandi==0){
                backgroundDrawable= ContextCompat.getDrawable(mContext, R.color.tamamlanmamis_not);

            }else {

            backgroundDrawable=ContextCompat.getDrawable(mContext, R.color.tamamlanmis_not);
            }
            mItemView.setBackground(backgroundDrawable);
        }

        public void setTarih(long notTarih) {
            mTextNotTarih.setText(DateUtils.getRelativeTimeSpanString(notTarih, System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS,0));
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {

        Button mBtnFooterEkle;

        public FooterHolder(View itemView) {
            super(itemView);

            mBtnFooterEkle= (Button) itemView.findViewById(R.id.btn_footer);
            mBtnFooterEkle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DataEvent.NotEkleDialogGoster(1));
                }
            });

        }


    }
    public class BosFiltreHolder extends RecyclerView.ViewHolder {


        public BosFiltreHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }
}
