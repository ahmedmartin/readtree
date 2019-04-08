package com.ahmed.martin.readtree;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class share_type extends AppCompatActivity {

    TextView tx_in_category,tx_out_category,tx_public,tx_in_part;
   static post_details post_d;
   static int fragment_num;
   static String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_type);

        tx_in_category = findViewById(R.id.tx_in_category);
        tx_out_category = findViewById(R.id.tx_out_category);
        tx_public = findViewById(R.id.tx_public);
        tx_in_part=findViewById(R.id.tx_in_part);

        post_d = (post_details) getIntent().getSerializableExtra("post");

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("prices");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tx_in_category.setText("ج.م"+dataSnapshot.child("out_category").getValue().toString()+"فقط");
                    tx_out_category.setText("ج.م"+dataSnapshot.child("home").getValue().toString()+"فقط");
                    tx_public.setText("ج.م"+dataSnapshot.child("public").getValue().toString()+"فقط");
                    tx_in_part.setText("ج.م"+dataSnapshot.child("in_category").getValue().toString()+"فقط");
                }
                ref.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    public void share_out_category(View view) {
       /* DatabaseReference out_ref =FirebaseDatabase.getInstance().getReference().child("saving").child("out_category");
        out_ref.setValue(post_d).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                if(task.isSuccessful()){
                    Intent t = new Intent(share_type.this,payment.class);
                    t.putExtra("typ","out_category");
                    t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(t);
                }else
                    Toast.makeText(share_type.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
       type="home";
       show_fragment();
    }

    public void share_in_category(View view) {
       /* */
       type="out_category";
       show_fragment();
    }

    public void share_public(View view) {
       /* if public is for free */
       if(tx_public.getText().toString().equals("ج.م"+"0"+"فقط")){
           Date d = new Date();
           SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yy");
           final String time = sm.format(d).toString();
           final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

           // save data by payment method & what kind he choose
           DatabaseReference save_ref = ref.child("saving").child("public").child(time).push();

           //key of data saved to add it to it's user
           final String key = save_ref.getKey();

           // upload photo by post key
           StorageReference pRef = FirebaseStorage.getInstance().getReference().child(key);
           UploadTask up = pRef.putFile(Uri.parse(post_d.getUr()));
           post_d.setUr(null);

           post_d.setShare_phone(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString());
           save_ref.setValue(post_d).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete( Task<Void> task) {
                   if(task.isSuccessful()){

                       // put key & payment method in save for user
                       String uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                       DatabaseReference user_save_ref = ref.child("users").child(uid).child("saving")
                               .child("public").child(time).child(key);

                       // put key for user to let him get his saving posts
                       user_save_ref.setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete( Task<Void> task) {
                               if(task.isSuccessful()){
                                   // go to home page
                                   Intent t = new Intent(share_type.this, category.class);
                                   t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   finish();
                                   startActivity(t);
                               }else
                                   Toast.makeText(share_type.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
                   }else
                       Toast.makeText(share_type.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
       }else {
           type = "public";
           show_fragment();
       }
    }

    public void share_in_part(View view) {
        type="in_category";
        show_fragment();
    }

    static payment_company comp;
    static how_pay how;
    LinearLayout layout;
    void show_fragment(){
        fragment_num=1;
        layout = findViewById(R.id.linear);
        layout.setVisibility(View.INVISIBLE);
        comp = new payment_company();
        how= new how_pay();
        FragmentManager mang = getFragmentManager();
        FragmentTransaction tr= mang.beginTransaction();
        tr.add(R.id.relative,comp,"comp");
        tr.commit();
    }

    @Override
    public void onBackPressed() {
        switch (fragment_num){
            case 0: super.onBackPressed(); break;
            case 1: { FragmentManager mang = getFragmentManager();
                      FragmentTransaction tr= mang.beginTransaction();
                      tr.remove(comp);
                      tr.commit();
                     layout.setVisibility(View.VISIBLE);
                     fragment_num=0;
                     break;
                     }
            case 2:{ FragmentManager mang = getFragmentManager();
                     FragmentTransaction tr= mang.beginTransaction();
                     tr.remove(how);
                     tr.add(R.id.relative,comp,"comp");
                     tr.commit();
                     fragment_num=1;
                     break;
                   }
        }

    }


}
