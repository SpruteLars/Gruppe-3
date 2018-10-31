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

    private no.hiof.emilie.efinder.model.EventInformation event;

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView clockTextView;
    private TextView paymentTextView;
    private TextView attendantsTextView;
    private TextView adresseTextView;
    private ImageView posterImageView;


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

        //region size decoding
        /*private void setPic() {
            //Dimensions used to display image
            //int targetWidth = imageView.getWidth();
            //int targetHeight = imageView.getHeight();

            //Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            bmOptions.inJustDecodeBounds = true;
            int imageWidth = bmOptions.outWidth;
            int imageHeight = bmOptions.outHeight;

            //Determine how much to scale down the image
            //int scaleFactor = Math.min(imageWidth/targetWidth, imageHeight/targetHeight);

            //Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            //bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true; //(?)

            //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            //imageView.setImageBitMap(bitmap);
        }*/
        // endregion

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
                    case R.id.action_make_event:
                        startActivity(new Intent(EventActivity.this, MakeEventActivity.class)); //Få denne til å ikke lage en ny intent????
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
}
