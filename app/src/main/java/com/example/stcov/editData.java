package com.example.stcov;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class editData extends AppCompatActivity {

    EditText firstname,lastname,email;
    Button edit;
    FirebaseAuth fAuth;
    FirebaseFirestore fStor;
    FirebaseUser user;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        firstname = findViewById(R.id.ftname);
        lastname = findViewById(R.id.ltname);
        email = findViewById(R.id.mail);
        edit = findViewById(R.id.edit);

        fAuth = FirebaseAuth.getInstance();
        fStor = FirebaseFirestore.getInstance();
        user =  fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();

        Intent data = getIntent();
        final String uid = data.getStringExtra("uid");

        fStor.collection("users").document(uid).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(!snapshot.exists()){
                    Log.d("TAG", "onEvent: Document do not exists");
                }else {
                    email.setText(snapshot.getString("email"));
                    firstname.setText(snapshot.getString("firstname"));
                    lastname.setText(snapshot.getString("lastname"));
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstname.getText().toString().isEmpty() || lastname.getText().toString().isEmpty()){
                    Toast.makeText(editData.this,"One Or More Fileds Are Empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference docRef = fStor.collection("users").document(uid);
                Map<String,Object> edited = new HashMap<>();
                edited.put("email",email.getText().toString());
                edited.put("firstname",firstname.getText().toString());
                edited.put("lastname",lastname.getText().toString());

                docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editData.this,"Data Has Been Changed",Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(editData.this, "Fatal problem", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
