package nl.reupload.guube.airports;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String selectedAirportName;
    private String schipholAirportName = "Schiphol Airport";
    private LatLng selectedAirportLatLng;
    private LatLng schipholAirportLatLng = new LatLng(52.3086013794, 4.76388978958);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle extras = getIntent().getExtras();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        selectedAirportName = extras.getString("AIRPORTNAME");
        selectedAirportLatLng = new LatLng(extras.getDouble("AIRPORTLATITUDE"), extras.getDouble("AIRPORTLONGITUDE"));
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
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(selectedAirportLatLng).title(selectedAirportName));
        mMap.addMarker(new MarkerOptions().position(schipholAirportLatLng).title(schipholAirportName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedAirportLatLng));
        PolylineOptions rectOptions = new PolylineOptions()
                .add(selectedAirportLatLng)
                .add(schipholAirportLatLng)
                .geodesic(true);
        Polyline polyline = mMap.addPolyline(rectOptions);
    }
}
