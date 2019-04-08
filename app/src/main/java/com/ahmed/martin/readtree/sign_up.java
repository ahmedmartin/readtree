package com.ahmed.martin.readtree;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class sign_up extends AppCompatActivity {

    EditText name,email,password,phone;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verification_callbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        phone=findViewById(R.id.phone);
    }

    public void sign_in(View view) {
        startActivity(new Intent(sign_up.this,sign_in.class));
        finish();


    }

    public void sign_up(View view) {

         if(check_fildes()) {
             Verify_phone_number();
         }

    }


    private void Verify_phone_number() {
        String phone_number ="+2"+ phone.getText().toString();
        setupverificationcallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number, 2, TimeUnit.MINUTES, this, verification_callbacks);
    }

    private void setupverificationcallbacks() {
        verification_callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(final PhoneAuthCredential Credential) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser uid = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString()).build();
                            uid.updateProfile(profile);
                            // check phone auth and uniqueness
                            uid.updatePhoneNumber(Credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete( Task<Void> task) {
                                    if(task.isSuccessful()){
                                        finish();
                                    }else {
                                        phone.setError(task.getException().getMessage());
                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                    }
                                }
                            });

                        /* user User = new user(name.getText().toString(), phone.getText().toString());
                         FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("personal info").setValue(User);
                         finish();*/
                        } else {
                            // show exception  in it's suitable place as error
                            String ex =task.getException().getMessage();
                            if(ex.contains("Email")||ex.contains("email")||ex.contains("user"))
                                email.setError(ex);
                            if(ex.contains("Password")||ex.contains("password"))
                                password.setError(ex);
                            Toast.makeText(sign_up.this, ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(sign_up.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }



    boolean check_fildes(){
        if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("يجب ادخال الاسم كامل");
            return false;
        }
        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("يجب ادخال البريد الالكترونى");
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("يجب ادخال كلمه مرور ");
            return false;
        }
        if(TextUtils.isEmpty(phone.getText().toString())){
            phone.setError("يجب ادخال رقم هاتف ");
            return false;
        }
        if (phone.getText().toString().length() != 11) {
            phone.setError("يجب ان يكون الهاتف 11 رقم");
            return false;
        }
        return true;
    }
}
