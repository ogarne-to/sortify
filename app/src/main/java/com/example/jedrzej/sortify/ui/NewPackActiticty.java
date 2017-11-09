package com.example.jedrzej.sortify.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jedrzej.sortify.Const;
import com.example.jedrzej.sortify.MyContentAdapter;
import com.example.jedrzej.sortify.MyLinearLayoutManager;
import com.example.jedrzej.sortify.R;
import com.example.jedrzej.sortify.Utils;
import com.example.jedrzej.sortify.datamodels.Pack;
import com.example.jedrzej.sortify.datamodels.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewPackActiticty extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NewPackActiticty.class.getSimpleName();
    private static final String REQUIRED = "Required";
    protected TextView mIdTextView;
    protected TextView mDateEditText;
    protected EditText mNameEditText;
    protected EditText mLocationEditText;
    protected RecyclerView mContentsRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected DatabaseReference mDatabase;
    protected ArrayList<String> mContents;
    private long mCreatedAt;
    private TextView mAddElementTextView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_entry);

        mCreatedAt = Calendar.getInstance().getTimeInMillis();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mContentsRecyclerView = (RecyclerView) findViewById(R.id.contents_recycler_view);

        mLayoutManager = new MyLinearLayoutManager(this);
        mContentsRecyclerView.setLayoutManager(mLayoutManager);

        //Set visibleID from current time
        mIdTextView = (TextView) findViewById(R.id.visible_id);
        mIdTextView.setText(Utils.getTime(mCreatedAt, Utils.FORMAT_VISIBLE_ID));

        // Set creation date from current time
        mDateEditText = (TextView) findViewById(R.id.date_textview);
        mDateEditText.setText(Utils.getTime(mCreatedAt, Utils.FORMAT_DATE_TIME));

        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mLocationEditText = (EditText) findViewById(R.id.location_edit_text);

        //Empty RecyclerView for content elements
        mContents = new ArrayList<>();
        mAdapter = new MyContentAdapter(mContents, this);
        mContentsRecyclerView.setAdapter(mAdapter);

        mAddElementTextView = (TextView) findViewById(R.id.add_element_text_view);
        mAddElementTextView.setOnClickListener(this);

        findViewById(R.id.save_button).setOnClickListener(this);
    }


    private void submitPack() {
        final String visibleId = mIdTextView.getText().toString();
        final String name = mNameEditText.getText().toString();
        final String location = mLocationEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNameEditText.setError(REQUIRED);
            return;
        }

        final String userId = mUser.getUid();
        mDatabase.child("users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User is unexpectedly null");
                            toast("Error: could not fetch user.");
                        } else {
//                            toast("This user currently has "
//                                    + String.valueOf(user.getPackCount()) + " packs");

                            saveData(userId, visibleId, mCreatedAt, name, location, mContents);
                        }

                        // Finish this Activity, back to the stream
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
//
//    private HashMap<String,Boolean> getHashMapOfContents(ArrayList<String> contents) {
//        HashMap<String, Boolean> map = new HashMap<>();
//        for (String element : contents) {
//            map.put(element, true);
//        }
//
//        return map;
//    }


    public void saveData(String userId, String visibleId, long createdAt, String name,
                         String location, ArrayList<String> contents) {

        final String key = mDatabase.child(Const.KEY_PACKS).push().getKey();
        final Pack paczka = new Pack(key, userId, visibleId, createdAt, name, location, contents);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/packs/" + key, paczka.toMap());
        childUpdates.put("/users/" + userId + "/packs/" + key, Utils.getNow());

        mDatabase.updateChildren(childUpdates)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                          mDatabase.child("packs").child(key).child("contents").setValue(paczka.getContents());
                            finish();

                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                Log.d(TAG, "Zapisz pushed");
                submitPack();
                break;
            case R.id.add_element_text_view:
                Log.d(TAG, "TextView should be added");
                addEmptyEditText();
                break;

        }
    }

    private void addEmptyEditText() {

        mContents.add(" ");
        Log.d(TAG, mContents.toString());
        mAdapter.notifyDataSetChanged();

    }

    private void toast(String message) {
        Toast.makeText(NewPackActiticty.this, message, Toast.LENGTH_SHORT).show();
    }
}
