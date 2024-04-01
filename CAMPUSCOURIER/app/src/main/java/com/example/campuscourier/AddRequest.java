package com.example.campuscourier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddRequest extends AppCompatActivity {

    TextInputEditText inputItem, inputDescription;
    DatePicker datePicker;
    TimePicker timePicker;
    Spinner inputLocation, inputUrgency;
    Button buttonAddImage, buttonRemoveImage, buttonPost;
    ImageView imageView;
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        inputItem = findViewById(R.id.inputItem);
        inputDescription = findViewById(R.id.inputDescription);
        buttonAddImage = findViewById(R.id.buttonAddImage);
        buttonRemoveImage = findViewById(R.id.buttonRemoveImage);
        imageView = findViewById(R.id.image);
        datePicker = findViewById(R.id.date);
        timePicker = findViewById(R.id.time);
        timePicker.setIs24HourView(true);
        inputLocation = findViewById(R.id.inputLocation);
        inputUrgency = findViewById(R.id.inputUrgency);
        buttonPost = findViewById(R.id.buttonPost);

        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }});

        buttonRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != selectedImageUri) {
                    imageView.setImageDrawable(null);
                }
            }
        });

        ArrayAdapter<CharSequence> adapterLocation = ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_item);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_item);
        inputLocation.setAdapter(adapterLocation);

        ArrayAdapter<CharSequence> adapterUrgency = ArrayAdapter.createFromResource(this, R.array.urgency, android.R.layout.simple_spinner_item);
        adapterUrgency.setDropDownViewResource(android.R.layout.simple_spinner_item);
        inputUrgency.setAdapter(adapterUrgency);

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item;
                String description = "";
                String imageStorageUri = "";
                String date;
                String time;
                String location;
                String urgency;
                String status = "Not accepted";
                item = String.valueOf(inputItem.getText());
                description = String.valueOf(inputDescription.getText());
                date = datePicker.getYear()+format(datePicker.getMonth())+format(datePicker.getDayOfMonth());
                time = format(timePicker.getHour())+format(timePicker.getMinute());
                location = String.valueOf(inputLocation.getSelectedItem());
                urgency = String.valueOf(inputUrgency.getSelectedItem());

                if (TextUtils.isEmpty(item)){
                    Toast.makeText(getApplicationContext(), "Enter item", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (null != selectedImageUri) {
                    // Get the data from an ImageView as bytes
                    String imageUri = String.valueOf(selectedImageUri);
                    StorageReference imageRef = storageRef.child(userId).child(imageUri);
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    imageRef.putBytes(data);
                    imageStorageUri = String.valueOf(imageRef);
                }
                addDataToFirestore(item, description, imageStorageUri, date, time, location, urgency, status, userId);
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addDataToFirestore(String item, String description, String imageStorageUri, String date, String time, String location, String urgency, String status, String userId) {

        // adding our data to our courses object class.
        Requests r = new Requests(item, description, imageStorageUri, date, time, location, urgency, status, userId);

        // below method is use to add data to Firebase Firestore.
        db.collection("users").document(userId).collection("posts").add(r).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(getApplicationContext(), "Request created.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(getApplicationContext(), "Request not created", Toast.LENGTH_SHORT).show();
            }
        });
        db.collection("posts").add(r).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("NEW REQUEST", "request stored");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("NEW REQUEST", "request not stored", e);
            }
        });
    }

    void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true));
                }
            }
        }
    }

    public String format(int x){
        if (String.valueOf(x).length()==1){
            return "0"+x;
        }
        return String.valueOf(x);
    }
}