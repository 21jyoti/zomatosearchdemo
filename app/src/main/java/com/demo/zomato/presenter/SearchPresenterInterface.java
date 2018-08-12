package com.demo.zomato.presenter;

import com.demo.zomato.SearchInterface;

/**
 * Created by Jyoti on 8/11/2018.
 */

public interface SearchPresenterInterface {
    void onAttachPresenter(SearchInterface searchListener);
    void callServiceAndGetResponse(String query, int start, int count);

    void reinitializeVariables();
}
