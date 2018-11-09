package no.hiof.emilie.efinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
        setContentView(R.layout.activity_other_users);
        personMap = new HashMap<String, String>();

        btnSook = findViewById(R.id.btnSook);
        editSook = findViewById(R.id.txtSokNavn);
        btnSook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sookNavn = editSook.getText().toString();
               // Log.d("Bruk",sookNavn);
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference followingRef = rootRef.child("users");
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                String followCase = ds.child("Navn").getValue(String.class);
                                String Marcus = "Marcus Olsen";
                                String Uid = ds.getKey();

                                if(followCase.contains(sookNavn)){
                                   // Log.d("Bruk", followCase);
                                   // Log.d("Bruk", Uid);
                                    if(personMap.containsKey(followCase) && personMap.containsValue(Uid)){

                                    }else{
                                        personMap.put(Uid, followCase);
                                    }

                                    for(String key : personMap.keySet()){
                                        Log.d("mapper",key);
                                    }
                                    for(String loop : personMap.values()){
                                        Log.d("mapper", loop);
                                    }
                                }

                        }
                        Intent intent = new Intent(DiscoveryActivity.this, SearchActivity.class);
                        intent.putExtra("map", personMap);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                followingRef.addListenerForSingleValueEvent(valueEventListener);

            }
        });


    }


}
