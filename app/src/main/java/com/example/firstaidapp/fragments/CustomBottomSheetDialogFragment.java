package com.example.firstaidapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.firstaidapp.R;
import com.example.firstaidapp.adapters.RecyclerViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private BottomSheetDialogFragment1 mBottomSheetDialogFragment1;
    private BottomSheetDialogFragment2 mBottomSheetDialogFragment2;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    public CustomBottomSheetDialogFragment(BottomSheetDialogFragment1 bottomSheetDialogFragment1, BottomSheetDialogFragment2 bottomSheetDialogFragment2, RecyclerViewAdapter recyclerViewAdapter) {
        this.mBottomSheetDialogFragment1 = bottomSheetDialogFragment1;
        this.mBottomSheetDialogFragment2 = bottomSheetDialogFragment2;
        this.mRecyclerViewAdapter = recyclerViewAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mBottomSheetDialogFragment1 != null) {
            mBottomSheetDialogFragment1.setRecyclerViewAdapter(mRecyclerViewAdapter);
            mBottomSheetDialogFragment1.setmBottomSheetDialogFragment2(mBottomSheetDialogFragment2);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.bottom_sheet_dialog_fragment_container, mBottomSheetDialogFragment1)
                    .addToBackStack(null)
                    .commit();
        }
    }

}
