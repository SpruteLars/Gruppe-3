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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    DatabaseReference dbref;
    FirebaseUser user;
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;
    TextView profNavn, profFollower, profDescription;
    ImageButton signOut, follow;
    ImageView profBilde;
    String Uid;
    int folgere;
    int followed = 0;
    public static final String CHANNEL_ID = "1A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbref = FirebaseDatabase.getInstance().getReference("users");
        FirebaseAuth fbdb = FirebaseAuth.getInstance();
        user = fbdb.getCurrentUser();
        Intent intent = getIntent();

        follow = findViewById(R.id.btnSub);
        signOut = findViewById(R.id.btnSignOut);
        profNavn = findViewById(R.id.txtProfilNavn);
        profBilde = findViewById(R.id.imgProfilBilde);
        profDescription = findViewById(R.id.txtAbout);

        if(intent.getExtras() == null){
            Uid = user.getUid();
            follow.setVisibility(View.GONE);
        }else{
            signOut.setVisibility(View.GONE);
             String hey = (String) getIntent().getSerializableExtra("Key");
             Log.d("Key", hey);
             Uid = hey;
        }

        profFollower = findViewById(R.id.txtFollowers);
        dbref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Database", "Got data");
                profNavn.setText(dataSnapshot.child(Uid).child("Navn").getValue(String.class));
               // profFollower.setText(""+dataSnapshot.child(Uid).child("folgere").getValue(Integer.class));
                //folgere = dataSnapshot.child(Uid).child("folgere").getValue(Integer.class);
                profDescription.setText(dataSnapshot.child(Uid).child("personinfo").getValue(String.class));
                //profBilde.setImageURI(Uri.parse(dataSnapshot.child(Uid).child("bilde").getValue(String.class)));
                int i = -1;
                for(DataSnapshot ks : dataSnapshot.child(Uid).child("FolgereList").getChildren()){
                    i += 1;
                }
                profFollower.setText(""+i);

                Log.d("Database", "Set data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                //String Navn = dbref.child(Marcus).child("Navn").getValue(String.class);
                final DatabaseReference following = root.child("users").child(Uid);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            //String followCase = dataSnapshot.child("Folgere").getValue(String.class);
                        for(DataSnapshot ds : dataSnapshot.child("FolgereList").getChildren()) {
                            String Marcus = FirebaseAuth.getInstance().getUid();
                            Log.d("datasnaper", "Marcus " + Marcus);
                           // String Navn = dbname.child("users").child(Marcus).child("Navn").getValue(String.class);
                            String Kid = dataSnapshot.getKey();
                            Log.d("datasnap", "Kid "+ Kid);
                            Log.d("datasnaper","Ds value "+ds.getValue());
                            if (ds.getKey().equals(Marcus)) {
                                Log.d("datasnape", "allerede følger");
                                following.child("FolgereList").child(Marcus).removeValue();
                                Toast.makeText(ProfileActivity.this, "Unfollowed", Toast.LENGTH_LONG).show();
                                break;
                            } else {
                                //folgere = ds.child(Kid).child("folgere").getValue(Integer.class);
                                //followingRef.child(Uid).setValue();
                                //folgere = folgere + 1;
                               // root.child("users").child(Kid).child("folgere").setValue(folgere);
                                //following.child(Marcus).setValue(Uid);
                                Log.d("datasnap","Dette her "+Marcus+" "+dataSnapshot);
                                following.child("FolgereList").child(Marcus).setValue("q");
                                Toast.makeText(ProfileActivity.this, "Followed", Toast.LENGTH_LONG).show();



                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                following.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });

        //region BotNav
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
                    case R.id.action_notification:
                        startActivity(new Intent(ProfileActivity.this, NotificationListActivity.class)); //Få denne til å ikke lage en ny intent????
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
        //endregion
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
