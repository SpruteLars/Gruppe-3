package no.hiof.emilie.efinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    //String email;
    TextView textEmail;
    //String password;
    TextView textPassword;
    Button but;
    String Uid;
    TextView Username;
    TextView Userage;
    TextView Userabout;
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
                textPassword= findViewById(R.id.txtPass);

                if(textEmail != null && textPassword != null){

                    CreateUser(textEmail.getText().toString(),textPassword.getText().toString());

                }

            }
        });
    }
    private void CreateUser(String email,String password){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Bruker", "createUserWithEmail:success");

                    user = mAuth.getCurrentUser();

                    Uid = user.getUid();
                    Log.d("Bruker",Uid);
                    Username = findViewById(R.id.txtName);
                    Log.d("Bruker",Username.getText().toString());
                    Userabout = findViewById(R.id.txtInfo);
                    Log.d("Bruker",Userabout.getText().toString());
                    Userage = findViewById(R.id.txtAlder);
                    dbref.child(Uid).child("Navn").setValue(Username.getText().toString());
                    dbref.child(Uid).child("alder").setValue(Userage.getText().toString());
                    dbref.child(Uid).child("personinfo").setValue(Userabout.getText().toString());
                    dbref.child(Uid).child("folgere").setValue("0");
                    Intent intent =new Intent(SignUpActivity.this,LogInActivity.class);
                    startActivity(intent);

                }
                else {
                    Log.d("Bruker", "createUserWithEmail:failed",task.getException());
                }
            }
        });
    }
}
