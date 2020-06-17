package com.esaip.arbresremarquables;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MapsActivity extends AppCompatActivity {


    //Mapbox
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    //Location
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    private Double latitude_arbre;
    private Double longitude_arbre;

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;

    private Icon iconArbre;
    private Icon iconAlignement;
    private Icon iconEspaceBoise;

    // Volley - GeoJSON
    private RequestQueue mQueue;

    private String urlArbres = "https://www.sauvegarde-anjou.org/arbres1/data/arbres.geojson";
    private String urlAlignements = "https://www.sauvegarde-anjou.org/arbres1/data/alignements.geojson";
    private String urlEspacesBoises = "https://www.sauvegarde-anjou.org/arbres1/data/espaces_boises.geojson";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoieWZvcnRpZXIiLCJhIjoiY2tiaTJ1dWI0MGJhODJ6cXY0ZnVoM2xiaSJ9.EGV5qIisvjuASgl2QwlgaA");
        setContentView(R.layout.activity_maps);

        //Boutons
        FloatingActionButton btnInfo = findViewById(R.id.floatingBtnInfo);
        final FloatingTextButton btnArbre = findViewById(R.id.floatingTxtBtnArbre);

        btnArbre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, AjoutPhoto.class);
                intent.putExtra("latitude_arbre", latitude_arbre);
                intent.putExtra("longitude_arbre", longitude_arbre);
                startActivity(intent);
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MapsActivity.this, CircleLayerClusteringActivity.class);
                //startActivity(intent);
            }
        });

        // Creation des icons
        IconFactory iconFactory = IconFactory.getInstance(MapsActivity.this);
        iconArbre = iconFactory.fromResource(R.drawable.arbre);
        iconAlignement = iconFactory.fromResource(R.drawable.alignement);
        iconEspaceBoise = iconFactory.fromResource(R.drawable.espace);

        //Gestion de la carte
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                MapsActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // User Location
                        enableLocationComponent(style);

                        //Chargement des arbres
                        mQueue = Volley.newRequestQueue(MapsActivity.this);
                        chargementJSON();

                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                latitude_arbre = point.getLatitude();
                                longitude_arbre = point.getLongitude();

                                mapboxMap.addMarker(new MarkerOptions()
                                        .setSnippet("Latitude : " + latitude_arbre + "\n" +
                                                "Longitude : " + longitude_arbre)
                                        .setPosition(new LatLng(latitude_arbre,
                                                longitude_arbre)));

                                btnArbre.setVisibility(View.VISIBLE);
                                return true;
                            }
                        });
                    }
                });
            }
        });


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

                                mapboxMap.addMarker(new MarkerOptions()
                                        .setIcon(iconArbre)
                                        .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(
                                                Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                                Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())))
                                        .setTitle("Informations")
                                        .setSnippet("Nom de l'arbre : " + jsonProperties.optString("Nom de l'arbre", "Inconnu") + "\n"
                                                + "Nom botanique : " + jsonProperties.optString("Nom botanique", "Inconnu") + "\n"
                                                + "Adresse : " + jsonProperties.optString("Adresse", "Inconnue") + "\n"
                                                + "Date : " + jsonProperties.optString("Date", "Inconnues") + "\n"
                                                + "Remarquable : " + jsonProperties.optString("Remarquable", "Inconnu") + "\n"
                                                + "Remarquabilité : " + jsonProperties.optString("Remarquabilité", "Inconnu") + "\n"
                                                + "Situé sur un espace : " + jsonProperties.optString("Situé sur un espace", "Inconnu") + "\n"
                                                + "Observations : " + jsonProperties.optString("Observations", "Pas d'observations") + "\n"
                                                + "Pseudonyme : " + jsonProperties.optString("Pseudonyme", "Inconnu") + "\n"
                                                + "Vérification : " + jsonProperties.optString("Vérification", "Inconnu") + "\n"
                                                + "Identifiant de la réponse : " + jsonProperties.optString("Identifiant de la réponse", "Inconnu") + "\n"
                                                + "<center>Cliquez pour afficher la photo</center>")
                                        .setIcon(iconArbre));
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

                                mapboxMap.addMarker(new MarkerOptions()
                                        .setIcon(iconAlignement)
                                        .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(
                                                Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                                Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())))
                                        .setTitle("Informations")
                                        .setSnippet("Nom botanique : " + jsonProperties.optString("Nom botanique", "Inconnu") + "\n"
                                                + "Nombre d'arbres : " + jsonProperties.optString("Nombre d'arbres", "Inconnu") + "\n"
                                                + "Adresse : " + jsonProperties.optString("Adresse de l'alignement", "Inconnue") + "\n"
                                                + "Date : " + jsonProperties.optString("Date", "Inconnues") + "\n"
                                                + "Espèces : " + jsonProperties.optString("Espèces", "Inconnu") + "\n"
                                                + "Nombre d'espèces : " + jsonProperties.optString("Nombre d'espèces", "Inconnu") + "\n"
                                                + "Protection : " + jsonProperties.optString("Protection", "Inconnue") + "\n"
                                                + "Situé sur un espace : " + jsonProperties.optString("Situé sur un espace", "Inconnu") + "\n"
                                                + "Observations : " + jsonProperties.optString("Observations", "Pas d'observations") + "\n"
                                                + "Pseudonyme : " + jsonProperties.optString("Pseudonyme", "Inconnu") + "\n"
                                                + "Vérification : " + jsonProperties.optString("Vérification", "Inconnu") + "\n"
                                                + "Identifiant de la réponse : " + jsonProperties.optString("Identifiant de la réponse", "Inconnu") + "\n"));
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

                                mapboxMap.addMarker(new MarkerOptions()
                                        .setIcon(iconEspaceBoise)
                                        .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(
                                                Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString()),
                                                Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString())))
                                        .setTitle("Informations")
                                        .setSnippet("Nombre d'arbres : " + jsonProperties.optString("Nombre d'arbres", "Inconnu") + "\n"
                                                + "Biodiversité : " + jsonProperties.optString("Biodiversité", "Inconnue") + "\n"
                                                + "Globalement : " + jsonProperties.optString("Globalement", "Inconnu") + "\n"
                                                + "Point d'eau : " + jsonProperties.optString("Point d'eau", "Inconnu") + "\n"
                                                + "Abris d'animaux : " + jsonProperties.optString("Abris d'animaux", "Inconnu") + "\n"
                                                + "Éclairage nocturne : " + jsonProperties.optString("Éclairage nocturne", "Inconnu") + "\n"
                                                + "Adresse : " + jsonProperties.optString("Adresse de l'espace boisé", "Inconnue") + "\n"
                                                + "Date : " + jsonProperties.optString("Date", "Inconnues") + "\n"
                                                + "Situé sur un espace : " + jsonProperties.optString("Situé sur un espace", "Inconnu") + "\n"
                                                + "Observations : " + jsonProperties.optString("Observations", "Pas d'observations") + "\n"
                                                + "Entretien : " + jsonProperties.optString("Entretien", "Inconnu") + "\n"
                                                + "Pseudonyme : " + jsonProperties.optString("Pseudonyme", "Inconnu") + "\n"
                                                + "Vérification : " + jsonProperties.optString("Vérification", "Inconnu") + "\n"
                                                + "Identifiant de la réponse : " + jsonProperties.optString("Identifiant de la réponse", "Inconnu") + "\n"));
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


    private void focusAngers() {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(47.468700, -0.558820))
                .zoom(10)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 5000);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager((PermissionsListener) this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}