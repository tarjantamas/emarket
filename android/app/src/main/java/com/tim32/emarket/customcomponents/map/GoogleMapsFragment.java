package com.tim32.emarket.customcomponents.map;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tim32.emarket.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;


@EFragment(R.layout.fragment_google_maps)
public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback {

    public static String LONGITUDE = "lng";

    public static String LATITUDE = "ltd";

    public static String MARKER_TITLE = "marker_title";

    private GoogleMap map;

    private Double longitude;

    private Double latitude;

    private String markerTitle;

    @AfterViews
    void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            longitude = bundle.getDouble(LONGITUDE);
            latitude = bundle.getDouble(LATITUDE);
            markerTitle = bundle.getString(MARKER_TITLE);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(latLng).title(markerTitle));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }
}
