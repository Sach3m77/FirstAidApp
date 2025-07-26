package com.example.firstaidapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.R;
import com.example.firstaidapp.adapters.RecyclerViewAdapter;
import com.example.firstaidapp.helpers.DatabaseHelper;
import com.example.firstaidapp.models.Ailment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OtherAilmentsFragment extends Fragment {
    private CustomBottomSheetDialogFragment mCustomBottomSheetDialogFragment;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private EditText mEditText;
    private DatabaseHelper mDatabaseHelper;
    private ImageView mImageView;
    private List<Ailment> mAilmentsList;
    private List<Ailment> mFilteredAilmentsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other_ailments, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabaseHelper = new DatabaseHelper(getContext());

        File database = getContext().getDatabasePath(DatabaseHelper.DBNAME);

        if (database.exists() == false) {

            mDatabaseHelper.getReadableDatabase();

            if (!mDatabaseHelper.copyDatabase()) {
                return;
            }

        }

        mAilmentsList = mDatabaseHelper.getData();

        mFilteredAilmentsList = new ArrayList<>(mAilmentsList);

        mEditText = view.findViewById(R.id.edit_text);
        mImageView = view.findViewById(R.id.filter_button);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerViewAdapter = new RecyclerViewAdapter(mAilmentsList, getContext());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setHasFixedSize(true);

        mCustomBottomSheetDialogFragment = new CustomBottomSheetDialogFragment(new BottomSheetDialogFragment1(), new BottomSheetDialogFragment2(), mRecyclerViewAdapter);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterAilments(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCustomBottomSheetDialogFragment != null) {
                    mCustomBottomSheetDialogFragment.show(getParentFragmentManager(), mCustomBottomSheetDialogFragment.getTag());
                }

            }
        });

    }

    private void filterAilments(String query) {
        mFilteredAilmentsList.clear();

        for (Ailment ailment : mAilmentsList) {
            if (ailment.getTitle().toLowerCase().contains(query.toLowerCase())) {
                mFilteredAilmentsList.add(ailment);
            }
        }
        mRecyclerViewAdapter.updateList(mFilteredAilmentsList);
    }

}

