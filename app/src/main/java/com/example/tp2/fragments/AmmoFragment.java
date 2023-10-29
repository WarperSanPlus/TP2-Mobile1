// (YYYY/MM/DD) [AUTHOR] : MODIFICATION
// (2023/10/28) [SAMUEL GAUTHIER] : Initialisation of the class

package com.example.tp2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tp2.activities.FightActivity;
import com.example.tp2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AmmoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmmoFragment extends Fragment {
    private static final String ARG_AMMO_AMOUNT = "ammoAmount";

    private byte ammoAmount;

    public byte getAmmoAmount() { return ammoAmount; }

    public AmmoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AmmoFragment.
     */
    public static AmmoFragment newInstance(byte ammoAmount) {
        AmmoFragment fragment = new AmmoFragment();
        Bundle args = new Bundle();
        args.putByte(ARG_AMMO_AMOUNT, ammoAmount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ammoAmount = getArguments().getByte(ARG_AMMO_AMOUNT, (byte) 1);
        }
    }

    private ImageButton AmmoSlotBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ammo, container, false);

        TextView ammoTV = v.findViewById(R.id.ammoFragment_ammoText);
        ammoTV.setText(ammoAmount + "");

        AmmoSlotBtn = v.findViewById(R.id.ammoFragment_ammoBtn);
        AmmoSlotBtn.setOnClickListener(view -> {
            FightActivity.Instance.useAmmo(ammoAmount);
        });

        return v;
    }

    public void setEnableFragment(boolean isEnable){
        AmmoSlotBtn.setEnabled(isEnable);
    }
}