package no.hiof.emilie.efinder;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final ActionBar actionBar = getSupportActionBar();

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