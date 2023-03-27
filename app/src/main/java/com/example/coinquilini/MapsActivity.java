package com.example.coinquilini;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.coinquilini.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code = 101;
    private double lat,lng;
    ImageButton tab,banc,post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("Mappa","Mappa");


        tab = findViewById(R.id.btn_tabbacchi);
        banc = findViewById(R.id.btn_banche);
        post = findViewById(R.id.btn_poste);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        tab.setOnClickListener(v -> {

            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=" + lat + "," + lng +
                    "&radius=1000" +
                    "&type=convenience_store" +
                    "&sensor=true" +
                    "&key=AIzaSyD8RsctjyWv6HxDBZ4-0DCjRZqbnvCcLFk");

            String url = stringBuilder.toString();
            Object[] prendiDati = new Object[2];
            prendiDati[0] = mMap;
            prendiDati[1] = url;

            PrendiDati prendiDati1 = new PrendiDati();
            prendiDati1.execute(prendiDati);

        });

        banc.setOnClickListener(v -> {

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=" + lat + "," + lng +
                    "&radius=1000" +
                    "&type=bank" +
                    "&sensor=true" +
                    "&key=AIzaSyD8RsctjyWv6HxDBZ4-0DCjRZqbnvCcLFk";
            Object[] prendiDati = new Object[2];
            prendiDati[0] = mMap;
            prendiDati[1] = url;

            PrendiDati prendiDati1 = new PrendiDati();
            prendiDati1.execute(prendiDati);
        });

        post.setOnClickListener(v -> {

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=" + lat + "," + lng +
                    "&radius=1000" +
                    "&type=post_office" +
                    "&sensor=true" +
                    "&key=AIzaSyD8RsctjyWv6HxDBZ4-0DCjRZqbnvCcLFk";
            Object[] prendiDati = new Object[2];
            prendiDati[0] = mMap;
            prendiDati[1] = url;

            PrendiDati prendiDati1 = new PrendiDati();
            prendiDati1.execute(prendiDati);

        });

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
        getCurrentLocation();
    }

    private void getCurrentLocation()
    {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_code);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(5000);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Toast.makeText(getApplicationContext(),"Posizione= "+locationResult,Toast.LENGTH_SHORT).show();

                if(locationResult == null)
                {

                    Toast.makeText(getApplicationContext(),"La posizione e' null",Toast.LENGTH_SHORT).show();
                    return;

                }

                for (Location location:locationResult.getLocations())
                    if(location!=null) Toast.makeText(getApplicationContext(),"Posizione corrente = "+location.getLongitude(),Toast.LENGTH_SHORT).show();

            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {

            if(location != null)
            {
                lat = location.getLatitude();
                lng = location.getLongitude();

                LatLng latLng = new LatLng(lat,lng);

                mMap.addMarker(new MarkerOptions().position(latLng).title("Posizione attuale"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            }

        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (Request_code)
        {
            case Request_code:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) getCurrentLocation();
        }
    }

}