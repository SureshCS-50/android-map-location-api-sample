package com.example.suresh.mapsample;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText mEtSearch;
    private MarkerOptions mMarketOption;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mEtSearch = (EditText) findViewById(R.id.etSearch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.none:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        mMap = googleMap;

        if (mMap == null) {

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    try {
                        // Reverse Geo-coding.. get Location address from lat lng.
                        Geocoder geocoder = new Geocoder(MapsActivity.this);
                        LatLng latLng = marker.getPosition();
                        List<Address> list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (list.size() > 0) {
                            Address address = list.get(0);
                            goTOLocation(address, 22);
                        } else {
                            Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View infoView = getLayoutInflater().inflate(R.layout.layout_location_snippet, null);
                    ImageView imgLocIcon = (ImageView) infoView.findViewById(R.id.imgLocationIcon);
                    TextView txtLocality = (TextView) infoView.findViewById(R.id.txtLocality);
                    TextView txtLatitude = (TextView) infoView.findViewById(R.id.txtLatitude);
                    TextView txtLongitude = (TextView) infoView.findViewById(R.id.txtLongitude);

                    LatLng latLng = marker.getPosition();
                    txtLocality.setText(marker.getTitle());
                    txtLatitude.setText(String.valueOf(latLng.latitude));
                    txtLongitude.setText(String.valueOf(latLng.longitude));

                    return infoView;
                }
            });
        }

    }

    private void goTOLocation(Address address, int zoom) {
        String locality = address.getLocality();
        double lat = address.getLatitude();
        double lng = address.getLongitude();
        LatLng latLng = new LatLng(lat, lng);
        if (mMarketOption == null) {
            mMarketOption = new MarkerOptions();
        }
        if (mMarker != null) {
            mMarker.remove();
        }
        mMarketOption.position(latLng)
                .draggable(true)
                .title(locality)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet(locality);
        mMarker = mMap.addMarker(mMarketOption);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cameraUpdate);
    }

    // Geo-coding.. get location address from name.
    public void findOnMap(View view) {
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> list = geocoder.getFromLocationName(mEtSearch.getText().toString(), 1);
            if (list.size() > 0) {
                Address address = list.get(0);
                goTOLocation(address, 22);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

