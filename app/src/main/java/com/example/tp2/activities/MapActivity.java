package com.example.tp2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import com.example.tp2.R;
import com.example.tp2.entities.DarknessMonster;
import com.example.tp2.entities.Hero;
import com.example.tp2.entities.JoynessMonstre;
import com.example.tp2.entities.MonstreEntity;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button b = findViewById(R.id.activityMap_btn);
        b.setOnClickListener(v -> startFight(1));
    }

    private void startFight(int difficulty) {
        Intent toFightActivity = new Intent(this, FightActivity.class);
        toFightActivity.putExtra(FightActivity.AMMO_OPTIONS_NAME, generateOptions(difficulty)); // Add options
        toFightActivity.putExtra(FightActivity.MONSTRES_ARRAY_NAME, generateMonsters(difficulty)); // Add monsters

        toFightActivity.putExtra(FightActivity.BACKGROUND_NAME, R.drawable.bg_game_desert);
        toFightActivity.putExtra(FightActivity.HERO_OBJECT_NAME, new Hero("Bob", 3, 123, Hero.genre.Homme));

        startActivity(toFightActivity);
    }

    private ArrayList<MonstreEntity> generateMonsters(int difficulty) {
        ArrayList<MonstreEntity> monstres = new ArrayList<>();
        monstres.add(new DarknessMonster(1, 1));
        monstres.add(new JoynessMonstre(2, 3));
        monstres.add(new DarknessMonster(3, 1));
        monstres.add(new DarknessMonster(3, 2));
        return  monstres;
    }
    private byte[] generateOptions(int difficulty) {
        return new byte[] { 1, 2, 3 };
    }
}