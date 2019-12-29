package com.project.mediScreenApp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.project.mediScreenApp.Controller.DAO;
import com.project.mediScreenApp.R;
import com.project.mediScreenApp.model.InsurancePro;

import java.util.*;


// This activity lists out the names of all insurance professionals  in the database, so the user can select one.
// The selected professional's ID is then passed back to the calling activity.
// A RecyclerView, RecyclerViewAdapter and itemClickListener are used for the listing and selection.

public class SelectInsurancePro extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    private MyRecyclerViewAdapter adapter; // A map to store professionals. The integer key is the pro's ID.
    private Map<Integer, InsurancePro> insurancePros; // The adapter for the RecyclerView.
    private DAO DAO; // The database access object.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_insurance_pros);
        this.setTitle("Insurance Professionals");
        Button btnBack = findViewById(R.id.btnKill);
        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View btn) {finish();}
        });


        // Asynchronously requesting all pro's from database.
        // A callback method below called 'listInsurancePros()' handles the response from the DAO.
        DAO = new DAO(this);
        DAO.getInsurancePros();
    }

    // Handling a selection. The selected pro's id is bundled back to calling activity.
    @Override
    public void onItemClick(View view, int position) {
        String name = adapter.getItem(position);
        int id = getKey(name);
        Toast.makeText(this, "You selected " + name + ", id =  " + getKey(name), Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("proID", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    // A helper method to get the id of the pro. It is the key value of the 'insurancePros' hashMap.
    public  Integer getKey( String value) {
        for (Map.Entry<Integer, InsurancePro> entry : insurancePros.entrySet()) {
            String name = ((InsurancePro)entry.getValue()).getFirstName() +" " +  ((InsurancePro)entry.getValue()).getLastName();
            if (name.equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // The DAO calls this method back, passing a Map of insurance pros. The pro's names are then listed as selectable RecyclerView adapter items.
    public void listInsurancePros(Map<Integer, InsurancePro> _insurancePros){
        ArrayList<String> names = new ArrayList<>();
        insurancePros = _insurancePros;
        Iterator it = insurancePros.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String name = ((InsurancePro)pair.getValue()).getFirstName() +" " +  ((InsurancePro)pair.getValue()).getLastName();
            names.add(name);
        }
        RecyclerView recyclerView = findViewById(R.id.prosRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, names);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

}
