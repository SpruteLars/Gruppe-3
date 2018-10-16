package no.hiof.emilie.efinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
//import com.google.firebase.database.DatabaseReference;

public class MakeEventActivity extends AppCompatActivity {
    ImageView imageView; //Burde være array????
    EditText textViewEventName; //Er egentlig PlainText
    EditText textViewDate;
    EditText textViewClock;
    EditText textViewPayment;
    EditText textViewAttendants;
    EditText textViewAdresse;
    Button buttonSubmit;
    //private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);

        imageView = (ImageView) findViewById(R.id.imgView);
        textViewEventName = (EditText) findViewById(R.id.txtEventName);
        textViewDate = (EditText) findViewById(R.id.txtDate);
        textViewClock = (EditText) findViewById(R.id.txtClock);
        textViewPayment = (EditText) findViewById(R.id.txtPayment);
        textViewAttendants = (EditText) findViewById(R.id.txtAttendants);
        textViewAdresse = (EditText) findViewById(R.id.txtAdress);
        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        //databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //Send datat til firebase
            }
        });
    }
}
