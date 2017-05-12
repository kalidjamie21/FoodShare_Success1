package com.example.android.foodshare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;


public class Donation extends AppCompatActivity implements View.OnClickListener {

    Button submit, attach_image;
    EditText company_name;
    EditText phone_number;
    EditText location;
    EditText foodType;
    EditText quantity;
    ImageView the_food;

    Uri URI = null;

    String companyNameString;
    String phoneNumberString;
    String locationString;
    String foodTypeString;
    String quantityString;
    String attachmentFile;

    String emailTo = "kalid.mawi@briswieth.com";
    String emailContent;
    String emailSubject;

    static final int PICK_FROM_GALLERY = 101;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    int columnIndex;
    Bitmap imageBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation);

        submit = (Button) findViewById(R.id.bSubmit);
        attach_image = (Button) findViewById(R.id.bAttach);
        the_food = (ImageView) findViewById(R.id.ivFood);
        company_name = (EditText) findViewById(R.id.etCompName);
        phone_number = (EditText) findViewById(R.id.etPhone);
        location = (EditText) findViewById(R.id.etLocation);
        foodType = (EditText) findViewById(R.id.etFoodType);
        quantity = (EditText) findViewById(R.id.etQuantity);
        submit.setOnClickListener(this);
        attach_image.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bAttach:
                openGallery();
                break;

            case R.id.bSubmit:

                try {

                companyNameString = company_name.getText().toString();
                phoneNumberString = phone_number.getText().toString();
                locationString = location.getText().toString();
                foodTypeString = foodType.getText().toString();
                quantityString = quantity.getText().toString();

                emailContent = "You have received a Food Pickup Request. Please find details below:\n\n" +
                        "Name of Organization (or Individual): " + companyNameString + "\n" +
                        "Telephone Number: " + phoneNumberString + "\n" +
                        "Location: " + locationString + "\n" +
                        "Available Food Type: " + foodTypeString + "\n" +
                        "Quantity Available: " + quantityString;

                emailSubject = "New Food Pickup Request From " + companyNameString;

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                if (URI != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                }
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);
                this.startActivity(Intent.createChooser(emailIntent,"Sending email..."));

                } catch (Throwable t) {
                    Toast.makeText(this,
                            "Request failed try again: " + t.toString(),
                            Toast.LENGTH_LONG).show();
                }
                break;

        } // end of switch statement

        };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {

            /**
             * Get Path
             */
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            attachmentFile = cursor.getString(columnIndex);
            Log.e("Attachment Path:", attachmentFile);
            URI = Uri.parse("file://" + attachmentFile);
            cursor.close();

            // Set the Image in ImageView after decoding the String
            //the_food.setImageBitmap(BitmapFactory.decodeFile(attachmentFile));

            // Change thumbnail to selected image
           // Bundle extras = data.getExtras();
            //imageBitmap = (Bitmap) extras.get("data");
            //the_food.setImageBitmap(imageBitmap);

        }
    }
    public void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY);

    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE (
            final Context context){
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

}