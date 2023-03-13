package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private TextView appName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.app_name);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appName.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myanim);
        appName.setAnimation(anim);

        mAuth = FirebaseAuth.getInstance();

        DbQuery.g_firestore = FirebaseFirestore.getInstance();


        new Thread(){

            @Override
            public void run()
            {
                try {
                    sleep(3000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                if (mAuth.getCurrentUser()!= null){

                    DbQuery.loadCategories(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(SplashActivity.this, "Something went wrong! Please try again Later.", Toast.LENGTH_SHORT).show();
                        }
                    });



                } else {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        } .start();
    }
}