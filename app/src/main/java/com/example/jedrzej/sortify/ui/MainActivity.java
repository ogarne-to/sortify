package com.example.jedrzej.sortify.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jedrzej.sortify.MyAdapter;
import com.example.jedrzej.sortify.R;
import com.example.jedrzej.sortify.datamodels.Pack;
import com.example.jedrzej.sortify.datamodels.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.jedrzej.sortify.R.id.fab;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_REQUEST_CODE = 1;
    FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Pack> mListOfPackages;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private ChildEventListener mDatabaseChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Lggging test");

        //Initialize Auth

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out. Navigating to login");
                    navigateToLogin();
                }
            }

        };

        //Initialize database

        mRef = FirebaseDatabase.getInstance().getReference();
        try {
            mRef.child("users").child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (user == null) {
                                // User is null, error out
                                Log.e(TAG, "User is unexpectedly null");
                                Toast.makeText(MainActivity.this, "Error: could not fetch user.",
                                        Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            } else {
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            mAuth.signOut();
                        }
                    });
        } catch (Exception e) {
            mAuth.signOut();
        }

        mDatabaseChangeListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();
                mRef.child("/packs/" + key)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onChildAdded() executed");
                                //Pack pack = dataSnapshot.getValue(Pack.class);

                                Pack changedPack = dataSnapshot.getValue(Pack.class);
                                mListOfPackages.add(changedPack);
                                mAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();
                mRef.child("/packs/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pack changedPack = dataSnapshot.getValue(Pack.class);
                        Log.d(TAG, "onChildChanged() executed");
                        for (Pack pack : mListOfPackages) {

                            // find and switch modified pack in recyclerView's data

                            if (pack.getKey().equals(changedPack.getKey())) {
                                int position = mListOfPackages.indexOf(pack);
                                mListOfPackages.set(position, changedPack);
                                mAdapter.notifyItemChanged(position);
                                Log.d(TAG, mListOfPackages.toString());
                                break;
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onCancelled (DatabaseError databaseError){

                    }

                });

            }



            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        String uid = mAuth.getCurrentUser().getUid();
        mRef.child("/users/" + uid + "/packs/").addChildEventListener(mDatabaseChangeListener);

        //Initialize UI

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mListOfPackages = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(mListOfPackages, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    //Downloads list of packages and notifies recyclerView
//    private void fetchPacks() {
//        String uid = mAuth.getCurrentUser().getUid();
//        mRef.child("/users/" + uid + "/packs/").addChildEventListener(mDatabaseChangeListener);
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_logout:
                mAuth.signOut();
                return true;

            case R.id.action_scan:
                Intent intent = new Intent(MainActivity.this, BarcodeScannerActivity.class);
                startActivityForResult(intent, BARCODE_REQUEST_CODE);
                return true;

            case R.id.action_database_entry:
                Intent databaseIntent = new Intent(MainActivity.this, NewPackActiticty.class);
                startActivity(databaseIntent);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BARCODE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("code")) {
                    String code = data.getExtras().getString("code");
                    Pack foundPack = null;
                    for (Pack pack : mListOfPackages) {
                        if (code.equals(pack.getKey())) {
                            foundPack = pack;
                            break;
                        }
                    }
                    if (foundPack == null) {
                        Snackbar.make(mFab, "No such pack in your database", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        goToDetails(foundPack);
                    }
                }

            }
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void goToDetails(Pack pack) {
        Log.d(TAG, "goToDetails()");
        Intent intent = new Intent(this, PackageActivity.class);
        intent.putExtra("packInfo", pack.toBundle());
        startActivity(intent);

    }

}
