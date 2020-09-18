package com.example.systemize;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter listAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        ArrayList<TaskItem> taskList = new ArrayList<>();

        taskList.add(new TaskItem("Title here", R.drawable.ic_baseline_home_24));

        taskList.add(new TaskItem("Title2 here", R.drawable.ic_baseline_home_24));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new ListAdapter(taskList, getResources().getFont(R.font.futura_medium));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);

        return view;
    }

}
