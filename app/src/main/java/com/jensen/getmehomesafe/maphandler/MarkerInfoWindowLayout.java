package com.jensen.getmehomesafe.maphandler;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jensen.getmehomesafe.R;

public class MarkerInfoWindowLayout implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public MarkerInfoWindowLayout(Context ctx) {
        this.context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_info_window_layout, null);

        TextView crime_type = view.findViewById(R.id.crime_type);
        ImageView picture = view.findViewById(R.id.pic);

        TextView location = view.findViewById(R.id.location);
        TextView date = view.findViewById(R.id.date);

        crime_type.setText(marker.getTitle());
        location.setText(marker.getSnippet().split("\n")[0]);
        date.setText(marker.getSnippet().split("\n")[1]);

//        InfoWindowHandler infoWindowData = (InfoWindowHandler) marker.getTag();
//
//        int imageId = context.getResources().getIdentifier(InfoWindowHandler.getImage().toLowerCase(),
//                "drawable", context.getPackageName());
//        img.setImageResource(imageId);

        return view;
    }

}
