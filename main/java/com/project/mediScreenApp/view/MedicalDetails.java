package com.project.mediScreenApp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.Patient;
import java.util.*;


// This activity class allows a patient enter disease specific numeric details required by the Medi-AI back end machine learning service.
// It contains RecyclerView lists (four in all, one for each disease) to list out the feature names and values for all the diseases.
// Each individual list item is editable when clicked by means of a simple text input pop-up dialog, thereby allowing the patient
// enter new data as well as view previously saved data.

// As well as this, there is a Medi-AI button for each list, allowing the user send completed disease feature form data to the back-end Medi-AI service.
// This service then returns a prediction which is displayed here via a pop-up dialog.

public class MedicalDetails extends AppCompatActivity {

    // These maps will contain the disease feature names as string keys and the numeric feature values as string values.
    private Map<String,String> diabetes, heartDisease, breastCancer, prostateCancer;

    // These arrays will hold the string keys for the above maps.
    private String[] diabetesKeys,heartKeys, cancerKeys, prostateKeys;

    // These arrayLists have to do with populating the recyclerViews.
    private ArrayList<String> diabetesList, heartList, cancerList, prostateList;

    // Buttons with which to call Medi-AI service.
    private Button btnAI_Diabetes, btnAI_Heart, btnAI_Cancer, btnAI_Prostate;

    // Constants to identify disease types.
    public static final int DIABETES = 1, HEART_DISEASE = 2, CANCER = 3, PROSTATE = 4;

    // More vars...
    private String inputValue = "", field;
    private int diseaseType;
    private Patient patient;
    private MedicalDetails thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_details);

        // Initialising the Medi-AI buttons.
        btnAI_Diabetes = findViewById(R.id.btn_AI_diabetes);
        btnAI_Heart = findViewById(R.id.btn_AI_heart);
        btnAI_Cancer = findViewById(R.id.btn_AI_cancer);
        btnAI_Prostate = findViewById(R.id.btn_AI_prostate);
        thisActivity = this;

        // Getting the patient passed by calling activity.
        Bundle bundle = getIntent().getExtras();
        patient = (Patient)bundle.getSerializable("patient");
        this.setTitle("View/Edit Medical Details");


        // These are the keys for the disease maps. They are also the labels for the disease feature lists.
        // They form part of the text for each list item. The actual value forms the remaining part.

        diabetesKeys = new String[]{"Times pregnant:","Plasma Glucose Concentration:","Diastolic blood pressure (mm Hg):","Triceps skin fold thickness (mm):",
                "2-Hour serum insulin (mu U/ml):","Body mass index:","Diabetes pedigree function:","Age:"};

        heartKeys =  new String[]{"Age:","Gender:","CP Type (chest pain 1-4):","Resting Blood Pressure (mm-Hg):","Cholesterol (mg/dl):","Fasting Blood Sugar > 120 mg/dl:",
                "Resting electrocardiographic results","Maximum Heart Rate Achieved:","Excercise induced angina:","ST depression induced by exercise relative to rest:",
                "The slope of the peak exercise ST segment:","Number of major vessels (0-3):","thal (3,6 or 8):"};

        cancerKeys = new String[]{"Clump Thickness (1-10):","Uniformity of Cell Size (1-10):","Uniformity of Cell Shape (1-10):","Marginal Adhesion (1-10):","Single Epithelial Cell Size (1-10):",
                "Bare Nuclei (1-10):","Bland Chromatin (1-10):","Normal Nucleoli (1-10):","Mitoses (1-10):"};

        prostateKeys = new String[]{"radius:", "texture:", "perimeter:", "area:", "smoothness:", "compactness:", "symmetry:", "fractal_dimension:"};


        // Adding handlers for the Medi-AI buttons...........
        btnAI_Diabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAIRisk(DIABETES)){    // getAIRisk() returns false if not all feature values for a disease have been set (see method further down).
                    Toast.makeText(thisActivity, "One or more values for diabetes haven't been set!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAI_Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAIRisk(HEART_DISEASE)){
                    Toast.makeText(thisActivity, "One or more values for heart disease haven't been set!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAI_Cancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAIRisk(CANCER)){
                    Toast.makeText(thisActivity, "One or more values for breast cancer haven't been set!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAI_Prostate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAIRisk(PROSTATE)){
                    Toast.makeText(thisActivity, "One or more values for prostate cancer haven't been set!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Asynchronously getting the patients's previously stored disease data from the database.
        // The init methods below are the callback methods which populate the disease maps with the returned data.
        new DAO(this).getDiseaseData(patient,DIABETES);
        new DAO(this).getDiseaseData(patient,HEART_DISEASE);
        new DAO(this).getDiseaseData(patient,CANCER);
        new DAO(this).getDiseaseData(patient,PROSTATE);
    }

    // **********************************************************************************************************************************
    // The following 4 methods initialise and populate the disease maps with the data returned from the database.
    // If a feature value is null, the value in the disease map is set to the string, "<value not set>" for display purposes.

    public void initDiabetes(ArrayList<String> fields){
        diabetes = new LinkedHashMap<String, String>();
        for (int i = 0; i < diabetesKeys.length; i++){
            if (fields==null){
                diabetes.put(diabetesKeys[i], "<value not set>");
            }
            else{
                String val = fields.get(i);
                if (val.equals("null")){
                    diabetes.put(diabetesKeys[i], "<value not set>");
                }
                else diabetes.put(diabetesKeys[i], val);
            }
        }
        populateDiabetesList();
    }

    public void initHeart(ArrayList<String> fields){
        heartDisease = new LinkedHashMap<String, String>();
        for (int i = 0; i < heartKeys.length; i++){
            if (fields==null){
                heartDisease.put(heartKeys[i], "<value not set>");
            }
            else{
                String val = fields.get(i);
                if (val.equals("null")){
                    heartDisease.put(heartKeys[i], "<value not set>");
                }
                else heartDisease.put(heartKeys[i], val);
            }
        }
        populateHeartList();
    }

    public void initBreastCancer(ArrayList<String> fields){
        breastCancer = new LinkedHashMap<String, String>();
        for (int i = 0; i < cancerKeys.length; i++){
            if (fields==null){
                breastCancer.put(cancerKeys[i], "<value not set>");
            }
            else{
                String val = fields.get(i);
                if (val.equals("null")){
                    breastCancer.put(cancerKeys[i], "<value not set>");
                }
                else breastCancer.put(cancerKeys[i], val);
            }
        }
        populateCancerList();
    }

    public void initProstate(ArrayList<String> fields){
        prostateCancer = new LinkedHashMap<String, String>();
        for (int i = 0; i < prostateKeys.length; i++){
            if (fields==null){
                prostateCancer.put(prostateKeys[i], "<value not set>");
            }
            else{
                String val = fields.get(i);
                if (val.equals("null")){
                    prostateCancer.put(prostateKeys[i], "<value not set>");
                }
                else prostateCancer.put(prostateKeys[i], val);
            }
        }
        populateProstateList();
    }

    //******************************************************************************************************************************************


    // The following 4 methods populate the 4 RecyclerView lists with a string as each list item.
    // Each string consists of the feature label and the current value for that feature.
    // Each list item is clickable and each method sets up an ItemClickListener, which triggers an input pop-up dialog
    // so that the user may enter/edit the value for a feature.

    private  void  populateDiabetesList(){
        if (diabetes==null) return;
        diabetesList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : diabetes.entrySet()){
            String temp = "";
            if (entry.getValue()!=null) temp = "\n" + entry.getValue();
            diabetesList.add(entry.getKey() + temp);
        }
        RecyclerView recyclerView = findViewById(R.id.diabetesRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerViewAdapter diabtetesAdapter = new MyRecyclerViewAdapter(this, diabetesList);
        diabtetesAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                diseaseType = DIABETES;
                field = diabetesList.get(position).split(":")[0];
                field = field + ":";
                inputDialog();
            }
        });
        recyclerView.setAdapter(diabtetesAdapter);
    }

    private  void populateHeartList(){
        if (heartDisease==null) return;
        heartList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : heartDisease.entrySet()){
            String temp = "";
            if (entry.getValue()!=null) temp = "\n" + entry.getValue();
            heartList.add(entry.getKey() + temp);
        }
        RecyclerView recyclerView = findViewById(R.id.heartRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerViewAdapter heartAdapter = new MyRecyclerViewAdapter(this, heartList);
        heartAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                diseaseType = HEART_DISEASE;
                field = heartList.get(position).split(":")[0];
                field = field + ":";
                inputDialog();
            }
        });
        recyclerView.setAdapter(heartAdapter);
    }

    private  void populateCancerList(){
        if (breastCancer==null) return;
        cancerList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : breastCancer.entrySet()){
            String temp = "";
            if (entry.getValue()!=null) temp = "\n" + entry.getValue();
            cancerList.add(entry.getKey() + temp);
        }
        RecyclerView recyclerView = findViewById(R.id.cancerRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerViewAdapter cancerAdapter = new MyRecyclerViewAdapter(this, cancerList);
        cancerAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                diseaseType = CANCER;
                field = cancerList.get(position).split(":")[0];
                field = field + ":";
                inputDialog();
            }
        });
        recyclerView.setAdapter(cancerAdapter);
    }

    private  void populateProstateList(){
        if (prostateCancer==null) return;
        prostateList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : prostateCancer.entrySet()){
            String temp = "";
            if (entry.getValue()!=null) temp = "\n" + entry.getValue();
            prostateList.add(entry.getKey() + temp);
        }
        RecyclerView recyclerView = findViewById(R.id.prostateRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerViewAdapter prostateAdapter = new MyRecyclerViewAdapter(this, prostateList);
        prostateAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                diseaseType = PROSTATE;
                field = prostateList.get(position).split(":")[0];
                field = field + ":";
                inputDialog();
            }
        });
        recyclerView.setAdapter(prostateAdapter);
    }

    //**********************************************************************************************************************************


    // This method displays a pop-up input dialog so that the user may enter/edit the data for a disease feature.
    // It is called by the ItemClick handler of the RecyclerView adapter.
    private void inputDialog(){
        inputValue = "CANCEL";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alert.setTitle(field);
        alert.setMessage("Enter a value: ");
        alert.setView(edittext);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                inputValue = edittext.getText().toString();
                saveInputData();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                saveInputData();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }


    // This helper method is used by the above method to validate and save the user's input data to the disease maps,
    // and to re-populate the RecyclerView lists to reflect the new data.
    private void saveInputData(){
        Map<String,String> map = null;
        switch(diseaseType){
            case DIABETES:
                map = diabetes;
                break;
            case HEART_DISEASE:
                map = heartDisease;
                break;
            case CANCER:
                map = breastCancer;
                break;
            case PROSTATE:
                map = prostateCancer;
                break;

        }
        if (!inputValue.equals("CANCEL")) {
            try {// Try to parse the input data

                Double val = Double.parseDouble(inputValue);

                // If it parsed, update the relevant map.
                map.put(field, inputValue);

                // Update the list views to reflect the new data.
                populateDiabetesList();
                populateHeartList();
                populateCancerList();
                populateProstateList();
                saveChanges(true);
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Invalid input. It should be numeric.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // This method saves any changes made, checks for null (incomplete) data and calls the Medi-AI service through the DAO class if the data is complete.
    // It returns false if the data for a disease is incomplete (missing values) and true if not.
    private boolean getAIRisk(int disease){
        diseaseType = disease;
        Map<String,String> dbValues = saveChanges(false);
        for (Map.Entry<String, String> entry : dbValues.entrySet()) {
            if (entry.getValue() == null) {
                return false;
            }
        }
        // This call is asynchronous - a call back method in this class will display the Medi-AI prediction result.
        new DAO(thisActivity).getAI_Risk(dbValues, diseaseType);
        return true;
    }


    // This helper method parses the disease map values to values appropriate for the Medi-AI service and the database.
    // i.e. it replaces the value '<value not set>' with NULL. Also, it optionally updates the database at the same time
    // depending on whether the 'upDateDB' param is true.
    private Map saveChanges(boolean updateDb){
        Map<String,String> map = null;
        switch(diseaseType){
            case DIABETES:
                map = diabetes;
                break;
            case HEART_DISEASE:
                map = heartDisease;
                break;
            case CANCER:
                map = breastCancer;
                break;
            case PROSTATE:
                map = prostateCancer;
                break;
        }

        LinkedHashMap<String, String> values  = null;
        values = new LinkedHashMap<String, String>();
        values.put("patient_id", "" + patient.getId());
        int index = 1;
        for (Map.Entry<String, String> entry : map.entrySet()){
            String val = entry.getValue();
            if (val.equals("<value not set>")){
                values.put("f" + index, "null");
            }
            else values.put("f" + index, entry.getValue());
            index++;
        }
        // Update database if required.
        if (updateDb) new DAO(this).putDiseaseData(diseaseType,values);

        // return the parsed data.
        return values;
    }



    // This method is called when the DAO asynchronously returns the Medi-AI prediction result and it displays
    // the result to the user in the form of a pop-up dialog.
    public void show_AI_results(double risk, double accuracy, int boolrisk){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String message = "";
        switch(diseaseType){
            case DIABETES:
                message = "Based on the data, our AI model has estimated that your risk of developing diabetes within the next five years is: \n" + risk + "%\n\n" +
                            "\n(AI model accuracy = " + accuracy + "%)";
                break;
            case HEART_DISEASE:
                message =  "Based on the data, our AI model has estimated that the probability that you have heart disease is: " + risk + "%\n\n" +
                        "\n(AI model accuracy = " + accuracy + "%)";
                break;
            case CANCER:
                message = "Based on the data, our AI model has estimated that the probability that you have a malignant tumour is:" + risk + "%\n\n" +
                        "\n(AI model accuracy = " + accuracy + "%)";
                break;
            case PROSTATE:
                message = "Based on the data, our AI model has estimated that the probability that you have a malignant tumour is:" + risk + "%\n\n" +
                        "\n(AI model accuracy = " + accuracy + "%)";
                break;
        }

        alert.setTitle("Medi-AI report:");
        alert.setMessage(message);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

}
