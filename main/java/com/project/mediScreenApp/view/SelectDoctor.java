package com.project.mediScreenApp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.project.mediScreenApp.model.Doctor;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import java.util.*;

// This activity lists out the names of all doctors in the database, so the user can select one.
// The selected doctor's ID is then passed back to the calling activity.
// A RecyclerView, RecyclerViewAdapter and itemClickListener are used for the listing and selection.

public class SelectDoctor extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    private Map<Integer, Doctor> doctors;  // A map to store doctors. The integer key is the doc's ID.
    private MyRecyclerViewAdapter adapter;  // The adapter for the RecyclerView.
    private DAO DAO;    // The database access object.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);
        this.setTitle("Select your doctor:");
        Button btnBack = findViewById(R.id.btnKill);
        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View btn) {finish();}
        });

        // Asynchronously requesting all doctors from database.
        // A callback method below called 'listDoctors()' handles the response from the DAO.
        DAO = new DAO(this);
        DAO.getDoctors();
    }


    // Handling a selection. The selected doctor id is bundled back to calling activity.
    @Override
    public void onItemClick(View view, int position) {
        String name = adapter.getItem(position);
        int id = getKey(name);
        Intent intent = new Intent();
        intent.putExtra("docID", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    // A helper method to get the id of the doctor. It is the key value of the 'doctors' hashMap.
    private  Integer getKey( String value) {
        for (Map.Entry<Integer, Doctor> entry : doctors.entrySet()) {
            String name = ((Doctor)entry.getValue()).getFirstName() +" " +  ((Doctor)entry.getValue()).getLastName();
            if (name.equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // The DAO calls this method back, passing a Map of doctors. The doctors names are then listed as selectable RecyclerView adapter items.
    public void listDoctors(Map <Integer, Doctor> _doctors){
        ArrayList<String> docNames = new ArrayList<>();
        doctors = _doctors;
        for (Map.Entry<Integer, Doctor> entry : doctors.entrySet()) {
            String name = ((Doctor)entry.getValue()).getFirstName() +" " +  ((Doctor)entry.getValue()).getLastName();
            docNames.add(name);
        }
        RecyclerView recyclerView = findViewById(R.id.docRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, docNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }
}
