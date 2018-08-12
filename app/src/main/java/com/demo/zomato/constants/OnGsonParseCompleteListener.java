package com.demo.zomato.constants;

import android.util.Log;

/**
 * Created by Jyoti on 8/11/2018.
 */

public abstract class OnGsonParseCompleteListener<T> {
    public abstract void onParseComplete(T data);
    public void onParseFailure(Exception exception) {
        Log.e("Exception ", exception.toString());
    }

}
