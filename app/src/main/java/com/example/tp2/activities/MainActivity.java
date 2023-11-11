package com.example.tp2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp2.R;
import com.example.tp2.managers.BackgroundManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button readyBtn = findViewById(R.id.mainActivity_readyBtn);
        readyBtn.setOnClickListener(v -> checkStartGame());

        generateDifficulties();
        setUpLifeCounter();
    }

    // region Start Game
    private boolean gameStarted = false;

    private void checkStartGame() {
        if (difficulty <= -1) {
            Toast.makeText(this, getString(R.string.invalidDifficuly), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isHealthValid()) {
            Toast.makeText(this, getString(R.string.invalidStartHealth), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isHeroNameValid()) {
            Toast.makeText(this, getString(R.string.invalidHeroName), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!gameStarted)
            startGame();
    }

    private void startGame() {
        gameStarted = true;

        Bundle extras = new Bundle();

        extras.putString(MapActivity.HERO_NAME, heroName);
        extras.putInt(MapActivity.HERO_HEALTH, health);

        extras.putInt(MapActivity.GAME_DIFFICULTY, difficulty);
        extras.putSerializable(MapActivity.GAME_WORLD, BackgroundManager.World.DESERT);

        AdActivity.startActivityWithAd(this, MapActivity.class, extras);
        gameStarted = false;
    }
    // endregion
    // region Difficulty
    private int difficulty = -1;

    private void setDifficulty(int amount) {
        difficulty = Math.max(amount, 0);
        setDifficultyHealth(difficulty);
    }

    private void generateDifficulties() {
        ArrayList<Pair<Integer, Integer>> difficulties = new ArrayList<>();

        difficulties.add(new Pair<>(MapActivity.EASY_DIFFICULTY, R.string.easyDifficulty));
        difficulties.add(new Pair<>(MapActivity.MEDIUM_DIFFICULTY, R.string.mediumDifficulty));
        difficulties.add(new Pair<>(MapActivity.HARD_DIFFICULTY, R.string.hardDifficulty));

        generateDifficulties(difficulties);
    }
    private void generateDifficulties(ArrayList<Pair<Integer, Integer>> difficulties) {
        RadioGroup difficultyRG = findViewById(R.id.mainActivity_difficultyRG);
        LayoutInflater inflater = getLayoutInflater();

        int counter = 0;
        for (Pair<Integer, Integer> difficulty : difficulties) {
            ConstraintLayout newDifficulty = inflater.inflate(R.layout.activity_main_difficulty_slot, null).findViewById(R.id.difficultyRadioMain);

            RadioButton difficultyRB = newDifficulty.findViewById(R.id.difficultyRadioBtn);
            difficultyRB.setText(difficulty.second);
            difficultyRB.setOnClickListener(v -> setDifficulty(difficulty.first));
            difficultyRB.setId(counter);

            newDifficulty.removeView(difficultyRB);
            difficultyRG.addView(difficultyRB);

            counter++;
        }
    }

    private void setDifficultyHealth(int difficulty) {
        int amount = health;

        if (difficulty == MapActivity.EASY_DIFFICULTY)
            amount = 100;
        else if (difficulty == MapActivity.MEDIUM_DIFFICULTY)
            amount = 50;
        else if (difficulty == MapActivity.HARD_DIFFICULTY)
            amount = 25;

        if (lifeSlider != null)
            lifeSlider.setProgress(amount);
    }
    // endregion
    // region Hero Health
    private TextView lifeCounterLabel;
    private SeekBar lifeSlider;
    private int health;

    private void setHealth(int amount) {
        health = amount;

        lifeCounterLabel.setText(health + "");
    }

    private void setUpLifeCounter() {
        lifeCounterLabel = findViewById(R.id.mainActivity_lifeCounterLabel);
        lifeSlider = findViewById(R.id.mainActivity_lifeCounterSlider);

        lifeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setHealth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        lifeSlider.setMin(1);
        lifeSlider.setMax(200);
        lifeSlider.setProgress((lifeSlider.getMax() + lifeSlider.getMin()) / 2);
    }

    private boolean isHealthValid() {
        return health > 0;
    }
    // endregion
    // region Hero Name
    private String heroName;
    private TextView heroNameTV;

    private boolean isHeroNameValid() {
        if (heroNameTV == null) {
            heroNameTV = findViewById(R.id.mainActivity_nameET);
        }

        heroName = heroNameTV.getText().toString();

        return heroName.trim().length() != 0;
    }
    // endregion
}