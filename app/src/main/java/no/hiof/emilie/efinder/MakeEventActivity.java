package no.hiof.emilie.efinder;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    //private EditText[] editTextArray;
    //private DatabaseReference databaseReference;
    final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);

        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        addPhotoButton = (Button) findViewById(R.id.btnAddPhoto);
        /*textViewEventName = (EditText) findViewById(R.id.txtEventName);
        textViewDate = (EditText) findViewById(R.id.txtDate);
        textViewClock = (EditText) findViewById(R.id.txtClock);
        textViewPayment = (EditText) findViewById(R.id.txtPayment);
        textViewAttendants = (EditText) findViewById(R.id.txtAttendants);
        textViewAdresse = (EditText) findViewById(R.id.txtAdress);
        textViewDescription = (EditText) findViewById(R.id.txtDescription);*/
        //databaseReference = FirebaseDatabase.getInstance().getReference();

        final EditText[] editTextArray = new EditText[] {
                textViewEventName = (EditText) findViewById(R.id.txtEventName),
                textViewDate = (EditText) findViewById(R.id.txtDate),
                textViewClock = (EditText) findViewById(R.id.txtClock),
                textViewPayment = (EditText) findViewById(R.id.txtPayment),
                textViewAttendants = (EditText) findViewById(R.id.txtAttendants),
                textViewAdresse = (EditText) findViewById(R.id.txtAdress),
                textViewDescription = (EditText) findViewById(R.id.txtDescription),
        };

        //addPhotoButton.setOnClickListener(addPhotoListener);

        /** Få tak i filnavn til bildet */
        /*Uri returnUri = returnIntent.getData();
        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null,null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        textAddedPhoto = (TextView) findViewById(R.id.txtAddPhoto);
        textAddedPhoto.setText(returnCursor.getString(nameIndex));*/


        /** Laste opp data til firebase */
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EditText textView : editTextArray) {
                    if (textView.getText().length() == 0) {
                        System.out.println("Du mangler tekst i et felt");
                        return;
                    }
                    else {
                        System.out.println("Dette kan sendes til firebase!");
                    }
                }

                /*if (imageView.getDrawable() == 0) {
                    return;
                }*/

                //Send data til firebase
            }
        });
    }

    /** Håndtering av å hente bilde og ta bilde */
    /*private View.OnClickListener addPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hentBilde();
        }
    };

    private void hentBilde() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCOde, @Nullable Intent data) {
        ImageView imageView = findViewById(R.id.txtAddPhoto);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCOde == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
        }
    }*/
}
