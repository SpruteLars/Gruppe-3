package no.hiof.emilie.efinder;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import no.hiof.emilie.efinder.Classes.EventInformation;
import no.hiof.emilie.efinder.adapter.EventRecyclerAdapter;

public class MakeEventActivity extends AppCompatActivity {
    Button addPhotoButton;
    EditText textViewEventName;
    EditText textViewDate;
    EditText textViewClock;
    EditText textViewPayment;
    EditText textViewAttendants;
    EditText textViewAdresse;
    EditText textViewDescription;
    TextView textAddedPhoto;
    Button buttonSubmit;
    //private DatabaseReference databaseReference;
    private List<EditText> editTextArray;
    private List<EventInformation> eventList;
    private List<String> eventKeyList;
    final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference eventdataReference;
    private EventRecyclerAdapter eventRecyclerAdapter;
    private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);
        editTextArray = new ArrayList<>();
        eventList = new ArrayList<>();
        eventKeyList = new ArrayList<>();

        /** Få XML koblet til variabler */
        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        addPhotoButton = (Button) findViewById(R.id.btnAddPhoto);
        textViewEventName = (EditText) findViewById(R.id.txtEventName);
        textViewDate = (EditText) findViewById(R.id.txtDate);
        textViewClock = (EditText) findViewById(R.id.txtClock);
        textViewPayment = (EditText) findViewById(R.id.txtPayment);
        textViewAttendants = (EditText) findViewById(R.id.txtAttendants);
        textViewAdresse = (EditText) findViewById(R.id.txtAdress);
        textViewDescription = (EditText) findViewById(R.id.txtDescription);
        //Skal også ha med bildet

        editTextArray.add(textViewEventName);
        editTextArray.add(textViewDate);
        editTextArray.add(textViewClock);
        editTextArray.add(textViewPayment);
        editTextArray.add(textViewAttendants);
        editTextArray.add(textViewAdresse);
        editTextArray.add(textViewDescription);
        //Skal også legge til bildet

        addPhotoButton.setOnClickListener(addPhotoListener);

        /** Firebase */
        firebaseDatabase = FirebaseDatabase.getInstance();
        eventdataReference = firebaseDatabase.getReference("events");

        createDatabaseReadListener();

        /** Laste opp data til firebase */
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EditText textView : editTextArray) {
                    //textView.setError("Feilmelding");
                    if (textView.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Your event requires all of the information above to be filled out", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Your event has been added!", Toast.LENGTH_LONG).show();
                    }
                }

                /*if (imageView.getDrawable() == 0) {
                    return;
                }*/

                //Lag objekt av Event-klassekonstruktør
                EventInformation eventInformation = new EventInformation(null, textViewEventName, textViewDate, textViewPayment, textViewAttendants, textViewAdresse, textViewDescription);

                //Send data til firebase
                eventdataReference.push().setValue(eventInformation);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (childEventListener != null)
            eventdataReference.addChildEventListener(childEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (childEventListener != null) {
            eventdataReference.removeEventListener(childEventListener);
        }

        eventKeyList.clear();
        eventList.clear();
        eventRecyclerAdapter.notifyDataSetChanged();
    }

    private void createDatabaseReadListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventInformation eventInformation = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                eventInformation.setEventUID(eventKey);

                if (!eventList.contains(eventInformation)) {
                    eventList.add(eventInformation);
                    eventKeyList.add(eventKey);
                    eventRecyclerAdapter.notifyItemInserted(eventList.size()-1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventInformation eventInformation = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                eventInformation.setEventUID(eventKey);

                int position = eventKeyList.indexOf(eventKey);

                eventList.set(position, eventInformation);
                eventRecyclerAdapter.notifyItemChanged(position);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                EventInformation removedEventInformation = dataSnapshot.getValue(EventInformation.class);
                String eventKey = dataSnapshot.getKey();
                removedEventInformation.setEventUID(eventKey);

                int position = eventKeyList.indexOf(eventKey);

                eventList.remove(removedEventInformation);
                eventKeyList.remove(position);
                eventRecyclerAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //empty
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //empty
            }
        };
    }

    /** Håndtering av å hente bilde og ta bilde */
    private View.OnClickListener addPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hentBilde();
        }
    };

    //android developer - take photos
    private void hentBilde() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    /*@Override
    protected void onActivityResult (int requestCode, int resultCOde, @Nullable Intent data) {
        ImageView imageView = findViewById(R.id.txtAddPhoto); //Hvilken ID skal denne referere til????
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCOde == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);

            /** Få tak i filnavn til bildet */
            /*Uri returnUri = data.getData();
            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null,null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            textAddedPhoto = (TextView) findViewById(R.id.txtAddPhoto);
            textAddedPhoto.setText(returnCursor.getString(nameIndex));
        }
    }*/

    private void sendEventDataToFirebase() {
        eventdataReference.push().setValue(eventList);
    }
}
