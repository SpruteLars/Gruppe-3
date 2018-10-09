package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textProfil;
    private TextView textInstilliger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profil:
                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                return true;
                            case R.id.action_instillinger:
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                return true;
                        }
                        return false;
                    }
                });

        /*final ActionBar actionBar = getSupportActionBar();

        //ActionBar vises
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Kalles på når brukeren bytter mellom tabs
        /*ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected (ActionBar.Tab tab, FragmentTransaction ft) {
                //Når en tab er valgt, bytt til tilhørende siden i ViewPager
                mViewpager.setCurrentItem(tab.getPosition());
            }
            public void onTabUnselected (ActionBar.Tab tab, FragmentTransaction ft) {
                //skjul gitt tab
            }
            public void onTabReselected (ActionBar.Tab tab, FragmentTransaction ft) {
                //Mest sannsynlig ignorere dette eventet
            }
        };

        //Legge til 2 tabs, spesifiser tabbenes tekst og TabListener
        for (int i = 0; i < 2; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Tab" + (i+1)) //TODO: Gi bedre navn siden
                            .setTabListener(tabListener));
        }*/
    }
}