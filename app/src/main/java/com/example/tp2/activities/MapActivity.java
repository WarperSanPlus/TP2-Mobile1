package com.example.tp2.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tp2.R;
import com.example.tp2.entities.monsters.DarknessMonster;
import com.example.tp2.entities.Hero;
import com.example.tp2.entities.monsters.JoynessMonstre;
import com.example.tp2.entities.monsters.MonstreEntity;
import com.example.tp2.entities.monsters.StonageMonster;
import com.example.tp2.managers.BackgroundManager;

import java.util.ArrayList;
import java.util.Random;

public class MapActivity extends AppCompatActivity {
    public static final int EASY_DIFFICULTY = 0;
    public static final int MEDIUM_DIFFICULTY = 2;
    public static final int HARD_DIFFICULTY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        loadBundle(getIntent().getExtras());

        FightActivity.RNG = new Random();
    }

    private void loadBundle(Bundle bundle) {
        loadGameStats(bundle);
        loadHero(bundle);
    }

    // region Fight
    public static final String GAME_DIFFICULTY = "gameDifficulty";
    public static final String GAME_WORLD = "gameWorld";

    private void startFight(int difficulty, BackgroundManager.World world) {
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

    private void loadGameStats(Bundle bundle) {
        int chosenDifficulty = bundle.getInt(GAME_DIFFICULTY, MEDIUM_DIFFICULTY);
        BackgroundManager.World world = (BackgroundManager.World) bundle.getSerializable(GAME_WORLD);

        Button b = findViewById(R.id.mapActivity_btn);
        b.setOnClickListener(v -> startFight(chosenDifficulty, world));

        ImageView bg = findViewById(R.id.fightActivity_backgroundImg);
        bg.setImageResource(BackgroundManager.getBackgroundImage(world, true));
    }
    // endregion
    // region Monsters
    private ArrayList<MonstreEntity> generateMonsters(int difficulty) {
        ArrayList<MonstreEntity> monstres = new ArrayList<>();
        monstres.add(new DarknessMonster(1, 2));
        monstres.add(new JoynessMonstre(2, 3));
        monstres.add(new DarknessMonster(3, 1));
        monstres.add(new DarknessMonster(3, 2));
        monstres.add(new DarknessMonster(3, 2));
        monstres.add(new DarknessMonster(3, 2));
        monstres.add(new DarknessMonster(3, 2));
        monstres.add(new StonageMonster(3, 2));
        return  monstres;
    }
    // endregion
    // region Hero
    public static final String HERO_NAME = "heroName";
    public static final String HERO_HEALTH = "heroHealth";

    private Hero HeroCharacter;

    private void loadHero(Bundle bundle) {
        String heroName = bundle.getString(HERO_NAME);
        int heroHealth = bundle.getInt(HERO_HEALTH);

        HeroCharacter = new Hero(heroName, 10, heroHealth); // PLACEHOLDERS
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

    // endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK)
            return;

        // --- Update Hero Health ---
        int h = data.getIntExtra(FightActivity.HERO_LAST_HEALTH, HeroCharacter.getHealth());

        if (h <= 0) { // If hero is dead
            finish(); // Return to previous activity
        }

        HeroCharacter.setHealth(h);
        // ---

        super.onActivityResult(requestCode, resultCode, data);
    }
}