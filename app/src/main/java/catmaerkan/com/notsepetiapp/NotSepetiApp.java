package catmaerkan.com.notsepetiapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Erkan ÇATMA on 21.10.2017.
 */

public class NotSepetiApp extends Application {

    public static void sharedYaz(Context context, int secilenFiltre){

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("filtre", secilenFiltre);
        editor.apply();

    }

    public static int sharedOku(Context context){

        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        int secilenFiltre=preferences.getInt("filtre", 0);
        return secilenFiltre;

    }
}
