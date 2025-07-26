package com.example.firstaidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstaidapp.adapters.DisplayRecyclerViewAdapter;
import com.example.firstaidapp.models.Ailment;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        TextView titleTextView = findViewById(R.id.display_title);
        TextView infoTextView = findViewById(R.id.dispaly_info);
        RecyclerView symptomsRecyclerView = findViewById(R.id.symptoms_recycler_view);
        RecyclerView rescueActivitiesRecyclerView = findViewById(R.id.rescue_activities_recycler_view);
        ImageView rescueActivitiesImageView = findViewById(R.id.display_rescue_activities_image);

        Intent intent = getIntent();
        Ailment ailment = (Ailment) intent.getSerializableExtra("Ailment");

        titleTextView.setText(ailment.getTitle());
        infoTextView.setText(ailment.getInfo());

        String[] symptomsArray = ailment.getSymptomsArray();
        symptomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DisplayRecyclerViewAdapter recyclerViewAdapter1 = new DisplayRecyclerViewAdapter(symptomsArray);
        symptomsRecyclerView.setAdapter(recyclerViewAdapter1);

        String[] rescueActivitiesArray = ailment.getRescueActivitiesArray();
        rescueActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DisplayRecyclerViewAdapter recyclerViewAdapter2 = new DisplayRecyclerViewAdapter(rescueActivitiesArray);
        rescueActivitiesRecyclerView.setAdapter(recyclerViewAdapter2);

        String resourceName = ailment.getRescueActivitiesImage();
        rescueActivitiesImageView.setImageResource(getResources().getIdentifier(resourceName, "drawable", getPackageName()));

    }


}