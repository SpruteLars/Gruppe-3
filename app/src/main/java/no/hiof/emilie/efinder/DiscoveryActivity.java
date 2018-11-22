package no.hiof.emilie.efinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class DiscoveryActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference dbref;
    String sookNavn;
    Button btnSook;
    EditText editSook;
    HashMap<String, String> personMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        personMap = new HashMap<String, String>();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        btnSook = findViewById(R.id.btnSoook);
        editSook = findViewById(R.id.editSook);

        btnSook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sookNavn = editSook.getText().toString();
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference followingRef = rootRef.child("users");

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String followCase = ds.child("Navn").getValue(String.class);
                            String Uid = ds.getKey();
                            Boolean insensitiveCaseStr = Pattern.compile(Pattern.quote(sookNavn), Pattern.CASE_INSENSITIVE).matcher(followCase).find(); //Gjør at søking ikke er case sensitive

                            if ( insensitiveCaseStr ) {
                                if (personMap.containsKey(followCase) && personMap.containsValue(Uid)) {

                                } else {
                                    personMap.put(Uid, followCase);
                                }

                                for (String key : personMap.keySet()) {
                                    Log.d("mapper", key);
                                }
                                for (String loop : personMap.values()) {
                                    Log.d("mapper", loop);
                                }
                            }

                        }
                        Intent intent = new Intent(DiscoveryActivity.this, SearchActivity.class);
                        intent.putExtra("map", personMap);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                followingRef.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        startActivity(new Intent(DiscoveryActivity.this, MainActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_profil:
                        startActivity(new Intent(DiscoveryActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.action_discovery:
                        startActivity(new Intent(DiscoveryActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                }
                return false;
            }
        });

    }
}
