package com.example.jedrzej.sortify.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.jedrzej.sortify.MyContentAdapter;
import com.example.jedrzej.sortify.Utils;
import com.example.jedrzej.sortify.datamodels.Pack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jedrz on 13.09.2016.
 */


public class EditPackActivity extends NewPackActiticty {
    Pack mPack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fillFieldsWithData();
    }

    private void fillFieldsWithData() {
        Bundle data = getIntent().getExtras().getBundle("packInfo");

        mPack = new Pack(data);

        mIdTextView.setText(mPack.getVisibleId());
        mDateEditText.setText(Utils.getTime(mPack.getCreatedAt(),Utils.FORMAT_DATE_TIME));
        mNameEditText.setText(mPack.getName());
        mLocationEditText.setText(mPack.getLocation());

        mContents = mPack.getContents();
        mAdapter = new MyContentAdapter(mContents, this);
        mContentsRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void saveData(String userId, String visibleId, long createdAt, String name,
                         String location, ArrayList<String> contents) {

        final String key = mPack.getKey();
        final Pack paczka = new Pack(key, userId, visibleId, createdAt, name, location, contents);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/packs/" + key, paczka.toMap());
        childUpdates.put("/users/" + userId + "/packs/" + key, Utils.getNow());


        mDatabase.updateChildren(childUpdates).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //mDatabase.child("packs").child(key).child("contents").setValue(paczka.getListOfContents());
                    finish();

                }
            }
        });
    }
}

