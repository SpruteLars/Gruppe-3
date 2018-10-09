package no.hiof.emilie.efinder.Fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import no.hiof.emilie.efinder.R;

/*
    NB: Veldig enkelt utgangspunkt!
 */

public class FeedFragment extends Fragment {

    public FeedFragment() {}

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_feed, container, false);
        return fragmentView;
    }
}
