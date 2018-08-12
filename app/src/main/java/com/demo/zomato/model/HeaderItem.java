package com.demo.zomato.model;

import android.support.annotation.NonNull;

/**
 * Created by Jyoti on 8/12/2018.
 */

public
class HeaderItem extends ListItem {

    @NonNull
    private String cuisineName;

    public void setCuisine(String cuisine) {
        cuisineName = cuisine;
    }

    @NonNull
    public String getCuisine() {
        return cuisineName;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

}