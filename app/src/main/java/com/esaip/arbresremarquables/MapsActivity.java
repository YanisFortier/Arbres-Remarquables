package com.esaip.arbresremarquables;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MapsActivity extends FragmentActivity {

    private static LatLngBounds FranceMetroBounds = new LatLngBounds(
            new LatLng(42.6965954131, -4.32784220122),
            new LatLng(50.4644483399, 7.38468690323)
    );
    //Location
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    // Volley - GeoJSON
    private RequestQueue mQueue;
    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    private Intent intentAjoutPhoto = new Intent(MapsActivity.this, AjoutPhoto.class);
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this)); //Cache pour la map
        setContentView(R.layout.activity_maps);

        //Volley - GeoJSON
        //TODO Attention, c'est pas en Async et ça fait ralentir l'appli
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //OpenStreetMap
        map = findViewById(R.id.mapview);
        final Marker markerPos = new Marker(map);

        //Chargement des arbres
        mQueue = Volley.newRequestQueue(this);
        chargementJSON();

        //Boutons
        FloatingActionButton btnInfo = findViewById(R.id.floatingBtnInfo);
        final FloatingTextButton btnArbre = findViewById(R.id.floatingTxtBtnArbre);

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
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });


        //Ajout de l'overlay avec la location utilisateur
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        GeoPoint startPoint = new GeoPoint(47.47372, -0.53829);


        mapController.setCenter(startPoint);
        mapController.setZoom(13.00);
        map.invalidate();


        //Fonction Onclick qui récupère lat/long et lance le prise de photo
        final MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                latitude = p.getLatitude();
                longitude = p.getLongitude();

                markerPos.setPosition(new GeoPoint(p.getLatitude(), p.getLongitude()));
                markerPos.setSnippet("<b> Latitude : </b>" + p.getLatitude() + "<br>"
                        + "<b> Longitude : </b>" + p.getLongitude() + "<br>");
                map.getOverlays().add(markerPos);
                map.invalidate();

                btnArbre.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);
    }

    private void chargementJSON() {
        String urlArbres = "https://www.sauvegarde-anjou.org/arbres1/data/arbres.geojson";
        String urlAlignements = "https://www.sauvegarde-anjou.org/arbres1/data/alignements.geojson";
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
                                m.setPosition(new GeoPoint( //Géolocalisation du marqueur
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())));
                                //On formate un jolie marqueur
                                m.setTitle("Informations");
                                m.setSnippet("<b> Nom de l'arbre : </b> " + jsonProperties.optString("Nom de l'arbre", "Inconnu") + "<br>"
                                        + "<b> Nom botanique : </b> " + jsonProperties.optString("Nom botanique", "Inconnu") + "<br>"
                                        + "<b> Adresse : </b> " + jsonProperties.optString("Adresse", "Inconnue") + "<br>"
                                        + "<b> Date : </b> " + jsonProperties.optString("Date", "Inconnues") + "<br>"
                                        + "<b> Remarquable : </b> " + jsonProperties.optString("Remarquable", "Inconnu") + "<br>"
                                        + "<b> Remarquabilité : </b> " + jsonProperties.optString("Remarquabilité", "Inconnu") + "<br>"
                                        + "<b> Situé sur un espace : </b> " + jsonProperties.optString("Situé sur un espace", "Inconnu") + "<br>"
                                        + "<b> Observations : </b> " + jsonProperties.optString("Observations", "Pas d'observations") + "<br>"
                                        + "<b> Pseudonyme : </b> " + jsonProperties.optString("Pseudonyme", "Inconnu") + "<br>"
                                        + "<b> Vérification : </b> " + jsonProperties.optString("Vérification", "Inconnu") + "<br>"
                                        + "<b> Identifiant de la réponse : </b> " + jsonProperties.optString("Identifiant de la réponse", "Inconnu") + "<br>"
                                        + "<center><b> Cliquez pour afficher la photo </b></center>"
                                );


                                // TODO : Afficher la photo de l'arbre
                                /*
                                Async asynPhoto = new Async();
                                Log.i("Arbre", jsonProperties.getString("Identifiant"));
                                String urlImage = "https://www.sauvegarde-anjou.org/arbres1/images/arbres/" + jsonProperties.getString("Photo");
                                asyncPhoto.execute(urlImage);
                                m.setImage(asyncPhoto.get());
                                m.setImage(drawableFromUrl(urlImage));
                                */

                                m.setIcon(getResources().getDrawable(R.drawable.arbre));
                                m.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
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

        //Chargemement des Alignements
        JsonObjectRequest requestAlignement = new JsonObjectRequest(Request.Method.GET, urlAlignements, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("features");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonProperties = jsonArray.getJSONObject(i).getJSONObject("properties");

                                Marker m = new Marker(map);
                                m.setPosition(new GeoPoint( //Géolocalisation du marqueur
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())));
                                //On formate un jolie marqueur
                                m.setTitle("Informations");
                                m.setSnippet("<b> Nom botanique : </b> " + jsonProperties.optString("Nom botanique", "Inconnu") + "<br>"
                                        + "<b> Nombre d'arbres : </b> " + jsonProperties.optString("Nombre d'arbres", "Inconnu") + "<br>"
                                        + "<b> Adresse : </b> " + jsonProperties.optString("Adresse de l'alignement", "Inconnue") + "<br>"
                                        + "<b> Date : </b> " + jsonProperties.optString("Date", "Inconnues") + "<br>"
                                        + "<b> Espèces : </b> " + jsonProperties.optString("Espèces", "Inconnu") + "<br>"
                                        + "<b> Nombre d'espèces : </b> " + jsonProperties.optString("Nombre d'espèces", "Inconnu") + "<br>"
                                        + "<b> Protection : </b> " + jsonProperties.optString("Protection", "Inconnue") + "<br>"
                                        + "<b> Situé sur un espace : </b> " + jsonProperties.optString("Situé sur un espace", "Inconnu") + "<br>"
                                        + "<b> Observations : </b> " + jsonProperties.optString("Observations", "Pas d'observations") + "<br>"
                                        + "<b> Pseudonyme : </b> " + jsonProperties.optString("Pseudonyme", "Inconnu") + "<br>"
                                        + "<b> Vérification : </b> " + jsonProperties.optString("Vérification", "Inconnu") + "<br>"
                                        + "<b> Identifiant de la réponse : </b> " + jsonProperties.optString("Identifiant de la réponse", "Inconnu") + "<br>"
                                );


                                // TODO : Afficher la photo de l'alignement

                                m.setIcon(getResources().getDrawable(R.drawable.alignement));
                                m.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
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
                                JSONObject jsonProperties = jsonArray.getJSONObject(i).getJSONObject("properties");

                                Marker m = new Marker(map);
                                m.setPosition(new GeoPoint( //Géolocalisation du marqueur
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                        Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())));
                                //On formate un jolie marqueur
                                m.setTitle("Informations");
                                m.setSnippet("<b> Nombre d'arbres : </b> " + jsonProperties.optString("Nombre d'arbres", "Inconnu") + "<br>"
                                        + "<b> Biodiversité : </b> " + jsonProperties.optString("Biodiversité", "Inconnue") + "<br>"
                                        + "<b> Globalement : </b> " + jsonProperties.optString("Globalement", "Inconnu") + "<br>"
                                        + "<b> Point d'eau : </b> " + jsonProperties.optString("Point d'eau", "Inconnu") + "<br>"
                                        + "<b> Abris d'animaux : </b> " + jsonProperties.optString("Abris d'animaux", "Inconnu") + "<br>"
                                        + "<b> Éclairage nocturne : </b> " + jsonProperties.optString("Éclairage nocturne", "Inconnu") + "<br>"
                                        + "<b> Adresse : </b> " + jsonProperties.optString("Adresse de l'espace boisé", "Inconnue") + "<br>"
                                        + "<b> Date : </b> " + jsonProperties.optString("Date", "Inconnues") + "<br>"
                                        + "<b> Situé sur un espace : </b> " + jsonProperties.optString("Situé sur un espace", "Inconnu") + "<br>"
                                        + "<b> Observations : </b> " + jsonProperties.optString("Observations", "Pas d'observations") + "<br>"
                                        + "<b> Entretien : </b> " + jsonProperties.optString("Entretien", "Inconnu") + "<br>"
                                        + "<b> Pseudonyme : </b> " + jsonProperties.optString("Pseudonyme", "Inconnu") + "<br>"
                                        + "<b> Vérification : </b> " + jsonProperties.optString("Vérification", "Inconnu") + "<br>"
                                        + "<b> Identifiant de la réponse : </b> " + jsonProperties.optString("Identifiant de la réponse", "Inconnu") + "<br>"
                                );


                                // TODO : Afficher la photo de l'espace boisé

                                m.setIcon(getResources().getDrawable(R.drawable.espace));
                                m.setAnchor(Marker.ANCHOR_TOP, Marker.ANCHOR_CENTER);
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
