package com.project.mediScreenApp.view;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import com.project.mediScreenApp.Controller.ServerURLs;
import com.project.mediScreenApp.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.net.MalformedURLException;
import java.net.URL;

// This is the launcher activity of the app and the first screen the user sees.
// It is a landing screen which prompts the user to proceed to the log-in/register activity.
// It also allows the anonymous user go to an activity where they can browse participating doctors before they decide whether to sign up.
// It also has an action menu item, which displays an input dialog that allows the URL of the central database to be entered manually.

public class StartScreen extends AppCompatActivity {

    // Declaring widget references, etc....
    private TextView textView;
    protected Toolbar toolbar;
    private ImageView logo;
    private Button btnCheck;

    private Intent login;


    // Initialising the widgets, etc....
    private void initWidgets(){
        setContentView(R.layout.activity_start_screen);
        logo  = findViewById(R.id.logo);
        textView = findViewById(R.id.tv1);
        toolbar = findViewById(R.id.toolbar);
        btnCheck = findViewById(R.id.btnCheck);

        login = new Intent(this, LoginRegister.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initWidgets();
            textView.setText("Welcome to Medi-Screen!\nTap the logo below to\nlog in/sign up, or the button\nunderneath to check for\nparticipaing GPs.");
            toolbar.setTitle("Patient Medi-Screen");
            setSupportActionBar(toolbar);

            // Setting the handlers for the buttons....
            logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View btn) {
                   startActivity(login);
            }
        });
            btnCheck.setOnClickListener(new View.OnClickListener(){
                public void onClick(View btn) {
                    listDocs();}
            });
    }



    // Starts the 'ListDoctors' activity described above.
    private void listDocs(){
            Intent intent = new Intent(this, ListDoctors.class);
            startActivity(intent);
    }


    // Inflating the menu for this activity.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu_server, menu);
        return true;
    }


    // This menu item click-handler displays a pop-up allowing the user to manually enter the URL address of the central database.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.mi_enterurl){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            edittext.setText("http://");
            alert.setTitle("ServerURLs URL");
            alert.setMessage("Enter the URL: ");
            alert.setView(edittext);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    getUrl(edittext.getText().toString());
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

    // A helper method for the above method which sets the server URL for the application (see ServerURLs class).
    private void getUrl(String url){
        try {
            URL u = new URL(url);
            ServerURLs.URL = url;
            Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            Toast.makeText(this, "Bad URL! Try again.", Toast.LENGTH_SHORT).show();
        }


    }

}
