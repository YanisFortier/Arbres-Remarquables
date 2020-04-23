package com.esaip.arbresremarquables;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static LatLngBounds FranceMetroBounds = new LatLngBounds(
            new LatLng(42.6965954131, -4.32784220122),
            new LatLng(50.4644483399, 7.38468690323)
    );

    //Location
    private GoogleMap mMap;
    private Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Double posLong, posLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Boutons
        FloatingTextButton btnArbre = findViewById(R.id.floatingTxtBtnArbre);
        FloatingTextButton btnAlignement = findViewById(R.id.floatingTxtBtnAlignement);
        FloatingTextButton btnEspaceBoise = findViewById(R.id.floatingTxtBtnEspaceBoise);

        // Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else {
            //Location
            getLocation();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnArbre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, AjoutArbre.class);
                intent.putExtra("type","arbre");
                intent.putExtra("longitude",mCurrentLocation.getLongitude());
                intent.putExtra("latitude",mCurrentLocation.getLatitude());
                startActivity(intent);
            }
        });

        btnAlignement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, AjoutPhoto.class);
                intent.putExtra("type","alignement");
                intent.putExtra("longitude",mCurrentLocation.getLongitude());
                intent.putExtra("latitude",mCurrentLocation.getLatitude());
                startActivity(intent);
            }
        });

        btnEspaceBoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, AjoutPhoto.class);
                intent.putExtra("type","espace");
                intent.putExtra("longitude",mCurrentLocation.getLongitude());
                intent.putExtra("latitude",mCurrentLocation.getLatitude());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(FranceMetroBounds, 150), 2000, null);
            }
        });

        LatLng pos1 = new LatLng(47.470750,-0.544733);
        mMap.addMarker(new MarkerOptions().position(pos1).title("New Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.arbre)));
    }

    private void getLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            mCurrentLocation = (Location) task.getResult();
                        }
                        else{
                            Toast.makeText(MapsActivity.this,"Impossible d'obtenir la localisation",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("tag","SecurityException");
        }
    }
}
