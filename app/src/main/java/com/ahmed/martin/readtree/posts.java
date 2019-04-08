package com.ahmed.martin.readtree;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class posts extends AppCompatActivity {

    private DatabaseReference book_ref;
    private ArrayList <String> book_name=new ArrayList<>();
    private ArrayList <Uri> images = new ArrayList<>();
    private String category,part,name;
    private books_adapt adapt;
    private ListView books_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        category = getIntent().getStringExtra("category");
        part = getIntent().getStringExtra("part");

        book_ref = FirebaseDatabase.getInstance().getReference().child("books").child(category).child(part);
        book_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int i=0;
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        i++;
                        book_name.add(data.getKey().toString());
                    }
                    if (i==dataSnapshot.getChildrenCount()){
                        photos(0);
                        book_ref.removeEventListener(this);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        adapt = new books_adapt(posts.this,book_name,images);
        books_list = findViewById(R.id.books_list);
        books_list.setAdapter(adapt);
        books_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent main = new Intent(posts.this,book_info.class);
                name = book_name.get(position);
                main.putExtra("book_name",name);
                main.putExtra("category",category);
                main.putExtra("part",part);
                startActivity(main);
            }
        });

    }

    void photos(final int i){
        StorageReference stor = FirebaseStorage.getInstance().getReference().child(category).child(part);
        stor.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            public void onSuccess(Uri uri) {
                images.add(uri);
                adapt.notifyDataSetChanged();
                if(images.size()-1!= book_name.size()-1){
                    photos(images.size());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {
                Toast.makeText(posts.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
