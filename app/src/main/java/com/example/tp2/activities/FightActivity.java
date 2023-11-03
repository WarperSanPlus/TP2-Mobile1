package com.example.tp2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tp2.Config;
import com.example.tp2.entities.Entity;
import com.example.tp2.managers.AudioManager;
import com.example.tp2.Logger;
import com.example.tp2.R;
import com.example.tp2.entities.Hero;
import com.example.tp2.entities.MonstreEntity;
import com.example.tp2.fragments.AmmoFragment;
import com.example.tp2.fragments.EnemyFragment;
import com.example.tp2.fragments.HeroStatFragment;

import java.util.ArrayList;
import java.util.Random;

public class FightActivity extends AppCompatActivity {
    public static FightActivity Instance;
    public static Random RNG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        Instance = this;

        loadBundles(getIntent().getExtras());

        roundStarted();
    }

    private void loadBundles(Bundle bundle) {
        loadAmmos(bundle);
        loadMonstres(bundle);
        loadBackground(bundle);
        loadHero(bundle);
    }

    private void ajouterFragment(int containerViewId, Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerViewId, frag).commit();
    }

    // region Round Manager
    private void roundStarted() {
        heroRound();
    }

    private void roundEnded() {
        addMessage("--- The round has ended ---", null);

        if (MonstresVivants.size() == 0) {
            gameEnded();
            return;
        }

        roundStarted();
    }

    private void gameEnded() {
        addMessage("--- The game has ended ---", null);

        View v = findViewById(R.id.fightActivity_mainLayout);
        v.setOnClickListener(view -> {
            Intent data = new Intent();

            // --- Add Extras ---
            data.putExtra(HERO_LAST_HEALTH, HeroCharacter.getHealth());
            // ---

            setResult(RESULT_OK,data);
            finish(); // Return to previous activity
        });

        findViewById(R.id.fightActivity_victoryImg).setVisibility(View.VISIBLE);

        AudioManager.playAudio(this, R.raw.victory, mp -> v.setClickable(true));
    }

    // endregion

    // region Monstres
    private ArrayList<MonstreEntity> MonstresVivants = new ArrayList<>();
    private ArrayList<View> monstersViewRows = new ArrayList<>();
    public static final String MONSTRES_ARRAY_NAME = "monstresArray";

    private void loadMonstres(Bundle bundle){
        MonstresVivants = bundle == null ?
                new ArrayList<>() :
                (ArrayList<MonstreEntity>) bundle.getSerializable(MONSTRES_ARRAY_NAME);

        if (MonstresVivants == null) {
            Logger.log("Aucun monstre n'a été chargés.");
            return;
        }

        for (MonstreEntity mons : MonstresVivants) {
            addMonster(mons);
        }
    }

    private void addMonster(MonstreEntity monstre) {
        // Créer le fragment pour le monstre
        EnemyFragment enemyFragment = EnemyFragment.newInstance(monstre);
        monstre.setDisplayFragment(enemyFragment);

        ajouterFragment(R.id.fightActivity_rowPlaceholder, enemyFragment);

        Logger.log("Monstre '" + monstre + "' ajouté !");
    }

    private void killMonster(int index) {
        int lastIndex = MonstresVivants.size() - 1;

        MonstreEntity monstreMort = MonstresVivants.get(index);

        // Mettre le monstre touché à la dernière position
        MonstresVivants.set(index, MonstresVivants.get(lastIndex));
        MonstresVivants.set(lastIndex, monstreMort);

        // Créer la transaction pour supprimer des monstres
        FragmentTransaction removeFragmentTransaction = getSupportFragmentManager().beginTransaction();

        removeFragmentTransaction.remove(monstreMort.getDisplayFragment());

        removeFragmentTransaction.commit();

        // Supprimer le monstre
        MonstresVivants.remove(MonstresVivants.size() - 1);
    }

    // TODO : Row management
    // Add view row
    // Remove view row
    // Move monsters in view rows

    private void monstersRound() {
        addMessage("--- Monsters' round started ---", null);

        for (MonstreEntity monstre : MonstresVivants){
            if (!monstre.isAlive())
                continue;

            HeroCharacter.takeDamage(monstre.attaque());
        }

        roundEnded();
    }

    // endregion

    // region Ammo
    public static final String AMMO_OPTIONS_NAME = "ammoOptions";

    private AmmoFragment[] ammoFragments;
    private void loadAmmos(Bundle bundle) {
        byte[] options = bundle.getByteArray(AMMO_OPTIONS_NAME);

        ammoFragments = new AmmoFragment[options.length];

        for (int i = 0; i < options.length; i++) {
            AmmoFragment newFrag = AmmoFragment.newInstance(options[i]);

            ajouterFragment(R.id.fightActivity_ammoLayout, newFrag);
            ammoFragments[i] = newFrag;
        }
    }

    private void setEnableAmmos(boolean isEnabled) {
        int maxAmount = HeroCharacter.getAmmoLeft();

        for (AmmoFragment frag : ammoFragments) {
            frag.setEnableFragment(isEnabled && frag.getAmmoAmount() <= maxAmount);
        }
    }

    public void useAmmo(byte amount) {
        if (MonstresVivants.size() == 0)
            return;

        HeroCharacter.logMessage(HeroCharacter.getName() + " used " + amount + " ammo(s).");

        setEnableAmmos(false);

        int ammoUsed = 0;

        for (int i = 0; i < amount; i++) {
            if (HeroCharacter.obtenirChanceAttaquer() < RNG.nextDouble()) {
                HeroCharacter.logMessage(HeroCharacter.getName() + " missed!");
                continue;
            }

            // Nombre aléatoire [0; MonstresVivatns.Length - successCount]
            int rdmIndex = RNG.nextInt(MonstresVivants.size());

            MonstreEntity monstreTouche = MonstresVivants.get(rdmIndex);

            monstreTouche.takeDamage(1);

            monstreTouche.logMessage(monstreTouche.getRace() + " got hit!");

            ammoUsed++;

            // Si le monstre n'est pas mort, continue
            if (monstreTouche.isAlive())
                continue;

            monstreTouche.logMessage(monstreTouche.getRace() + " got killed by " + HeroCharacter.getName() + ".");
            killMonster(rdmIndex);

            if (MonstresVivants.size() == 0)
                break;
        }

        if (ammoUsed == 0)
        {
            AudioManager.playUrlAudio(
                    "https://sounds.pond5.com/weapons-bullet-large-whoosh-miss-sound-effect-152700983_nw_prev.m4a",
                    true,
                    null);
        }
        else
            HeroCharacter.removeAmmo(ammoUsed); // Update the # of ammo left

        monstersRound(); // Monsters' turn
    }
    // endregion

    // region Background
    public static final String BACKGROUND_ID = "bgGameId";

    private void loadBackground(Bundle bundle) {
        ImageView bg = findViewById(R.id.fightActivity_backgroundImg);
        bg.setImageResource(bundle.getInt(BACKGROUND_ID));
    }
    // endregion

    // region Hero
    private int roundCount = 0;

    public static final String HERO_OBJECT_NAME = "heroCharacter";
    public static final String HERO_LAST_HEALTH = "heroHealth";

    private Hero HeroCharacter;

    private void loadHero(Bundle bundle) {
        int statsId = R.id.fightActivity_playerStatsLayout;

        HeroCharacter = (Hero) bundle.getSerializable(HERO_OBJECT_NAME);

        if (HeroCharacter == null)
            return;

        // Health
        HeroStatFragment heroHealthFragment = HeroStatFragment.newInstance(HeroCharacter.getHealth() + "", R.drawable.heart);
        HeroCharacter.setDisplayFragment(heroHealthFragment);
        ajouterFragment(statsId, heroHealthFragment);
        // ---

        // Ammo
        HeroStatFragment heroAmmoFragment = HeroStatFragment.newInstance(HeroCharacter.getAmmoLeft() + "", R.drawable.old_skill_ranger);
        HeroCharacter.setAmmoStatFragment(heroAmmoFragment);
        ajouterFragment(statsId, heroAmmoFragment);
        // ---
    }

    private void heroRound() {
        roundCount++;

        // If the player can't play, give 1 ammo
        if(!HeroCharacter.canPlay())
        {
            HeroCharacter.logMessage(HeroCharacter.getName() + " ran out of ammo... so we gave one more!");
            HeroCharacter.removeAmmo(-1);
        }

        // Enable ammos
        setEnableAmmos(true);

        addMessage("Round " + roundCount, null);
        HeroCharacter.logMessage("--- " + HeroCharacter.getName() + "'s turn started ---");
    }

    // endregion

    // region In Game Log
    LinearLayout logVertical = null;

    public void addMessage(String message, Entity source) {

        if (logVertical == null)
            logVertical = findViewById(R.id.fightActivity_logVertical);

        if (logVertical == null) {
            Logger.log("Couldn't find fightActivity_logVertical");
            return;
        }

        View in_game_log_entry = getLayoutInflater().inflate(R.layout.fight_in_game_log_entry, null);
        TextView tv = in_game_log_entry.findViewById(R.id.fight_log_entry_label);

        if (Config.SHOW_UUID_LOG && source != null)
            message = "[" + source.getUUID() + "] " + message;

        tv.setText(message);

        logVertical.addView(in_game_log_entry, 0);
    }

    // endregion

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}