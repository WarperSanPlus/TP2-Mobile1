package com.example.tp2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tp2.Logger;
import com.example.tp2.R;
import com.example.tp2.entities.MonstreEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnemyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnemyFragment extends Fragment {
    private static final String ARG_PORTRAIT = "portraitImgId";
    private static final String ARG_FRAME = "frameImgId";

    private int portraitImgId;
    private int frameImgId;
    private MonstreEntity owner;

    public EnemyFragment(MonstreEntity owner) {
        this.owner = owner;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EnemyFragment.
     */
    public static EnemyFragment newInstance(MonstreEntity monstre) {
        EnemyFragment fragment = new EnemyFragment(monstre);
        Bundle args = new Bundle();
        args.putInt(ARG_PORTRAIT, monstre.getPortraitID());
        args.putInt(ARG_FRAME, monstre.getFrameID());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            portraitImgId = getArguments().getInt(ARG_PORTRAIT);
            frameImgId = getArguments().getInt(ARG_FRAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_enemy, container, false);

        ImageView portraitImg = v.findViewById(R.id.enemyFragment_portraitImg);
        portraitImg.setImageResource(portraitImgId);

        ImageView frameImg = v.findViewById(R.id.enemyFragment_frameImg);
        frameImg.setImageResource(frameImgId);

        HealthLabel = v.findViewById(R.id.enemyFragment_healthLabel);

        setHealthLabel(owner.getHealth() + "");

        return v;
    }

    private TextView HealthLabel;
    public void setHealthLabel(String text) {
        if (HealthLabel == null) {
            Logger.log("HealthLabel is not defined.");
            return;
        }

        HealthLabel.setText(text);
    }
}