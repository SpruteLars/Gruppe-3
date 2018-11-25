package no.hiof.emilie.efinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.hiof.emilie.efinder.model.EventInformation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private no.hiof.emilie.efinder.model.EventInformation event;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public GoogleMap mMap;
    private String eventUid;
    public String adresse;
    public LatLng dest;
    public LatLng mPosition;

    //Firebase
    private FirebaseDatabase firebaseDatabase;

    //metode for å konvertere adressestreng til LatLng
    public LatLng getLocationFromAddress(String adresse) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(adresse, 1);
            if (adresse == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventUid = getIntent().getStringExtra("eventID");

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventReference = firebaseDatabase.getReference("events").child(eventUid);

        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               event = dataSnapshot.getValue(EventInformation.class);
               event.setEventUID(dataSnapshot.getKey());

               //Henter eventAdress verdien fra firebase og setter variabel adresse til å ha lik verdi
                adresse = event.getEventAdress();

                //Gir variabel dest verdien som metoden gir tilbake med stringen adresse
                dest = getLocationFromAddress(adresse);

                //Setter merke på kartet på posisjonen lik verdien til dest
                mMap.addMarker(new MarkerOptions().position(dest).title("Destinasjon"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        enableMyLocationIfPermitted();
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(5);
        }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Ikke gitt tilatelse til å finne lokasjonene din, " + "viser ferdig satt lokasjon.", Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(58.940090, 11.634092);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }


    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
        new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
        new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {

                mMap.setMinZoomPreference(4);

                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(new LatLng(location.getLatitude(),
                    location.getLongitude()));

                circleOptions.radius(200);
                circleOptions.fillColor(Color.RED);
                circleOptions.strokeWidth(6);

                mMap.addCircle(circleOptions);
            }
        };
}
