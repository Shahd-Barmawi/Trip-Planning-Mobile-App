package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    EditText etTitle, etCity;
    Button btnSelectDate, btnSave;
    TextView tvDate;
    RadioGroup rgTime;
    Switch switchImportant;
    CheckBox checkDone;
    Spinner spinnerType;

    SharedPreferences prefs;
    private static final String PREF_NAME = "TripPref";
    private static final String KEY_LIST = "tasks_list";

    ArrayList<TripTask> taskList;
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTitle = findViewById(R.id.etTitle);
        etCity = findViewById(R.id.etCity);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        tvDate = findViewById(R.id.tvDate);
        rgTime = findViewById(R.id.rgTime);
        switchImportant = findViewById(R.id.switchImportant);
        checkDone = findViewById(R.id.checkDone);
        btnSave = findViewById(R.id.btnSave);
        spinnerType = findViewById(R.id.spinnerType);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loadTasks();

        String[] types = {"Flight", "Hotel", "Beach", "Food", "Shopping"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                types
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        btnSelectDate.setOnClickListener(v -> openDatePicker());
        btnSave.setOnClickListener(v -> saveTask());
    }

    private void openDatePicker() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    month++;
                    selectedDate = dayOfMonth + "/" + month + "/" + year;
                    tvDate.setText(selectedDate);
                }, y, m, d
        );
        dialog.show();
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();

        if (title.isEmpty() || city.isEmpty() || selectedDate.isEmpty()) {
            Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = rgTime.getCheckedRadioButtonId();
        String time = "Morning";
        if (id == R.id.rbAfternoon) time = "Afternoon";
        else if (id == R.id.rbNight) time = "Night";

        TripTask task = new TripTask(
                title, city, selectedDate, time,
                switchImportant.isChecked(),
                checkDone.isChecked(),
                type
        );

        taskList.add(task);
        saveList();
        finish();
    }

    private void loadTasks() {
        Gson gson = new Gson();
        String json = prefs.getString(KEY_LIST, null);

        Type type = new TypeToken<ArrayList<TripTask>>(){}.getType();
        taskList = gson.fromJson(json, type);

        if (taskList == null) taskList = new ArrayList<>();
    }

    private void saveList() {
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        prefs.edit().putString(KEY_LIST, json).apply();
    }
}
