package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView textUsername, textAge, textEmail, textPassword1, textPassword2, textAbout;
    Button but;
    String Uid;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        dbref = FirebaseDatabase.getInstance().getReference("users");
        but = findViewById(R.id.btnUpSign);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEmail = findViewById(R.id.txtEmail);
                textPassword1 = findViewById(R.id.txtPass1);
                textPassword2 = findViewById(R.id.txtPass2);
                textUsername = findViewById(R.id.txtName);
                textAbout = findViewById(R.id.txtInfo);
                textAge = findViewById(R.id.txtAlder);

                String uEmail = textEmail.getText().toString();
                String uPass1 = textPassword1.getText().toString();
                String uPass2 = textPassword2.getText().toString();
                String uName = textUsername.getText().toString();
                String uAbout = textAbout.getText().toString();
                String uAge = textAge.getText().toString();

                /* If-testene er satt til å gi mening ettersom hva brukeren
                 * fyller ut og i hvilken rekkefølge feltene står i
                 */
                if (uName.matches("")) { //Brukernavn må være fylt ut
                    Toast.makeText(SignUpActivity.this, "Fill out your full name", Toast.LENGTH_LONG).show();
                }else if (uAge.matches("")) { //Alder må være fylt ut
                    Toast.makeText(SignUpActivity.this, "Fill out your age", Toast.LENGTH_LONG).show();
                }else if (uEmail.matches("")){ //Sjekker om brukeren har fylt ut e-post
                    Toast.makeText(SignUpActivity.this, "You need an E-mail to sign up", Toast.LENGTH_SHORT).show();
                }else if (uPass1.matches("")) { //Sjekker at passord er fylt ut
                    Toast.makeText(SignUpActivity.this, "Make sure to create a password", Toast.LENGTH_SHORT).show();
                }else if (!(uPass1.equals(uPass2))) { //Passordene matcher ikke
                    Toast.makeText(SignUpActivity.this, "You passwords have to match", Toast.LENGTH_LONG).show();
                }else if(uAbout.matches("")){ //"Om meg" må være fylt ut
                    Toast.makeText(SignUpActivity.this,"Fill out about you", Toast.LENGTH_LONG).show();
                }else if (!(uName.length() >= 6 && uPass1.length() >= 6)){ //Passer på at brukernavn og passord har mer enn fem tegn
                    Toast.makeText(SignUpActivity.this, "Make sure your username and password is 6 characters or longer", Toast.LENGTH_SHORT).show();
                }else{ //Oppretter bruker
                    CreateUser(uEmail, uPass1);
                    Toast.makeText(SignUpActivity.this,"success", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void CreateUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();
                    Uid = user.getUid();

                    dbref.child(Uid).child("Navn").setValue(textUsername.getText().toString());
                    dbref.child(Uid).child("alder").setValue(Integer.class);
                    dbref.child(Uid).child("personinfo").setValue(textAbout.getText().toString());
                    dbref.child(Uid).child("FolgereList").child("key").setValue("value");

                    Intent intent =new Intent(SignUpActivity.this,LogInActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("Bruker", "createUserWithEmail:failed",task.getException());
                }
            }
        });
    }
}
