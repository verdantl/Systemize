package com.example.systemize;

import android.app.Dialog;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;

public abstract class ListFragment extends Fragment {

    public abstract void saveCompletion(TaskItem taskItem);

    public void showFragment(TaskItem taskItem){
        Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setContentView(R.layout.task_info_dialog);

        dialog.findViewById(R.id.layout).setBackgroundColor(taskItem.getColor());

        TextView task = dialog.findViewById(R.id.task_name);
        task.setText(taskItem.getTitle());

        TextView duration = dialog.findViewById(R.id.duration);
        duration.setText(taskItem.getDuration());

        TextView date = dialog.findViewById(R.id.calendar_day);
        LocalDate newDate = LocalDate.parse(taskItem.getDate());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, newDate.getYear());
        c.set(Calendar.MONTH, newDate.getMonthValue());
        c.set(Calendar.DAY_OF_MONTH, newDate.getDayOfMonth());
        date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));

        ImageView category = dialog.findViewById(R.id.category);
        category.setImageResource(taskItem.getCategoryImage());
        dialog.show();
    }
}
