package com.example.stcov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity {

    public static final String TAG = "Activity_AddUser";
    TextInputLayout email,firstname,lastname;
    Button addUser;
    String userID;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);


        email = findViewById(R.id.email);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        addUser = findViewById(R.id.add_user);


        fStor = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validateFirstname() | !validateLastname() ) {
                    return;
                } else {
                    final String  prenom = firstname.getEditText().getText().toString();
                    final String nom = lastname.getEditText().getText().toString();
                    final String mail = email.getEditText().getText().toString();

                    progressBar.setVisibility(View.VISIBLE);
                                //verifier mail

                                Toast.makeText(AddUser.this, "User Crated", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStor.collection("users").document();
                                Map<String,Object> user = new HashMap<>();
                                user.put("firstname",prenom);
                                user.put("lastname",nom);
                                user.put("email",mail);
                                user.put("role","user");
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG,"onSuccess : user Profile is created for "+mail);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"onFailure : "+e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),Help2.class));
                                finish();
                            //-------

                }
            }
        });

    }

    private Boolean validateFirstname(){
        String nom = firstname.getEditText().getText().toString();
        String noWhiteSpace = "^[a-zA-Z]{3,15}$";

        if(nom.isEmpty()){
            firstname.setError("Your Firstname is Required");
            return false;
        }
        else if(!nom.matches(noWhiteSpace)){
            firstname.setError("Your firstname is Unacceptable");
            return false;
        }
        else{
            firstname.setError(null);
            firstname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateLastname(){
        String prenom = lastname.getEditText().getText().toString();
        String noWhiteSpace = "^[a-zA-Z]{3,15}$";

        if(prenom.isEmpty()){
            lastname.setError("Your Lastname is Required");
            return false;
        }
        else if(!prenom.matches(noWhiteSpace)){
            lastname.setError("Your lastname is Unacceptable");
            return false;
        }
        else{
            lastname.setError(null);
            lastname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateEmail(){
        String mail = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(mail.isEmpty()){
            email.setError("Email is Required");
            return false;
        }

        else if(!mail.matches(emailPattern)){
            email.setError("Ivalide email addresse");
            return false;
        }

        else{
            email.setError(null);
            return true;
        }
    }


}
