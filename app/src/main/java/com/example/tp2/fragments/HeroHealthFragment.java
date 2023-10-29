package com.example.tp2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tp2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeroHealthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeroHealthFragment extends Fragment {
    private static final String ARG_HEALTH = "health";

    private String health;

    public HeroHealthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HeroHealthFragment.
     */
    public static HeroHealthFragment newInstance(String health) {
        HeroHealthFragment fragment = new HeroHealthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HEALTH, health);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            health = getArguments().getString(ARG_HEALTH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hero_health, container, false);

        HealthLabel = v.findViewById(R.id.heroHealthFragment_healthTxt);
        updateHealthLabel(health + "");

        return v;
    }
    private TextView HealthLabel;
    public void updateHealthLabel(String text){
        HealthLabel.setText(text);
    }
}