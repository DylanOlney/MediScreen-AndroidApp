package com.project.mediScreenApp.model;

public class InsurancePro extends Professional {
    public InsurancePro (String _firstName,
                  String _lastName,
                  String _email,
                  String _password,
                  String _phone) {
        super(_firstName, _lastName, _email, _password, _phone);

    }

    public InsurancePro (int _id,
                  String _firstName,
                  String _lastName,
                  String _email,
                  String _password,
                  String _phone) {
        super(_id, _firstName, _lastName, _email, _password, _phone);

    }
}