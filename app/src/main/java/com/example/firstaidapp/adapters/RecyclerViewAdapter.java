package com.example.firstaidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.DisplayActivity;
import com.example.firstaidapp.R;
import com.example.firstaidapp.helpers.AilmentTitleComparatorAsc;
import com.example.firstaidapp.helpers.AilmentTitleComparatorDesc;
import com.example.firstaidapp.models.Ailment;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Ailment> mAilmentsList;
    private Context mContext;

    public RecyclerViewAdapter(List<Ailment> list, Context context) {
        mAilmentsList = list;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String resourceName = mAilmentsList.get(position).getImage();
        holder.mImageView.setImageResource(mContext.getResources().getIdentifier(resourceName, "drawable", mContext.getPackageName()));

        holder.mTextView.setText(mAilmentsList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mAilmentsList.size();
    }

    public void updateList(List<Ailment> newList) {
        mAilmentsList = newList;
        notifyDataSetChanged();
    }

    public void sortList(String sortType) {

        AilmentTitleComparatorAsc ailmentTitleComparatorAsc = new AilmentTitleComparatorAsc();
        AilmentTitleComparatorDesc ailmentTitleComparatorDesc = new AilmentTitleComparatorDesc();

        if (sortType == "ASC") {
            mAilmentsList.sort(ailmentTitleComparatorAsc);
        } else if (sortType == "DESC") {
            mAilmentsList.sort(ailmentTitleComparatorDesc);
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.single_view_image_view);
            mTextView = itemView.findViewById(R.id.single_view_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DisplayActivity.class);
            intent.putExtra("Ailment", mAilmentsList.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}
