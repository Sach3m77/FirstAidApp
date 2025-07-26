package com.example.firstaidapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firstaidapp.R;

public class DisplayRecyclerViewAdapter extends RecyclerView.Adapter<DisplayRecyclerViewAdapter.MyViewHolder>{

    private String[] mArray;

    public DisplayRecyclerViewAdapter(String[] array) {
        this.mArray = array;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(mArray[position]);
    }

    @Override
    public int getItemCount() {
        return mArray.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.single_row_text_view);
        }
    }
}
