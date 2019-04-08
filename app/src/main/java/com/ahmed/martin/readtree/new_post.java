package com.ahmed.martin.readtree;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.util.ArrayList;

public class new_post extends AppCompatActivity {

    EditText description,area ;
    TextView adviser_name,adviser_phone,delay,sale_price,rent_price;
    ImageView image;
    AutoCompleteTextView book_name;
    CheckBox rent,sale;
    Spinner city;
    Spinner categories;
    Spinner parts;
    String [] category ,part ;
    ArrayAdapter<String> city_adapter;
    ArrayAdapter <String> category_adapter;
    ArrayAdapter<String> part_adapter;


    String [] cities = {"القاهره","الجيزه","الاسكندريه","الشرقيه","الاسماعيليه","اسوان","اسيوط","الاقصر","البحر الاحمر","البحيره",
            "بنى سويف","بورسعيد","جنوب سيناء","الدقهليه","دمياط","سوهاج","السويس","شمال سيناء","الغربيه","الفيوم",
            "القليوبيه","قنا","كفر الشيخ","مطروح","منوفيه","المنيا","الوادى الجديد"};



    String city_name;
    String category_name ="روايات";
    String part_name;
    private Uri ur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        description=findViewById(R.id.description);
        area=findViewById(R.id.area);
        adviser_name=findViewById(R.id.adviser_name);
        adviser_phone=findViewById(R.id.adviser_phone);
        book_name=findViewById(R.id.book_name);
        city=findViewById(R.id.city);
        categories=findViewById(R.id.category);
        parts=findViewById(R.id.part);
        delay=findViewById(R.id.dayle);
        image = findViewById(R.id.imageView);
        sale_price = findViewById(R.id.sale_price);
        rent_price=findViewById(R.id.rent_price);
        rent=findViewById(R.id.rent);
        sale=findViewById(R.id.sale);
        category=this.getResources().getStringArray(R.array.مجالات);


        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        //check if user sign in or not if didn't go to sign in activity
        if(user==null){
            Toast.makeText(this, "يجب تسجيل الدخول لاتمام اعلان البوست", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,sign_in.class));
            finish();
        }else {
            adviser_name.setText(user.getDisplayName());
            adviser_phone.setText(user.getPhoneNumber());
        }

        // city spinner and it's adapter and listen
        city_adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,cities);
        city_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        city.setAdapter(city_adapter);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_name=cities[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // category spinner and it's adapter and listen
        category_adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,category);
        category_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categories.setAdapter(category_adapter);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_name=category[position];
                get_part_array(category_name);
                part_adapt();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // get suitable part array for category
        get_part_array(category_name);

       part_adapt();



    }

    private void get_part_array(String category_name) {
        switch (category_name){
            case "روايات":
                part=getResources().getStringArray(R.array.روايات);
                break;
            case "الادب" :
                part=getResources().getStringArray(R.array.الادب);
                break;
            case "العلوم البحته":
                part=getResources().getStringArray(R.array.بحته);
                break;
            case "تكنولوجيا" :
                part=getResources().getStringArray(R.array.تكنولوجيا);
                break;
            case "اللغات":
                part=getResources().getStringArray(R.array.اللغات);
                break;
            case "العلوم الدينيه" :
                part=getResources().getStringArray(R.array.الاديان);
                break;
            case "علم الاجتماع":
                part=getResources().getStringArray(R.array.الاجتماع);
                break;
            case "فلسفه" :
                part=getResources().getStringArray(R.array.الفلسفه);
                break;
            case "علوم اخرى" :
                part=getResources().getStringArray(R.array.اخرى);
                break;
        }
    }

    private void part_adapt(){
        // part spinner and it's adapter and listen
        part_adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,part);
        part_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        parts.setAdapter(part_adapter);
        parts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                part_name=part[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void take_photo(View view) {
        // request permission for get photo
        String [] permission ={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permission[0])== PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[1])== PackageManager.PERMISSION_GRANTED){

            Intent inte=new Intent(Intent.ACTION_PICK);
            inte.setType("image/*");
            startActivityForResult(inte,2);
        }else
            ActivityCompat.requestPermissions(new_post.this,permission,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // request get photos from galary
        if(requestCode==2&&resultCode==RESULT_OK) {
            ur = data.getData();
            Picasso.with(new_post.this).load(ur).into(image);
        }
        // request get photo permission
        if (requestCode==1&&resultCode== RESULT_OK){
            Intent inte=new Intent(Intent.ACTION_PICK);
            inte.setType("image/*");
            startActivityForResult(inte,2);
        }
    }

    public void share_post(View view) {

        if(fildes_complete()){
            if(connected_networt()) {
                // put data in post details class and transfer it to share type activity
                post_details pd = new post_details(book_name.getText().toString(), category_name, part_name,
                    description.getText().toString(),adviser_name.getText().toString(), adviser_phone.getText().toString(),
                 city_name+","+area.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),ur.toString());
                if (sale.isChecked()) {
                    pd.setSale(sale_price.getText().toString());
                }
                if (rent.isChecked()) {
                    pd.setRent(rent_price.getText().toString());
                }

                Intent t = new Intent(new_post.this, share_type.class);
                t.putExtra("post", pd);
                startActivity(t);
            }else
                Toast.makeText(this, "يجب الاتصال بالانترنت لاتمام نشر الاعلان", Toast.LENGTH_SHORT).show();
        }
    }

    // check if have network connection
    boolean connected_networt(){
        ConnectivityManager mang = (ConnectivityManager) this.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(mang!=null){
            NetworkInfo inf = mang.getActiveNetworkInfo();
            if(inf!=null){
                if(inf.isConnected())
                    return true;
            }
        }
        return false;
    }

    // check all inputs
    private boolean fildes_complete() {
        if(TextUtils.isEmpty(book_name.getText().toString())){
            book_name.setError("ادخل اسم الكتاب");
            return false;
        }
        if(TextUtils.isEmpty(area.getText().toString())){
            area.setError("ادخل المنطقه");
            return false;
        }
        if(sale.isChecked()||rent.isChecked()) {
            if (TextUtils.isEmpty(sale_price.getText().toString())&&sale.isChecked()) {
                sale_price.setError("ادخل السعر المناسب لك فى البيع");
                return false;
            }
            if (TextUtils.isEmpty(rent_price.getText().toString())&&rent.isChecked()) {
                rent_price.setError("ادخل السعر المناسب لك فى التاجير");
                return false;
            }
        }else{
            sale.setError("يجب اختيار بيع او تاجير");
            return false;
        }

        if(TextUtils.isEmpty(description.getText().toString())){
            description.setError("ادخل الوصف للكتاب");
            return false;
        }

        if(ur==null){
            Toast.makeText(this, "يجب اختيار صوره", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


   /* for sale and rent check box  */
    boolean sale_b;
    public void sale(View view) {
        if(sale_b){
            sale.setChecked(false);
            sale_price.setEnabled(false);
            sale_b=false;
        }else {
            sale.setChecked(true);
            sale_price.setEnabled(true);
            sale_b=true;
        }

    }

    boolean rent_b;
    public void rent(View view) {
        if(rent_b){
            rent.setChecked(false);
            rent_price.setEnabled(false);
            rent_b=false;
        }else {
            rent.setChecked(true);
            rent_price.setEnabled(true);
            rent_b=true;
        }
    }
    /* */


}
