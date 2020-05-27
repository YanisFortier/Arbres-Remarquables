package com.esaip.arbresremarquables;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MapsActivity extends FragmentActivity {

    private static LatLngBounds FranceMetroBounds = new LatLngBounds(
            new LatLng(42.6965954131, -4.32784220122),
            new LatLng(50.4644483399, 7.38468690323)
    );

    // Volley - GeoJSON
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
        //TODO Attention, c'est pas en Async et ça fait ralentir l'appli
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mQueue = Volley.newRequestQueue(this);
        chargementJSON();

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

        mapController.setZoom(13.00);
        map.invalidate();

        /*
        //Fonction Onclick qui récupère lat/long et lance le prise de photo
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
        };
        */

    }

    private void chargementJSON() {
        String urlArbres = "https://www.sauvegarde-anjou.org/arbres1/data/arbres.geojson";
        final String urlAlignements = "https://www.sauvegarde-anjou.org/arbres1/data/alignements.geojson";
        String urlEspacesBoises = "https://www.sauvegarde-anjou.org/arbres1/data/espaces_boises.geojson";

        //Chargemement des Arbres
        JsonObjectRequest requestArbre = new JsonObjectRequest(Request.Method.GET, urlArbres, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("features");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonProperties = jsonArray.getJSONObject(i).getJSONObject("properties");

                                Marker m = new Marker(map);
                                m.setPosition(new GeoPoint(
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())));
                                m.setSnippet(
                                        "<b> Date : </b> " + jsonProperties.optString("Date", "Dates inconnues") + "<br>"
                                                + "<b> Remarquable : </b> " + jsonProperties.optString("Remarquable", "Inconnu") + "<br>"
                                                + "<b> Remarquabilité : </b> " + jsonProperties.optString("Remarquabilité", "Inconnu") + "<br>"
                                                + "<b> Nom botanique : </b> " + jsonProperties.optString("Nom botanique", "Inconnu") + "<br>"
                                                + "<b> Situé sur un espace : </b> " + jsonProperties.optString("Situé sur un espace", "Inconnu") + "<br>"
                                                + "<b> Vérification : </b> " + jsonProperties.optString("Vérification", "Inconnu") + "<br>"
                                                + "<b> Adresse : </b> " + jsonProperties.optString("Adresse", "Inconnue") + "<br>"
                                                + "<b> Pseudonyme : </b> " + jsonProperties.optString("Pseudonyme", "Inconnu") + "<br>"
                                                + "<b> Nom de l'arbre : </b> " + jsonProperties.optString("Nom de l'arbre", "Inconnu") + "<br>"
                                                + "<b> Observations : </b> " + jsonProperties.optString("Observations", "Pas d'observations") + "<br>"
                                                + "<b> Identifiant de la réponse : </b> " + jsonProperties.optString("Identifiant de la réponse", "Inconnu") + "<br>");

                                m.setTitle("Informations");

                                // TODO : Oui c'est dégeu, je sais, Je patcherai plus tard. Promis !
                                if (!jsonProperties.optString("Photo").equals("Aucune")) {
                                    if (!jsonProperties.optString("Photo").equals("171.jpg")) {
                                        if (!jsonProperties.optString("Photo").equals("206.jpg")) {
                                            if (!jsonProperties.optString("Photo").equals("242.jpg")) {
                                                if (!jsonProperties.optString("Photo").equals("245.jpg")) {
                                                    if (!jsonProperties.optString("Photo").equals("246.jpg")) {
                                                        if (!jsonProperties.optString("Photo").equals("263.jpg")) {
                                                            Log.i("Arbre", jsonProperties.getString("Identifiant"));
                                                            String urlImage = "https://www.sauvegarde-anjou.org/arbres1/images/arbres/" + jsonProperties.getString("Photo");
                                                            m.setImage(drawableFromUrl(urlImage));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                m.setIcon(getResources().getDrawable(R.drawable.arbre));
                                m.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
                                map.getOverlays().add(m);
                                map.invalidate();
                            }
                        } catch (JSONException | IOException e) {
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

        //Chargemement des Alignements
        JsonObjectRequest requestAlignement = new JsonObjectRequest(Request.Method.GET, urlAlignements, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("features");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Marker m = new Marker(map);
                                m.setPosition(new GeoPoint(
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())));
                                m.setIcon(getResources().getDrawable(R.drawable.alignement));
                                m.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
                                m.setSnippet(jsonArray.getJSONObject(i).getJSONObject("properties").get("Identifiant").toString());
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

        //Chargemement des Espaces boisés
        JsonObjectRequest requestEspaceBoise = new JsonObjectRequest(Request.Method.GET, urlEspacesBoises, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("features");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Marker m = new Marker(map);
                                m.setPosition(new GeoPoint(
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())));
                                m.setIcon(getResources().getDrawable(R.drawable.espace));
                                m.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
                                m.setSnippet(jsonArray.getJSONObject(i).getJSONObject("properties").get("Identifiant").toString());
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
        mQueue.add(requestAlignement);
        mQueue.add(requestEspaceBoise);
    }

    // TODO : Metre ça en Async
    Drawable drawableFromUrl(String url) throws java.io.IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent", "Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        Bitmap bitmap = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), bitmap);
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
