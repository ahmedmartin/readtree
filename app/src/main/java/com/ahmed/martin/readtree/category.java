package com.ahmed.martin.readtree;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class category extends AppCompatActivity {

    private DrawerLayout dl ;
    private ActionBarDrawerToggle abdt;
    private Menu menu;
    private ViewFlipper flipper;
    private String [] category ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        flipper=findViewById(R.id.flipper);
        category= this.getResources().getStringArray(R.array.مجالات);

        ListView category_list=findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.list_show,R.id.category,category);
        category_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent post = new Intent(category.this,parts.class);
                post.putExtra("category",category[position]);
                startActivity(post);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        dl =findViewById(R.id.draw_layout);
        abdt = new ActionBarDrawerToggle(this,dl,toolbar,R.string.open,R.string.close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav = findViewById(R.id.navigation);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                switch (id){
                    case R.id.new_post:
                        new_post();
                        break;

                    case R.id.history :
                        history();
                        break;

                    case R.id.person_info :
                        personal_info();
                        break;

                    case R.id.sign_in :
                        sign_in();
                        break;

                    case R.id.log_out :
                        log_out();
                        break;

                    case R.id.about_us :
                        about_us();
                        break;

                    case R.id.saving :
                        saving();
                        break;
                }

                return true;
            }
        });

         menu=nav.getMenu();
        MenuItem tools= menu.findItem(R.id.new_post);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);

        MenuItem log_out = menu.findItem(R.id.log_out);
        SpannableString ss = new SpannableString(log_out.getTitle());
        ss.setSpan(new TextAppearanceSpan(this, R.style.Text), 0, ss.length(), 0);
        log_out.setTitle(ss);



    }



    @Override
    protected void onStart() {
        super.onStart();

        if(getInstance().getCurrentUser()==null) {
            MenuItem history = menu.findItem(R.id.history);
            history.setVisible(false);

            MenuItem log_out = menu.findItem(R.id.log_out);
            log_out.setVisible(false);

            MenuItem personal_info = menu.findItem(R.id.person_info);
            personal_info.setVisible(false);

            MenuItem sign_in = menu.findItem(R.id.sign_in);
            sign_in.setVisible(true);
        }else {
            MenuItem history = menu.findItem(R.id.history);
            history.setVisible(true);

            MenuItem log_out = menu.findItem(R.id.log_out);
            log_out.setVisible(true);

            MenuItem personal_info = menu.findItem(R.id.person_info);
            personal_info.setVisible(true);

            MenuItem sign_in = menu.findItem(R.id.sign_in);
            sign_in.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void new_post() {

        if(connected_networt())
        startActivity(new Intent(this,new_post.class));
        else
            Toast.makeText(this, "يجب الاتصال بالانترنت....!!", Toast.LENGTH_LONG).show();
    }

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

    private void history() {
        startActivity(new Intent(this,history.class));
    }

    private void saving() { startActivity(new Intent(this,saving.class));}

    public void personal_info() {
        startActivity(new Intent(this,personal_info.class));
    }

    private void sign_in() {
        startActivity(new Intent(this,sign_in.class));
    }

    private void log_out() {

        FirebaseAuth mauth=FirebaseAuth.getInstance();
        mauth.signOut();

        MenuItem history = menu.findItem(R.id.history);
        history.setVisible(false);

        MenuItem log_out = menu.findItem(R.id.log_out);
        log_out.setVisible(false);

        MenuItem personal_info = menu.findItem(R.id.person_info);
        personal_info.setVisible(false);

        MenuItem sign_in = menu.findItem(R.id.sign_in);
        sign_in.setVisible(true);


    }

    private void about_us() {
        startActivity(new Intent(this,about_us.class));
    }

    private void flipper(int image){
        ImageView img = new ImageView(this);
        img.setImageResource(image);
        flipper.addView(img);
        flipper.setFlipInterval(1000);
        flipper.setAutoStart(true);
        flipper.setInAnimation(this,android.R.anim.slide_in_left);
        flipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }


}
