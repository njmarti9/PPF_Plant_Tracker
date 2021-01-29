package com.example.messystart2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private TextView searchedPlantInfoView, createAccountTV;
    private Button logoutBtn, goToPlantBtn, searchPlantBtn, deletePlantBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference reff;
    private String dateCut, datePlanted, daughterID, greenhouse, motherID, plantID, rating, sequence, strain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("something", "in main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutBtn = findViewById(R.id.logoutBtn);
        searchedPlantInfoView = findViewById(R.id.searchedPlantInfoView);
        goToPlantBtn = findViewById(R.id.goToPlantBtn);
        searchPlantBtn = findViewById(R.id.searchPlantBtn);
        deletePlantBtn = findViewById(R.id.deletePlantBtn);
        createAccountTV = findViewById(R.id.createAccountTV);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        reff = FirebaseDatabase.getInstance().getReference().child("Company Placeholder");



        // If not signed in change to login screen
        // Else log user's email and stay in main screen
        if (currentUser == null) {
            Intent intent
                    = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        } else {
            Log.d("something", "already signed in " + currentUser.getEmail());
            
        }

        // Check Permissions to see if able to use specific buttons/functions
        currentUser.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String role = (String) getTokenResult.getClaims().get("role");
                if (role.equals("manager")){
                    deletePlantBtn.setVisibility(View.VISIBLE);
                    createAccountTV.setVisibility(View.VISIBLE);
                }
                else {
                    deletePlantBtn.setVisibility(View.INVISIBLE);
                    createAccountTV.setVisibility(View.INVISIBLE);
                }
            }
        });

        createAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(MainActivity.this,
                        SignupActivity.class);
                startActivity(intent);
            }
        });

        // Take to logoutUser() Function
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logoutUser();
            }
        });

        // Set screen to AddPlant Screen
        goToPlantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(MainActivity.this,
                        AddPlantActivity.class);
                startActivity(intent);
            }
        });

        searchPlantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlant();
            }
        });
    }


    private void searchPlant(){
        Log.d("something", "in searchPlant method");

        String[] options = {"Scan For Plant", "Look Through Greenhouses"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How would you like to search for a plant?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.setCaptureActivity(CaptureAct.class);
                    integrator.setOrientationLocked(false);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scanning Code");
                    integrator.initiateScan();
                }
                else if (which == 1){
                    Intent intent
                            = new Intent(MainActivity.this,
                            GreenhouseListView.class);
                    startActivity(intent);
                }
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){

                final String results = "-" + result.getContents();
                Log.d("something", "Content of barcode: " + result.getContents());

                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("something", "In onDataChange");
                        dateCut = dataSnapshot.child("Plants").child(results).child("dateCut").getValue().toString();
                        datePlanted = dataSnapshot.child("Plants").child(results).child("datePlanted").getValue().toString();
                        daughterID = dataSnapshot.child("Plants").child(results).child("daughterID").getValue().toString();
                        greenhouse = dataSnapshot.child("Plants").child(results).child("greenhouse").getValue().toString();
                        motherID = dataSnapshot.child("Plants").child(results).child("motherID").getValue().toString();
                        plantID = dataSnapshot.child("Plants").child(results).child("plantID").getValue().toString();
                        rating = dataSnapshot.child("Plants").child(results).child("rating").getValue().toString();
                        sequence = dataSnapshot.child("Plants").child(results).child("sequence").getValue().toString();
                        strain = dataSnapshot.child("Plants").child(results).child("strain").getValue().toString();

                        searchedPlantInfoView.setText("Date Cut: " + dateCut + "\nDate Planted: " + datePlanted + "\nDaughterID(s): " + daughterID + "\nGreenhouse: " + greenhouse + "\nMotherID: " + motherID + "\nPlantID: " + plantID + "\nRating: " + rating + "\nSequence: " + sequence + "\nStrain: " + strain);

                        Log.d("something", "dateCut: " + dateCut);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("something", "Cancelled");
                    }
                });


            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void logoutUser(){
        Log.d("something", "in logoutUser method");
        mAuth.signOut();

        Toast.makeText(getApplicationContext(),
                "Successfully Logged Out",
                Toast.LENGTH_LONG)
                .show();

        Intent intent
                = new Intent(MainActivity.this,
                LoginActivity.class);
        startActivity(intent);
    }

}
