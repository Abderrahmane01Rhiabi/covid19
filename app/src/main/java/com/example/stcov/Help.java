package com.example.stcov;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Help extends AppCompatActivity {

    FirebaseUser user;
    StorageReference storageReference;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStor;
    public String role;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        fAuth = FirebaseAuth.getInstance();
        fStor = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();



        DocumentReference docRef = fStor.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String role = document.getString("role");
                        Log.d("TAG", "DocumentSnapshot data: " + document.getString("role"));
                        if(role.equals("user")){
                            startActivity(new Intent(getApplicationContext(),Profile.class));
                            finish();
                        }
                        else if(role.equals("admin")){
                            startActivity(new Intent(getApplicationContext(),ProfileAdmin.class));
                            finish();
                        }
                        else if(role.equals("supperadmin")){
                            startActivity(new Intent(getApplicationContext(),ProfileSupperAdmin.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(),Help.class));
                            Toast.makeText(Help.this, "Fatal problem", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("TAG", "No such document");
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });



    }
}
