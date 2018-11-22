package no.hiof.emilie.efinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView clockTextView;
    private TextView paymentTextView;
    private TextView attendantsTextView;
    private TextView adresseTextView;
    private ImageView posterImageView;
    private String imageName;
    private Button melde;
    private String evenUid;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String Uid = user.getUid();
    String eventUid;
    //Google Maps
    public ImageButton myButton;

    //Firebase
    private FirebaseStorage firebaseStorage;
    private StorageReference imagePath;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Log.d("eventIkke",""+getIntent().getStringExtra(EVENT_UID));

        if(getIntent().getStringExtra("event_uid") != null){
            //Log.d("eventIkke",""+eventUid);
            eventUid = getIntent().getStringExtra(EVENT_UID);

            evenUid = eventUid;
        } else {
            eventUid = getIntent().getStringExtra("EventUid");
            Log.d("eventSøk",""+eventUid);
            evenUid = eventUid;
        }

        Log.d("event",""+eventUid);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventReference = firebaseDatabase.getReference("events").child(eventUid);
        firebaseAuth = FirebaseAuth.getInstance();

        //region XML items
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
        //endregion

        final DatabaseReference Usedbref = firebaseDatabase.getReference("events").child(eventUid);

        Usedbref.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("KeyP", ""+Uid);

                Log.d("KeyP", ""+dataSnapshot.child("paameldte").getKey());
                for(DataSnapshot ds : dataSnapshot.child("paameldte").getChildren()){
                    Log.d("KeyP", "hei");
                    if(ds.getKey().equals(Uid)){

                        melde.setText("Not Going");


                        break;
                    }else {
                        melde.setText("Going");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //region Google Maps
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,GoogleMapsActivity.class);
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
                    //getReference().child("events/images/" + imageName);

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //endregion

        melde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference dbref = firebaseDatabase.getReference("events").child(evenUid);
                final DatabaseReference Userdbref = firebaseDatabase.getReference("users").child(Uid);

                //dbref.child("paameldte").child(Uid).setValue("q");

                ValueEventListener valueEventListener = (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.child("paameldte").getChildren()){
                            if(ds.getKey().equals(Uid)){
                                dbref.child("paameldte").child(Uid).removeValue();
                                Userdbref.child("Event").child(evenUid).removeValue();
                                Toast.makeText(EventActivity.this,"NOT GOING anymore",Toast.LENGTH_LONG).show();
                                break;
                            }else {
                                dbref.child("paameldte").child(Uid).setValue("q");
                                Userdbref.child("Event").child(evenUid).setValue("q");
                                Toast.makeText(EventActivity.this,"Going",Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            dbref.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        //region size decoding
        //IKKE SLETT
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
                    case R.id.action_notification:
                        startActivity(new Intent(EventActivity.this, NotificationListActivity.class)); //Få denne til å ikke lage en ny intent????
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
