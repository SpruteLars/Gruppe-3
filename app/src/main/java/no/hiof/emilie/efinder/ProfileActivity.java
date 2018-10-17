package no.hiof.emilie.efinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
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
    TextView kaare;
    DataSnapshot dbsnap;
    AdapterView mAdapter;



    String Uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseAuth fbdb = FirebaseAuth.getInstance();
        kaare = findViewById(R.id.txtProfilNavn);
        dbref = FirebaseDatabase.getInstance().getReference("users");
        user = fbdb.getCurrentUser();
        Uid = user.getUid();
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Database", "Got data");
                kaare.setText(dataSnapshot.child(Uid).child("Navn").getValue(String.class));
                Log.d("Database", "Set data");
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
    }
}
