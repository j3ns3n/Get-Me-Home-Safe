package com.jensen.getmehomesafe.maphandler;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jensen.getmehomesafe.MainActivity;
import com.jensen.getmehomesafe.MapActivity;

import java.util.ArrayList;

public class MarkerStruct {

    /* Private variable initialization */
    private GoogleMap _map;

    private LatLng _location;
    private String _crime_type;
    private Marker _marker;
    private String _date;

    /* Initialize MarkerStruct object */
    public MarkerStruct(LatLng location, String crime_type, String date, GoogleMap map) {
        this._location = location;
        this._crime_type = crime_type;
        this._date = date;
        this._map = map;

        _init();
    }

    /* Initialize marker */
    private void _init() {
        this._marker = this._map.addMarker(new MarkerOptions()
                .position(this._location)
                .title(this._crime_type)
                .snippet("Location: " + this.latitude() + ", " + this.longitude() + "\n" +
                         "Date: " + this.month()));

    }

    /* Remove the marker */
    public void remove() {
        this._marker.remove();
    }

    /* Return the latitude of the marker */
    public double latitude() {
        return this._location.latitude;
    }

    /* Return the longitude of the marker */
    public double longitude() {
        return this._location.longitude;
    }

    /* Get the type of crime specified */
    public String get_crime_type() {
        return this._crime_type;
    }

    /* Get the date of the crime (YYYY-MM) */
    public String month() {
        return this._date;
    }

    /* Pass the Marker.showInfoWindow() function through */
    public void showInfoWindow() {
        this._marker.showInfoWindow();
    }

    /* Pass the Marker.hideInfoWindow() function through */
    public void hideInfoWindow() {
        this._marker.hideInfoWindow();
    }

    public Marker get_marker() {
        return this._marker;
    }

    public void hide_marker() {
        this._marker.setVisible(false);
    }

    public void show_marker() {
        this._marker.setVisible(true);
    }

}
