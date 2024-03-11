package com.example.scannpark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText lastnameEditText;
    private EditText firstnameEditText;
    private EditText middlenameEditText;
    private EditText addressEditText;
    private Spinner vehicleSpinner;
    private EditText plateNoEditText;
    private EditText licenseNoEditText;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeractivity);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        lastnameEditText = findViewById(R.id.lastnameEditText);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        middlenameEditText = findViewById(R.id.middlenameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        vehicleSpinner = findViewById(R.id.vehicleSpinner);
        plateNoEditText = findViewById(R.id.plateNoEditText);
        licenseNoEditText = findViewById(R.id.licenseNoEditText);
        Button submitButton = findViewById(R.id.submitButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_array, android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        vehicleSpinner.setAdapter(adapter);

        submitButton.setOnClickListener(v -> {
            String lastname = lastnameEditText.getText().toString().toUpperCase();
            String firstname = firstnameEditText.getText().toString().toUpperCase();
            String middlename = middlenameEditText.getText().toString().toUpperCase();
            String address = addressEditText.getText().toString().toUpperCase();
            String selectedVehicle = vehicleSpinner.getSelectedItem().toString();
            String plateNo = plateNoEditText.getText().toString().toUpperCase();
            String licenseNo = licenseNoEditText.getText().toString().toUpperCase();

            if (lastname.isEmpty() || firstname.isEmpty() || address.isEmpty() || selectedVehicle.isEmpty() || plateNo.isEmpty() || licenseNo.isEmpty()) {

                Toast.makeText(RegisterActivity.this, "Please fill out all required fields.", Toast.LENGTH_SHORT).show();
            } else {
                checkExistingUser(lastname, firstname, middlename, address, selectedVehicle, plateNo, licenseNo);
            }
        });
    }

    private void checkExistingUser(String lastname, String firstname, String middlename, String address, String selectedVehicle, String plateNo, String licenseNo) {
        Query query = mDatabase.orderByChild("LASTNAME").equalTo(lastname);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Toast.makeText(RegisterActivity.this, "User already registered!", Toast.LENGTH_SHORT).show();

                } else {

                    storeUserData(lastname, firstname, middlename, address, selectedVehicle, plateNo, licenseNo);

                    Intent intent = new Intent(RegisterActivity.this, QRCodeActivity.class);
                    intent.putExtra("LASTNAME", lastname);
                    intent.putExtra("FIRSTNAME", firstname);
                    intent.putExtra("MIDDLENAME", middlename);
                    intent.putExtra("ADDRESS", address);
                    intent.putExtra("VEHICLE", selectedVehicle);
                    intent.putExtra("PLATE_NO", plateNo);
                    intent.putExtra("LICENSE_NO", licenseNo);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserData(String lastname, String firstname, String middlename, String address, String selectedVehicle, String plateNo, String licenseNo) {
        String userId = mDatabase.push().getKey();

        mDatabase.child(userId).child("LASTNAME").setValue(lastname);
        mDatabase.child(userId).child("FIRSTNAME").setValue(firstname);
        mDatabase.child(userId).child("MIDDLENAME").setValue(middlename);
        mDatabase.child(userId).child("ADDRESS").setValue(address);
        mDatabase.child(userId).child("VEHICLE").setValue(selectedVehicle);
        mDatabase.child(userId).child("PLATE_NO").setValue(plateNo);
        mDatabase.child(userId).child("LICENSE_NO").setValue(licenseNo);
    }
}