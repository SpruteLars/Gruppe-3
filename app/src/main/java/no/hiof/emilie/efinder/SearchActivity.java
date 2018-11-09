package no.hiof.emilie.efinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    FirebaseUser User;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ListView listView = findViewById(R.id.listEdit);


        Intent intent = getIntent();
        final HashMap<String, String> map = (HashMap<String, String>)intent.getSerializableExtra("map");
        final List<String> list = new ArrayList<String>(map.keySet());
        final List<String> name = new ArrayList<String>(map.values());
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, name);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("Item", "int "+i);
                Log.d("Item", "Adapter "+adapterView);
                Log.d("Item", "View "+view);
                Log.d("Item", "long "+ l);
                Log.d("Item", list.get(i));
                Log.d("Item", name.get(i));
                Intent intent = new Intent(SearchActivity.this,ProfileActivity.class);
                intent.putExtra("Key",list.get(i));
                startActivity(intent);
            }
        });

        for(String key : map.keySet()){
            Log.d("mapper2", key);
        }
        for(String key : map.values()){
            Log.d("mapper2", key);
        }

        //region BotNav
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.tools);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MakeEventActivity.class));
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        startActivity(new Intent(SearchActivity.this, MainActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_profil:
                        startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.action_notification:
                        startActivity(new Intent(SearchActivity.this, NotificationListActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_discovery:
                        startActivity(new Intent(SearchActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                }
                return false;
            }
        });
        //endregion
    }
}
