package com.srsohan.markmyword.Activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.srsohan.markmyword.R;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar toolbar;

    private double latitude, longitude;
    private String address, description;

    private TextView mAddress, mDescription;
    private Button mCancel;
    private RelativeLayout mDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //setup toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Delivery Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //setup google map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //setup UI
        mAddress = (TextView) findViewById(R.id.address);
        mDescription = (TextView) findViewById(R.id.description);
        mCancel = (Button) findViewById(R.id.layout_gone);
        mDetails = (RelativeLayout) findViewById(R.id.details_layout);

        //get data from parent activity
        Bundle mapValue = getIntent().getExtras();
        latitude = mapValue.getDouble("lat");
        longitude = mapValue.getDouble("lng");
        address = mapValue.getString("address");
        description = mapValue.getString("description");

        Log.d("latitude", "" + latitude);
        Log.d("longitude", "" + longitude);
        Log.d("address", address);
        Log.d("description", description);

        //send data to UI
        mAddress.setText(address);
        mDescription.setText(description);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDetails.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(address);
        //   marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_tag));

        mMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(15).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mDetails.setVisibility(View.VISIBLE);
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
