package com.architectica.rental05.thevendorsapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener{

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                //permission granted

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation,"Your Location");

            }

        }
        else {

            //permission denied

            finish();
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void backpressed(View v){
        super.onBackPressed();
    };


    public void centerMapOnLocation(Location location, String title){

        //center map on the location passed

        if (location != null){

            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.clear();

            if (title != "Your Location") {
                mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            } else {

                mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

            }

        }
        else {

            Toast.makeText(this, "Unable to detect location.Please turn on your gps to detect your location manually", Toast.LENGTH_SHORT).show();

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //map is ready

        mMap.setOnMapClickListener(this);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //centerMapOnLocation(location,"Your location");

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23){

            //if version is less than 23,then get the location updates directly
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }
        else {

            //if version is more than 23,then check if the user has permission for location updates

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                //user have permission
                //get the location updates

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation,"Your Location");

            }
            else {

                //user does not have permission
                //request for permission
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }

        }

    }

    @Override
    public void onMapClick(LatLng latLng) {

        //a location on the map is clicked

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        //get the address and lat,long of that location

        String address = "";

        try {

            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            if (listAddresses != null && listAddresses.size()>0){

                if (listAddresses.get(0).getThoroughfare() != null){

                    if (listAddresses.get(0).getSubThoroughfare() != null){

                        address += listAddresses.get(0).getSubThoroughfare() + " ";

                    }

                    address += listAddresses.get(0).getThoroughfare();

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (address == ""){

            address = "Your room's location";

        }

        //center map on the clicked location

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);

        centerMapOnLocation(location,address);

        //pass the location details for uploading them to firebase

        UploadVehicleActivity.locationLatitude = Double.toString(latLng.latitude);
        UploadVehicleActivity.locationLongitude = Double.toString(latLng.longitude);

    }
}
