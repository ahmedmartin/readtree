package com.ahmed.martin.readtree;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_in extends AppCompatActivity {

    private TextView password;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        password=findViewById(R.id.password);
        email = findViewById(R.id.email);
    }

    public void sign_in(View view) {
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
            Toast.makeText(this,"ادخل كل المعلومات المطلوبه",Toast.LENGTH_LONG).show();
        }else {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                // show exception  in it's suitable place as error
                                String ex =task.getException().getMessage();
                                if(ex.contains("Email")||ex.contains("email")||ex.contains("user"))
                                    email.setError(ex);
                                if(ex.contains("Password")||ex.contains("password"))
                                    password.setError(ex);
                                Toast.makeText(sign_in.this, ex, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void sign_up(View view) {
        startActivity(new Intent(sign_in.this,sign_up.class));
        finish();
    }

    public void forget_password(View view) {
        final EditText text = new EditText(sign_in.this);
        text.setHint("بريدك الالكتروني");
        text.setGravity(Gravity.CENTER);
        AlertDialog.Builder alart = new AlertDialog.Builder(sign_in.this);
        alart.setMessage("ادخل البريد الالكتروني الخاص بك ثم اذهب الى بريدك لاتباع باقى التعليمات اللازمه");
        alart.setView(text);
        alart.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(text.getText().toString())){
                    Toast.makeText(sign_in.this, "ادخل الايميل الخاص بك لاتمام العمليه بنجاح", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseAuth mAuth =FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete( Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(sign_in.this, "اذهب و تفقد بريدك لاتمام باقى التعليمات", Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(sign_in.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        alart.show();
    }
}
