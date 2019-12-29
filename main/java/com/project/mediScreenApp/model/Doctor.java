package com.project.mediScreenApp.model;

import java.io.Serializable;

public class Doctor extends Professional {


    public Doctor(String _firstName,
                        String _lastName,
                        String _email,
                        String _password,
                        String _phone) {
        super(_firstName, _lastName, _email, _password, _phone);

    }

    public Doctor(int _id,
                        String _firstName,
                        String _lastName,
                        String _email,
                        String _password,
                        String _phone) {
        super(_id, _firstName, _lastName, _email, _password, _phone);

    }
}
