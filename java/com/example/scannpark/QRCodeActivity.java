package com.example.scannpark;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;



public class QRCodeActivity extends AppCompatActivity {

    private ImageView imageViewQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_activity);

        imageViewQRCode = findViewById(R.id.imageViewQRCode);
        TextView textViewInformation = findViewById(R.id.textViewInformation);


        String lastname = getIntent().getStringExtra("LASTNAME").toUpperCase();
        String firstname = getIntent().getStringExtra("FIRSTNAME").toUpperCase();
        String middlename = getIntent().getStringExtra("MIDDLENAME").toUpperCase();
        String address = getIntent().getStringExtra("ADDRESS").toUpperCase();
        String vehicle = getIntent().getStringExtra("VEHICLE");
        String plateNo = getIntent().getStringExtra("PLATE_NO").toUpperCase();
        String licenseNo = getIntent().getStringExtra("LICENSE_NO").toUpperCase();


        String qrData = "Name: " + lastname + ", " + firstname + " " + middlename + "\nAddress: " + address + "\nVehicle: " + vehicle + "\nLicense No: " + licenseNo + "\nPlate No: " + plateNo;


        Bitmap generatedQRCode = generateTransparentQRCode(qrData);


        if (generatedQRCode != null) {
            imageViewQRCode.setImageBitmap(generatedQRCode);
        }
        String informationText = "Name: " + lastname + ", " + firstname + " " + middlename + "\n\nAddress: " + address + "\n\nVehicle: " + vehicle + "\n\nLicense No: " + licenseNo + "\n\nPlate No: " + plateNo;
        textViewInformation.setText(informationText);

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> home());
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
    private void home() {
        startActivity(new Intent(QRCodeActivity.this, HomeActivity.class));
        finish();
    }
}

