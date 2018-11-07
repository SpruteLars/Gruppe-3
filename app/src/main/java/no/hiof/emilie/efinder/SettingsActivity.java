package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.tools);

        //Bottom Nav
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_profil:
                        startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.action_notification:
                        startActivity(new Intent(SettingsActivity.this, NotificationListActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_discovery:
                        startActivity(new Intent(SettingsActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                }
                return false;
            }
        });

        //Apply settings
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            //Toast.makeText(getApplicationContext(), "Settings applies", Toast.LENGTH_SHORT).show();
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            }
        });
    }
}
