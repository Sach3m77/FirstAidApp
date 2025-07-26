package com.example.firstaidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.firstaidapp.R;


public class CallingTheEmergencyNumberFragment extends Fragment {
    public TextView alertTextView;
    private String text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_calling_the_emergency_number, container, false);

        alertTextView = view.findViewById(R.id.alert_text_view);
        alertTextView.setText(text);

        return view;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

}