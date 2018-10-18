package no.hiof.emilie.efinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import no.hiof.emilie.efinder.model.Event;



public class EventActivity extends AppCompatActivity {
    public static final String EVENT_UID = "event_uid";

    private Event event;

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView clockTextView;
    private TextView paymentTextView;
    private TextView attendantsTextView;
    private TextView adresseTextView;
    private ImageView posterImageView;


    private DatabaseReference ratingReference;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        String eventUid = getIntent().getStringExtra(EVENT_UID);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventReference = firebaseDatabase.getReference("events").child(eventUid);

        firebaseAuth = FirebaseAuth.getInstance();

        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        dateTextView = findViewById(R.id.dateTextView);
        clockTextView = findViewById(R.id.clockTextView);
        paymentTextView = findViewById(R.id.paymentTextView);
        attendantsTextView = findViewById(R.id.attendantsTextView);
        adresseTextView = findViewById(R.id.adresseTextView);
        posterImageView  = findViewById(R.id.posterImageView);

        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(Event.class);
                event.setUid(dataSnapshot.getKey());

                nameTextView.setText(event.getName());
                descriptionTextView.setText(event.getDescription());
                dateTextView.setText(event.getDate());
                clockTextView.setText(event.getClock());
                paymentTextView.setText(event.getPayment());
                attendantsTextView.setText(event.getAttendants());
                adresseTextView.setText(event.getAdresse());

                if (event.getPosterUrl() != null) {
                    Glide.with(EventActivity.this)
                            .load(event.getPosterUrl())
                            .into(posterImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Bottom Nav
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profil:
                                startActivity(new Intent(EventActivity.this, ProfileActivity.class));
                                return true;
                            case R.id.action_instillinger:
                                startActivity(new Intent(EventActivity.this, SettingsActivity.class));
                                return true;
                        }
                        return false;
                    }
                }
        );
    }
}
