package no.hiof.emilie.efinder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    String editNavn;
    EditText editAlder;
    EditText editTlf;
    DatabaseReference dbref;
    FirebaseUser user;
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;
    TextView profNavn;
    TextView profFollower;
    ImageView profBilde;
    TextView profDescription;
    String Uid;
    public static final String CHANNEL_ID = "1A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        FirebaseAuth fbdb = FirebaseAuth.getInstance();
        profNavn = findViewById(R.id.txtProfilNavn);
        profBilde = findViewById(R.id.imgProfilBilde);
        profDescription = findViewById(R.id.txtAbout);
        dbref = FirebaseDatabase.getInstance().getReference("users");
        user = fbdb.getCurrentUser();
        Uid = user.getUid();
        profFollower = findViewById(R.id.txtFollowers);
        dbref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Database", "Got data");
                profNavn.setText(dataSnapshot.child(Uid).child("Navn").getValue(String.class));
                profFollower.setText(dataSnapshot.child(Uid).child("folgere").getValue(String.class));
                profDescription.setText(dataSnapshot.child(Uid).child("personinfo").getValue(String.class));
                //profBilde.setImageURI(Uri.parse(dataSnapshot.child(Uid).child("bilde").getValue(String.class)));

                Log.d("Database", "Set data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /** Bottom Nav */
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.tools);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_profil:
                        startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.action_make_event:
                        startActivity(new Intent(ProfileActivity.this, MakeEventActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_discovery:
                        startActivity(new Intent(ProfileActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                }
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });
    }

    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_baseline_notifications_24px)
        .setContentTitle("Du har fått et annet varsel")
        .setContentText("Dette er ditt andre varsel")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
