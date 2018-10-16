package no.hiof.emilie.efinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MakeEventActivity extends AppCompatActivity {
    ImageView imageView;
    EditText textViewEventName;
    EditText textViewDate;
    EditText textViewClock;
    EditText textViewPayment;
    EditText textViewAttendants;
    EditText textViewAdresse;
    EditText textViewDescription;
    Button buttonSubmit;
    //private EditText[] editTextArray;
    //private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);

        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        imageView = (ImageView) findViewById(R.id.imgView);
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
}
