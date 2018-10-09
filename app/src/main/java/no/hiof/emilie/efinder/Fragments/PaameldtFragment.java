package no.hiof.emilie.efinder.Fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import no.hiof.emilie.efinder.R;

public class PaameldtFragment extends Fragment {

    public PaameldtFragment() {}

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_paameldt, container, false);
        return fragmentView;
    }
}
