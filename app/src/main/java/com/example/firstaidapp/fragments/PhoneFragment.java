package com.example.firstaidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;

import com.example.firstaidapp.R;
import com.example.firstaidapp.helpers.Utility;

public class PhoneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageButton phoneButton1 = view.findViewById(R.id.phone_button_1);
        ImageButton phoneButton2 = view.findViewById(R.id.phone_button_2);
        ImageButton phoneButton3 = view.findViewById(R.id.phone_button_3);
        ImageButton phoneButton4 = view.findViewById(R.id.phone_button_4);
        ImageButton phoneButton5 = view.findViewById(R.id.phone_button_5);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = (String) v.getTag();
                Utility.showAlertDialog(getContext(), message);
            }
        };

        phoneButton1.setTag("112");
        phoneButton2.setTag("998");
        phoneButton3.setTag("997");
        phoneButton4.setTag("999");
        phoneButton5.setTag("986");

        phoneButton1.setOnClickListener(onClickListener);
        phoneButton2.setOnClickListener(onClickListener);
        phoneButton3.setOnClickListener(onClickListener);
        phoneButton4.setOnClickListener(onClickListener);
        phoneButton5.setOnClickListener(onClickListener);
    }
}
