package com.project.mediScreenApp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.Doctor;

import java.util.*;

// This activity lists out the names of all doctors in the database, so that an anonymous user can see if their doctor
// is participating in the service. A RecyclerView and a RecyclerViewAdapter are used to create the list.

public class ListDoctors extends AppCompatActivity {

    private DAO DAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_doctors);
        this.setTitle("Participating Doctors");
        Button btnBack = findViewById(R.id.btnKill);
        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View btn) {finish();}
        });

        // Asynchronously requesting all doctors from database.
        // The callback method below handles the response from the DAO.
        DAO = new DAO(this);
        DAO.getDoctors();
    }

    // The DAO calls this method back, passing a Map of doctors. The doctors names are then listed as RecyclerView adapter items.
    public void listDoctors(Map <Integer, Doctor> doctors){
        ArrayList<String> docNames = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.docRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Iterator it = doctors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String name = ((Doctor)pair.getValue()).getFirstName() +" " +  ((Doctor)pair.getValue()).getLastName();
            docNames.add(name);
        }
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, docNames);
        recyclerView.setAdapter(adapter);
    }


}
