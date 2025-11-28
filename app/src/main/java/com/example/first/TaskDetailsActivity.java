package com.example.first;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TaskDetailsActivity extends AppCompatActivity {

    SharedPreferences prefs;
    ArrayList<TripTask> taskList;
    int index;

    ImageView imgIcon;
    TextView tvTitle, tvCity, tvDate, tvTime, tvImportant, tvDone;
    Button btnEdit, btnDelete;

    private static final int EDIT_REQUEST = 100;
    private static final String PREF_NAME = "TripPref";
    private static final String KEY_LIST = "tasks_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        imgIcon = findViewById(R.id.imgTask);
        tvTitle = findViewById(R.id.tvTitle);
        tvCity = findViewById(R.id.tvCity);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvImportant = findViewById(R.id.tvImp);
        tvDone = findViewById(R.id.tvDone);

        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        index = getIntent().getIntExtra("index", -1);
        if (index == -1) finish();

        loadTask();

        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(TaskDetailsActivity.this, EditTaskActivity.class);
            i.putExtra("index", index);
            startActivityForResult(i, EDIT_REQUEST);
        });

        btnDelete.setOnClickListener(v -> {

            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(TaskDetailsActivity.this);

            builder.setTitle("Delete Trip")
                    .setMessage("Are you sure you want to delete this trip?")
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        taskList.remove(index);
                        saveList();
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.parseColor("#D9534F"));   // أحمر

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#C2A280"));   // بيج
        });


    }

    @SuppressLint("SetTextI18n")
    private void loadTask() {

        Gson gson = new Gson();
        String json = prefs.getString(KEY_LIST, null);

        Type type = new TypeToken<ArrayList<TripTask>>(){}.getType();
        taskList = gson.fromJson(json, type);

        TripTask t = taskList.get(index);

        tvTitle.setText("Title: " + t.getTitle());
        tvCity.setText("City: " + t.getCity());
        tvDate.setText("Date: " + t.getDate());
        tvTime.setText("Time: " + t.getTimeOfDay());
        tvImportant.setText("Important: " + (t.isImportant() ? "Yes" : "No"));
        tvDone.setText("Done: " + (t.isDone() ? "Yes" : "No"));

        switch (t.getType()) {
            case "Flight": imgIcon.setImageResource(R.drawable.ic_flight); break;
            case "Hotel": imgIcon.setImageResource(R.drawable.ic_hotel); break;
            case "Beach": imgIcon.setImageResource(R.drawable.ic_beach); break;
            case "Food": imgIcon.setImageResource(R.drawable.ic_food); break;
            case "Shopping": imgIcon.setImageResource(R.drawable.ic_shopping); break;
            default: imgIcon.setImageResource(R.drawable.ic_task); break;
        }
    }

    private void saveList() {
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        prefs.edit().putString(KEY_LIST, json).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            loadTask();
        }
    }
}
