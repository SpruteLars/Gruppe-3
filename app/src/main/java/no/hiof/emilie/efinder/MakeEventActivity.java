package no.hiof.emilie.efinder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import java.io.InputStream;
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
                textViewDescription;
    TextView    textAddedPhoto,
                textViewDate,
                textViewAdresse,
                textViewClock;
    Button  buttonPickDate,
            buttonTimePicker,
            buttonChoosePlace;
    ImageButton btnTakePicture,
                btnChooseGalleryPicture;
    String  mCurrentPhotoPath,
            fileName;
    int     year_x,
            month_x,
            day_x,
            hour_x,
            minute_x;
    Bitmap picture;
    private List<String> editTextArray;
    private DatePickerDialog startDate;
    private SimpleDateFormat simpleDateFormatter;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int DIALOG_INT_CALENDAR = 2;
    static final int DIALOG_INT_TIME = 3;
    static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 4;
    static final int IMAGE_GALLERY_REQUEST = 5;

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
        hour_x = calendar.get(Calendar.HOUR_OF_DAY);
        minute_x = calendar.get(Calendar.MINUTE);

        findViewById();
        onClickedDateListener();
        onClickedTimeListener();

        /* Listener til å legge til bildet - ikke helt funksjonabel ennå */
        btnTakePicture.setOnClickListener(addTakePictureListener);
        btnChooseGalleryPicture.setOnClickListener(addImageFromGalleryListener);

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

        buttonChoosePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace(v);
            }
        });
    }
    // endregion

    //region XML items
    private void findViewById() {
        btnTakePicture = findViewById(R.id.btnTakePic);
        btnChooseGalleryPicture = findViewById(R.id.btnGalleryPic);
        buttonPickDate = findViewById(R.id.btnDatePicker);
        buttonTimePicker = findViewById(R.id.btnTimePicker);
        buttonChoosePlace = findViewById(R.id.btnPlaces);
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
        if (id == DIALOG_INT_TIME)
            return new TimePickerDialog(this, timePickerListener, hour_x, minute_x, true);
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

    //region Google Places
    public void findPlace(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
    //endregion

    /* Håndtering av å hente bilde og ta bilde */
    // region bildehåndtering
    private View.OnClickListener addTakePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dispatchPictureIntent();
        }
    };
    // endregion

    //region Gallery testing
    private View.OnClickListener addImageFromGalleryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dispatchGalleryIntent();
        }
    };

    private void dispatchGalleryIntent() {
        Intent getPictureFromGalleryIntent = new Intent(Intent.ACTION_PICK);

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = file.getPath();
        Uri uriData = Uri.parse(pictureDirectoryPath);

        getPictureFromGalleryIntent.setDataAndType(uriData, "image/*");
        startActivityForResult(getPictureFromGalleryIntent, IMAGE_GALLERY_REQUEST);
    }
    //endregion

    // region forespørsel om bildetagning til OS
    private void dispatchPictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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

    // region bildethumbnail & Places Intent
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        //Bilde
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
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
        }

        //Galleri
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData(); //Adressen ti bildet på SD kortet
            InputStream inputStream; //deklarerer en stream for å lese bildedata fra SD kortet

            try {
                inputStream = getContentResolver().openInputStream(imageUri);

                Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream);

                btnChooseGalleryPicture.setImageBitmap(bitmapImage); //TODO: Få dette til å bli bildeteksten i stedet
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
            }
        }

        //Places
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                textViewAdresse.setText(place.getAddress());

                Toast.makeText(this, "Place: " + place.getName() + " has been added!", Toast.LENGTH_SHORT);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // Handles error
                Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Toast.makeText(this, "Operation canceled", Toast.LENGTH_SHORT);
            }
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
