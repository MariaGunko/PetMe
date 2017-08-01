package com.example.petme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class EmailPasswordActivity extends Activity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText mailEdit;
    private EditText pwdEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        // Views
        mailEdit = (EditText) findViewById(R.id.create_mail);
        pwdEdit = (EditText) findViewById(R.id.create_password);

        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.signUpButton).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d("TAG", "User is signed in");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void createAccount (String email, String password){
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUINewUser(user);
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }

            // ...
        }
            });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



//        Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
    private void updateUI(Object o) {
        Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("key", 1); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
        finish();
    }

    private void updateUINewUser(Object o) {
        Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("key", 2); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signUpButton) {
            if (checkFields(mailEdit.getText().toString(),pwdEdit.getText().toString())) {
                    createAccount(mailEdit.getText().toString(), pwdEdit.getText().toString());
                }
            }
        else if (i == R.id.signInButton) {
            if (checkFields(mailEdit.getText().toString(),pwdEdit.getText().toString())) {
                    signIn(mailEdit.getText().toString(), pwdEdit.getText().toString());
            }
        }
//         else if (i == R.id.sign_out_button) {
//            signOut();
//        }

    }

    private boolean checkFields (String m, String p){
        if (m.equals("")) {
            mailEdit.setError("E-mail is required");
            return false;
        }else {
            if (p.equals("")){
                pwdEdit.setError("Password is required");
                return false;
            }
                else
                {
                    if (p.length()<6) {
                        pwdEdit.setError("Password must contain at least 6 characters");
                        return false;
                    }
                }

        }
        return true;
    }

    @Override
    protected void onStop (){
        super.onStop();
        Log.d(TAG, "Authentication Activity - Stop method");
    }
}
