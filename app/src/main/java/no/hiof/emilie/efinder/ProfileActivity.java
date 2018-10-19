package no.hiof.emilie.efinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    TextView kaare;
    TextView per;
    ImageView kalle;
    TextView roy;



    String Uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        FirebaseAuth fbdb = FirebaseAuth.getInstance();
        kaare = findViewById(R.id.txtProfilNavn);
        kalle = findViewById(R.id.imgProfilBilde);
        roy = findViewById(R.id.txtAbout);
        dbref = FirebaseDatabase.getInstance().getReference("users");
        user = fbdb.getCurrentUser();
        Uid = user.getUid();
        per = findViewById(R.id.txtFollowers);
        dbref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Database", "Got data");
                kaare.setText(dataSnapshot.child(Uid).child("Navn").getValue(String.class));
                per.setText(dataSnapshot.child(Uid).child("folgere").getValue(String.class));
                roy.setText(dataSnapshot.child(Uid).child("personinfo").getValue(String.class));
                //kalle.setImageURI(Uri.parse(dataSnapshot.child(Uid).child("bilde").getValue(String.class)));

                Log.d("Database", "Set data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /** Bottom Nav */
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.tools);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profil:
                                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                                return true;
                            case R.id.action_instillinger:
                                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                                return true;
                        }
                        return false;
                    }
                }
        );

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MakeEventActivity.class));
            }
        });
    }
}
