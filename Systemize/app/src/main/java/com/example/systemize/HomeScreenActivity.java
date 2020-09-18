package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ArrayList<TaskItem> taskList = new ArrayList<>();

        taskList.add(new TaskItem("Title here", R.drawable.ic_baseline_home_24));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ListAdapter listAdapter = new ListAdapter(taskList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);
    }
}