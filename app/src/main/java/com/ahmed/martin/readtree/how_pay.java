package com.ahmed.martin.readtree;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;


public class how_pay extends Fragment {

    String pay_type,uid;

    TextView payment,how_pay,phone;
    Button share;

    public how_pay() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_how_pay, container, false);

        pay_type=payment_company.pay_type;
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        payment=view.findViewById(R.id.payment_method);
        how_pay=view.findViewById(R.id.how_you_pay);
        share =view.findViewById(R.id.btn_share);
        phone=view.findViewById(R.id.phone);

        payment.setText("طريقه الدفع باستخدام"+pay_type);

        switch (pay_type){
            case "fawry":fawry(); break;
            case "elahly":elahly(); break;
            case "cib":cib(); break;
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if phone correct or not
                if(check_phone()) {
                    Date d = new Date();
                    SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yy");
                    final String time = sm.format(d).toString();

                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    // save data by payment method & what kind he choose
                    final DatabaseReference save_ref = ref.child("saving").child(share_type.type)
                            .child(pay_type).child(time).push();

                    //key of data saved to add it to it's user
                    final String key = save_ref.getKey();

                    // upload photo by post key
                    StorageReference pRef = FirebaseStorage.getInstance().getReference().child(key);
                    UploadTask up = pRef.putFile(Uri.parse(share_type.post_d.getUr()));
                    share_type.post_d.setUr("");
                    up.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                // put phone that user will send money on it
                                share_type.post_d.setShare_phone(phone.getText().toString());

                                //save data
                                save_ref.setValue(share_type.post_d).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            // put key & payment method in parent (saving) for user
                                            DatabaseReference user_save_ref = ref.child("users").child(uid).child("saving")
                                                    .child(share_type.type).child(time).child(key);
                                            user_save_ref.setValue(pay_type);

                                            // go to home page
                                            Intent t = new Intent(getActivity(), category.class);
                                            t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            getActivity().finish();
                                            startActivity(t);
                                        } else
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else
                                Toast.makeText(getActivity(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });


        return view;
    }

    boolean check_phone(){
        if(TextUtils.isEmpty(phone.getText().toString())) {
            phone.setError("يجب ادخال رقم الهاتف الذى ستتم عليه العمليه");
            return false;
        }
        if(phone.getText().toString().length()!=11){
            phone.setError("يجب ادخال رقم الهاتف مكون من 11 رقم");
            return false;
        }
        return true;
    }

    void fawry(){
        String how = "1-اذا كان لديك محفظه فى فورى اذهب للخطوه 2 اذا لم يكن لديك محفظه اذهب الى اقرب فرع فورى وقم بعمل محفظه (مجانا)"+"\n";
        how+="\n"+"2-قم بشحن المحفظه بالمبلغ المراد ثم قم بتحويل المبلغ المراد من المحفظه الى الرقم 01553484109"+"\n";
        how+="\n"+"3- سوف يتم نشر الاعلان بعد يوم ع الاكثر من اتمام تحويل المبلغ"+"\n";
        how+="\n"+"ملحوظه سوف يتم حفظ المنشور لمده يومين فقط ان لم يتم الدفع سوف يتم ازاله المنشور"+"\n";
        how_pay.setText(how);
    }



    void cib(){
        String how = "1-اذا كان لديك محفظه فى البنك اذهب للخطوه 2 اذا لم يكن لديك محفظه اذهب الى اقرب فرع للبنك وقم بعمل محفظه (مجانا)" +"\n";
        how+="\n"+"2-قم بشحن المحفظه بالمبلغ المراد ثم قم بتحويل المبلغ المراد من المحفظه الى الرقم 01553484109" +"\n";
        how+="\n"+"3- سوف يتم نشر الاعلان بعد يوم ع الاكثر من اتمام تحويل المبلغ" +"\n";
        how+="\n"+"ملحوظه سوف يتم حفظ المنشور لمده يومين فقط ان لم يتم الدفع سوف يتم ازاله المنشور" +"\n";
        how_pay.setText(how);
    }

    void elahly(){
        String how = "1-اذا كان لديك محفظه فى البنك اذهب للخطوه 2 اذا لم يكن لديك محفظه اذهب الى اقرب فرع للبنك وقم بعمل محفظه (مجانا)" +"\n";
        how+="\n"+"2-قم بشحن المحفظه بالمبلغ المراد ثم قم بتحويل المبلغ المراد من المحفظه الى الرقم 01151155821" +"\n";
        how+="\n"+"3- سوف يتم نشر الاعلان بعد يوم ع الاكثر من اتمام تحويل المبلغ"  +"\n";
        how+="\n"+"ملحوظه سوف يتم حفظ المنشور لمده يومين فقط ان لم يتم الدفع سوف يتم ازاله المنشور" +"\n";
        how_pay.setText(how);
    }
}
