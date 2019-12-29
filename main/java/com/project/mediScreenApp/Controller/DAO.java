package com.project.mediScreenApp.Controller;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.project.mediScreenApp.model.*;
import com.project.mediScreenApp.view.*;
import org.json.*;
import java.util.*;

import static com.project.mediScreenApp.view.MedicalDetails.*;


// This class contains the many methods used by the app to post and receive data to and from the various server-side PHP scripts which
// make up the app's 'mobile API' on the server. These scripts, in turn interact with the server database and the Medi-AI Flask service and
// return the results back to the app. Android's Volley library is used to make the http POST requests and to parse the responses.
// The Volley requests are asynchronous and therefore require that many of the app's calling activities have callback methods to receive and act on the
// data passed back.

public class DAO {

    // These variables need class scope in order to be accessible by inner classes used in the various Volley requests below.
    private  String email, password;
    private  static Map<Integer, Doctor> doctors;
    private  static Map<Integer, InsurancePro> insurancePros;
    private  Map<String, String> diseaseData;
    private  AppCompatActivity sender;
    private  Patient patient;
    private  int doc_id = 0, prof_id = 0, diseaseType;


    //******************************************************************************************************************
    // Get all the doctors in the database.
    //*******************************************************************************************************************
    public DAO(AppCompatActivity _sender){
        ServerURLs.init();
        sender = _sender;
    }
    

    public  void getDoctors(){

        doctors = new HashMap<Integer,Doctor>();
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.GET_PROFESSIONALS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("none found")){
                            msg("No doctors found!");
                        }
                        else {
                            try {
                                JSONArray arr = new JSONArray(response);
                                JSONObject details ;
                                for (int i = 0; i < arr.length(); i ++){
                                    details = arr.getJSONObject(i);
                                    Doctor d = new Doctor(details.getInt("ID"),
                                            details.getString("FNAME"),
                                            details.getString("LNAME"),
                                            details.getString("EMAIL"),
                                            details.getString("PWORD"),
                                            details.getString("PHONE"));
                                    doctors.put(d.getId(),d);
                                }
                                returnDoctors();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("doctors", "doctors");
                return params;
            }
        };
        myRequestQueue.add(postRequest);
    }

    private  void returnDoctors(){
        if (sender instanceof ListDoctors){
            ((ListDoctors)sender).listDoctors(doctors);
        }
        else if (sender instanceof SelectDoctor) {
            ((SelectDoctor) sender).listDoctors(doctors);
        }
    }

    public  Doctor getDocByID(int id){
        return doctors.get(id);
    }
    //**********************************************************************************************************************************************



    //********************************************************************************************************************
    // Get all the insurance professionals in the database.
    //**********************************************************************************************************
    public  void getInsurancePros(){

        insurancePros = new HashMap<Integer,InsurancePro>();
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.GET_PROFESSIONALS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("none found")){
                            msg("No insurance pros found!");
                        }
                        else {
                            try {
                                JSONArray arr = new JSONArray(response);
                                JSONObject details ;
                                for (int i = 0; i < arr.length(); i ++){
                                    details = arr.getJSONObject(i);
                                    InsurancePro p = new InsurancePro(details.getInt("ID"),
                                            details.getString("FNAME"),
                                            details.getString("LNAME"),
                                            details.getString("EMAIL"),
                                            details.getString("PWORD"),
                                            details.getString("PHONE"));
                                    insurancePros.put(p.getId(),p);
                                }
                                returnInsurancePros();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("insurance_professionals", "insurance_professionals");
                return params;
            }
        };
        myRequestQueue.add(postRequest);
    }

    public  void returnInsurancePros(){
        ((SelectInsurancePro) sender).listInsurancePros(insurancePros);
    }

    public  InsurancePro getProByID(int id){
        return insurancePros.get(id);
    }
    //***************************************************************************************************************************



    //**************************************************************************************************************
    // Get patient by email and password...log-in if found, register if not found.
    //***************************************************************************************************************
    public  void getPatient(String _email, String _password) {

        email = _email; password = _password;
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.GET_PATIENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("not found")){
                            register();
                        }
                        else {
                            try {
                                JSONArray array = new JSONArray(response);
                                JSONObject details = array.getJSONObject(0);
                                Patient patient = new Patient(
                                        details.getInt("ID"),
                                        details.getString("FNAME"),
                                        details.getString("LNAME"),
                                        details.getString("EMAIL"),
                                        details.getString("PWORD"),
                                        details.getString("PHONE"),
                                        details.getString("DOB"),
                                        details.getString("GENDER"),
                                        details.getString("APP_RATING"));
                                login(patient);
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return getPatientParams();
            }
        };
        myRequestQueue.add(postRequest);
    }
    private  Map<String, String> getPatientParams(){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password",password);
        return params;
    }
    private  void register(){
        ((LoginRegister) sender).register();
    }
    private  void login(Patient p){
        ((LoginRegister) sender).login(p);
    }
    //*******************************************************************************************************************************



    //*************************************************************************************************************************************
    // Insert new patient to database (and return ID).
    //*********************************************************************************************************************************
    public  void putPatient(Patient _patient){
        patient = _patient;
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.CREATE_NEW_PATIENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        if (r.equals("insertion error")){
                            msg("Error with database!");
                        }
                        else {
                            registerSuccess(Integer.parseInt(r));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return patientPutParams();
            }
        };
        myRequestQueue.add(postRequest);
    }
    private  Map<String,String> patientPutParams(){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("fname", patient.getFirstName());
        params.put("lname", patient.getLastName());
        params.put("email", patient.getEmail());
        params.put("pword", patient.getPassword());
        params.put("phone", patient.getPhone());
        params.put("dob", patient.getDateofBirth());
        params.put("gender", patient.getGender());
        return params;
    }

    private  void registerSuccess(int id){
        ((RegistrationConfirm) sender).registerSuccess(id);
    }
    //**************************************************************************************************************************************



    //*************************************************************************************************************************
    // Insert an new patient/doctor relationship to database.
    //***********************************************************************************************************************
    public  void setPatientDoc(Patient _patient, int _doc_id){
        patient = _patient;

        doc_id = _doc_id;
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.PUT_PATIENT_PROF_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        if (r.equals("insertion error")){
                            msg("Error with database!");
                        }
                        else {
                            ((MainScreen)sender).doctorUpdated();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return putPatientDocParams();
            }
        };
        myRequestQueue.add(postRequest);
    }

    private  Map<String, String> putPatientDocParams(){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("patient_id",String.valueOf(patient.getId()));
        params.put("prof_id",String.valueOf(doc_id));
        params.put("profession","doctor");
        return params;
    }
    //**************************************************************************************************************************************




    //*****************************************************************************************************************
    // Get patient's doctor from database.
    //********************************************************************************************************
    public  void getPatientDoc(Patient _patient){

        patient = _patient; // The patient
          // The activity object.
      //  profType = _profType; // The profession type we want.

        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.GET_PATIENT_PROF_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        System.out.println(r);
                        if (r.equals("none found")){
                            returnDoctor(null); // Pass nothing back to callback function below.
                        }
                        else {
                            try {
                                    JSONArray array = new JSONArray(response);
                                    JSONObject details = array.getJSONObject(0);
                                    Doctor   d = new Doctor(details.getInt("ID"),
                                            details.getString("FNAME"),
                                            details.getString("LNAME"),
                                            details.getString("EMAIL"),
                                            details.getString("PWORD"),
                                            details.getString("PHONE"));

                                    returnDoctor(d);  // Pass it to the callback function below.

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return getPatientDocParams();
            }
        };
        myRequestQueue.add(postRequest);
    }
    private  Map<String, String> getPatientDocParams(){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("patient_id","" + patient.getId());
        params.put("profession","doctor");
        return params;
    }
    private  void returnDoctor(Doctor d){
        ((MainScreen) sender).initDoctor(d);
    }

    //********************************************************************************************************************

    //*************************************************************************************************************************
    // Insert an new patient/insurance pro relationship to database.
    //***********************************************************************************************************************
    public void setPatientPro(Patient _patient, int _prof_id){
        patient = _patient;

        prof_id = _prof_id;
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.PUT_PATIENT_PROF_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        if (r.equals("insertion error")){
                            msg("Error with database!");
                        }
                        else {
                            ((MainScreen)sender).insuranceProUpdated();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return putPatientProParams();
            }
        };
        myRequestQueue.add(postRequest);
    }

    private  Map<String, String> putPatientProParams(){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("patient_id",String.valueOf(patient.getId()));
        params.put("prof_id",String.valueOf(prof_id));
        params.put("profession","insurance");
        return params;
    }
    //**************************************************************************************************************************************



    //*****************************************************************************************************************
    // Get patient's insurance pro from database.
    //********************************************************************************************************
    public  void getPatientPro(Patient _patient){

        patient = _patient; // The patient
          // The activity object.

        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.GET_PATIENT_PROF_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        System.out.println(r);
                        if (r.equals("none found")){
                            returnPro(null); // Pass nothing back to callback function below.
                        }
                        else {
                            try {
                                JSONArray array = new JSONArray(response);
                                JSONObject details = array.getJSONObject(0);
                                InsurancePro   p = new InsurancePro  (details.getInt("ID"),
                                        details.getString("FNAME"),
                                        details.getString("LNAME"),
                                        details.getString("EMAIL"),
                                        details.getString("PWORD"),
                                        details.getString("PHONE"));

                                returnPro(p);  // Pass it to the callback function below.

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return getPatientProParams();
            }
        };
        myRequestQueue.add(postRequest);
    }
    private  Map<String, String> getPatientProParams(){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("patient_id","" + patient.getId());
        params.put("profession","insurance");
        return params;
    }
    private  void returnPro(InsurancePro p){
        ((MainScreen) sender).initInsurancePro(p);
    }

    //********************************************************************************************************************

    //*************************************************************************************************************************
    // Insert an new patient diabetes data relationship to database.
    //***********************************************************************************************************************
    public void getDiseaseData(Patient _patient,  int _diseaseType){
        patient = _patient;
        String URL = ""; String message = "";
        diseaseType = _diseaseType;
        switch (diseaseType){
            case DIABETES:
               URL = ServerURLs.GET_DIABETES_DATA;
               break;
            case HEART_DISEASE:
                URL = ServerURLs.GET_HEART_DATA;
                break;
            case CANCER:
                URL = ServerURLs.GET_CANCER_DATA;
                break;
            case PROSTATE:
                URL = ServerURLs.GET_PROSTATE_DATA;
                break;
        }
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();

                            ArrayList<String> fields;
                            try {
                                if (!r.equals("not found")) {
                                    response = response.replace("[", "");
                                    response = response.replace("]", "");
                                    String[] x = response.split(",");
                                    fields = new ArrayList<String>();

                                    for (String y : x) {
                                        fields.add(y);
                                    }
                                }
                                else fields = null;
                                switch (diseaseType){
                                    case DIABETES:
                                        ((MedicalDetails) sender).initDiabetes(fields);
                                        break;
                                    case HEART_DISEASE:
                                        ((MedicalDetails) sender).initHeart(fields);
                                        break;
                                    case CANCER:
                                        ((MedicalDetails) sender).initBreastCancer(fields);
                                        break;
                                    case PROSTATE:
                                        ((MedicalDetails) sender).initProstate(fields);
                                        break;
                                }

                            }
                            catch(Exception e){
                                System.out.println("XXXXXXXX ERROR XXXXXXXX");
                            }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("patient_id",String.valueOf(patient.getId()));
                return params;
                //return postID_get();
            }
        };
        myRequestQueue.add(postRequest);
    }

//    private  Map<String, String> postID_gety(){
//        Map<String, String>  params = new HashMap<String, String>();
//        params.put("patient_id",String.valueOf(patient.getId()));
//        return params;
//    }
    //**************************************************************************************************************************************


    //*************************************************************************************************************************
    // Insert an new patient disease relationship to database.
    //***********************************************************************************************************************
    public  void putDiseaseData( int _diseaseType, Map data){

        String URL = "";
        diseaseType = _diseaseType;
        diseaseData = data;

        switch (diseaseType){
            case DIABETES:
                URL = ServerURLs.PUT_DIABETES_DATA;
                break;
            case HEART_DISEASE:
                URL = ServerURLs.PUT_HEART_DATA;
                break;
            case CANCER:
                URL = ServerURLs.PUT_CANCER_DATA;
                break;
            case PROSTATE:
                URL = ServerURLs.PUT_PROSTATE_DATA;
                break;
        }
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        if (r.equals("insertion error")){
                            msg("Database Error!");
                        }
                        else {
                            msg("Data saved!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return diseaseData;

            }
        };
        myRequestQueue.add(postRequest);
    }

    //**************************************************************************************************************************************

    public void getAI_Risk(Map<String, String> data, int _diseaseType){
        diseaseType = _diseaseType;
        diseaseData = data;
        String URL = "";

        switch (diseaseType){
            case DIABETES:
                URL = ServerURLs.GET_DIABETES_RISK;
                break;
            case HEART_DISEASE:
                URL = ServerURLs.GET_HEART_RISK;
                break;
            case CANCER:
                URL = ServerURLs.GET_CANCER_RISK;
                break;
            case PROSTATE:
                URL = ServerURLs.GET_PROSTATE_RISK;
                break;
        }
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        String[] results = r.split(",");
                        double risk = Double.parseDouble(results[0]);
                        int boolRisk = Integer.parseInt(results[1]);
                        double accuracy =  Double.parseDouble(results[2]);
                        ((MedicalDetails)sender).show_AI_results(risk, accuracy, boolRisk);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return diseaseData;

            }
        };
        myRequestQueue.add(postRequest);

    }


    public void getPatient_Prof_Report(Patient _patient){
        patient = _patient; // The patient
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.GET_PATIENT_PROF_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        System.out.println(r);
                        if (r.equals("none found")){
                            msg("You are not associated with any insurance professional yet!");
                        }
                        else {
                            ((MainScreen)sender).showProfReport(r);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return getPatientProParams();
            }
        };
        myRequestQueue.add(postRequest);
    }

    public void getPatient_Doc_Report(Patient _patient){
        patient = _patient;


        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.GET_PATIENT_PROF_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        System.out.println(r);
                        if (r.equals("none found")){
                            msg("You are not associated with any doctor yet!");
                        }
                        else {
                            ((MainScreen)sender).showDocReport(r);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return getPatientDocParams();
            }
        };
        myRequestQueue.add(postRequest);
    }

    public void updatePatient_Rating(Patient _patient, String _rating){
        final String rating  = _rating;
        patient = _patient;
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.PUT_APP_RATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        System.out.println(r);
                        if (r.equals("not found")){
                            msg("No record of patient!");
                        }
                        else  {
                            msg("Patient app rating updated!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("patient_id",String.valueOf(patient.getId()));
                params.put("rating", rating );
                return params;
            }
        };
        myRequestQueue.add(postRequest);
    }

    public void updatePatient_Message(Patient _patient, String _message, String  _profession){
        final String message  = _message;
        final String profession = _profession;
        patient = _patient;
        RequestQueue myRequestQueue = Volley.newRequestQueue(sender);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ServerURLs.PUT_PATIENT_MSG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response.trim();
                        System.out.println(r);
                        if (r.equals("not found")){
                            msg("No record of patient!");
                        }
                        else  {
                            msg("Patient message updated!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error with server!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("patient_id", String.valueOf(patient.getId()));
                params.put("message", message);
                params.put("profession", profession );
                return params;
            }
        };
        myRequestQueue.add(postRequest);
    }


    private  void msg(String message){
        Toast.makeText(sender.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }




}
