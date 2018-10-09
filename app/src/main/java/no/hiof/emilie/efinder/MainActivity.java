package no.hiof.emilie.efinder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textProfil;
    private TextView textInstilliger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< Updated upstream
        textProfil = (TextView) findViewById(R.id.textProfil);
        textInstilliger = (TextView) findViewById(R.id.textInstillinger);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profil:
                                textProfil.setVisibility(View.VISIBLE);
                                textInstilliger.setVisibility(View.GONE);
                                break;
                            case R.id.action_instillinger:
                                textProfil.setVisibility(View.GONE);
                                textInstilliger.setVisibility(View.VISIBLE);
                                break;
                        }
                        return false;
                    }
                });

        /*final ActionBar actionBar = getSupportActionBar();
=======
        /*final ActionBar actionBar = getActionBar();
>>>>>>> Stashed changes

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
            }*/
      /*  };

        //Legge til 2 tabs, spesifiser tabbenes tekst og TabListener
        for (int i = 0; i < 2; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Tab" + (i+1)) TODO: Gi bedre navn siden
                            .setTabListener(tabListener));
<<<<<<< Updated upstream
        }*/
    }
=======
        }
    */}
>>>>>>> Stashed changes
}