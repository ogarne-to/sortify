package com.example.jedrzej.sortify.datamodels;

import java.util.HashMap;

/**
 * Created by jedrz on 01.07.2016.
 */

public class User {

    private HashMap<String, Long> packs;
    private String name;
    private String email;
    private String uid;


    //constructors

    public User() {
        this.packs = new HashMap<String, Long>();

    }

    public User (String email, String uid) {
        this();
        this.email = email;
        this.uid = uid;
    }


    public void addPack(String id, long date) {
        packs.put(id, date);
    }

    //geters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashMap<String, Long> getPacks() {
        return packs;
    }

    public void setPacks(HashMap<String, Long> packs) {
        this.packs = packs;
    }




}
