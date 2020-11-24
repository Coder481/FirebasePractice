package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.firebase.Models.Account;
import com.example.firebase.Models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /** Adding **/
        //addDataToFirebase();
        //setOptionsMerge();
        //addNestedDocument();
        //addingObject();


        /** Getting Data**/
        //getData();
        //getObject();

        /** Deleting **/
        //deleteDoc();
        //deleteField();

        /** Transaction and WriteBatch **/
        //addDataForTransactions();
        //makeTransaction(60,300);      // Transaction is used to read and write data in multiple documents
        //useWriteBatchToAddDataForTransaction();      //  WriteBatch is used to write data in multiple documents

        /** Perform Query**/
        //performQuery();

        /** Ordering Data with limit**/
        //orderByWithLimit();

        //paginatingData();
    }

    private void getObject() {

        db.collection("Students").document("Hrithik")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Student s1 = documentSnapshot.toObject(Student.class);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Student Data")
                                .setMessage("Name:"+s1.name+"\nAge: "+s1.age+"\nCollage: "+s1.collage+"\nBranch: "+s1.branch)
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addingObject() {
        Student s1 = new Student("Hrithik",20,"JECRC","B-Tech");
        db.collection("Students").document(s1.name)
                .set(s1);
    }

    private void paginatingData() {
        db.collection("Cities")
                .orderBy("Population").startAt(1000000)     //.endAt(1000000)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            Toast.makeText(MainActivity.this, ""+doc, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void orderByWithLimit() {
        CollectionReference cities =  db.collection("Cities");
        cities
                .orderBy("Population", Query.Direction.DESCENDING).limit(3)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            Toast.makeText(MainActivity.this, ""+doc, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    // Filtering Data when getting from firebase

    private void performQuery() {
        CollectionReference cities =  db.collection("Cities");

        Map<String,Object> city1 = new HashMap<>();
        city1.put("Name","Los Angeles");
        city1.put("State","CA");
        city1.put("Capital",false);
        city1.put("Population",3900000);
        cities.document("LA").set(city1);

        Map<String,Object> city2 = new HashMap<>();
        city2.put("Name","Tokyo");
        city2.put("State",null);
        city2.put("Capital",true);
        city2.put("Population",9000000);
        cities.document("TOK").set(city2);

        Map<String,Object> city3 = new HashMap<>();
        city3.put("Name","San Francisco");
        city3.put("State","CA");
        city3.put("Capital",false);
        city3.put("Population",860000);
        cities.document("SF").set(city3);

        Map<String,Object> city4 = new HashMap<>();
        city4.put("Name","Washington DC");
        city4.put("State",null);
        city4.put("Capital",true);
        city4.put("Population",680000);
        cities.document("DC").set(city4);

        // Simple Query
        /*db.collection("Cities")
                //.whereGreaterThanOrEqualTo("Name","San Francisco")
                //.whereArrayContains("Array","")
                //.whereEqualTo("Capital",true)
                //.whereIn("Name",Arrays.asList("Tokyo","Los Angeles"))
                //.whereNotIn("Name",Arrays.asList("San Francisco","USA"))
                .whereArrayContainsAny("Name",Arrays.asList("San Francisco","Washington DC"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            Toast.makeText(MainActivity.this, "Data:\n"+doc, Toast.LENGTH_LONG).show();
                        }
                    }
                });*/

        // Collection Group Query
        Map<String,Object> ggbData = new HashMap<>();
        ggbData.put("name","Golden Gate Bridge");
        ggbData.put("type","bridge");
        cities.document("SF").collection("landmarks").add(ggbData);

        Map<String,Object> lohData = new HashMap<>();
        lohData.put("name","Legion of Honor");
        lohData.put("type","museum");
        cities.document("SF").collection("landmarks").add(lohData);

        Map<String,Object> gpData = new HashMap<>();
        gpData.put("name","Griffith Park");
        gpData.put("type","park");
        cities.document("LA").collection("landmarks").add(gpData);

        Map<String,Object> tgData = new HashMap<>();
        tgData.put("name","The Getty");
        tgData.put("type","museum");
        cities.document("LA").collection("landmarks").add(tgData);

        db.collectionGroup("landmarks")
                .whereEqualTo("type","museum")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            Toast.makeText(MainActivity.this, "Data:\n"+doc, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Compound Query
        /*db.collection("Cities")
                .whereEqualTo("State","CA")
                .whereGreaterThan("Population",1000000)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            Toast.makeText(MainActivity.this, "Doc:\n"+doc, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });*/
    }
    // using WriteBatch to adding data for transaction

    private void useWriteBatchToAddDataForTransaction() {
        WriteBatch writeBatch = db.batch();

        writeBatch.set(db.collection("User").document("C"),new Account("C",400));
        writeBatch.set(db.collection("User").document("D"),new Account("D",350));
        writeBatch.set(db.collection("User").document("E"),new Account("E",250));

        writeBatch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Data Added!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // Making a transaction

    private void makeTransaction(int amtFromA, int amtFromD) {
        // A  --amtFromA--> B
        // D --amtFromD--> A

        final int[] balOfA = new int[1];
        final int[] balOfD = new int[1];
        ArrayList<Integer> remAmt = new ArrayList<>();

        db.runTransaction(new Transaction.Function<ArrayList<Integer>>() {
            @Nullable
            @Override
            public ArrayList<Integer> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                Account fromAccA = transaction.get(db.collection("User").document("A")).toObject(Account.class);
                Account fromAccD = transaction.get(db.collection("User").document("D")).toObject(Account.class);
                balOfA[0] = fromAccA.Balance;
                balOfD[0] = fromAccD.Balance;
                if (fromAccA.Balance < amtFromA || fromAccD.Balance<amtFromD){
                    if (fromAccA.Balance < amtFromA)
                        remAmt.add(amtFromA);
                    else
                        remAmt.add(amtFromD);
                    return remAmt;
                }

                transaction.update(db.collection("User").document("A"),"Balance",FieldValue.increment(-amtFromA));
                transaction.update(db.collection("User").document("B"),"Balance",FieldValue.increment(amtFromA));
                transaction.update(db.collection("User").document("D"),"Balance",FieldValue.increment(-amtFromD));
                transaction.update(db.collection("User").document("A"),"Balance",FieldValue.increment(amtFromD));

                remAmt.add(0,fromAccA.Balance - amtFromA+amtFromD);
                remAmt.add(1,fromAccD.Balance - amtFromD);
                return  remAmt;
            }
        }).addOnSuccessListener(new OnSuccessListener<ArrayList<Integer>>() {


            @Override
            public void onSuccess(ArrayList<Integer> integers) {

                if (remAmt.get(0) == amtFromA || remAmt.get(0)==amtFromD){

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Failed!")
                            .setMessage("Insufficient Balance \n Balance of A account: "+ balOfA[0]
                                    +"\n Balance of D account: "+ balOfD[0])
                            .show();
                }
                else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Success!")
                            .setMessage("Transaction Done \n Balance of A account: "+remAmt.get(0)+"\nBalance of D account: "+remAmt.get(1))
                            .show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Transaction Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Adding data for transaction

    private void addDataForTransactions() {
        Map<String , Object> from = new HashMap<>();
        from.put("Name","A");
        from.put("Balance",200);

        Map<String,Object> to = new HashMap<>();
        to.put("Name","B");
        to.put("Balance",250);

        db.collection("User").document("A")
                .set(from);
        db.collection("User").document("B")
                .set(to);
    }
    // Collection --> Document --> Collection1 --> Document1

    private void addNestedDocument() {

        // Create a document with name user
        Map<String , Object> user = new HashMap<>();
        user.put("First Name","Ajay");
        user.put("Last Name","Mittal");
        user.put("Age",22);

        db.collection("Users").document("user1")
                .collection("user1A").document()
                .set(user);
    }
    // Delete any Field

    private void deleteField() {
        DocumentReference docRef = db.collection("Users").document("user2");
        docRef.update("Last Name", FieldValue.delete());

    }

    // Delete a document
    /** Deleting a document does not delete its subCollections
     *  --> When a document is deleted the subCollections of that document will not visible in that document
     *      but you can still access the subCollections by reference
     **/
    private void deleteDoc() {
        db.collection("Users").document("user1")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Delete Successful!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Task Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Get complete data of any collection

    private void getData() {
        db.collection("Users").document("user1")
                .collection("user1A")
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
    // Add data to firebase with a automatically generated unique document ID

    private void addDataToFirebase() {


        // Create a document with name user
        Map<String , Object> user = new HashMap<>();
        user.put("First Name","Hrithik");
        user.put("Last Name","Sharma");
        user.put("Age",20);
        //user.put("Born",2000);


        // Create a new collection of documents with automatically generated ID
        db.collection("Users").document("user1")
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Document Added!")
                                .show();
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