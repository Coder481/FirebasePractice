package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        //addDataToFirebase();
        setOptionsMerge();
        //getData();
    }

    private void getData() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                Toast.makeText(MainActivity.this, document.getId()+" -> "+document.getData(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // Using nested data and OptionsMerge
    private void setOptionsMerge() {
        // Create a document with name user
        Map<String , Object> user = new HashMap<>();
        user.put("String","Hello");
        user.put("Number",20.2);
        user.put("Boolean",true);
        user.put("Time Stamp",new Timestamp(new Date()));

        Map<String,Integer> map = new HashMap<>();
        map.put("a",1);
        map.put("b",2);
        user.put("Nested Map",map);

        Toast.makeText(this, "addData", Toast.LENGTH_SHORT).show();
        // Create a new collection of documents with automatically generated ID
        db.collection("Datatypes").document("dataTypes")
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDataToFirebase() {


        // Create a document with name user
        Map<String , Object> user = new HashMap<>();
        user.put("First Name","Hrithik");
        user.put("Last Name","Sharma");
        user.put("Age",20);
        //user.put("Born",2000);

        Toast.makeText(this, "addData", Toast.LENGTH_SHORT).show();
        // Create a new collection of documents with automatically generated ID
        db.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, documentReference.getId()+"", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}