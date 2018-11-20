package no.hiof.emilie.efinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    Integer harald = 0;
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
                String tEmail = textEmail.getText().toString();
                textPassword = findViewById(R.id.txtPass);
                String tPass =  textPassword.getText().toString();
                Username = findViewById(R.id.txtName);
                String uName = Username.getText().toString();
                Log.d("Bruker",Username.getText().toString());
                Userabout = findViewById(R.id.txtInfo);
                String uAbout = Userabout.getText().toString();
                Log.d("Bruker",Userabout.getText().toString());
                Userage = findViewById(R.id.txtAlder);
                String uAge = Userage.getText().toString();
                if (uName.matches("")){
                    Toast.makeText(SignUpActivity.this,"Fill out your full name", Toast.LENGTH_LONG).show();
                }else if(uAbout.matches("")){
                    Toast.makeText(SignUpActivity.this,"Fill out about you", Toast.LENGTH_LONG).show();
                }else if(uAge.matches("")){
                    Toast.makeText(SignUpActivity.this,"Fill out your age", Toast.LENGTH_LONG).show();
                } else if(tPass.matches("") && tEmail.matches("")){

                    Toast.makeText(SignUpActivity.this,"Fill out a email and a password", Toast.LENGTH_LONG).show();
                }else{
                    CreateUser(textEmail.getText().toString(),textPassword.getText().toString());
                    Toast.makeText(SignUpActivity.this,"success", Toast.LENGTH_LONG).show();
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
                    dbref.child(Uid).child("FolgereList").child("key").setValue("value");
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
