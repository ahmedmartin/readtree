package com.ahmed.martin.readtree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class parts extends AppCompatActivity {

    private String category;
    private String [] part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);

        category=getIntent().getStringExtra("category");

        switch (category){
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
            case "مواد دراسيه" :
                part=getResources().getStringArray(R.array.دراسه);
                break;
        }

        GridView category_list=findViewById(R.id.grid_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.grid_show,R.id.category,part);
        category_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent post = new Intent(parts.this,posts.class);
                post.putExtra("category",category);
                post.putExtra("part",part[position]);
                startActivity(post);
            }
        });
    }
}
