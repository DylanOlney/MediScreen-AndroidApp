package com.project.mediScreenApp.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.Patient;

// This activity class allows a patient record a star rating and a personal review of the app.
// The star rating selection is made via a drop-down spinner (0-5 stars) and the review is entered
// via a simple multiline EditText view.

// Both the rating and review are persisted to the database in a column of the 'patients' table.

public class AppRating extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    // Declaring widgets, vars, etc....
    private Patient patient;
    private String review;
    private EditText editText_review;
    private Spinner spinner;
    private Button btnDone;
    private String[] ratings;
    private String currentRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_rating);

        editText_review = findViewById(R.id.editText_review);
        spinner = findViewById(R.id.spinner2);

        // Setting the drop-down spinner's selectable star ratings.
        ratings = new String[]{"Star Rating: <none>","Star Rating: *","Star Rating: **","Star Rating: ***","Star Rating: ****","Star Rating: *****"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ratings);
        spinner.setAdapter(adp);

        // Setting the listener for the spinner.
        spinner.setOnItemSelectedListener(this);


        btnDone = findViewById(R.id.btn_review);
        btnDone.setOnClickListener(this);

        // Getting the patient from the calling activity.
        Bundle bundle = getIntent().getExtras();
        patient = (Patient)bundle.getSerializable("patient");

        // getting the previously stored review, if any.
        review = patient.getAppReview();
        if (review.equals("null")|| review==null){ review =  ""; }

        // Splitting the review from the star rating for display purposes.
        int index = 0;
        for (int i= 5; i >= 0;i--){
            if (review.contains(ratings[i])){
                review = review.substring(0,review.indexOf("\n" + ratings[i]));
                index = i;
                break;
            }
        }
        // Displaying the previously stored review and star rating.
        spinner.setSelection(index);
        currentRating = ratings[index];
        editText_review.setText(review);

    }

    // The submit button click handler. Updates the text review and star rating to the patients's record in the database and
    // passes the updated patient object back to the calling activity.
    @Override
    public void onClick(View view) {
        review = editText_review.getText() + "\n" + currentRating;
        new DAO(this).updatePatient_Rating(patient,review);
        patient.setAppReview(review);
        Intent intent = new Intent();
        intent.putExtra("patient", patient);
        setResult(RESULT_OK, intent);
        finish();
    }


    // The drop-down spinner selection handler.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        currentRating = ratings[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}
