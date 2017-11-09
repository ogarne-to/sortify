package com.example.jedrzej.sortify.datamodels;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jedrz on 29.06.2016.
 */

public class Pack {

    public static final String KEY_KEY = "key";
    public static final String KEY_OWNER = "owner";


    public static final String KEY_VISIBLEID = "visibleId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_CONTENTS = "contents";


    public String key;
    public String owner;

    public String visibleId;
    public Long createdAt;
    private String name;
    private String location;
    private ArrayList<String> contents;



    public Pack() {
        contents = new ArrayList<>();

    }

    public Pack(String key, String owner, String visibleId, long createdAt, String name, String location,
                ArrayList<String> contents) {
        this();
        this.key = key;
        this.owner = owner;
        this.visibleId = visibleId;
        this.createdAt = createdAt;
        this.name = name;
        this.location = location;
        this.contents = contents;


    }

    public Pack(Bundle bundle) {
        this();
        this.key = bundle.getString(KEY_KEY);
        this.owner = bundle.getString(KEY_OWNER);
        this.visibleId = bundle.getString(KEY_VISIBLEID);
        this.createdAt = bundle.getLong(KEY_CREATED_AT);
        this.name = bundle.getString(KEY_NAME);
        this.location = bundle.getString(KEY_LOCATION);
        this.contents = bundle.getStringArrayList(KEY_CONTENTS);
    }

    //Getters

    public String getKey() {
        return key;
    }

    public String getOwner() {
        return owner;
    }

    public String getVisibleId() {
        return visibleId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public String getStringOfContents() {
        String output = "";
        for (String element : contents) {
            output += element;

            if (contents.indexOf(element) < contents.size() - 1) {
                output += ", ";
            }
        }

        return output;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(KEY_KEY, key);
        map.put(KEY_OWNER, owner);
        map.put(KEY_VISIBLEID, visibleId);
        map.put(KEY_CREATED_AT, createdAt);
        map.put(KEY_NAME, name);
        map.put(KEY_LOCATION, location);
        map.put(KEY_CONTENTS, contents);

        return map;
    }



    public Bundle toBundle() {

        //Create new bundle and put the field values there
        Bundle bundle = new Bundle();
        bundle.putString(KEY_KEY, key);
        bundle.putString(KEY_OWNER, owner);
        bundle.putString(KEY_VISIBLEID, visibleId);
        bundle.putLong(KEY_CREATED_AT, createdAt);
        bundle.putString(KEY_NAME, name);
        bundle.putString(KEY_LOCATION, location);
        bundle.putStringArrayList(KEY_CONTENTS, contents);

        return bundle;
    }



    @Override
    public String toString() {
        return "Pack{" +
                "key='" + key + '\'' +
                ", owner='" + owner + '\'' +
                ", visibleId='" + visibleId + '\'' +
                ", createdAt=" + createdAt +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", contents=" + contents +
                '}';
    }



    //Setters


//    public void setCreatedAt(String createdAt) {
//        this.createdAt = Long.parseLong(createdAt);
//    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setVisibleId(String visibleId) {
        this.visibleId = visibleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

 }
