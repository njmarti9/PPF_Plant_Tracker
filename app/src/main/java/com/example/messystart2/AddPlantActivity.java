package com.example.messystart2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class AddPlantActivity extends AppCompatActivity {

    private EditText dateCutET, datePlantedET, daughterIDET, greenhouseET, motherIDET, ratingET, sequenceET, strainET;
    private Button createPlantBtn;
    private DatabaseReference reff;
    private Plant plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        dateCutET = findViewById(R.id.dateCutET);
        datePlantedET = findViewById(R.id.datePlantedET);
        daughterIDET = findViewById(R.id.daughterIDET);
        greenhouseET = findViewById(R.id.greenhouseET);
        motherIDET = findViewById(R.id.motherIDET);
        ratingET = findViewById(R.id.ratingET);
        sequenceET = findViewById(R.id.sequenceET);
        strainET = findViewById(R.id.strainET);

        createPlantBtn = findViewById(R.id.createPlantBtn);
        plant = new Plant();
        reff = FirebaseDatabase.getInstance().getReference().child("Company Placeholder");

        createPlantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("something", "in create plant listener");
                try {
                    int greenhouseNum = Integer.parseInt(greenhouseET.getText().toString());
                    int ratingNum = Integer.parseInt(ratingET.getText().toString());

                    if (greenhouseNum < 1 || greenhouseNum > 10 || ratingNum < 1 || ratingNum > 5){
                        Toast.makeText(AddPlantActivity.this, "Ratings should be 1-5, and Greenhouses should be 1-10", Toast.LENGTH_LONG).show();
                    }
                    else if (dateCutET.getText().toString().equals("") || datePlantedET.getText().toString().equals("") || daughterIDET.getText().toString().equals("") || greenhouseET.getText().toString().equals("") || motherIDET.getText().toString().equals("") || ratingET.getText().toString().equals("") || sequenceET.getText().toString().equals("") || strainET.getText().toString().equals("")){
                        Toast.makeText(AddPlantActivity.this, "Please Fill in all fields", Toast.LENGTH_LONG).show();
                    }
                    else{
                        String dID = daughterIDET.getText().toString();
                        String[] daughterID = dID.split("[ ,]+");
                        List<String> dList = Arrays.asList(daughterID);


                        plant.setDateCut(dateCutET.getText().toString().trim());
                        plant.setDatePlanted(datePlantedET.getText().toString().trim());
                        plant.setDaughterID(dList);
                        plant.setGreenhouse(greenhouseET.getText().toString().trim());
                        plant.setMotherID(motherIDET.getText().toString().trim());
                        plant.setRating(ratingET.getText().toString().trim());
                        plant.setSequence(sequenceET.getText().toString().trim());
                        plant.setStrain(strainET.getText().toString().trim());

                        Log.d("something", "about to push data");

                        reff.child("Greenhouse_"+ greenhouseET.getText().toString().trim()).child("Plants").push().setValue(plant);

                        reff.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Log.d("something", "datasnapshot = " + dataSnapshot);
                                String[] values = dataSnapshot.getValue().toString().split("[-=]");
                                Log.d("something", values[2]);
                                Toast.makeText(AddPlantActivity.this, "Plant "+ values[2] + " inserted successfully", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Log.d("something", "before intent");

                        Intent intent
                                = new Intent(AddPlantActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                    }
                }
                catch (NumberFormatException e){
                    Log.d("something", e.toString());
                    Toast.makeText(AddPlantActivity.this, "Please enter Numbers for Ratings and Greenhouses", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
