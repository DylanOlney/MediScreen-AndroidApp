package com.project.mediScreenApp.model;

import java.io.Serializable;

public class UnknownUser implements Serializable{


    protected String email, password;
    public UnknownUser(String _email, String pword){
        email = _email;
        password = pword;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }


}

