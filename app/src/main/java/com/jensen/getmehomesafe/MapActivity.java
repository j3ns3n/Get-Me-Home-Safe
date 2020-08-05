package com.jensen.getmehomesafe;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.jensen.getmehomesafe.maphandler.MarkerInfoWindowLayout;

import java.util.ArrayList;
import java.util.TimerTask;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLngBounds UK = new LatLngBounds(
            new LatLng(49.383639452689664, -17.39866406249996), new LatLng(59.53530451232491, 8.968523437500039));

    PoliceDBHandler handler = new PoliceDBHandler();

    Marker lastOpenned = null;

    private Button heatmap_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        heatmap_button = this.findViewById(R.id.heatmap_button);

        handler.previous = this;

        heatmap_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                handler.toggleHeatmap();
            }

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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        MarkerInfoWindowLayout customInfoWindow = new MarkerInfoWindowLayout(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                // Check if there is an open info window
                if (lastOpenned != null) {
                    // Close the info window
                    lastOpenned.hideInfoWindow();

                    // Is the marker the same marker that was already open
                    if (lastOpenned.equals(marker)) {
                        // Nullify the lastOpenned object
                        lastOpenned = null;
                        // Return so that the info window isn't openned again
                        return true;
                    }
                }

                // Open the info window for the marker
                marker.showInfoWindow();
                // Re-assign the last openned such that we can close it later
                lastOpenned = marker;

                // Event was handled by our code do not launch default behaviour.
                return true;
            }
        });

        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        final LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.setLatLngBoundsForCameraTarget(UK);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
                        mMap.setMinZoomPreference(12);
                        mMap.setMyLocationEnabled(true);

                        handler.map = mMap;
                        handler.context = getApplicationContext();

                        handler.streetLevelCrime(mMap.getProjection().getVisibleRegion().latLngBounds);
                        mMap.setOnCameraIdleListener(handler.cameraIdleListener);
                    }
                }
            });

        } catch (SecurityException err) {
            Toast.makeText(getApplicationContext(), "LOCATION PERMISSION REQUIRED", Toast.LENGTH_SHORT).show();
            Log.e("MAP", err.getMessage());
        }
    }




}
