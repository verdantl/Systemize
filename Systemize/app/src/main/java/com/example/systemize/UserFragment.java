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

public class UserFragment extends Fragment {
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ArrayList<TaskItem> taskList;
    private String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        buildRecyclerView(view);

        return view;
    }

    private void buildRecyclerView(View view){
        recyclerView = view.findViewById(R.id.upcoming_tasks);
//        readDatabase();
        taskList = new ArrayList<>();
        taskList.add(new TaskItem(1, "Title 1","", false ));
        taskList.add(new TaskItem(1, "Title 2","", true ));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new ListAdapter(taskList, getResources().getFont(R.font.futura_medium));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                taskList.get(position).changeCompleted();
                System.out.println(taskList.get(position).getTitle());
                System.out.println(taskList.get(position).getCompleted());
            }
        });
    }
}
