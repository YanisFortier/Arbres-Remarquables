package com.esaip.arbresremarquables;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.esaip.arbresremarquables.Dialogs.DialogAbout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MapsActivity extends AppCompatActivity implements PermissionsListener {
    //Mapbox
    private MapboxMap mapboxMap;
    private MapView mapView;

    //Affichage des marqueurs
    private Marker marqueurArbre;
    private Icon iconArbre;
    private Icon iconAlignement;
    private Icon iconEspaceBoise;

    //Location
    private Double latitude_arbre;
    private Double longitude_arbre;

    // Volley - GeoJSON
    private RequestQueue mQueue;

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
                DialogAbout dialog = new DialogAbout();
                dialog.show(getSupportFragmentManager(), "Dialog About");
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


                //Setup de l'info window pour afficher une image
                mapboxMap.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {

                        //Setup view
                        View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                        ImageView imageView = view.findViewById(R.id.imageView);
                        TextView descView = view.findViewById(R.id.descView);

                        //Setup Description
                        descView.setText(marker.getSnippet());
                        descView.setTextColor(Color.BLACK);

                        //Setup image
                        imageView.setImageResource(R.drawable.pine_tree);
                        String urlImage = "https://www.sauvegarde-anjou.org/arbres1/images/arbres/" + marker.getTitle();
                        Picasso.get().load(urlImage).placeholder(R.drawable.pine_tree).fit().into(imageView);

                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 1000);
                        return view;
                    }
                });

                mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.setMaxZoomPreference(17.4);
                        enableLocationComponent(style); // User Location

                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(47.475, -0.55))
                                .zoom(12)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 10000);

                        //Chargement des arbres
                        mQueue = Volley.newRequestQueue(MapsActivity.this);
                        chargementJSON();

                        //Fonction OnClick pour ajouter un marqueur
                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                latitude_arbre = point.getLatitude();
                                longitude_arbre = point.getLongitude();

                                if (marqueurArbre != null) {
                                    mapboxMap.removeMarker(marqueurArbre);
                                }
                                marqueurArbre = mapboxMap.addMarker(new MarkerOptions()
                                        .setSnippet("Latitude : " + latitude_arbre + "\n" +
                                                "Longitude : " + longitude_arbre)
                                        .setPosition(new LatLng(point)));

                                mapboxMap.updateMarker(marqueurArbre);
                                mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(point), 1000);

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
                                        .setTitle(jsonProperties.optString("Identifiant de la réponse", "0"))
                                        .setSnippet("Nom de l'arbre : " + jsonProperties.optString("Nom de l'arbre", "Inconnu") + "\n"
                                                + "Nom botanique : " + jsonProperties.optString("Nom botanique", "Inconnu") + "\n"
                                                + "Adresse : " + jsonProperties.optString("Adresse", "Inconnue") + "\n"
                                                + "Date : " + jsonProperties.optString("Date", "Inconnues") + "\n"
                                                + "Remarquable : " + jsonProperties.optString("Remarquable", "Inconnu") + "\n"
                                                + "Remarquabilité : " + jsonProperties.optString("Remarquabilité", "Inconnu") + "\n"
                                                + "Situé sur un espace : " + jsonProperties.optString("Situé sur un espace", "Inconnu") + "\n"
                                                + "Observations : " + jsonProperties.optString("Observations", "Pas d'observations") + "\n"
                                                + "Pseudonyme : " + jsonProperties.optString("Pseudonyme", "Inconnu") + "\n"
                                                + "Vérification : " + jsonProperties.optString("Vérification", "Inconnu")));
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
                                        .setTitle(jsonProperties.optString("Identifiant de la réponse", "0"))
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
                                                + "Vérification : " + jsonProperties.optString("Vérification", "Inconnu")));
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
                                        .setTitle(jsonProperties.optString("Identifiant de la réponse", "0")).setSnippet("Nombre d'arbres : " + jsonProperties.optString("Nombre d'arbres", "Inconnu") + "\n"
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
                                                + "Vérification : " + jsonProperties.optString("Vérification", "Inconnu")));
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

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            PermissionsManager permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    public void focusUser(View view) {
        if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                    mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(),
                    mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 14));
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }
}