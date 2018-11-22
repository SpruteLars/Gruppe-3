package no.hiof.emilie.efinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Currency;

public class bufferActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);


        if(getIntent().getStringExtra("Delete") != null){

            DatabaseReference deletedbref = firebaseDatabase.getInstance().getReference("events");

            deletedbref.child(getIntent().getStringExtra("Delete")).removeValue();
            Intent intent = new Intent (bufferActivity.this,MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent (bufferActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
