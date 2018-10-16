package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;


public class logInActivity extends AppCompatActivity {
        private FirebaseAuth auth;
        public Button but;
        ArrayAdapter<String> nisse;
        public EditText editEmail;
        public EditText editPass;
        public FirebaseUser user;
        List<String> itemlist;
        String uid;
        private DatabaseReference dbref;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            auth = FirebaseAuth.getInstance();
            but = findViewById(R.id.myButton);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editEmail = findViewById(R.id.editText);
                    editPass = findViewById(R.id.editText2);

                    signIn(editEmail.getText().toString(), editPass.getText().toString());
                }
            });




        }
        private void signIn (String email, String password){
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                
                                Intent intent = new Intent(logInActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {

                                Toast.makeText(logInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            if (!task.isSuccessful()) {

                            }
                        }
                    });

        }
    }



