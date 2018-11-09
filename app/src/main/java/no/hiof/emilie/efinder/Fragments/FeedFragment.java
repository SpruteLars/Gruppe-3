package no.hiof.emilie.efinder.Fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import no.hiof.emilie.efinder.model.EventInformation;
import no.hiof.emilie.efinder.R;
import no.hiof.emilie.efinder.EventActivity;
import no.hiof.emilie.efinder.adapter.EventRecyclerAdapter;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/*
    NB: Veldig enkelt utgangspunkt!
 */

public class FeedFragment extends Fragment {
    private static final int RC_SIGN_IN = 1;
    private List<EventInformation> eventList;
    private List<String> eventListKeys;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference eventdataReference;
    private ChildEventListener childEventListener;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private EventRecyclerAdapter eventAdapter;

    public FeedFragment() {}

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_feed, container, false);
        eventList = new ArrayList<>();
        eventListKeys = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        eventdataReference = firebaseDatabase.getReference("events");

        // Run this _once_ to set up some testdata in your firebase database
        // generateTestData();

        createAuthenticationListener();
        createDatabaseReadListener();
        setUpRecyclerView(fragmentView);

        return fragmentView;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (firebaseAuthStateListener != null)
            firebaseAuth.addAuthStateListener(firebaseAuthStateListener);

        if (childEventListener != null)
            eventdataReference.addChildEventListener(childEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (firebaseAuthStateListener != null)
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);

        if (childEventListener != null) {
            eventdataReference.removeEventListener(childEventListener);
        }

        eventList.clear();
        eventListKeys.clear();
        eventAdapter.notifyDataSetChanged();
    }

    private void createAuthenticationListener() {
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void createDatabaseReadListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventInformation event = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                event.setEventUID(eventKey);

                if (!eventList.contains(event)) {
                    eventList.add(event);
                    Collections.sort(eventList, EventInformation.Sortering);
                    eventListKeys.add(eventKey);
                    eventAdapter.notifyItemInserted(eventList.size()-1);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventInformation event = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                event.setEventUID(eventKey);

                int position = eventListKeys.indexOf(eventKey);

                eventList.set(position, event);
                eventAdapter.notifyItemChanged(position);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                EventInformation removedMovie = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                removedMovie.setEventUID(eventKey);

                int position = eventListKeys.indexOf(eventKey);

                eventList.remove(removedMovie);
                eventListKeys.remove(position);
                eventAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void setUpRecyclerView(View parentView) {
        recyclerView = parentView.findViewById(R.id.recyclerView);
        eventAdapter = new EventRecyclerAdapter(getActivity(), eventList);
        eventAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);

                EventInformation event = eventList.get(position);

                Intent intent = new Intent(getActivity(), EventActivity.class);
                intent.putExtra(EventActivity.EVENT_UID, event.getEventUID());

                startActivity(intent);
            }
        });
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(getActivity(), "Signed in as " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Sign in canceled", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }
}
