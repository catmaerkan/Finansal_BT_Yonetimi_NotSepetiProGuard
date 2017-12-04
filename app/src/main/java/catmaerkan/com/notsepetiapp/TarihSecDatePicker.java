package catmaerkan.com.notsepetiapp;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EventListener;

/**
 * Created by Erkan ÇATMA on 21.10.2017.
 */

public class TarihSecDatePicker extends LinearLayout implements View.OnTouchListener {

    TextView mTextGun;
    TextView mTextAy;
    TextView mTextYil;
    Calendar mCalendar;
    SimpleDateFormat mFormatter;

    public TarihSecDatePicker(Context context) {
        super(context);
        init(context);
    }

    public TarihSecDatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TarihSecDatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tarih_view, this);
        mCalendar = Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextGun = (TextView) this.findViewById(R.id.tv_tarih_gun);
        mTextAy = (TextView) this.findViewById(R.id.tv_tarih_ay);
        mTextYil = (TextView) this.findViewById(R.id.tv_tarih_yil);

        mTextGun.setOnTouchListener(this);
        mTextAy.setOnTouchListener(this);
        mTextYil.setOnTouchListener(this);

        int gun = mCalendar.get(Calendar.DATE);
        int ay = mCalendar.get(Calendar.MONTH);
        int yil = mCalendar.get(Calendar.YEAR);

        guncelle(gun, ay, yil, 0, 0, 0);
    }

    private void guncelle(int gun, int ay, int yil, int saat, int dakika, int saniye) {

        mTextGun.setText("" + gun);
        mTextAy.setText(mFormatter.format(mCalendar.getTime()));
        mTextYil.setText("" + yil);

    }

    public long getTime() {
        return mCalendar.getTimeInMillis();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {


            case R.id.tv_tarih_gun:
                processEventFor(mTextGun, event);
                break;

            case R.id.tv_tarih_ay:
                processEventFor(mTextAy, event);
                break;

            case R.id.tv_tarih_yil:
                processEventFor(mTextYil, event);
                break;

        }


        return true;
    }

    private void processEventFor(TextView textView, MotionEvent event) {
/*
*
* SOL - 0
* YUKARIDA - 1
* SAG - 2
* AŞAĞI - 3
*
* */
        Drawable[] drawables = textView.getCompoundDrawables();

        if (yukariDrawableVarmi(drawables) && asagiDrawableVarmi(drawables)) {


            Rect yukariSinir = drawables[1].getBounds();
            Rect asagiSinir = drawables[3].getBounds();

            float x = event.getX();
            float y = event.getY();

            if (yukariDrawableTiklandi(textView, yukariSinir, x, y)) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   // Toast.makeText(getContext(), "Yukarı tıklandı : " + textView.getId(), Toast.LENGTH_SHORT).show();
                    arttir(textView.getId());
                    arkaplaniDegistir(textView, 1);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    arkaplaniEskiHalineGetir(textView);
                }


            } else if (asagiDrawableTiklandi(textView, asagiSinir, x, y)) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   // Toast.makeText(getContext(), "Aşağı tıklandı :" + textView.getId(), Toast.LENGTH_SHORT).show();
                    azalt(textView.getId());
                    arkaplaniDegistir(textView, 2);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    arkaplaniEskiHalineGetir(textView);
                }

            } else {


            }


        }

    }

    private void arkaplaniEskiHalineGetir(TextView textView) {

        textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal,0,R.drawable.down_normal);
    }

    private void arkaplaniDegistir(TextView textView, int i) {

        if(i==1){

            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal_pressed,0,R.drawable.down_normal);

        }else {


            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal,0,R.drawable.down_normal_pressed);

        }


    }

    private void azalt(int id) {

        switch (id){

            case R.id.tv_tarih_gun:
                mCalendar.add(Calendar.DATE, -1);
                break;

            case R.id.tv_tarih_ay:
                mCalendar.add(Calendar.MONTH, -1);
                break;

            case R.id.tv_tarih_yil:
                mCalendar.add(Calendar.YEAR, -1);
                break;
        }

        textviewlariYeniTarihleGuncelle(mCalendar);
    }

    private void textviewlariYeniTarihleGuncelle(Calendar mCalendar) {

        int gun=mCalendar.get(Calendar.DATE);
        int yil=mCalendar.get(Calendar.YEAR);

        mTextGun.setText(""+gun);
        mTextAy.setText(mFormatter.format(mCalendar.getTime()));
        mTextYil.setText(""+yil);
    }

    private void arttir(int id) {

        switch (id){

            case R.id.tv_tarih_gun:
                mCalendar.add(Calendar.DATE, 1);
                break;

            case R.id.tv_tarih_ay:
                mCalendar.add(Calendar.MONTH, 1);
                break;

            case R.id.tv_tarih_yil:
                mCalendar.add(Calendar.YEAR, 1);
                break;
        }

        textviewlariYeniTarihleGuncelle(mCalendar);

    }


    private boolean yukariDrawableTiklandi(TextView textView, Rect yukariSinir, float x, float y) {

        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();

        int ymin = textView.getPaddingTop();
        int ymax = textView.getPaddingTop() + yukariSinir.height();

        return x > xmin && x < xmax && y > ymin && y < ymax;

    }

    private boolean asagiDrawableTiklandi(TextView textView, Rect asagiSinir, float x, float y) {

        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();

        int ymax = textView.getHeight() - textView.getPaddingBottom();
        int ymin = ymax - asagiSinir.height();


        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean yukariDrawableVarmi(Drawable[] drawables) {

        return drawables[1] != null;
    }

    private boolean asagiDrawableVarmi(Drawable[] drawables) {

        return drawables[3] != null;
    }


}
