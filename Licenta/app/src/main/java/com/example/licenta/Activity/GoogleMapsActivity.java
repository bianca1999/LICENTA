package com.example.licenta.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import com.example.licenta.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback{
    private boolean locationPermission=false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private static final float DEFAULT_ZOOM=16f;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        if(isServiceAvailable()){
            getLocationPermission();
            initMap();


        }
    }
    private void initMap() {
        SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(GoogleMapsActivity.this);
    }

    public boolean isServiceAvailable() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(GoogleMapsActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d("GoogleMapsActivity","Google Platy Service is Working");
            return true;
        }
        return false;
    }

    public void getLocationPermission(){
        String[] permission={Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locationPermission=true;
                    initMap();
            }
            else{
                ActivityCompat.requestPermissions(this
                        ,permission
                        ,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            {
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }

        }
    }

    public void moveCamera(LatLng latlng, float zoom,String title){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        MarkerOptions markerOptions=new MarkerOptions()
                .position(latlng)
                .title(title);
        map.addMarker(markerOptions);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermission=false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length>0){
                    for(int i=0; i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            locationPermission=false;
                            return;
                        }
                    }
                    locationPermission=true;
                    initMap();
                }
        }
    }

    public void geoLocate(){
        Log.d("GoogleMapsActivity", "geoLocate: geolocating");
        String addressUser=getIntent().getStringExtra("address");
        Geocoder geocoder=new Geocoder(GoogleMapsActivity.this);
        List<Address> list=new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(addressUser,1);
            Log.e("GoogleMapsActiviry","California");

        }catch (IOException e){
            Log.e("GoogleMapsActiviry","geoLocate:IOException"+e);
        }
        if(list.size()>0){
            Address address=list.get(0);
            moveCamera(new LatLng(address.getLatitude()
                    ,address.getLongitude()),
                    DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Map is ready",Toast.LENGTH_SHORT).show();
        map=googleMap;

        if(locationPermission){
            geoLocate();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);

        }

    }
}
