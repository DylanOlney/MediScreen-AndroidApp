package com.project.mediScreenApp.view;

import android.content.*;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.*;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.Patient;

// This is the log-in / register activity.
// It has two text inputs for e-mail and password and decides whether to log in an existing
// patient or to register a new patient based on whether or not the log in credentials exist in
// the 'patients' table of the database.

public class LoginRegister extends  AppCompatActivity  implements TextWatcher{

    // The two main input fields.
    private EditText            emailEditText;
    private EditText            passwordEditText;

    // Other widgets, vars, etc.....
    private Button              loginButton;
    private TextView            forgotPword;
    private String              email,password;
    private ProgressBar         loadingProgressBar;
    private SharedPreferences   prefs;
    private Editor              prefsEditor;
    private CheckBox            cbRemember;
    private DAO DAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Sign In/Register");
        setContentView(R.layout.activity_login);

        emailEditText       = findViewById(R.id.email);
        passwordEditText    = findViewById(R.id.password);
        loginButton         = findViewById(R.id.login);
        loadingProgressBar  = findViewById(R.id.loading);
        forgotPword         = findViewById(R.id.tvForgotPassword);
        cbRemember          = findViewById(R.id.cbRemember);
        email = "";
        password = "";

        // Adding text-change listeners for the main input fields so that the login/register button
        // may be enabled only after the e-mail & password are of a certain character length.
        emailEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);


        loginButton.setOnClickListener(new OnClickListener(){
            public void onClick(View btn) {loginRegister();}
        });
        forgotPword.setOnClickListener(new OnClickListener(){
            public void onClick(View btn) {changePword();}
        });

        // Setting up a shared prefs variable, so that the last sign-in credentials are remembered and displayed.
        prefs = getApplicationContext().getSharedPreferences("lastUser", 0);

        prefsEditor = prefs.edit();
        if ((email=prefs.getString("email", null))!=null){
            emailEditText.setText(email);
        }
        if ((password=prefs.getString("password", null))!=null){
            passwordEditText.setText(password);
        }

        // Getting a reference to the DAO (database access object).
        DAO = new DAO(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadingProgressBar.setVisibility(View.INVISIBLE);
    }


    // This method is called by the log-in/register button click handler.
    // It validates the credentials and checks the database via the DAO object for the existence
    // of the user. Requests made by the DAO object are asynchronous and so require this class to
    // have callback methods (see these further down).
    private void loginRegister(){
        if (!validateEmail(email)){
            Toast.makeText(getApplicationContext(), "Invalid e-mail address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cbRemember.isChecked()) {
            prefsEditor.putString("email", email);
            prefsEditor.putString("password", password);
        }
        else{
            prefsEditor.remove("email");
            prefsEditor.remove("password");
        }
        prefsEditor.apply();
        loadingProgressBar.setVisibility(View.VISIBLE);
        DAO.getPatient(email,password);
    }

    //***********************************************************************************************************************************************
    // Callback methods. These are called back by the DAO class when it has checked the database for the log-in credentials.

    // This method is called if the DAO found the credentials in the database and takes the user to the main screen activity.
    public void login(Patient patient){
        loadingProgressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Welcome  "+ patient.getFirstName() + ", you are now logged in! ", Toast.LENGTH_LONG).show();
        Bundle b = new Bundle();
        b.putSerializable("patient", patient);
        Intent intent = new Intent(this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    // This method is called if the DAO did not find the credentials in the database and takes the user to a register/sign up activity.
    public void register(){
        Bundle b = new Bundle();
        b.putString("email", email);
        b.putString("password", password);
        Intent intent = new Intent(this, RegistrationForm.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    //***************************************************************************************************************************************************


    // A helper method used to validate the e-mail address credential.
    public static boolean validateEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    // Text-change handlers. These enable the sign-in register button once the e-mail and password entries are of a certain length.
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
    @Override
    public void afterTextChanged(Editable editable) { }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        if (email.length() > 5 && password.length() > 8) {
            loginButton.setEnabled(true);
        }
        else {
            loginButton.setEnabled(false);
        }
    }

    private void changePword(){
        Toast.makeText(getApplicationContext(), "Not implemented yet! ", Toast.LENGTH_SHORT).show();
    }

}
