package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
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
    BottomNavigationView bottomNavigationView;
    TextView profNavn, profFollower, profDescription, profAlder;
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
        profFollower = findViewById(R.id.txtFollowers);
        profAlder = findViewById(R.id.txtAge);

        if(intent.getExtras() == null){
            Uid = user.getUid();
            follow.setVisibility(View.GONE);
        }else{
            signOut.setVisibility(View.GONE);
            String hey = (String) getIntent().getSerializableExtra("Key");
            Uid = hey;
        }

        dbref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profNavn.setText(dataSnapshot.child(Uid).child("Navn").getValue(String.class));
                profDescription.setText(dataSnapshot.child(Uid).child("personinfo").getValue(String.class));
                profAlder.setText(dataSnapshot.child(Uid).child("alder").getValue(Integer.class) + " years old");

                int i = -1;
                for(DataSnapshot ks : dataSnapshot.child(Uid).child("FolgereList").getChildren()){
                    i += 1;
                }
                profFollower.setText(i + " followers");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        //region Følgere
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference following = root.child("users").child(Uid);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.child("FolgereList").getChildren()) {
                            String authUid = FirebaseAuth.getInstance().getUid();
                            //String dsKey = dataSnapshot.getKey();

                            if (ds.getKey().equals(authUid)) {
                                following.child("FolgereList").child(authUid).removeValue();

                                Toast.makeText(ProfileActivity.this, "Unfollowed", Toast.LENGTH_LONG).show();
                                break;
                            } else {
                                Log.d("datasnap","Dette her "+ authUid +" "+dataSnapshot);
                                following.child("FolgereList").child(authUid).setValue("q");
                                Toast.makeText(ProfileActivity.this, "Followed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                following.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        //endregion

        //region Logg ut
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });
        //endregion

        //region BotNav
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

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
                    case R.id.action_discovery:
                        startActivity(new Intent(ProfileActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                }
                return false;
            }
        });
        //endregion
    }
}
