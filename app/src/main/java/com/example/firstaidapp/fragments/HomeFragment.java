package com.example.firstaidapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.firstaidapp.FirstAidStepsActivity;
import com.example.firstaidapp.R;

public class HomeFragment extends Fragment {

    private MapFragment mMapFragment = new MapFragment();
    private PhoneFragment mPhoneFragment = new PhoneFragment();

    private OtherAilmentsFragment mOtherAilmentsFragment = new OtherAilmentsFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView firstAidCardView = view.findViewById(R.id.first_aid_card_view);
        firstAidCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FirstAidStepsActivity.class);
                startActivity(intent);
            }
        });

        CardView aedCardView = view.findViewById(R.id.nearest_aed_card_view);
        aedCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, mMapFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        CardView emergencyNumbersCardView = view.findViewById(R.id.emergency_numbers_card_view);
        emergencyNumbersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, mPhoneFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        CardView otherAilmentsCardView = view.findViewById(R.id.other_ailments_card_view);
        otherAilmentsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, mOtherAilmentsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

}
