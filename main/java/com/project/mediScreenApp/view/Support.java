package com.project.mediScreenApp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.*;
import java.text.*;
import java.util.Date;

// This activity enables a patient to call or send a message to either their GP or insurance professional.
// Radio buttons enable the selection of the recipient. A simple multi-line text input field captures the message
// and a button which creates a 'dial intent' displays the devices dialler with the recipients number ready to call.
// The message may be sent as an email to the recipient. If not, it can be viewed it via the professional's website portal.

public class Support extends AppCompatActivity {

    // Declaring widgets, vars, etc......
    private Button btnSubmit, btnCall;
    private RadioGroup rgSelectPro;
    private EditText etMessage;
    private Patient patient;
    private Doctor doctor;
    private InsurancePro insurancePro;
    private String phoneNo;
    private String messageRecipient;
    private String message;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        this.setTitle("Support/Contact");

        // Initialising the widgets..........
        btnSubmit = findViewById(R.id.btn_submitMessage);
        btnCall = findViewById(R.id.btnCall);
        rgSelectPro = findViewById(R.id.rg_choosePro);
        etMessage = findViewById(R.id.et_message);

        // Getting the patient, his/her doctor and insurance pro from the calling activity.
        Intent x = getIntent();
        doctor = (Doctor)x.getSerializableExtra("doctor");
        insurancePro = (InsurancePro)x.getSerializableExtra("insurancePro");
        patient = (Patient)x.getSerializableExtra("patient");

        // Attempt to get the patient's GP details. May be NULL (The patient may not yet have selected a GP).
        try {
            phoneNo = doctor.getPhone();
            email = doctor.getEmail();
            messageRecipient = "doctor";
        }
        catch (Exception e){}

        rgSelectPro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id==R.id.rb_doc) {
                    // Attempt to get the patient's GP details. May be NULL (The patient may not yet have selected a GP).
                    try {
                        phoneNo = doctor.getPhone();
                        email = doctor.getEmail();
                        messageRecipient = "doctor";
                    }
                    catch (Exception e){}
                }
                else if (id==R.id.rb_insPro)
                {
                    // Attempt to get the patient's insurance pro details. May be NULL (The patient may not yet have selected one).
                    try {
                        phoneNo = insurancePro.getPhone();
                        email = insurancePro.getEmail();
                        messageRecipient = "insurancePro";
                    }
                    catch (Exception e){}
                }
            }
        });


        // Handle the call button click. Opens the device's dialler and dials the recipients number.
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageRecipient==null){
                    Toast.makeText(Support.this, "You are not yet registered as a patient/client!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
                startActivity(intent);
            }
        });


        // Handle the send message button. Inserts the message into the patient's patient/professional database table record AND
        // offers the option to send the message via email to the selcted recipient.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (messageRecipient==null){
                    Toast.makeText(Support.this, "You are not yet registered as a patient/client!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                message = etMessage.getText().toString();
                message = message  + "\n\n(Sent at: " + dateFormat.format(date) + " as an e-mail from app.)";
                Toast.makeText(Support.this, message, Toast.LENGTH_SHORT).show();
                new DAO(Support.this).updatePatient_Message(patient, message, messageRecipient);
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setData(Uri.parse("mailto:" + email)); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });

    }
}
