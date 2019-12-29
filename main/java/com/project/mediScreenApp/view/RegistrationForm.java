package com.project.mediScreenApp.view;

import androidx.appcompat.app.AppCompatActivity;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.Patient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// If the user trying to sign-in was not recognized, they are taken to this activity.
// Here they are presented with a registration form to collect their personal data which they submit
// in order to sign up and become registered patients. On submission, they are taken to a confirmation activity
// which allows them to review and confirm their data before continuing.

public class RegistrationForm extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    // Declaring registration form input widgets.....
    private EditText etFname, etLname, etPhone, etDob;
    private RadioButton rbM, rbF;
    private Button btnSubmit;
    private RadioGroup rgSex;
    private String email, password, gender;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_registration_form);
        this.setTitle("Registration Form");

        // Getting the email and password already entered in the previous activity.
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        password =  bundle.getString("password");

        // Initializing the form widgets......
        etFname = findViewById(R.id.etfName);
        etLname = findViewById(R.id.etlName);
        etPhone = findViewById(R.id.etPhoneNo);
        etDob = findViewById(R.id.etDateOfBirth);
        rbM = findViewById(R.id.rbMale);
        rbF  = findViewById(R.id.rbFemale);
        rgSex = findViewById(R.id.rgSex);

        // Setting up the submit button.....
        btnSubmit = findViewById(R.id.btnSubmitNewPatient);
        btnSubmit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View btn) {submitDetails();}
        });

        // Radio buttons for specifying gender.
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged (RadioGroup group,int id){
                int x = rgSex.getCheckedRadioButtonId();
                switch(x) {
                    case R.id.rbMale:
                            gender = "M";
                            break;
                    case R.id.rbFemale:
                            gender = "F";
                            break;
                }
            }
        });

        // A calender object for specifying date of birth.
        myCalendar = Calendar.getInstance();
        etDob.setOnFocusChangeListener(this);
        Toast.makeText(this, "Welcome new user, we'll need\nyou to fill in some basic\nregistration data!", Toast.LENGTH_LONG).show();

    }


    // The submit button handler. Validates input, creates a new patient object and bundles it to the next activity, 'RegistrationConfirm'.
    private void submitDetails(){
        String fn = etFname.getText().toString().trim();
        String ln = etLname.getText().toString().trim();
        String ph = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        if (fn.length() < 1 || ln.length() < 1 || ph.length() < 1 || dob.length() < 1 || gender.length() < 1) {
            Toast.makeText(this, "All fields are required!\nOne or more is empty!", Toast.LENGTH_LONG).show();
            return;
        }
        Patient p = new Patient(fn,ln,email,password,ph,dob,gender);
        Bundle b = new Bundle();
        b.putSerializable("patient", p);
        Intent intent = new Intent(this, RegistrationConfirm.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    // Handling the calendar/date picker input...
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {
        String myFormat = "MMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.getDefault());
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        etDob.setText(sdformat.format(myCalendar.getTime()));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(hasFocus){
            new DatePickerDialog(this, this, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

}
