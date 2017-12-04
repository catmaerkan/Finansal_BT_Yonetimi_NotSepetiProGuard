package catmaerkan.com.notsepetiapp.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import catmaerkan.com.notsepetiapp.R;

/**
 * Created by Erkan ÇATMA on 14.10.2017.
 */

public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mOrientation;

    public Divider(Context context, int orientation){

       // mDivider=context.getDrawable(R.drawable.divider);
        mDivider= ContextCompat.getDrawable(context, R.drawable.divider);
        if(orientation != LinearLayoutManager.VERTICAL){
            throw new IllegalArgumentException("Bu dekorasyon burda kullanılamaz");
        }
        mOrientation= orientation;

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == LinearLayoutManager.VERTICAL){
            drawHorizontalDivider(c, parent, state);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int sol, yukari, sag, asagi;
        sol=parent.getPaddingLeft();
        sag=parent.getWidth()-parent.getPaddingRight();
        int elemanSayisi=parent.getChildCount();

        for(int i=0; i<elemanSayisi; i++){

            if(AdapterNotlarListesi.FOOTER != parent.getAdapter().getItemViewType(i)){
                View suankiView=parent.getChildAt(i);
                RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) suankiView.getLayoutParams();

                yukari=suankiView.getTop() -params.topMargin;
                asagi=yukari+mDivider.getIntrinsicHeight();

                mDivider.setBounds(sol, yukari, sag, asagi);
                mDivider.draw(c);
               // Log.e("CATMA", ""+sol +" " + yukari + " " + sag + " " + asagi );

            }




        }


    }
}
