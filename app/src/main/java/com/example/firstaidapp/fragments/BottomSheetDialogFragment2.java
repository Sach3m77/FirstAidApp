package com.example.firstaidapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.firstaidapp.R;
import com.example.firstaidapp.adapters.RecyclerViewAdapter;
import com.example.firstaidapp.helpers.DatabaseHelper;
import com.example.firstaidapp.models.Ailment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BottomSheetDialogFragment2 extends Fragment {
    private DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    private List<Ailment> mAilmentList;
    private String[] mSymptomsArray;
    private ArrayAdapter<String> mAdapter;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ImageView mCloseImageView;
    private ImageView mBackImageView;
    private Button mSubmitButton;
    private CustomBottomSheetDialogFragment mParentFragment;
    private BottomSheetDialog mBottomSheetDialog;
    private boolean isCloseIconVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog2, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabaseHelper = new DatabaseHelper(getContext());

        File database = getContext().getDatabasePath(DatabaseHelper.DBNAME);

        if (database.exists() == false) {

            mDatabaseHelper.getReadableDatabase();

            if (!mDatabaseHelper.copyDatabase()) {
                return;
            }

        }

        mAilmentList = new ArrayList<>();

        mParentFragment = (CustomBottomSheetDialogFragment) getParentFragment();

        if (mParentFragment != null) {
            mBottomSheetDialog = (BottomSheetDialog) mParentFragment.getDialog();
        }

        setBottomSheetDialogBehavior();

        mSymptomsArray = mDatabaseHelper.getSymtomsDistinct();

        mBackImageView = view.findViewById(R.id.back_to_fragment_sheet_dialog1);
        mListView = view.findViewById(R.id.symptoms_list_view);
        mCloseImageView = view.findViewById(R.id.close_button);
        mSubmitButton = view.findViewById(R.id.submit_button2);

        if (isCloseIconVisible) {
            mCloseImageView.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_multiple_choice, mSymptomsArray);
        }

        mListView.setAdapter(mAdapter);

        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.clearChoices();
                mAdapter.notifyDataSetChanged();
                mCloseImageView.setVisibility(View.GONE);
                isCloseIconVisible = false;
            }
        });

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCloseImageView.setVisibility(View.VISIBLE);
                isCloseIconVisible = true;
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> selectedSymptosmList = new ArrayList<>();

                for (int i = 0; i < mListView.getCount(); i++) {
                    if (mListView.isItemChecked(i)) {
                        selectedSymptosmList.add(mListView.getItemAtPosition(i).toString());
                    }
                }

                String[] selectedSymptomsArray = selectedSymptosmList.toArray(new String[0]);

                if (selectedSymptomsArray.length > 0) {
                    mAilmentList = mDatabaseHelper.getDataWhereSymptomsLike(selectedSymptomsArray);
                    mRecyclerViewAdapter.updateList(mAilmentList);

                    if (mBottomSheetDialog != null) {
                        mBottomSheetDialog.dismiss();
                    }
                } else {

                    if (mRecyclerViewAdapter.getItemCount() != mDatabaseHelper.getDataCount()) {
                        mAilmentList = mDatabaseHelper.getData();
                        mRecyclerViewAdapter.updateList(mAilmentList);
                    }

                    if (mBottomSheetDialog != null) {
                        mBottomSheetDialog.dismiss();
                    }
                }

            }
        });

    }

    private void setBottomSheetDialogBehavior() {
        if (mBottomSheetDialog != null) {
            FrameLayout bottomSheet = mBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setDraggable(false);
            }
        }
    }

    public void setRecyclerViewAdapter(RecyclerViewAdapter adapter) {
        mRecyclerViewAdapter = adapter;
    }
}
