package com.example.scannpark;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentVehiclesActivity extends AppCompatActivity {

    private TextView vehiclesCountTextView;
    private TextView registrantsCountTextView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currentvehicles);

        vehiclesCountTextView = findViewById(R.id.vehiclesCountTextView);
        registrantsCountTextView = findViewById(R.id.registrantsCountTextView);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        final Handler handler = new Handler();
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                fetchRegistrantsCount();

                handler.postDelayed(this, 5000); // 5000 milliseconds = 5 seconds
            }
        };
        handler.postDelayed(updateRunnable, 0); // Start immediately
    }

    private void fetchRegistrantsCount() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int registrantsCount = (int) dataSnapshot.getChildrenCount();


                registrantsCountTextView.setText(String.valueOf(registrantsCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
