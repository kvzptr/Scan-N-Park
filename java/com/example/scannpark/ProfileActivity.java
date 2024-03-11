package com.example.scannpark;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imageViewQRCode;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);

        imageViewQRCode = findViewById(R.id.imageViewQRCode);
        TextView textViewInformation = findViewById(R.id.textViewInformation);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        String licenseNo = getIntent().getStringExtra("LICENSE_NO").toUpperCase();


        mDatabase.orderByChild("LICENSE_NO").equalTo(licenseNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        String lastname = userSnapshot.child("LASTNAME").getValue(String.class);
                        String firstname = userSnapshot.child("FIRSTNAME").getValue(String.class);
                        String middlename = userSnapshot.child("MIDDLENAME").getValue(String.class);
                        String address = userSnapshot.child("ADDRESS").getValue(String.class);
                        String vehicle = userSnapshot.child("VEHICLE").getValue(String.class);
                        String plateNo = userSnapshot.child("PLATE_NO").getValue(String.class);


                        String qrData = "Name: " + lastname + ", " + firstname + " " + middlename +
                                "\nAddress: " + address + "\nVehicle: " + vehicle +
                                "\nLicense No: " + licenseNo + "\nPlate No: " + plateNo;

                        Bitmap generatedQRCode = generateTransparentQRCode(qrData);


                        if (generatedQRCode != null) {
                            imageViewQRCode.setImageBitmap(generatedQRCode);
                        }

                        String informationText = "Name: " + lastname + ", " + firstname + " " + middlename +
                                "\n\nAddress: " + address + "\n\nVehicle: " + vehicle +
                                "\n\nLicense No: " + licenseNo + "\n\nPlate No: " + plateNo;
                        textViewInformation.setText(informationText);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Bitmap generateTransparentQRCode(String data) {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 1312, 1312);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.TRANSPARENT);
            }
        }

        return bmp;
    }
}
