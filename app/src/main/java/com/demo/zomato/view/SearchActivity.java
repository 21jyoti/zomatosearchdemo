package com.demo.zomato.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.zomato.SearchInterface;
import com.demo.zomato.constants.EndlessRecyclerOnScrollListener;
import com.demo.zomato.model.ListItem;
import com.demo.zomato.presenter.SearchPresenter;
import com.demo.zomatosearch.R;

import java.util.List;


public class SearchActivity extends BaseActivity implements SearchInterface {

    public SearchPresenter searchPresenter;
    private RecyclerView orderList_rv;
    private EditText searchET;
    private ImageView searchIV;
    private ProgressBar pbar;
    private TextView emptyText;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager layoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    protected static final int LIMIT = 20;
    protected int page = 0;
    private boolean NO_MORE_ITEMS = false;
    protected int PAGE_COUNT = 1;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachPresenter();
        initViews();
        showBlankUI();
        setClickListeners();
        setListAdapter();

    }

    private void setClickListeners() {
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListAdapter();
                reintitializeVariables();
                makeServiceCall(searchET.getText().toString(), page, LIMIT);
            }
        });
    }

    private void reintitializeVariables() {
        searchPresenter.reinitializeVariables();
        page = 0;
    }


    @Override
    public void attachPresenter() {
        //Attach Presenter
        if (searchPresenter == null) {
            searchPresenter = new SearchPresenter();
            searchPresenter.onAttachPresenter(SearchActivity.this);

        }
    }

    @Override
    public Context getContext() {
        return SearchActivity.this;
    }

    @Override
    public void initViews() {
        orderList_rv = (RecyclerView) findViewById(R.id.orderList_rv);
        searchET = (EditText) findViewById(R.id.searchET);
        searchIV = (ImageView) findViewById(R.id.searchIV);
        emptyText = (TextView) findViewById(R.id.emptyText);
        pbar = (ProgressBar) findViewById(R.id.pbar);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderList_rv.setLayoutManager(layoutManager);
    }

    @Override
    public void makeServiceCall(String query, int start, int count) {
        searchPresenter.callServiceAndGetResponse(query, start, count);
    }

    @Override
    public void showLoader() {
        pbar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);
    }

    @Override
    public void hideLoader() {
        pbar.setVisibility(View.GONE);
        emptyText.setVisibility(View.GONE);
    }

    @Override
    public void setLoading(Boolean flag) {
        loading = flag;
    }

    @Override
    public void showBlankUI() {
        emptyText.setVisibility(View.VISIBLE);
        pbar.setVisibility(View.GONE);
    }

    @Override
    public void updateUIonSuccess(List<ListItem> list) {
        updateListAdapter(list);
        setEndLessRecycler();
    }

    private void updateListAdapter(List<ListItem> list) {
        searchAdapter.update(list);
    }

    private void setEndLessRecycler() {

        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d("The current page is " + current_page + " No More Items " + NO_MORE_ITEMS, " Is Loadding is " + loading);
                if (!NO_MORE_ITEMS) {
                    PAGE_COUNT = current_page;
                    pbar.setVisibility(View.VISIBLE);
                    page = page + LIMIT;
                    makeServiceCall(searchET.getText().toString(), page, LIMIT);
                }
            }
        };
        orderList_rv.setOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void setListAdapter() {
        if(searchAdapter!=null) {
            searchAdapter = new SearchAdapter(getContext());
            orderList_rv.setAdapter(searchAdapter);
        }else{
            searchAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void updateUIonFailure(String msg) {
        pbar.setVisibility(View.GONE);
        emptyText.setVisibility(View.VISIBLE);
        emptyText.setText(msg);
    }
}
