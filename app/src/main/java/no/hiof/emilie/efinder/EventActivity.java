package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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

import no.hiof.emilie.efinder.model.EventInformation;



public class EventActivity extends AppCompatActivity {
    public static final String EVENT_UID = "event_uid";

    private EventInformation event;

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

        nameTextView = findViewById(R.id.txtEventName);
        descriptionTextView = findViewById(R.id.txtDescription);
        dateTextView = findViewById(R.id.txtDate);
        clockTextView = findViewById(R.id.txtClock);
        paymentTextView = findViewById(R.id.txtPayment);
        attendantsTextView = findViewById(R.id.txtAttendants);
        adresseTextView = findViewById(R.id.txtAddresse);
        posterImageView  = findViewById(R.id.imgView);

        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(EventInformation.class);
                event.setEventUID(dataSnapshot.getKey());

                nameTextView.setText(event.getEventTitle());
                descriptionTextView.setText(event.getEventDescription());
                dateTextView.setText(event.getEventDate());
                clockTextView.setText(event.getEventTime());
                paymentTextView.setText(event.getEventPayment()+"");
                attendantsTextView.setText(event.getEventAttendants()+"");
                adresseTextView.setText(event.getEventAdress());

                if (event.getEventImage() != null) {
                    Glide.with(EventActivity.this)
                            .load(event.getEventImage())
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
