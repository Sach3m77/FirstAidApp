package com.example.firstaidapp.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.firstaidapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import java.util.HashMap;
import java.util.Map;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final LayoutInflater inflater;
    private GeoJsonLayer layer;
    private Map<String, GeoJsonFeature> featureMap = new HashMap<>();
    public CustomInfoWindowAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void mapGeoJsonLayer(GeoJsonLayer layer) {
        this.layer = layer;

        for (GeoJsonFeature feature : layer.getFeatures()) {
            String osmId = feature.getProperty("osm_id");
            featureMap.put(osmId, feature);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View view = inflater.inflate(R.layout.custom_info_window, null);

        TextView defibrillatorLocationTextView = view.findViewById(R.id.defibrillator_location);
        TextView indoorTextView = view.findViewById(R.id.indoor);
        TextView openingHoursTextView = view.findViewById(R.id.opening_hours);
        TextView noteTextView = view.findViewById(R.id.note);
        TextView phoneTextView = view.findViewById(R.id.phone);


        GeoJsonFeature feature = featureMap.get(marker.getTitle());
        if (feature != null) {

            if (!setText(defibrillatorLocationTextView, feature, "defibrillator:location:pl")) {
                setText(defibrillatorLocationTextView, feature, "defibrillator:location");
            }
            setText(indoorTextView, feature, "indoor");
            setText(openingHoursTextView, feature, "opening_hours");
            setText(noteTextView, feature, "note");
            setText(phoneTextView, feature, "phone");

        }

        return view;
    }

    private boolean setText(TextView textView, GeoJsonFeature feature, String propertyName) {

        String propertyValue = feature.getProperty(propertyName);

        if (propertyValue != null) {

            if (propertyValue.equals("yes")) {
                textView.setText("tak");
            } else if (propertyValue.equals("no")) {
                textView.setText("nie");
            } else {
                textView.setText(propertyValue);
            }

            return true;

        } else {

            if (propertyName.equals("defibrillator:location:pl")) { return false; }

            textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
            textView.setText("brak informacji");

            return true;
        }

    }

}
