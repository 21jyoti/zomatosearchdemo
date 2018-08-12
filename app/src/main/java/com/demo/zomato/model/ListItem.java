package com.demo.zomato.model;

/**
 * Created by Jyoti on 8/12/2018.
 */

public abstract class ListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    abstract public int getType();

}