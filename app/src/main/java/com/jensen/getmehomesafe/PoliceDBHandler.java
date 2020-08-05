package com.jensen.getmehomesafe;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.jensen.getmehomesafe.maphandler.MarkerStruct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.Handler;
public class PoliceDBHandler {

    private static String USER_AGENT = "GetMeHomeSafe/1.0";

    public static JSONArray crimes;

    public static GoogleMap map;

    public static ArrayList<LatLng> global_markers = new ArrayList<>();

    public static Context context;

    public static ArrayList<MarkerStruct> markers = new ArrayList<>();

    public static MapActivity previous;

    private static boolean heatmapEnabled = false;

    private static HeatmapTileProvider heatMapProvider;
    private static TileOverlay overlay;

    public static GoogleMap.OnCameraIdleListener cameraIdleListener = new GoogleMap.OnCameraIdleListener() {
        @Override
        public void onCameraIdle() {
            markers.forEach((marker) -> {
                marker.hide_marker();
            });
            streetLevelCrime(map.getProjection().getVisibleRegion().latLngBounds);
        }
    };

    public static void streetLevelCrime (LatLngBounds bounds) {
        LatLng nEastBounds = bounds.northeast;
        LatLng sWestBounds = bounds.southwest;
        LatLng sEastBounds = new LatLng(sWestBounds.latitude, nEastBounds.longitude);
        LatLng nWestBounds = new LatLng(nEastBounds.latitude, sWestBounds.longitude);

        String baseURL = "https://data.police.uk/api/crimes-street/all-crime?poly=";
        String locationData = nEastBounds.latitude + "," + nEastBounds.longitude + ":"
                + sWestBounds.latitude + "," + sWestBounds.longitude + ":"
                + sEastBounds.latitude + "," + sEastBounds.longitude + ":"
                + nWestBounds.latitude + "," + nWestBounds.longitude;
        final String fullURL = baseURL + locationData;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL urlObj = new URL(fullURL);
                    HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", USER_AGENT);
                    int responseCode = con.getResponseCode();
                    Log.i("INFO", Integer.toString(responseCode));
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    crimes = new JSONArray(response.toString());

                    for (int i = 0; i < crimes.length(); i++) {
                        JSONObject crime = crimes.getJSONObject(i);
                        JSONObject location = crime.getJSONObject("location");

                        LatLng locationLatLng = new LatLng(location.getDouble("latitude"), location.getDouble("longitude"));
                        addMarker(locationLatLng, crime.getString("category"), crime.getString("month"));
                    }

                } catch (Exception e) {
                    Log.e("ERR", e.getMessage());
                }

            }

        });
        thread.start();
    }

    private static void addMarker(LatLng location, String crimetype, String date) {
        Handler uiHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                MarkerStruct marker = new MarkerStruct(location, crimetype, date, map);
                if(heatmapEnabled) marker.hide_marker();
                if(!markers.contains(marker)) {
                    markers.add(marker);
                    global_markers.add(new LatLng(marker.latitude(), marker.longitude()));
                } else {
                    marker.hide_marker();
                }
            }
        };
        uiHandler.post(myRunnable);
    }

    public void toggleHeatmap() {
        if (overlay == null) {
            heatmapEnabled = true;
            heatmapHandler();
            markers.forEach((marker) -> {
                marker.hide_marker();
            });
        }
        else {
            heatmapEnabled = false;
            overlay.remove();
            overlay = null;
            Log.e("e", markers.size()+ " markers");
            markers.forEach((marker) -> {
                marker.show_marker();
            });
        }
    }

    public static void heatmapHandler() {
        heatMapProvider = new HeatmapTileProvider.Builder()
                .data(global_markers)
                .build();
        heatMapProvider.setRadius(50);
        if (overlay != null) overlay.remove();
        overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(heatMapProvider));
        overlay.clearTileCache();
    }
}
