package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, pass, confirmPass;
    private Button signupB;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.username);
        email = findViewById(R.id.emailID);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        signupB = findViewById(R.id.signupB);
        backB = findViewById(R.id.backB);


        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering user...");

        mAuth = FirebaseAuth.getInstance();

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    signupNewUser();
                }


                signupNewUser();
            }
        });

    }

    private boolean validate (){


        nameStr = name.getText().toString().trim(); //trim will delete spaces from start to end
        passStr = pass.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();
        boolean status =false;
        if (nameStr.isEmpty()){
            name.setError("Enter your name");
            return false;
        }

        if (emailStr.isEmpty()){
            email.setError("Enter email ID");
            return false;
        }

        if (passStr.isEmpty()){
            pass.setError("Enter Password");

        }
        if (confirmPassStr.isEmpty()){
            confirmPass.setError("Enter Password");
        }
        if (passStr.compareTo(confirmPassStr) !=0){
            Toast.makeText(SignUpActivity.this,"Password and confirm Password should be the same!",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void signupNewUser(){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SignUpActivity.this, "Sign up Successfull",Toast.LENGTH_SHORT).show();

                            DbQuery.createUserData(emailStr,nameStr, new MyCompleteListener()
                                    {

                                        @Override
                                        public void onSuccess() {

                                            DbQuery.loadCategories(new MyCompleteListener() {
                                                @Override
                                                public void onSuccess() {
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                    SignUpActivity.this.finish();
                                                }

                                                @Override
                                                public void onFailure() {
                                                    Toast.makeText(SignUpActivity.this, "Something went wrong! Please try again Later.", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });


                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignUpActivity.this, "Something went wrong! Please try again Later.", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}