package com.motherslove.stephnoutsa.mothersloveteam;


import android.content.ContentValues;

public class Contact {

    // Private variables
    int _contactid;
    String contactphone;
    String contactlmp;

    // Emptry constructor
    public Contact() {

    }

    // Constructor
    public Contact(int _contactid, String contactphone, String contactlmp) {
        this._contactid = _contactid;
        this.contactphone = contactphone;
        this.contactlmp = contactlmp;
    }

    // Constructor
    public Contact(String contactphone, String contactlmp) {
        this.contactphone = contactphone;
        this.contactlmp = contactlmp;
    }

    // Getter and Setter methods
    public int get_contactid() {
        return _contactid;
    }

    public void set_contactid(int _contactid) {
        this._contactid = _contactid;
    }

    public String getContactlmp() {
        return contactlmp;
    }

    public void setContactlmp(String contactlmp) {
        this.contactlmp = contactlmp;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }
}
