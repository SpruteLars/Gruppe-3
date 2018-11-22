package no.hiof.emilie.efinder;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;
import no.hiof.emilie.efinder.Fragments.FeedFragment;
import no.hiof.emilie.efinder.Fragments.PaameldtFragment;
import no.hiof.emilie.efinder.model.SearchModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName(); //Debug tag
    public static final String CHANNEL_ID = "1A";

    FragmentStatePagerAdapter eksempelPagerAdapter;
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public HashMap<String, String> personMap;
    DatabaseReference Eventdbref = firebaseDatabase.getReference("events");
    ArrayList<SearchModel> items = new ArrayList<>();;
    Intent kntent;
    public HashMap<String, String> SearchDataBase(String sook){

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("events");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String followCase = ds.child("eventTitle").getValue(String.class);
                    String Marcus = "Marcus Olsen";
                    String Uid = ds.getKey();

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

            @Override
            public void onCancelled(DatabaseError databaseError) { }

        };
        dbref.addListenerForSingleValueEvent(valueEventListener);

        return personMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ValueEventListener ValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String kaffe = ds.child("eventTitle").getValue(String.class);
                    items.add(new SearchModel(kaffe));
                    Log.d("ItemLog",""+ds.child("eventTitle").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        Eventdbref.addListenerForSingleValueEvent(ValueListener);

        //region Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24px)
            .setContentTitle("Du har fått et annet varsel")
            .setContentText("Dette er ditt andre varsel")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //endregion

        //region Viewpager
        // View pageren som vi har definert i XML-layouten.
        ViewPager viewPager = findViewById(R.id.view_pager);
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

        //region Tablayout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //endregion

        //region BotNav
        FabSpeedDial fabSpeedDial = (FabSpeedDial)findViewById(R.id.fabSpeedDial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                //Vis meny
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                String strMakeEvent = "Make an Event";
                String strSearchEvent = "Search for Event";

                //Sendes til MakeEventActivity
                if (menuItem.getTitle().toString().equals(strMakeEvent))
                    startActivity(new Intent(MainActivity.this, MakeEventActivity.class));

                //Sendes til SearchDialog
                if (menuItem.getTitle().toString().equals(strSearchEvent)) {
                    new SimpleSearchDialogCompat(MainActivity.this,
                        "Search...",
                        "What events are you looking for...?",
                        null,
                        initData(),
                        new SearchResultListener<Searchable>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, final Searchable searchable, int i) {
                                kntent = new Intent(MainActivity.this, EventActivity.class);
                                Toast.makeText(MainActivity.this, "" + searchable.getTitle(), Toast.LENGTH_LONG).show();

                                final String searchedTitle = searchable.getTitle();

                                ValueEventListener valueListener = (new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                            if (ds.child("eventTitle").getValue().equals(searchedTitle)) {
                                                Log.d("eventsPlease", "Nøkkel " + ds.getKey() + " Title " + searchedTitle);
                                                kntent.putExtra("EventUid", "" + ds.getKey());
                                            }
                                        }
                                        startActivity(kntent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });
                                baseSearchDialogCompat.dismiss();
                                Eventdbref.addValueEventListener(valueListener);

                            }
                        }).show();
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
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
                    case R.id.action_discovery:
                        startActivity(new Intent(MainActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                }
                return false;
            }
        });
        //endregion
    }

    //region Search Events Dialog Items
    private ArrayList<SearchModel> initData() {
        return items;
    }
    //endregion

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