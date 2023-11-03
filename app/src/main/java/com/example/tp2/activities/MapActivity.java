package com.example.tp2.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tp2.R;
import com.example.tp2.entities.DarknessMonster;
import com.example.tp2.entities.Hero;
import com.example.tp2.entities.JoynessMonstre;
import com.example.tp2.entities.MonstreEntity;
import com.example.tp2.managers.BackgroundManager;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    public static final int EASY_DIFFICULTY = 0;
    public static final int MEDIUM_DIFFICULTY = 2;
    public static final int HARD_DIFFICULTY = 4;

    BackgroundManager.World world = BackgroundManager.World.DESERT;
    Hero HeroCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button b = findViewById(R.id.mapActivity_btn);
        b.setOnClickListener(v -> startFight(HARD_DIFFICULTY));

        ImageView bg = findViewById(R.id.fightActivity_backgroundImg);
        bg.setImageResource(BackgroundManager.getBackgroundImage(world, true));

        HeroCharacter = new Hero("Bob", 3, 123);
    }

    private void startFight(int difficulty) {
        Intent toFightActivity = new Intent(this, FightActivity.class);
        toFightActivity.putExtra(FightActivity.AMMO_OPTIONS_NAME, generateOptions(difficulty)); // Add options
        toFightActivity.putExtra(FightActivity.MONSTRES_ARRAY_NAME, generateMonsters(difficulty)); // Add monsters

        toFightActivity.putExtra(FightActivity.BACKGROUND_ID, BackgroundManager.getBackgroundImage(world, false));

        // --- Hero ---
        HeroCharacter.setAmmoLeft(getAmmos(difficulty));
        toFightActivity.putExtra(FightActivity.HERO_OBJECT_NAME, HeroCharacter); // Pass hero reference
        // ---

        startActivityForResult(toFightActivity, 1);
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
        byte[] options;

        if (difficulty == EASY_DIFFICULTY)
            options = new byte[] { 1, 3, 5 };
        else if (difficulty == MEDIUM_DIFFICULTY)
            options = new byte[] { 1, 2, 3 };
        else if (difficulty == HARD_DIFFICULTY)
            options = new byte[] { 1 };
        else {
            options = new byte[] { 1, 2, 3};
        }
        return options;
    }

    private int getAmmos(int difficulty) {
        int amount;

        if (difficulty == EASY_DIFFICULTY)
            amount = 20;
        else if (difficulty == MEDIUM_DIFFICULTY)
            amount = 15;
        else if (difficulty == HARD_DIFFICULTY)
            amount = 10;
        else {
            amount = Math.max(20 - difficulty / 2 * 5, 1);
        }
        return amount;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK)
            return;

        // --- Update Hero Health ---
        int h = data.getIntExtra(FightActivity.HERO_LAST_HEALTH, HeroCharacter.getHealth());

        HeroCharacter.setHealth(h);
        // ---

        super.onActivityResult(requestCode, resultCode, data);
    }
}