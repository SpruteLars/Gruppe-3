package no.hiof.emilie.efinder;

import android.content.Intent;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignInActivity extends Activity {
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth auth;
    public Button but;
    public EditText editEmail;
    public EditText editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        but = findViewById(R.id.myButton);
        but.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                editEmail = findViewById(R.id.editText4);
                editPass = findViewById(R.id.editText3);

                signIn(editEmail.getText().toString(),editPass.getText().toString());
            }
        });

    }


    private void signIn(String email, String password) {
       // Log.d(TAG, "signIn:" + email);
        //if (!validateForm()) {
         //   return;
        //}

        //showProgressDialog();

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);
                            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    private void checkEmailVerification() {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Boolean emailflag = firebaseUser.isEmailVerified();

            //startActivity(new Intent(SignInActivity.this, MainActivity.class));

       if(emailflag){
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }else{
            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
        }
        }



    }




