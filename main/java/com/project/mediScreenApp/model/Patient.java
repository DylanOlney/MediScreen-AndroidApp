package com.project.mediScreenApp.model;


import java.io.Serializable;
import java.text.*;

public class Patient extends UnknownUser implements Serializable {

    private DateFormat dateFormat;
    private String firstName, lastName, phone,dob, app_review, gender;
    private int id;

    public Patient(String _firstName,
                   String _lastName,
                   String _email,
                   String _password,
                   String _phone,
                   String _dob,
                   String _gender){
        super( _email, _password);
        firstName = _firstName ;
        lastName = _lastName;
        phone = _phone;
        gender = _gender;
        dob = _dob;
    }
    public Patient(int _id,
                   String _firstName,
                   String _lastName,
                   String _email,
                   String _password,
                   String _phone,
                   String _dob,
                   String _gender,
                   String _review){
        super( _email, _password);
        id = _id;
        firstName = _firstName ;
        lastName = _lastName;
        phone = _phone;
        gender = _gender;
        app_review = _review;
        dob  = _dob;
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

    public String getDateofBirth(){return dob;}

    public String getApp_review(){ return app_review; }

    public void setApp_review(String _history){ app_review = _history;};

    public String getPhone(){return phone;}

    public void setPhone(String _phone){phone = _phone;}

    public String getGender(){return gender;}
    public String getAppReview(){
        return app_review;
    }

    public void setAppReview(String review){
        app_review = review;
    }


}
