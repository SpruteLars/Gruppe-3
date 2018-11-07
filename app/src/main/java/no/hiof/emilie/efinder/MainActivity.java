package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import no.hiof.emilie.efinder.Fragments.FeedFragment;
import no.hiof.emilie.efinder.Fragments.PaameldtFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName(); //Debug tag
    FragmentStatePagerAdapter eksempelPagerAdapter;
    public static final String CHANNEL_ID = "1A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View pageren som vi har definert i XML-layouten.
        ViewPager viewPager = findViewById(R.id.view_pager);

        //region Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24px)
            .setContentTitle("Du har fått et annet varsel")
            .setContentText("Dette er ditt andre varsel")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //endregion

        //region Tablayout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //endregion

        //region Viewpager
        eksempelPagerAdapter = new MainActivity.EksempelPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(eksempelPagerAdapter);

        /* Her kontrolleres hva som skal skje når brukeren interagerer med viewet
         * Skal noe i aktiviteten reagere på endringer i view-pageren, skal dette gjøres her
         * Kjør i debug og se i konsollen for eksempel
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.d(TAG, "Side scrollet!");
            }

            @Override
            public void onPageSelected(int i) {
                Log.d(TAG, "Side valgt!");
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.d(TAG, "State endret!");
            }
        });
        //endregion

        //region BotNav
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.tools);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        startActivity(new Intent(MainActivity.this, MainActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_profil:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.action_make_event:
                        startActivity(new Intent(MainActivity.this, MakeEventActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_discovery:
                        startActivity(new Intent(MainActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                }
                return false;
            }
        });
        //endregion
    }

    // region Fragments
    public class EksempelPagerAdapter extends FragmentStatePagerAdapter {
        //Lettvint metode - burde gjøres dynamisk
        private final int NUMBER_OF_FRAGMENTS = 2;

        //Eksempel på hvordan vi kan holde på titlene på hver side
        private final String [] tabTitles = {"Feed", "Påmeldt"};

        public EksempelPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem (int i) {
            switch (i) {
                case 0:
                    return new FeedFragment();
                case 1:
                    return new PaameldtFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_FRAGMENTS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle (int position) {
            //Bruker string-arrayen til å hente ut tittel ut i fra brukerens posisjon
            return tabTitles[position];
        }
    }
    // endregion
}