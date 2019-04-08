package com.ahmed.martin.readtree;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


public class payment_company extends Fragment {

     static String pay_type;


    public payment_company() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_company, container, false);

        LinearLayout fawry = view.findViewById(R.id.fawry);
        fawry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_fragment();
                pay_type="fawry";
            }
        });

        LinearLayout nbe = view.findViewById(R.id.nbe);
        nbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_fragment();
                pay_type="elahly";
            }
        });
        LinearLayout cib = view.findViewById(R.id.cib);
        cib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               show_fragment();
               pay_type="cib";
            }
        });


        return view;
    }

     static FragmentTransaction tr;
    void show_fragment(){
        share_type.fragment_num=2;
        FragmentManager mang = getFragmentManager();
        tr= mang.beginTransaction();
        tr.add(R.id.relative,share_type.how,"how");
        tr.remove(share_type.comp);
        tr.commit();

    }



}
