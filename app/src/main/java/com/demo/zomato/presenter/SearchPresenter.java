package com.demo.zomato.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.zomato.SearchInterface;
import com.demo.zomato.constants.EndPoints;
import com.demo.zomato.constants.GSONHelper;
import com.demo.zomato.constants.OnGsonParseCompleteListener;
import com.demo.zomato.model.HeaderItem;
import com.demo.zomato.model.Item;
import com.demo.zomato.model.ListItem;
import com.demo.zomato.model.Restaurant;
import com.demo.zomato.model.Restaurant_;
import com.demo.zomato.model.SearchResultModel;
import com.demo.zomatosearch.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SearchPresenter implements SearchPresenterInterface {


    private Context context;
    private SearchInterface searchListener;
    private List<Restaurant> list;
    private HashMap<String, ArrayList<Restaurant_>> map = new HashMap<>();
    private List<ListItem> consolidatedList = new ArrayList<>();

    @Override
    public void onAttachPresenter(SearchInterface searchListener) {
        try {
            this.searchListener = searchListener;
            this.context = searchListener.getContext();
            list = new ArrayList<>();

        } catch (Exception e) {

        }
    }

    protected GSONHelper getGsonHelper() {
        return GSONHelper.getInstance();
    }

    @Override
    public void callServiceAndGetResponse(String query, final int start, int count) {
        searchListener.showLoader();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = EndPoints.SEARCH_API;
        String url = String.format(URL, query, start, count);
        JSONObject jsonBody = new JSONObject();
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                searchListener.hideLoader();
                getGsonHelper().parse(response, SearchResultModel.class, new OnGsonParseCompleteListener<SearchResultModel>() {
                    @Override
                    public void onParseComplete(SearchResultModel data) {
                        if (data.getRestaurants().size() > 0) {
                            filterCuisineWiseData(data);
                            createConsolidateList();
                            list = data.getRestaurants();
                            searchListener.setLoading(false);
                            searchListener.updateUIonSuccess(consolidatedList);
                        }else if(start==0){
                            searchListener.updateUIonFailure(context.getResources().getString(R.string.no_data_found));
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("VOLLEY", volleyError.toString());
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                searchListener.updateUIonFailure(message);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(EndPoints.API_KEY_HEADER, EndPoints.API_KEY);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void reinitializeVariables() {
        map = new HashMap<>();
        list = new ArrayList<>();
        consolidatedList = new ArrayList<>();
    }

    private void createConsolidateList() {
        // We linearly add every item into the consolidatedList.

        for (String cuisine : map.keySet()) {
            HeaderItem hItem = new HeaderItem();
            hItem.setCuisine(cuisine);
            consolidatedList.add(hItem);

            for (Restaurant_ pojoOfJsonArray : map.get(cuisine)) {
                Item item = new Item();
                item.setRestaurant_(pojoOfJsonArray);
                consolidatedList.add(item);
            }
        }

    }

    private void filterCuisineWiseData(SearchResultModel data) {
        List<Restaurant> restaurantList = data.getRestaurants();
        ArrayList<String> cuisineTempList = new ArrayList<>();
        for (int i = 0; i < restaurantList.size(); i++) {
            String cuisine = restaurantList.get(i).getRestaurant().getCuisines();
            if (cuisine.contains(",")) {
                String[] arrOfStr = cuisine.split(",");
                for (String str : arrOfStr)
                    cuisineTempList.add(str.trim());
            } else {
                cuisineTempList.add(cuisine.trim());
            }
        }
        HashSet<String> listToSet = new HashSet<String>(cuisineTempList);
//Creating Arraylist without duplicate values
        List<String> cuisineList = new ArrayList<String>(listToSet);
        for (int i = 0; i < cuisineList.size(); i++) {
            ArrayList<Restaurant_> tempList = new ArrayList<>();
            for (int j = 0; j < restaurantList.size(); j++) {
                if (restaurantList.get(j).getRestaurant().getCuisines().contains(cuisineList.get(i))) {

                    tempList.add(restaurantList.get(j).getRestaurant());

                }

            }
            map.put(cuisineList.get(i), tempList);
        }
        Log.d("cuisineList", "==>" + map.toString());


    }
}
