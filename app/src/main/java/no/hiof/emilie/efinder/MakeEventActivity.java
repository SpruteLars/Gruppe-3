package no.hiof.emilie.efinder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.hiof.emilie.efinder.model.EventInformation;

public class MakeEventActivity extends AppCompatActivity {
    EditText    textViewEventName,
                textViewPayment,
                textViewAttendants,
                textViewAdresse,
                textViewDescription;
    TextView    textAddedPhoto,
                textViewDate,
                textViewClock;
    Button  buttonPickDate,
            buttonTimePicker,
            addPhotoButton;
    private List<String> editTextArray;
    private DatePickerDialog startDate;
    private SimpleDateFormat simpleDateFormatter;
    int year_x, month_x, day_x, hour_x, minute_x;

    final int REQUEST_IMAGE_CAPTURE = 1;
    static final int DIALOG_INT_CALENDAR = 1;
    static final int DIALOG_INT_TIME = 2;
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
        simpleDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        hour_x = calendar.get(Calendar.HOUR);
        minute_x = calendar.get(Calendar.MINUTE);

        findViewById();
        onClickedDateListener();
        onClickedTimeListener();

        /* Listener til å legge til bildet - ikke helt funksjonabel ennå */
        addPhotoButton.setOnClickListener(addPhotoListener);

        /* Firebase */
        firebaseDatabase = FirebaseDatabase.getInstance();
        eventdataReference = firebaseDatabase.getReference("events");
        storageReference = FirebaseStorage.getInstance().getReference();

        /** Bottom Navigation */
        // region botnav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        FloatingActionButton floatingActionButton = findViewById(R.id.tools);

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
                    case R.id.action_notification:
                        startActivity(new Intent(MakeEventActivity.this, NotificationListActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    case R.id.action_discovery:
                        startActivity(new Intent(MakeEventActivity.this, DiscoveryActivity.class)); //Få denne til å ikke lage en ny intent????
                        return true;
                    }
                    return false;
                }
                }
        );
        //endregion

        // region Sende inn et arrangement
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String textView : editTextArray) {
                    //textView.setError("Feilmelding");
                    if (textView.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Your event requires all of the information above to be filled out", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

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
                        Toast.makeText(getApplicationContext(), "Your event has been added!", Toast.LENGTH_LONG).show();

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
                        String uid = eventdataReference.push().getKey();
                        eventdataReference.child(uid).setValue(eventInformation);

                        // TODO: new intent til Event, send med uid
                        Toast.makeText(MakeEventActivity.this, "File uploaded", Toast.LENGTH_SHORT).show();

                        //Sendes videre til aktiviteten som blir lagd
                        Intent intent = new Intent(MakeEventActivity.this, EventActivity.class);
                        intent.putExtra(EventActivity.EVENT_UID, uid);
                        startActivity(intent);
                    }
                });
            }
        });
        //endregion
    }
    // endregion

    //region XML items
    private void findViewById() {
        addPhotoButton = findViewById(R.id.btnAddPhoto);
        buttonPickDate = findViewById(R.id.btnDatePicker);
        buttonTimePicker = findViewById(R.id.btnTimePicker);
        textViewEventName = findViewById(R.id.txtEventName);
        textViewDate = findViewById(R.id.txtDate);
        textViewClock = findViewById(R.id.txtClock);
        textViewPayment = findViewById(R.id.txtPayment);
        textViewAttendants = findViewById(R.id.txtAttendants);
        textViewAdresse = findViewById(R.id.txtAdress);
        textViewDescription = findViewById(R.id.txtDescription);

        editTextArray.add(textViewEventName.getText().toString());
        editTextArray.add(textViewDate.getText().toString());
        editTextArray.add(textViewClock.getText().toString());
        editTextArray.add(textViewPayment.getText().toString());
        editTextArray.add(textViewAttendants.getText().toString());
        editTextArray.add(textViewAdresse.getText().toString());
        editTextArray.add(textViewDescription.getText().toString());
    }
    //endregion XML-items

    //region Datepicker & Timepicker
    private void onClickedDateListener() {
        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_INT_CALENDAR);
            }
        });
    }

    public void onClickedTimeListener() {
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_INT_TIME);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_INT_CALENDAR)
            return new DatePickerDialog(this, datePickerListener, year_x, month_x, day_x);
        /*if (id == DIALOG_INT_TIME)
            return new TimePickerDialog(this, timePickerListener, hour_x, minute_x);*/
        else
            return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;

            String chosenDate = day_x + "-" + month_x + "-" + year_x;
            textViewDate.setText(chosenDate);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            hour_x = hour;
            minute_x = minute;

            String chosenTime = hour_x + ":" + minute_x;
            textViewClock.setText(chosenTime);
        }
    };

    //endregion

    /* Håndtering av å hente bilde og ta bilde */
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
        //Intent getPictureFromGalleryIntent = new Intent(Intent.ACTION_PICK);

        //Make sure there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Lager en fil hvor bildet skal
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
                //InputStream imageStream = getContentResolver().openInputStream(contentURI);

                picture = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);

                textAddedPhoto = findViewById(R.id.txtAddPhoto);
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
}
