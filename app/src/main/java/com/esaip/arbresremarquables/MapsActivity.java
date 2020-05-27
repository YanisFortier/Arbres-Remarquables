package com.esaip.arbresremarquables;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.esaip.arbresremarquables.Formulaires.AjoutAlignement;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MapsActivity extends FragmentActivity {

    private static LatLngBounds FranceMetroBounds = new LatLngBounds(
            new LatLng(42.6965954131, -4.32784220122),
            new LatLng(50.4644483399, 7.38468690323)
    );
    // Volley - GeoJSON
    public static JSONArray JsonArbre;
    private RequestQueue mQueue;
    //Location
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this)); //Cache pour la map
        setContentView(R.layout.activity_maps);

        //Volley - GeoJSON
        mQueue = Volley.newRequestQueue(this);
        jsonParse();

        //Boutons
        FloatingTextButton btnArbre = findViewById(R.id.floatingTxtBtnArbre);
        FloatingTextButton btnYanis = findViewById(R.id.floatingTxtBtnYanis);
        FloatingTextButton btnMaxime = findViewById(R.id.floatingTxtBtnMaxime);

        // Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }

        //Boutons
        btnArbre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, AjoutPhoto.class);
                startActivity(intent);
            }
        });

        btnMaxime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, AjoutAlignement.class);
                startActivity(intent);
            }
        });

        btnYanis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();
            }
        });

        map = findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);
        IMapController mapController = map.getController();

        GeoPoint startPoint = new GeoPoint(47.47372, -0.53829);
        mapController.setCenter(startPoint);


        Marker m = new Marker(map);
        m.setPosition(new GeoPoint(47.5, -0.50));
        m.setIcon(getResources().getDrawable(R.drawable.arbre));
        m.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
        m.setSnippet("Je suis le snippet  \n d'un arbre");
        m.setTitle("Je suis un arbre");
        map.getOverlays().add(m);

        Marker m2 = new Marker(map);
        m2.setPosition(new GeoPoint(47.45, -0.50));
        m2.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
        m2.setTitle("Je suis un alignement");
        m2.setIcon(getResources().getDrawable(R.drawable.alignement));
        map.getOverlays().add(m2);

        Marker m3 = new Marker(map);
        m3.setPosition(new GeoPoint(47.4, -0.50));
        m3.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
        m3.setTitle("Je suis un espace boisé");
        m3.setIcon(getResources().getDrawable(R.drawable.espace));
        map.getOverlays().add(m3);

        mapController.setZoom(13.00);
        map.invalidate();

/* //Fonction Onclick qui récupère lat/long et lance le prise de photo
        final MapEventsReceiver mReceive = new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Toast.makeText(getBaseContext(),p.getLatitude() + " - "+p.getLongitude(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MapsActivity.this, AjoutEspaceBoise.class);

                intent.putExtra("latitude",p.getLatitude());
                intent.putExtra("longitude",p.getLongitude());

                startActivity(intent);
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };*/
        //map.getOverlays().add(new MapEventsOverlay(mReceive));

    }

    private void jsonParse() {
        String urlArbres = "https://www.sauvegarde-anjou.org/arbres1/data/arbres.geojson";
        String urlAlignements = "https://www.sauvegarde-anjou.org/arbres1/data/arbres.geojson";
        String urlEspacesBoises = "https://www.sauvegarde-anjou.org/arbres1/data/arbres.geojson";

        JsonObjectRequest requestArbre = new JsonObjectRequest(Request.Method.GET, urlArbres, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("features");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Marker m = new Marker(map);
                                m.setPosition(new GeoPoint(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())));
                                m.setIcon(getResources().getDrawable(R.drawable.arbre));
                                m.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
                                m.setSnippet(jsonArray.getJSONObject(i).getJSONObject("properties").get("Nom de l'arbre").toString());
                                m.setTitle(jsonArray.getJSONObject(i).getJSONObject("properties").get("Photo").toString());
                                map.getOverlays().add(m);
                                map.invalidate();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


        mQueue.add(requestArbre);
        //mQueue.add(requestAlignement);
        //mQueue.add(requestEspaceBoise);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}
