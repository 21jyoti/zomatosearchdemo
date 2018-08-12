package com.demo.zomato.model;

import android.support.annotation.NonNull;

/**
 * Created by Jyoti on 8/12/2018.
 */

public class Item extends ListItem {

    @NonNull
    private Restaurant_ Restaurant_;


    @NonNull
    public Restaurant_ getRestaurant_() {
        return Restaurant_;
    }
    public void setRestaurant_(Restaurant_ Restaurant_){
        this.Restaurant_ = Restaurant_;
    }

    @Override
    public int getType() {
        return TYPE_ITEM;
    }

}