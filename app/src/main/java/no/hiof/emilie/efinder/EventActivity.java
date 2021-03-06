package no.hiof.emilie.efinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import no.hiof.emilie.efinder.model.EventInformation;

public class EventActivity extends AppCompatActivity {
    public static final String EVENT_UID = "event_uid";
    private no.hiof.emilie.efinder.model.EventInformation event;
    private TextView nameTextView, descriptionTextView, dateTextView, clockTextView, paymentTextView, attendantsTextView, adresseTextView;
    private ImageView posterImageView;
    private String imageName;
    private Button melde;
    private ImageButton Deleter;
    private String stringEventUid;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String Uid = user.getUid();
    String eventUid;
    public ImageButton myButton;

    //Firebase
    private FirebaseStorage firebaseStorage;
    private StorageReference imagePath;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        findViewById();

        if(getIntent().getStringExtra("event_uid") != null){
            eventUid = getIntent().getStringExtra(EVENT_UID);
            stringEventUid = eventUid;
        } else {
            eventUid = getIntent().getStringExtra("EventUid");
            stringEventUid = eventUid;
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventReference = firebaseDatabase.getReference("events").child(eventUid);
        final DatabaseReference Ownerbref = firebaseDatabase.getReference("events").child(stringEventUid).child("eventMaker");
        final DatabaseReference Usedbref = firebaseDatabase.getReference("events").child(eventUid);

        firebaseAuth = FirebaseAuth.getInstance();


        //region slett
        Ownerbref.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().equals(Uid)) {
                    Deleter.setVisibility(View.VISIBLE);
                } else { }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //endregion

        //region følg
        Usedbref.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.child("paameldte").getChildren()){
                    if(ds.getKey().equals(Uid)){
                        melde.setText("Not Going");
                        break;
                    }else{
                        melde.setText("Going");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        //endregion

        //region Google Maps
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,GoogleMapsActivity.class);
                intent.putExtra("eventID", eventUid);
                startActivity(intent);
            }
        });
        //endregion

        //region Display clicked event
        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(EventInformation.class);
                event.setEventUID(dataSnapshot.getKey());

                nameTextView.setText(event.getEventTitle());
                descriptionTextView.setText(event.getEventDescription());
                dateTextView.setText(event.getEventDate());
                clockTextView.setText(event.getEventTime());
                paymentTextView.setText(String.valueOf(event.getEventPayment()));
                attendantsTextView.setText(String.valueOf(event.getEventAttendants()));
                adresseTextView.setText(event.getEventAdress());

                //Hente path til bildet i Storage
                imageName = event.getEventImage();
                imagePath = FirebaseStorage.getInstance().getReferenceFromUrl(imageName);

                if (event.getEventImage() != null ) {
                    imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(EventActivity.this)
                                .load(uri.toString()) //path to image in Firebase Storage
                                .into(posterImageView);  //Upload picture to imgView in EventActivity
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        //endregion

        //region påmelding
        melde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference dbref = firebaseDatabase.getReference("events").child(stringEventUid);
                final DatabaseReference Userdbref = firebaseDatabase.getReference("users").child(Uid);

                ValueEventListener valueEventListener = (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.child("paameldte").getChildren()){
                            if(ds.getKey().equals(Uid)){
                                dbref.child("paameldte").child(Uid).removeValue();
                                Userdbref.child("Event").child(stringEventUid).removeValue();

                                break;
                            }else {
                                dbref.child("paameldte").child(Uid).setValue("q");
                                Userdbref.child("Event").child(stringEventUid).setValue("q");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            dbref.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        //endregion

        //region Delete
        Deleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);

                    builder.setMessage("Are you sure you wanna delete this").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent newIntent = new Intent(EventActivity.this, bufferActivity.class);
                            newIntent.putExtra("Delete", stringEventUid);

                            startActivity(newIntent);
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        //endregion

        //region botnav
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        startActivity(new Intent(EventActivity.this, MainActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_profil:
                        startActivity(new Intent(EventActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.action_discovery:
                        startActivity(new Intent(EventActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                }
                return false;
            }
        });
        //endregion
    }

    //region findViewById
    private void findViewById() {
        nameTextView = findViewById(R.id.txtEventName);
        descriptionTextView = findViewById(R.id.txtDescription);
        dateTextView = findViewById(R.id.txtDate);
        clockTextView = findViewById(R.id.txtClock);
        paymentTextView = findViewById(R.id.txtPayment);
        attendantsTextView = findViewById(R.id.txtAttendants);
        adresseTextView = findViewById(R.id.txtAddresse);
        posterImageView  = findViewById(R.id.imgView);
        myButton = findViewById(R.id.mapButton);
        melde = findViewById(R.id.btnMeld);
        Deleter = findViewById(R.id.btnDelete);
    }
    //endregion
}
