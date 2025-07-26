package com.example.firstaidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.firstaidapp.R;
import com.example.firstaidapp.adapters.RecyclerViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class BottomSheetDialogFragment1 extends Fragment {
    private BottomSheetDialogFragment2 mBottomSheetDialogFragment2;
    private ChipGroup mChipGroup;
    private RelativeLayout mLinearLayout;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private CustomBottomSheetDialogFragment mParentFragment;
    private BottomSheetDialog mBottomSheetDialog;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChipGroup = view.findViewById(R.id.chip_group);
        mLinearLayout = view.findViewById(R.id.bottom_sheet_dialog1_linear_layout);
        mButton = view.findViewById(R.id.submit_button1);

        mParentFragment = (CustomBottomSheetDialogFragment) getParentFragment();

        if (mParentFragment != null) {
            mBottomSheetDialog = (BottomSheetDialog) mParentFragment.getDialog();
        }

        mChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, List<Integer> checkedIds) {

                if (checkedIds.size() > 0) {

                    Chip checkedChip = group.findViewById(checkedIds.get(0));

                    if (checkedChip != null) {
                        switch (checkedChip.getId()) {
                            case R.id.chip_asc:
                                mRecyclerViewAdapter.sortList("ASC");
                                break;
                            case R.id.chip_desc:
                                mRecyclerViewAdapter.sortList("DESC");
                                break;
                        }

                    }
                }
            }
        });

        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBottomSheetDialogFragment2 != null) {
                    mBottomSheetDialogFragment2.setRecyclerViewAdapter(mRecyclerViewAdapter);

                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.bottom_sheet_dialog_fragment_container, mBottomSheetDialogFragment2)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.dismiss();
                }
            }
        });
    }

    public void setRecyclerViewAdapter(RecyclerViewAdapter adapter) {
        mRecyclerViewAdapter = adapter;
    }

    public void setmBottomSheetDialogFragment2(BottomSheetDialogFragment2 bottomSheetDialogFragment2) {
        mBottomSheetDialogFragment2 = bottomSheetDialogFragment2;
    }
}
