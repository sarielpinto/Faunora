package com.example.faunora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.faunora.Detector1.Main2Activity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    ImageView bgapp, clover;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;
    FloatingActionButton circulo;
    BottomAppBar barra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        barra=(BottomAppBar) findViewById(R.id.bar);
        circulo=(FloatingActionButton)findViewById(R.id.fab);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);


        bgapp.animate().translationY(-2300).setDuration(1800).setStartDelay(1800);
        clover.animate().alpha(0).setDuration(1800).setStartDelay(1600);
        textsplash.animate().translationY(140).alpha(0).setDuration(1800).setStartDelay(1300);

        texthome.startAnimation(frombottom);
        circulo.startAnimation(frombottom);
        menus.startAnimation(frombottom);
        barra.startAnimation(frombottom);

    }
        public void pasar_detector1(View view){
        Intent intent =new Intent(this, Main2Activity.class);
        startActivity(intent);
        }
    }

