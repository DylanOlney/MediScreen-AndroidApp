package com.project.mediScreenApp.model;

import java.io.Serializable;

public class Professional extends UnknownUser implements Serializable {
    private String firstName, lastName, phone;
    private int id;
    public Professional(String _firstName,
                  String _lastName,
                  String _email,
                  String _password,
                  String _phone) {
        super( _email, _password);
        firstName = _firstName ;
        lastName = _lastName;
        phone = _phone;
    }

    public Professional(int _id,
                  String _firstName,
                  String _lastName,
                  String _email,
                  String _password,
                  String _phone) {
        super( _email, _password);
        id = _id;
        firstName = _firstName ;
        lastName = _lastName;
        phone = _phone;
    }

    public int getId(){return id;}

    public void setID(int _id){id = _id;}

    public void setFirstName(String _firstName){
        this.firstName = _firstName;
    }

    public void setLastName(String _lastName) {
        lastName = _lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String _email) {
        email = _email;
    }

    public void setPassword(String _password) {
        password = _password;
    }

    public String getPhone(){return phone;}

    public void setPhone(String _phone){phone = _phone;}


}
