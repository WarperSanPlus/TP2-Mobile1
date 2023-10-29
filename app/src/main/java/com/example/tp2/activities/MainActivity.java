package com.example.tp2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.tp2.R;
import com.example.tp2.entities.DarknessMonster;
import com.example.tp2.entities.JoynessMonstre;
import com.example.tp2.entities.MonstreEntity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FightActivity.RNG = new Random();

        Intent c = new Intent(this, MapActivity.class);
        startActivity(c);
    }
}