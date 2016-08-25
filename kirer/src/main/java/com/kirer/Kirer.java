package com.kirer;

import android.content.Context;

/**
 * Created by xinwb on 2016/8/23.
 */
public class Kirer {

    private static Context mContext;

    public static void init(Context context){
        Kirer.mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }
}
