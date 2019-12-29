package com.project.mediScreenApp.Controller;


// This class stores the URL addresses of the various server-side PHP scripts to which the DAO class
// posts requests so that it can interact with the database and the Medi-AI service.

public class ServerURLs {
    public static String URL = "http://localhost:5000/";
    //"http://dylanolney.serveo.net";
    //"http://192.168.0.129/";

    public static String GET_PROFESSIONALS_URL;

    public static String GET_PATIENT_URL ;
    public static String CREATE_NEW_PATIENT_URL ;
    public static String GET_PATIENT_PROF_URL ;
    public static String PUT_PATIENT_PROF_URL ;
    public static String GET_PATIENT_PROF_REPORT;

    public static String GET_DIABETES_DATA ;
    public static String PUT_DIABETES_DATA ;
    public static String GET_DIABETES_RISK ;

    public static String GET_HEART_DATA ;
    public static String PUT_HEART_DATA ;
    public static String GET_HEART_RISK ;

    public static String GET_CANCER_DATA ;
    public static String PUT_CANCER_DATA ;
    public static String GET_CANCER_RISK;

    public static String GET_PROSTATE_DATA ;
    public static String PUT_PROSTATE_DATA ;
    public static String GET_PROSTATE_RISK;

   // public static String GET_APP_RATING;
    public static String PUT_APP_RATING;
    public static String PUT_PATIENT_MSG;

    public static void init(){

        GET_PROFESSIONALS_URL =  URL + "/MediScreenWeb/mobileAPI/getProfessionals.php";

        GET_PATIENT_URL =        URL + "/MediScreenWeb/mobileAPI/patient/getPatient.php";
        CREATE_NEW_PATIENT_URL = URL + "/MediScreenWeb/mobileAPI/patient/createNewPatient.php";
        GET_PATIENT_PROF_URL =   URL + "/MediScreenWeb/mobileAPI/patient/getPatientProf.php";
        PUT_PATIENT_PROF_URL =   URL + "/MediScreenWeb/mobileAPI/patient/putPatientProf.php";
        GET_PATIENT_PROF_REPORT = URL + "/MediScreenWeb/mobileAPI/patient/getProfReport.php";

        GET_DIABETES_DATA =     URL + "/MediScreenWeb/mobileAPI/diabetes/getPatientDiabetes.php";
        PUT_DIABETES_DATA =     URL + "/MediScreenWeb/mobileAPI/diabetes/putPatientDiabetes.php";
        GET_DIABETES_RISK =     URL + "/MediScreenWeb/mobileAPI/diabetes/getDiabetesRisk.php";

        GET_HEART_DATA =        URL + "/MediScreenWeb/mobileAPI/heartDisease/getPatientHeart.php";
        PUT_HEART_DATA =        URL + "/MediScreenWeb/mobileAPI/heartDisease/putPatientHeart.php";
        GET_HEART_RISK =        URL + "/MediScreenWeb/mobileAPI/heartDisease/getHeartRisk.php";

        GET_CANCER_DATA =       URL + "/MediScreenWeb/mobileAPI/breastCancer/getPatientCancer.php";
        PUT_CANCER_DATA =       URL + "/MediScreenWeb/mobileAPI/breastCancer/putPatientCancer.php";
        GET_CANCER_RISK =       URL + "/MediScreenWeb/mobileAPI/breastCancer/getCancerRisk.php";

        GET_PROSTATE_DATA =     URL + "/MediScreenWeb/mobileAPI/prostateCancer/getPatientProstate.php";
        PUT_PROSTATE_DATA =     URL + "/MediScreenWeb/mobileAPI/prostateCancer/putPatientProstate.php";
        GET_PROSTATE_RISK =     URL + "/MediScreenWeb/mobileAPI/prostateCancer/getProstateRisk.php";

        //GET_APP_RATING =        URL + "/MediScreenWeb/mobileAPI/patient/getAppReview.php";
        PUT_APP_RATING =        URL + "/MediScreenWeb/mobileAPI/patient/putAppReview.php";
        PUT_PATIENT_MSG =       URL + "/MediScreenWeb/mobileAPI/patient/putMessage.php";
    }
}
