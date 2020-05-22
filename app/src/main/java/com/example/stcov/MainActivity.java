package com.example.stcov;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    //VARIABLE
    Animation topAnim,bottomAnim;
    ImageView img,img2;
    TextView logo,slogan;

    FirebaseAuth fAuth;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //ANIMATION
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
    //--

        img = findViewById(R.id.img1);
        logo = findViewById(R.id.t1);
        slogan = findViewById(R.id.t2);
        img2 = findViewById(R.id.img2);

        img.setAnimation(topAnim);
        img2.setAnimation(bottomAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Help.class));
            finish();
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){

                Intent intent=new Intent(MainActivity.this,Login.class);
                // Attach all the elements those you want to animate in design
                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View, String>(img,"logo_image");
                pairs[1]=new Pair<View, String>(logo,"logo_text");
                //wrap the call in API level 21 or higher
                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                    finish();
                }
            }
        },SPLASH_SCREEN);

    }
}
