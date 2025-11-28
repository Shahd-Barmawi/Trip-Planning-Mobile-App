package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerTasks;
    EditText etSearch;
    Button btnAdd, btnLogout;

    ArrayList<TripTask> taskList;
    TripTaskAdapter adapter;

    SharedPreferences prefs;
    private static final String PREF_NAME = "TripPref";
    private static final String KEY_LIST = "tasks_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.google.android.material.color.DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#C2A280"));


        recyclerTasks = findViewById(R.id.recyclerTasks);
        etSearch = findViewById(R.id.etSearch);
        btnAdd = findViewById(R.id.btnAdd);
        btnLogout = findViewById(R.id.btnLogout);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        loadTasks();

        adapter = new TripTaskAdapter(taskList, position -> {
            Intent i = new Intent(MainActivity.this, TaskDetailsActivity.class);
            i.putExtra("index", position);
            startActivity(i);
        });

        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(i);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences loginPrefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
            loginPrefs.edit().putBoolean("logged_in", false).apply();

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
        adapter.update(taskList);
    }

    private void loadTasks() {
        Gson gson = new Gson();
        String json = prefs.getString(KEY_LIST, null);

        Type type = new TypeToken<ArrayList<TripTask>>(){}.getType();
        taskList = gson.fromJson(json, type);

        if (taskList == null) {
            taskList = new ArrayList<>();

            taskList.add(new TripTask(
                    "Flight to Paris", "Paris",
                    "10/11/2025", "Morning",
                    true, false,
                    "Flight"
            ));

            taskList.add(new TripTask(
                    "Hotel Check-in", "London",
                    "12/11/2025", "Afternoon",
                    false, false,
                    "Hotel"
            ));

            taskList.add(new TripTask(
                    "Beach Tour", "Dubai",
                    "15/11/2025", "Night",
                    false, false,
                    "Beach"
            ));

            saveTasks();
        }
    }

    private void saveTasks() {
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        prefs.edit().putString(KEY_LIST, json).apply();
    }
}
