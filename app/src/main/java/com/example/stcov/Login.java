package com.example.stcov;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Login extends AppCompatActivity{

    Button callSinUp,login_btn;
    ImageView img;
    TextView logo,slogan,forget_pwd;
    TextInputLayout email,pwd;
    ProgressBar progressBar2;
    FirebaseAuth fAuth;
    FirebaseUser user;
    StorageReference storageReference;
    FirebaseFirestore fStor;
    FirebaseFirestore firebaseFirestore;
    String userId;
    public String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        callSinUp = findViewById(R.id.signup);
        img = findViewById(R.id.logo_image);
        logo = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan_name);
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.password);
        login_btn = findViewById(R.id.signin);
        forget_pwd = findViewById(R.id.forget_pwd);

        progressBar2 = findViewById(R.id.progressBar2);

        fAuth = FirebaseAuth.getInstance();
        fStor = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user = fAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Help.class));
            finish();
        }//else{
           // startActivity(new Intent(getApplicationContext(),Login.class));
        //}



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassword()) {
                    return;
                }
                final String mail = email.getEditText().getText().toString().trim();
                final String password = pwd.getEditText().getText().toString().trim();

                progressBar2.setVisibility(View.VISIBLE);

                fStor.collection("users").whereEqualTo("email",mail).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                QuerySnapshot snapshots = task.getResult();
                                if(snapshots.isEmpty()){
                                    System.out.println("else login 1");
                                    Toast.makeText(Login.this, "Error ! User Not Founded", Toast.LENGTH_SHORT).show();
                                    progressBar2.setVisibility(View.GONE);
                                }else {
                                    //authentification de user
                                    fAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                System.out.println(task.getResult().getUser());
                                                System.out.println(111);
                                                Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), Help.class));
                                                progressBar2.setVisibility(View.GONE);

                                            }

                                            else{
                                                System.out.println("else login 2");
                                                Toast.makeText(Login.this, "Error ! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar2.setVisibility(View.GONE);
                                            }
                                        }
                                    });


                                }
                            }
                        });



            }
        });

        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override

                public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });

        callSinUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                // Attach all the elements those you want to animate in design
                Pair[] pairs=new Pair[7];

                pairs[0]=new Pair<View, String>(img,"logo_image");
                pairs[1]=new Pair<View, String>(logo,"logo_text");
                pairs[2]=new Pair<View, String>(slogan,"slogan_name");
                pairs[3]=new Pair<View, String>(email,"email_tran");
                pairs[4]=new Pair<View, String>(pwd,"pass_tran");
                pairs[5]=new Pair<View, String>(login_btn,"go_tran");
                pairs[6]=new Pair<View, String>(callSinUp,"button_tran");


                //wrap the call in API level 21 or higher
                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                    startActivity(intent,options.toBundle());
                }
            }
        });

    }

    private Boolean validatePassword(){
        String password = pwd.getEditText().getText().toString();

        if(password.isEmpty()){
            pwd.setError("Field cannot be empty");
            return false;
        }


        else{
            pwd.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String mail = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(mail.isEmpty()){
            email.setError("Field cannot be empty");
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
