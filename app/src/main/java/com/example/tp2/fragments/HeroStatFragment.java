package com.example.tp2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tp2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeroStatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeroStatFragment extends Fragment {
    private static final String ARG_TEXT = "text";
    private static final String ARG_IMAGE_ID = "imgId";

    private String text;
    private int imgId;

    public HeroStatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HeroHealthFragment.
     */
    public static HeroStatFragment newInstance(String text, int imgId) {
        HeroStatFragment fragment = new HeroStatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_IMAGE_ID, imgId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text = getArguments().getString(ARG_TEXT);
            imgId = getArguments().getInt(ARG_IMAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hero_stat, container, false);

        StatLabel = v.findViewById(R.id.heroStatFragment_txt);
        updateStatLabel(text);

        ImageView iv = v.findViewById(R.id.heroStatFragment_img);
        iv.setImageResource(imgId);

        return v;
    }
    private TextView StatLabel;
    public void updateStatLabel(String text){
        StatLabel.setText(text);
    }
}