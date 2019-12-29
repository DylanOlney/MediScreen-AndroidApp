package com.project.mediScreenApp.view;

import androidx.appcompat.app.*;
import androidx.recyclerview.widget.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.*;
import java.util.ArrayList;

// This class is the main screen of the application and features the main options menu for the app.
// The options menu is implemented with a RecyclerView (an advanced list view)
public class MainScreen extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    // Instance vars...
    private Patient patient;
    private Doctor doctor,newDoc;
    private InsurancePro insurancePro, newInsurancePro;
    private MyRecyclerViewAdapter adapter;
    private AppCompatActivity thisActivity;

    // Reference to DAO object (database access object). Mostly consists of Volley methods to interact with database.
    private DAO DAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // The logged in patient is passed in via a bundle.
        Bundle bundle = getIntent().getExtras();
        patient = (Patient)bundle.getSerializable("patient");


        this.setTitle( patient.getFirstName() + " " + patient.getLastName());
        thisActivity = this;

        // An arrayList to hold the menu option strings. This gets passed the recyclerViewAdapter,
        // which is in turn displayed in the RecyclerView.
        ArrayList<String> menuOptions = new ArrayList<>();
        menuOptions.add("Select/Change your Doctor");
        menuOptions.add("Upload Medical Details & View Medi-AI Risk Estimation");
        menuOptions.add("View Doctor Report");
        menuOptions.add("Select/Change Insurance Pofessional");
        menuOptions.add("View Insurance Pofessional Report");
        menuOptions.add("Pay Insurance Premium");
        menuOptions.add("Rate This App");
        menuOptions.add("Support");

        // Populating the RecycleView with he menu items by way of a recycleViewAdapter object.
        RecyclerView recyclerView = findViewById(R.id.optionsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, menuOptions);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        // Instantiating the DAO and requesting the patient's doctor and insurance pro.
        DAO = new DAO(this);
        // Asynchronous calls....callback methods are below.
        DAO.getPatientDoc(patient);
        DAO.getPatientPro(patient);
    }

    // These methods are called back by the DAO once it has retreived the patient's doctor and insurance pro.
    public void initDoctor(Doctor d){
        doctor = d;
    }
    public void initInsurancePro(InsurancePro p){
        insurancePro = p;
    }


    // Menu item click handler (MyRecyclerViewAdapter.ItemClickListener implementation method)
    // There are 8 options in all, the majority of which start new activities to carry out the required action.
    @Override
    public void onItemClick(View view, int position) {
        int selection = position + 1;
        Intent intent;
        Bundle bundle = new Bundle();
        switch (selection){
            case 1:
                // Select/register patient with doctor.
                intent = new Intent(this, SelectDoctor.class);
                startActivityForResult(intent,selection);
                break;
            case 2:
                // Starts the Medical Details activity with medical data forms etc.
                intent = new Intent(this, MedicalDetails.class);
                bundle.putSerializable("patient", patient);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 3:
                // An asynchronous call to the DAO to get doctor's report. A callback below handles the response.
                DAO.getPatient_Doc_Report(patient);
                break;
            case 4:
                // Select/register patient with insurance pro.
                intent = new Intent(this, SelectInsurancePro.class);
                startActivityForResult(intent,selection);
                break;
            case 5:
                // An asynchronous call to the DAO to get insurance pro report. A callback below handles the response.
                DAO.getPatient_Prof_Report(patient);
                break;
            case 6:
                // Starts an activity with a webView to display a PayPal payment page.
                intent = new Intent(this, MakePayment.class);
                startActivity(intent);
                break;
            case 7:
                // Starts an 'app rating' activity with with the patient can rate the app and leave a review.
                intent = new Intent(this, AppRating.class);
                bundle.putSerializable("patient", patient);
                intent.putExtras(bundle);
                startActivityForResult(intent, selection);
                break;
            case 8:
                // Starts an activity which enables the patient to call or leave a written message to either doc or insurance pro.
                intent = new Intent(this, Support.class);
                bundle.putSerializable("patient", patient);
                bundle.putSerializable("doctor", doctor);
                bundle.putSerializable("insurancePro", insurancePro);
                intent.putExtras(bundle);
                startActivity(intent);
                break;


        }

    }


    // Callback function called by the DAO when it gets the doc report from the database.
    // It displays the report as a pop-up dialog.
    public void showDocReport(String report){
        String docInfo = "Dr. " + doctor.getFirstName() + " " + doctor.getLastName() + " wrote:\n\n";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Doctors Report");
        alert.setMessage(docInfo + report);
        alert.setPositiveButton("OK",null);
        alert.show();
    }

    // Callback function called by the DAO when it gets the insurance pro report from the database.
    // It displays the report as a pop-up dialog.
    public void showProfReport(String report){
        String info =  insurancePro.getFirstName() + " " + insurancePro.getLastName() + " wrote:\n\n";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Insurance Pro Report");
        alert.setMessage(info + report);
        alert.setPositiveButton("OK",null);
        alert.show();
    }


    // More DAO callback methods. These have to do with updating doctor, insurance pro.
    public void doctorUpdated(){
        doctor = newDoc;
        Toast.makeText(this, "Doctor updated!", Toast.LENGTH_SHORT).show();
    }

    public void insuranceProUpdated(){
        insurancePro = newInsurancePro;
        Toast.makeText(this, "Insurance Professional updated!", Toast.LENGTH_SHORT).show();
    }


    // This method handles the results of activities started for a result.
    // Case 1:  handles the updating of doctor and displays a confirmation dialog.
    // Case 4:  handles the updating of insurance pro and displays a confirmation dialog.
    // Case 7:  updates the patient's review and star rating as sent back from the 'rapp rating' activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dialogMessage = "";
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                     newDoc =  DAO.getDocByID(data.getIntExtra("docID", 0));
                     if (doctor == null) DAO.setPatientDoc(patient, newDoc.getId()) ;
                     else {
                        dialogMessage = "Are you sure you want to replace "
                                + doctor.getFirstName() + " "
                                + doctor.getLastName() +" with "
                                + newDoc.getFirstName() + " "
                                + newDoc.getLastName()+ " as your doctor?";
                        confimationDialog("Confirm", dialogMessage, requestCode);
                    }
                    break;

                case 4:
                    newInsurancePro =  DAO.getProByID(data.getIntExtra("proID", 0));
                    if (insurancePro == null) DAO.setPatientPro(patient, newInsurancePro.getId());
                    else {
                        dialogMessage = "Are you sure you want to replace "
                                + insurancePro.getFirstName() + " "
                                + insurancePro.getLastName() +" with "
                                + newInsurancePro.getFirstName() + " "
                                + newInsurancePro.getLastName() + " as your insurance professional?";
                        confimationDialog("Confirm", dialogMessage, requestCode);
                    }
                    break;
                case 7:
                    patient = (Patient)data.getSerializableExtra("patient");
                    break;
            }
        }
    }

    // A confirmation dialog used by the above method to confirm patient's choice regarding change of doctor or insurance pro.
    private void confimationDialog(String title, String message, int action){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener OK = null;
        builder.setTitle(title);
        builder.setMessage(message);

        if (action == 1) OK = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DAO.setPatientDoc(patient, newDoc.getId());
            }};

        else if (action == 5) OK = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DAO.setPatientPro(patient, newInsurancePro.getId());
            }};

        builder.setPositiveButton("OK", OK);
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, StartScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    



}
