package com.example.jean.testcontact.modele;

import android.graphics.Bitmap;

/**
 * Created by JEAN on 17/05/2017.
 */

public class Contact {
    private String id;
    private String name;
    private Bitmap quickContact;
    private String tel;

    public Contact(String id,String name, Bitmap quickContact, String tel) {
        this.id = id;
        this.name = name;
        this.quickContact = quickContact;
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getQuickContact() {
        return quickContact;
    }

    public void setQuickContact(Bitmap quickContact) {
        this.quickContact = quickContact;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}

