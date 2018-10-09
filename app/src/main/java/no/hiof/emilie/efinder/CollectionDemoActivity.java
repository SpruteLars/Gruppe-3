package no.hiof.emilie.efinder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CollectionDemoActivity extends AppCompatActivity {
    /*DemoCollectionPagerAdapter mDemoCollectionPagerActivity;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_demo);

        //ViewPager bruker SupportLibraryFragemnts, bruk disse:
        mDemoCollectionPagerActivity = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerActivity);

    }
}

//Siden dette er en object collection, brukes FragmentStatePagerAdapter, og IKKE FragmentPagerAdapter
public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
    public DemoCollectionPagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DemoObjectFragment();
        Bundle args = new Bundle();

        //Objektet vårt er en int
        args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT: " + (position + 1);
    }
}

//instanser av denne klassen er fragments som representerer ett objekt i collection
public static class DemoObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //De to siste argumentene forsikrer oss om at LayoutParams er inflated ordentlig

        View rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(Integer.toString(args.getInt(ARG_OBJECT)));

        return rootView;
    }*/
}