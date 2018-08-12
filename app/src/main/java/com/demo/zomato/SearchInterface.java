package com.demo.zomato;

import android.content.Context;

import com.demo.zomato.model.ListItem;

import java.util.List;

/**
 * Created by Jyoti on 8/11/2018.
 */

public interface SearchInterface {
     void attachPresenter();
     Context getContext();
    void initViews();
    void makeServiceCall(String query, int start, int count);
    void showLoader();
    void hideLoader();
    void setLoading(Boolean set);
    void showBlankUI();
    void updateUIonSuccess(List<ListItem> list);
    void updateUIonFailure(String msg);
}
