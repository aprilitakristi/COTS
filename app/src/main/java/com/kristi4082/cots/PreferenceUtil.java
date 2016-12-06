package com.kristi4082.cots;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aprilita on 12/6/2016.
 */

public class PreferenceUtil {
    private static final String PREF_NAME = "animal_pref";
    private static final String COUNT = "count";
    private SharedPreferences sharedPreferences;

    private PreferenceUtil (Context context){
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceUtil with(Context context){
        return new PreferenceUtil(context);
    }

    public PreferenceUtil viewCountAdd(String animalName){
        int current = sharedPreferences.getInt(animalName, 0);
        current+=1;
        sharedPreferences.edit()
                .putInt(animalName, current)
                .apply();
        return this;
    }

    public int getCount(String animalName){
        return sharedPreferences.getInt(animalName, 1);
    }

}
