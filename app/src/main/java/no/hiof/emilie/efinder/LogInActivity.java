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
import com.google.firebase.database.DatabaseReference;

import java.util.List;


public class LogInActivity extends AppCompatActivity {
        private FirebaseAuth auth;
        public Button but;
        public Button mBut;
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
            setContentView(R.layout.activity_login);

            auth = FirebaseAuth.getInstance();
            but = findViewById(R.id.btnLogIn);
            mBut = findViewById(R.id.btnSignUp);

            mBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                    startActivity(intent);
                }
            });
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        editEmail = findViewById(R.id.txtEmail);
                        String Username = editEmail.getText().toString();
                        editPass = findViewById(R.id.txtPassword);
                        String password = editPass.getText().toString();

                    if(Username.matches("")){
                        Toast.makeText(LogInActivity.this,"Fill in your Email",Toast.LENGTH_LONG).show();
                    }else if(password.matches("")){
                        Toast.makeText(LogInActivity.this,"Fill in your Password",Toast.LENGTH_LONG).show();
                    }else{
                        signIn(Username, password);
                    }

                }
            });




        }
        private void signIn (String email, String password){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LogInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()) { }
                    }
            });

        }
    }



