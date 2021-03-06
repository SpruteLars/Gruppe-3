package no.hiof.emilie.efinder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, name){
            public View getView(int pos,View convert, ViewGroup par) {
                View view = super.getView(pos,convert,par);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        startActivity(new Intent(SearchActivity.this, MainActivity.class));
                        return true;
                    case R.id.action_profil:
                        startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.action_discovery:
                        startActivity(new Intent(SearchActivity.this, DiscoveryActivity.class));
                        return true;
                }
                return false;
            }
        });
        //endregion
    }
}
