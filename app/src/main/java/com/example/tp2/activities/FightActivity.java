package com.example.tp2.activities;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.tp2.entities.monsters.MonstreEntity;
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

        AudioManager.playAudio(getApplicationContext(), R.raw.laugh, null);

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
        addMessage("--- " + getString(R.string.roundStarted) + " ---", null, R.drawable.info_game_icon_asset);

        if (MonstresVivants.size() == 0 || !HeroCharacter.isAlive()) {
            gameEnded(HeroCharacter.isAlive());
            return;
        }

        roundStarted();
    }

    private void gameEnded(boolean win) {
        View v = findViewById(R.id.fightActivity_mainLayout);

        int resultId = R.drawable.txt_victory;
        int soundId = R.raw.victory;

        if (win) {
            addMessage("--- " + getString(R.string.gameEnded) +" ---", null, R.drawable.info_game_icon_asset);

            resultId = R.drawable.txt_victory;
            soundId = R.raw.victory;
        }
        else {
            addMessage("--- " + getString(R.string.heroDeath) + " ---", null, R.drawable.orc_green);

            resultId = R.drawable.game_over_defeat;
            soundId = R.raw.defeat;
        }

        v.setOnClickListener(view -> {
            Intent data = new Intent();

            // --- Add Extras ---
            data.putExtra(HERO_LAST_HEALTH, HeroCharacter.getHealth());
            // ---

            setResult(RESULT_OK, data);
            finish(); // Return to previous activity
        });

        ImageView iv = findViewById(R.id.fightActivity_resultImg);
        iv.setImageResource(resultId);
        iv.setVisibility(View.VISIBLE);

        AudioManager.playAudio(this, soundId, mp -> v.setClickable(true));
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
            Logger.log("No monster were loaded.");
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

        ajouterFragment(R.id.fightActivity_gridLayout, enemyFragment);

        Logger.log("Monster '" + monstre + "' has been added !");
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

    private void monstersRound() {
        addMessage("--- " + getString(R.string.monsterRoundStarted) + " ---", null, R.drawable.orc_green);

        for (MonstreEntity monstre : MonstresVivants){
            if (!monstre.isAlive())
                continue;

            int amount = monstre.attaque();

            String message;

            if (amount > 0) {
                monstre.removeAmmo(amount);
                message = formatString(R.string.enemyDamagePlayer, monstre.getRace(), amount, HeroCharacter.getName());
            } else if (amount == 0)
                message = formatString(R.string.enemyNoAmmo, monstre.getRace());
            else
                message = formatString(R.string.enemyHealedPlayer, monstre.getRace(), HeroCharacter.getName(), -amount);

            if (message.trim().length() != 0)
                monstre.logMessage(message);

            HeroCharacter.takeDamage(amount);
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

        HeroCharacter.logMessage(formatString(R.string.heroUsedAmmo, HeroCharacter.getName(), amount));

        setEnableAmmos(false);

        int hitCount = 0;

        for (int i = 0; i < amount; i++) {
            if (HeroCharacter.obtenirChanceAttaquer() < RNG.nextDouble()) {
                HeroCharacter.logMessage(formatString(R.string.heroMissed, HeroCharacter.getName()));
                continue;
            }

            // Nombre aléatoire [0; MonstresVivatns.Length - successCount]
            int rdmIndex = RNG.nextInt(MonstresVivants.size());

            MonstreEntity monstreTouche = MonstresVivants.get(rdmIndex);

            monstreTouche.takeDamage(1);

            monstreTouche.logMessage(formatString(R.string.monsterHit, monstreTouche.getRace()));

            hitCount++;

            // Si le monstre n'est pas mort, continue
            if (monstreTouche.isAlive())
                continue;

            monstreTouche.logMessage(formatString(R.string.monsterKilled, monstreTouche.getRace(), HeroCharacter.getName()));
            killMonster(rdmIndex);

            if (MonstresVivants.size() == 0)
                break;
        }

        if (hitCount == 0)
        {
            AudioManager.playUrlAudio(
                    "https://sounds.pond5.com/weapons-bullet-large-whoosh-miss-sound-effect-152700983_nw_prev.m4a",
                    true,
                    null);
        }
        else
            HeroCharacter.removeAmmo(amount); // Update the # of ammo left

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
            HeroCharacter.logMessage(formatString(R.string.heroNoAmmo, HeroCharacter.getName()));
            HeroCharacter.removeAmmo(-1);
        }

        // Enable ammos
        setEnableAmmos(true);

        // TODO : Set Hero Icon
        addMessage("Round " + roundCount, null, R.drawable.old_skill_ranger);
        HeroCharacter.logMessage("--- " + formatString(R.string.heroRoundStart, HeroCharacter.getName()) + " ---");
    }

    // endregion
    // region In Game Log
    LinearLayout logVertical = null;

    public void addMessage(String message, Entity source, @DrawableRes int resId) {

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

        ImageView iv = in_game_log_entry.findViewById(R.id.fight_log_icon_img);
        iv.setImageResource(resId);

        logVertical.addView(in_game_log_entry, 0);
    }

    // endregion

    private String formatString(@StringRes int resId, Object...args) {
        String text = getString(resId); // Get text from id

        // Replace arguments
        // ${0} est con => Popo est con
        for (int i = 0; i < args.length; i++) {
            text = text.replace("${" + i + "}", args[i].toString());
        }
        return text;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}