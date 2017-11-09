package com.example.jedrzej.sortify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jedrzej.sortify.R;
import com.example.jedrzej.sortify.datamodels.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.jedrzej.sortify.R.id.passwordField;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();


    private EditText mEmailField;
    private EditText mPasswordField;

    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mReference = FirebaseDatabase.getInstance().getReference();


        mEmailField = (EditText) findViewById(R.id.email_field);
        mPasswordField = (EditText) findViewById(passwordField);

        findViewById(R.id.registerButton).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in, check if the user object is present
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//
//                    createUserObjectIfNecessary(user.getUid());
//
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
////                    Intent backToSignInIntent = new Intent(RegisterActivity.this, LoginActivity.class);
////                    finish();
//                }
//
//            }
//        };


    }


//    @Override
//    protected void onStart() {
//        super.onStart();
////        mAuth.addAuthStateListener(mAuthListener);
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
////            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }



    private void registerWithEmail(final String email, String password) {
        if (!verifyInput(email, password)) {
            notifyUser("Either email or password are empty");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    Log.d(TAG,e.getErrorCode());
                                    String message = "";
                                    switch (e.getErrorCode()) {
                                        case  "ERROR_INVALID_EMAIL" :
                                            message = "Invalid email";
                                            break;
                                        case "ERROR_WEAK_PASSWORD" :
                                            message = "The password has to have at least 6 letters";
                                            break;
                                        case "ERROR_EMAIL_ALREADY_IN_USE" :
                                            message = "Error: email already in use";
                                    }

                                    notifyUser(message);


                                } else {

                                    String uid = mAuth.getCurrentUser().getUid();

                                    Log.d(TAG, "createUserObjectIfNEcessary() " + uid);
                                    createUserObjectIfNecessary(uid, email);

                                }
                            }
                        }
                );
    }


    private void createUserObjectIfNecessary(final String userId, final String email) {

        mReference.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null) {
                            // User is null, error out
                            Log.d(TAG, "User is null, creating new User object");

                            User userObject = new User(email, userId);
                            mReference.child("users").child(userId).setValue(userObject).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG,"User created");
                                                //notifyUser("New user object created.");
                                                goToMainActivity();
                                            } else {
                                                Log.d(TAG,"Some error");
                                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                                Log.d(TAG,e.getErrorCode());
                                            }
                                        }
                                    }
                            );
                        } else {
                            goToMainActivity();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private boolean verifyInput(String email, String password) {
        return email.length() > 0 && password.length() > 0;
    }

    private void notifyUser(String message) {
        Snackbar.make(mPasswordField, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton :
                registerWithEmail(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }




}
