package com.example.stcov;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {

    public static final String TAG = "Activity_Register";
    TextInputLayout email,firstname,lastname,pwd;
    Button signup_btn,callLogin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStor;
    ImageView img;
    TextView logo,slogan;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.Mail);
        firstname = findViewById(R.id.Firstname);
        lastname = findViewById(R.id.Lastname);
        pwd = findViewById(R.id.Password);
        signup_btn = findViewById(R.id.add_user);
        callLogin = findViewById(R.id.signin);
        img = findViewById(R.id.logo_image);
        logo = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan_name);

        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,Login.class);
                // Attach all the elements those you want to animate in design
                Pair[] pairs=new Pair[7];

                pairs[0]=new Pair<View, String>(img,"logo_image");
                pairs[1]=new Pair<View, String>(logo,"logo_text");
                pairs[2]=new Pair<View, String>(slogan,"slogan_name");
                pairs[3]=new Pair<View, String>(email,"email_tran");
                pairs[4]=new Pair<View, String>(pwd,"pass_tran");
                pairs[5]=new Pair<View, String>(signup_btn,"go_tran");
                pairs[6]=new Pair<View, String>(callLogin,"button_tran");


                //wrap the call in API level 21 or higher
                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Register.this,pairs);
                    startActivity(intent,options.toBundle());
                }
            }
        });



        progressBar = findViewById(R.id.progressBar);
        fStor = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        //PROBLEM BETWEEN DRAWER AND REGISTER
        if(fAuth.getCurrentUser() != null){
        startActivity(new Intent(getApplicationContext(),Help.class));
            finish();
        }//else{
             //       startActivity(new Intent(getApplicationContext(),Login.class));
           //     }

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateEmail() | !validateFirstname() | !validateLastname() | !validatePassword()) {
                    return;
                } else {
                    final String  prenom = firstname.getEditText().getText().toString();
                    final String nom = lastname.getEditText().getText().toString();
                    final String mail = email.getEditText().getText().toString();
                    final String password = pwd.getEditText().getText().toString();

                    progressBar.setVisibility(View.VISIBLE);

                    //registration dans le firebase

                    fAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                //verifier mail

                                FirebaseUser fUser = fAuth.getCurrentUser();
                                fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Register.this, "Verification Email Has Been Sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure : Email Not Sent "+e.getMessage());
                                    }
                                });

                                Toast.makeText(Register.this, "User Crated", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStor.collection("users").document(userID);
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
                              startActivity(new Intent(getApplicationContext(),Profile.class));
                              //finish();
                            }
                            else{
                                Toast.makeText(Register.this, "Error ! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                      



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
    private Boolean validatePassword(){
        String password = pwd.getEditText().getText().toString();
        //String passwordVal = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        if(password.isEmpty()){
            pwd.setError("Password is Required");
            return false;
        }

        else if(password.length()<8){
            pwd.setError("Your password It must be at least greater than 8 ");
            return false;
        }

        else if(password.length()>=20){
            pwd.setError("Your password It must be at most greater than 20 ");
            return false;
        }

       // else if(!password.matches(passwordVal)){
         //   pwd.setError("Password is too weak");
           // return false;
       // }



        else{
            pwd.setError(null);
            return true;
        }
    }


}
