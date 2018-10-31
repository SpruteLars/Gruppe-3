package no.hiof.emilie.efinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.hiof.emilie.efinder.model.EventInformation;

public class MakeEventActivity extends AppCompatActivity {
    EditText textViewEventName,
            textViewDate,
            textViewClock,
            textViewPayment,
            textViewAttendants,
            textViewAdresse,
            textViewDescription;
    TextView textAddedPhoto;
    Button addPhotoButton,
            buttonSubmit;
    private List<EditText> editTextArray;

    final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    Bitmap picture;
    String mCurrentPhotoPath, fileName;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference eventdataReference;
    private StorageReference storageReference;

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
        addPhotoButton = (Button) findViewById(R.id.btnAddPhoto);
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
        storageReference = FirebaseStorage.getInstance().getReference();

        /** Laste opp data til firebase */
        buttonSubmit.setOnClickListener(submitEventListener);
        /** Bottom Navigation */
        // region botnav
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profil:
                        startActivity(new Intent(MakeEventActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.action_feed:
                        startActivity(new Intent(MakeEventActivity.this, MainActivity.class));
                        return true;
                    }
                    return false;
                }
                }
        );
    }
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

            /* TODO: Er rekkefølgen på ting her feil? */
            try {
                picFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
            }

            //https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
            if (picFile != null) {
                Uri picURI = FileProvider.getUriForFile(this, "no.hiof.emilie.efinder", picFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    // endregion

    // region bildethumbnail
    @Override
    protected void onActivityResult (int requestCode, int resultCOde, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCOde == RESULT_OK) {
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

            /** Få tak i filnavn til bildet */
            /*Uri returnUri = data.getData();
            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null,null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            textAddedPhoto = (TextView) findViewById(R.id.txtAddPhoto);
            textAddedPhoto.setText(returnCursor.getString(nameIndex));*/

            //private void sendEventDataToFirebase(){ /* TODO: Håndteres denne riktig? Sende bildet til Storage eller Database? */
                //eventdataReference.push().setValue(eventList);
            //}
        }
    }
    // endregion

    // region lagre bilde på enhet
    private File createImageFile() throws IOException {
        //Make a unique name for the image file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "event_" + timestamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(/*prefix*/ imageFileName, /*suffix*/ ".jpg", /*directory*/ storageDir);

        fileName = imageFileName + " .jpg";
        //Save a file: the path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }
    // endregion

    // region legg bildet til i galleri
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentURI = Uri.fromFile(file);

        mediaScanIntent.setData(contentURI);
        this.sendBroadcast(mediaScanIntent);
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

            Uri file = Uri.fromFile(new File(mCurrentPhotoPath));
            StorageReference imageRef = storageReference.child("events/images/" + file.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(file);

            //Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(MakeEventActivity.this, "File not uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...


                    //Lag objekt av Event-klassekonstruktør
                    EventInformation eventInformation = new EventInformation(
                            null,
                            textViewEventName.getText().toString(),
                            textViewDate.getText().toString(),
                            Integer.parseInt(textViewPayment.getText().toString()),
                            Integer.parseInt(textViewAttendants.getText().toString()),
                            textViewAdresse.getText().toString(),
                            textViewDescription.getText().toString(),
                            taskSnapshot.getStorage().toString());

                    //Send objektet til firebase
                    eventdataReference.push().setValue(eventInformation); /* TODO: Ønsker også å sende med bildet i Event-objektet, hvordan???? OG blir dette gjort riktig nå? */
                    String uid = eventdataReference.getKey();

                    // new intent til Event, send med uid
                    Toast.makeText(MakeEventActivity.this, "File uploaded", Toast.LENGTH_SHORT).show();

                    //Sendes videre til aktiviteten som blir lagd
                    startActivity(new Intent(MakeEventActivity.this, EventActivity.class));
                    /* TODO: Send med uid som extra og i EventDetaljerActivity -> hent ut fra fireBase med uid */
                }
            });

            /*StorageReference imgBitmapRef = storageReference.child(image);
            StorageReference imgBitmapImagesRef = storageReference.child(events/images/imageBitmap);

            imgBitmapRef.getName().equals(imgBitmapImagesRef.getName());    //true
            imgBitmapRef.getPath().equals(imgBitmapImagesRef.getPath());    //false
            */
            // Få tilbake URL
        }
    };
    //endregion
}
