package no.hiof.emilie.efinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.hiof.emilie.efinder.model.EventInformation;

public class MakeEventActivity extends AppCompatActivity {
    EditText textViewEventName;
    EditText textViewDate;
    EditText textViewClock;
    EditText textViewPayment;
    EditText textViewAttendants;
    EditText textViewAdresse;
    EditText textViewDescription;
    TextView textAddedPhoto;
    Button addPhotoButton;
    Button buttonSubmit;
    private List<EditText> editTextArray;

    /*final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    String mCurrentPhotoPath, fileName;*/

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference eventdataReference;

    Bitmap picture;

    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);
        editTextArray = new ArrayList<>();
        //eventList = new ArrayList<>();
        //eventKeyList = new ArrayList<>();

        /** Få XML koblet til variabler */
        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        //addPhotoButton = (Button) findViewById(R.id.btnAddPhoto);
        textViewEventName = (EditText) findViewById(R.id.txtEventName);
        textViewDate = (EditText) findViewById(R.id.txtDate);
        textViewClock = (EditText) findViewById(R.id.txtClock);
        textViewPayment = (EditText) findViewById(R.id.txtPayment);
        textViewAttendants = (EditText) findViewById(R.id.txtAttendants);
        textViewAdresse = (EditText) findViewById(R.id.txtAdress);
        textViewDescription = (EditText) findViewById(R.id.txtDescription);

        editTextArray.add(textViewEventName);
        editTextArray.add(textViewDate);
        editTextArray.add(textViewClock);
        editTextArray.add(textViewPayment);
        editTextArray.add(textViewAttendants);
        editTextArray.add(textViewAdresse);
        editTextArray.add(textViewDescription);

        /* Listener til å legge til bildet - ikke helt funksjonabel ennå */
        addPhotoButton.setOnClickListener(addPhotoListener);

        /** Firebase */
        firebaseDatabase = FirebaseDatabase.getInstance();
        eventdataReference = firebaseDatabase.getReference("events");

        /** Laste opp data til firebase */
        buttonSubmit.setOnClickListener(submitEventListener);
    }
    // endregion

    /** Henting av data - skal implementeres et annet sted */
    // region database listener
    /*private void createDatabaseReadListener() {
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
    }*/
    // endregion

    /** Håndtering av å hente bilde og ta bilde - koden er ikke ferdigstilt */
    // region bildehåndtering
    private View.OnClickListener addPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dispatchTakePictureIntent();
        }
    };
    // endregion

    // region forespørsel om bildetagning til OS
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Make sure there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a File where the picture should go
            File picFile = null;

            try {
                picFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
            }

            //https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
            if (picFile != null) {
                Uri picURI = FileProvider.getUriForFile(this, "no.hiof.emilie.efinder", picFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picURI);
                //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    // endregion

    // region bildethumbnail
    /*@Override
    protected void onActivityResult (int requestCode, int resultCOde, @Nullable Intent data) {
        //ImageView imageView = findViewById(R.id.txtAddPhoto); /* TODO: Hvilken ID skal denne referere til???? */
        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCOde == RESULT_OK) {
            try {
                galleryAddPic();

                File f = new File(mCurrentPhotoPath);
                Uri contentURI = Uri.fromFile(f);

                picture = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);

                textAddedPhoto = (TextView) findViewById(R.id.txtAddPhoto);
                textAddedPhoto.setText(fileName);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            //imageView.setImageBitmap(imageBitmap);*/

            /** Få tak i filnavn til bildet */
            /*Uri returnUri = data.getData();
            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null,null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            textAddedPhoto = (TextView) findViewById(R.id.txtAddPhoto);
            textAddedPhoto.setText(returnCursor.getString(nameIndex));

            //private void sendEventDataToFirebase(){ /* TODO: Håndteres denne riktig? Sende bildet til Storage eller Database? */
                /*eventdataReference.push().setValue(eventList);
            }
        }
    }*/
    // endregion

    // region lagre bilde på enhet
    private File createImageFile() throws IOException {
        //Make a name for the image file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "event_" + timestamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(/*prefix*/ imageFileName, /*suffix*/ ".jpg", /*directory*/ storageDir);

        //fileName = imageFileName + " .jpg";
        //Save a file: the path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    // endregion

    // region legg bildet til i galleri
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        //File file = new File(mCurrentPhotoPath);
        //Uri contentURI = Uri.fromFile(file);

        //mediaScanIntent.setData(contentURI);
        this.sendBroadcast(mediaScanIntent);
    }
    // endregion

    //region size decoding
    private void setPic() {
        //Dimensions used to display image
        //int targetWidth = imageView.getWidth(); /* TODO: Skal dette håndteres der eventet skal vises? CardView og EventActivity */
        //int targetHeight = imageView.getHeight();

        //Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        bmOptions.inJustDecodeBounds = true;
        int imageWidth = bmOptions.outWidth;
        int imageHeight = bmOptions.outHeight;

        //Determine how much to scale down the image
        //int scaleFactor = Math.min(imageWidth/targetWidth, imageHeight/targetHeight);

        //Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        //bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //(?)

        //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        //imageView.setImageBitMap(bitmap);
    }
    // endregion

    /** Sende inn event */
    //region send inn arrangement
    private View.OnClickListener submitEventListener = new View.OnClickListener() {
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

            /* TODO: Hvordan sjekke om bildet eksisterer? */
            /*if (imageView.getDrawable() == 0) {
                return;
            }*/

            //Lag objekt av Event-klassekonstruktør
            EventInformation eventInformation = new EventInformation(
                    null,
                    textViewEventName.getText().toString(),
                    textViewDate.getText().toString(),
                    Integer.parseInt(textViewPayment.getText().toString()),
                    Integer.parseInt(textViewAttendants.getText().toString()),
                    textViewAdresse.getText().toString(),
                    textViewDescription.getText().toString());

            //Send objektet til firebase
            eventdataReference.push().setValue(eventInformation); /* TODO: Ønsker også å sende med bildet i Event-objektet, hvordan???? OG blir dette gjort riktig nå? */
            String uid = eventdataReference.getKey();

            // picture - bitmap
            // Laste opp til Firebase Storage
            // Få tilbake URL

            // Lage intent til EventDetaljerActivity
            // Send med uid som extra
            // I EventDetaljerActivity -> hent ut fra FireBase med uid
        }
    };
    //endregion
}
