package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import no.hiof.emilie.efinder.Fragments.FeedFragment;
import no.hiof.emilie.efinder.Fragments.PaameldtFragment;

public class MainActivity extends AppCompatActivity {
    //Debug tag
    private static final String TAG = MainActivity.class.getSimpleName();
    FragmentStatePagerAdapter eksempelPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View pageren som vi har definert i XML-layouten.
        ViewPager viewPager = findViewById(R.id.view_pager);

        eksempelPagerAdapter = new MainActivity.EksempelPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(eksempelPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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


        /** Bottom Navigation */
        // region botnav
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                                                                 }
        );

        /* Vurdere om vi skal endre Bot Nav til Bot App Bar
         * BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_navigation);
         */

        //Størrelse på ikoner i Bot Nav
        for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
            final View iconView = bottomNavigationMenuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            //Høyde & bredde
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
        }
        // endregion
    }

    //Adapteren
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
}