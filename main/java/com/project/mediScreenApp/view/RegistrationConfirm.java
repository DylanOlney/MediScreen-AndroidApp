package com.project.mediScreenApp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.Patient;

// This activity is called when a new user is signing up. It displays the registration details they entered
// in the previous activity (RegistrationForm) and allows them to review and confirm same before storing
// the details in the database and registering them.

public class RegistrationConfirm extends AppCompatActivity {

    private TextView text;
    private Button btnConfirm;
    private Patient patient;
    private DAO DAO;  // Database access object.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_confirm);
        setTitle("Confirm details...");

        // Getting the new patient object created in the RegistrationForm activity.
        Bundle bundle = getIntent().getExtras();
        patient = (Patient)bundle.getSerializable("patient");

        // Displaying the new patient's details so that they may review and confirm what they entered.
        text = this.findViewById(R.id.tvConfirm);
        text.setText("Name: " + patient.getFirstName() + " " + patient.getLastName() +
                    "\nEmail: " + patient.getEmail() +
                    "\nPassword: " + patient.getPassword()+
                    "\nPhone: " + patient.getPhone() +
                    "\nDOB: " + patient.getDateofBirth() +
                    "\nGender: " + patient.getGender());

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener(){
            public void onClick(View btn) {
                confirm();}
        });
        DAO = new DAO(this);
    }

    // The confirm button handler. Inserts the new patient record to the database via the DAO object.
    // The DAO method call is asynchronous and requires a callback method in this class (see below).
    private void confirm(){
          DAO.putPatient(patient);
      }


    // DAO Callback method. If the record insertion was successful this method is called back by the DAO.
    // It displays a welcom message and bundles the new patient to the 'MainScreen' activity as a registered patient.
    public void registerSuccess(int id){
        if (id==0){
            Toast.makeText(this, "Already Registered!", Toast.LENGTH_LONG).show();
        }
        else {
            patient.setID(id);
        }

        Toast.makeText(this, "Welcome " + patient.getFirstName() + ", you are new registered.", Toast.LENGTH_LONG).show();
        Bundle b = new Bundle();
        b.putSerializable("patient", patient);
        Intent intent = new Intent(this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

}
