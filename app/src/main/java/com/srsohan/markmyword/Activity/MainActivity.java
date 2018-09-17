package com.srsohan.markmyword.Activity;

/*
Creator Sohanoor Rahman
date: 16 Sep 2018
*/

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.srsohan.markmyword.Adapter.DeliveryDataAdapter;
import com.srsohan.markmyword.App.API;
import com.srsohan.markmyword.App.CacheRequest;
import com.srsohan.markmyword.App.EndlessRecyclerViewScrollListener;
import com.srsohan.markmyword.App.InternetConnectionDetector;
import com.srsohan.markmyword.Dialog.showNetworkConnectionPopUp;
import com.srsohan.markmyword.Model.DeliveryModel;
import com.srsohan.markmyword.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private ProgressDialog pDialog;
    private InternetConnectionDetector internetDetector;
    private showNetworkConnectionPopUp showNetworkPopup;

    private DeliveryDataAdapter ddAdapter = new DeliveryDataAdapter();
    private List<DeliveryModel> deliveryList = new ArrayList<>();

    private EndlessRecyclerViewScrollListener scrollListener;
    private RelativeLayout mEmptyLayout;


    int offset = 0;
    int limit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyLayout = (RelativeLayout) findViewById(R.id.empty_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Things To Deliver");
        setSupportActionBar(toolbar);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        internetDetector = new InternetConnectionDetector(this);
        showNetworkPopup = new showNetworkConnectionPopUp(this);

        //checking cache is clear or not
        try {
            File cache = getCacheDir();
            File appDir = new File(cache.getParent());
            if (appDir.exists()) {
                mEmptyLayout.setVisibility(View.VISIBLE);
                if(!internetDetector.isConnectingToInternet()){
                    showNetworkPopup.showNetworkDialog();
                }
            }
        } catch (Exception e) { e.printStackTrace();}


        mEmptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reload
                Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();

            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_delivery);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };


        mRecyclerView.addOnScrollListener(scrollListener);
        DeliveryDataRequest(offset, limit);
    }


    private void DeliveryDataRequest(int offset, int limit) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = API.HOME_PAGE_API + "offset=" + offset + "&limit=" + limit;
        pDialog.show();

        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONArray responseA = new JSONArray(jsonString);

                    for (int i = 0; i < responseA.length(); i++) {
                        JSONObject item = responseA.getJSONObject(i);

                        int id = item.getInt("id");
                        String description = item.getString("description");
                        String imageUrl = item.getString("imageUrl");
                        JSONObject location = item.getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");
                        String address = location.getString("address");


                        Log.e("id", "" + id);
                        Log.e("description", "" + description);
                        Log.e("imageUrl", "" + imageUrl);
                        Log.e("lat", "" + lat);
                        Log.e("lng", "" + lng);
                        Log.e("address", "" + address);

                        deliveryList.add(new DeliveryModel(id, description, imageUrl, lat, lng, address));

                    }

                    ddAdapter.DeliveryDataAdapter(deliveryList, MainActivity.this);
                    mRecyclerView.setAdapter(ddAdapter);
                    ddAdapter.notifyDataSetChanged();
                    scrollListener.resetState();
                    mEmptyLayout.setVisibility(View.GONE);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);
    }

    private void loadNextDataFromApi(int offset) {
        DeliveryDataRequest(offset, limit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
