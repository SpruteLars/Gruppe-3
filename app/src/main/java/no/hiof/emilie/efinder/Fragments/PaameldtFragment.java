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
import no.hiof.emilie.efinder.model.FirebaseAccessModel;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PaameldtFragment extends Fragment {
    private static final int RC_SIGN_IN = 1;
    private List<EventInformation> paameldtList;
    private List<String> paameldtListKeys;
    private RecyclerView recyclerView;
    private ChildEventListener childEventListener;
    private EventRecyclerAdapter eventAdapter;

    //Forslag til å importere alt av Firebase connections på denne måten
    FirebaseAccessModel firebaseAccessModel = new FirebaseAccessModel();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference eventdataReference;
    private DatabaseReference userdataReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String Uid = user.getUid();


    public PaameldtFragment() {}
    private static final int RC_SIGN_IN = 1;
    private List<EventInformation> eventList;
    private List<String> eventListKeys;
    private RecyclerView recyclerView;
    private ChildEventListener childEventListener;
    private EventRecyclerAdapter eventAdapter;

    //Forslag til å importere alt av Firebase connections på denne måten
    FirebaseAccessModel firebaseAccessModel = new FirebaseAccessModel();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference eventdataReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_feed, container, false);
        paameldtList = new ArrayList<>();
        paameldtListKeys = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        eventdataReference = firebaseDatabase.getReference("events");
        userdataReference = firebaseDatabase.getReference("users").child(Uid);


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

        paameldtList.clear();
        paameldtListKeys.clear();
        eventAdapter.notifyDataSetChanged();
    }

    //region Database reader
    private void createDatabaseReadListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventInformation event = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                event.setEventUID(eventKey);
                for(DataSnapshot ds:
                    dataSnapshot.child("paameldte").getChildren()){
                    if (ds.getKey().equals(Uid)){
                        if (!paameldtList.contains(event)) {
                            paameldtList.add(event);
                            Collections.sort(paameldtList, EventInformation.Sortering);
                            paameldtListKeys.add(eventKey);
                            eventAdapter.notifyItemInserted(paameldtList.size() - 1);
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventInformation event = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                event.setEventUID(eventKey);

                int position = paameldtListKeys.indexOf(eventKey);

                paameldtList.set(position, event);
                eventAdapter.notifyItemChanged(position);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                EventInformation removedEvent = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                removedEvent.setEventUID(eventKey);

                int position = paameldtListKeys.indexOf(eventKey);

                paameldtList.remove(removedEvent);
                paameldtListKeys.remove(position);
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
        eventAdapter = new EventRecyclerAdapter(getActivity(), paameldtList);
        eventAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);

                EventInformation event = paameldtList.get(position);

                Intent intent = new Intent(getActivity(), EventActivity.class);
                intent.putExtra(EventActivity.EVENT_UID, event.getEventUID());

                startActivity(intent);
            }
        });
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }
    //endregion

    //region Innlogging

    //endregion
}