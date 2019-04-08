package com.ahmed.martin.readtree;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class personal_info extends AppCompatActivity {

    EditText adviser_name,adviser_phone;
    TextView email;
    String last_name,last_phone;
    FirebaseUser user;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verification_callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        adviser_name=findViewById(R.id.name);
        adviser_phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);

        user= FirebaseAuth.getInstance().getCurrentUser();
        adviser_phone.setText(user.getPhoneNumber().substring(2,13));
        adviser_name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        last_name=user.getDisplayName();
        last_phone=user.getPhoneNumber();
    }

    public void change_password(View view) {

        final EditText text = new EditText(personal_info.this);
        text.setHint("كلمه المرور الجديده ");
        text.setGravity(Gravity.CENTER);
        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog.Builder alart = new AlertDialog.Builder(personal_info.this);
        alart.setMessage(" ادخل كلمه المرور الجديده ");
        alart.setView(text);
        alart.setPositiveButton("تغير", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(text.getText().toString())){
                    Toast.makeText(personal_info.this, "لم يحدث اى تغير", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(personal_info.this, "تم تغير كلمه المرور بنجاح", Toast.LENGTH_LONG).show();
                                FirebaseAuth mauth=FirebaseAuth.getInstance();
                                mauth.signOut();
                                Intent sign_in=new Intent(personal_info.this,sign_in.class);
                                finish();
                                startActivity(sign_in);
                            }else
                                Toast.makeText(personal_info.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        alart.show();

    }

    public void update(View view) {

        if(checked_fild()) {
          if(! last_name.equals(adviser_name.getText().toString())){
              UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName("ahmed").build();
              user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful())
                          Toast.makeText(personal_info.this, "تم تحديث الاسم بنجاح ", Toast.LENGTH_SHORT).show();
                      else
                          Toast.makeText(personal_info.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                  }
              });
          }
          if(! last_phone.equals(adviser_phone.getText().toString())){
                Verify_phone_number();
          }
        }
    }

    private void Verify_phone_number() {
        String phone_number ="+2"+ adviser_phone.getText().toString();
        setupverificationcallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number, 2, TimeUnit.MINUTES, this, verification_callbacks);
    }

    private void setupverificationcallbacks() {
        verification_callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(final PhoneAuthCredential Credential) {
                user.updatePhoneNumber(Credential);
                Toast.makeText(personal_info.this, "تم تحديث رقم الهاتف  بنجاح ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(personal_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    boolean checked_fild(){

        if(TextUtils.isEmpty(adviser_name.getText().toString())){
            adviser_name.setError("يجب ادخال الاسم كامل");
            return false;
        }
        if(TextUtils.isEmpty(adviser_phone.getText().toString())){
            adviser_phone.setError("يجب ادخال رقم هاتف");
            return false;
        }
        if (adviser_phone.getText().toString().length() != 11) {
            adviser_phone.setError("يجب ان يكون الهاتف 11 رقم");
            return false;
        }
        return true;

    }
}
