package com.example.jean.testcontact.modele;

/**
 * Created by JEAN on 17/05/2017.
 */

public class Contact {
    private String id;
    private String name;
    private String prenom;
    private String tel;

    public Contact(String id,String name, String prenom, String tel) {
        this.id = id;
        this.name = name;
        this.prenom = prenom;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}

