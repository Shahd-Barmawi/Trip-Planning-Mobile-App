package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    EditText etTitle, etCity;
    Button btnSelectDate, btnUpdate, btnDelete;
    TextView tvDate;
    RadioGroup rgTime;
    Switch switchImportant;
    CheckBox checkDone;
    Spinner spinnerType;

    SharedPreferences prefs;
    private static final String PREF_NAME = "TripPref";
    private static final String KEY_LIST = "tasks_list";

    ArrayList<TripTask> taskList;
    int index;
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTitle = findViewById(R.id.etTitle);
        etCity = findViewById(R.id.etCity);
        tvDate = findViewById(R.id.tvDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        rgTime = findViewById(R.id.rgTime);
        switchImportant = findViewById(R.id.switchImportant);
        checkDone = findViewById(R.id.checkDone);
        spinnerType = findViewById(R.id.spinnerType);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loadList();

        index = getIntent().getIntExtra("index", -1);
        if (index == -1) finish();

        TripTask t = taskList.get(index);

        etTitle.setText(t.getTitle());
        etCity.setText(t.getCity());
        tvDate.setText(t.getDate());
        selectedDate = t.getDate();

        switch (t.getTimeOfDay()) {
            case "Morning": rgTime.check(R.id.rbMorning); break;
            case "Afternoon": rgTime.check(R.id.rbAfternoon); break;
            case "Night": rgTime.check(R.id.rbNight); break;
        }

        switchImportant.setChecked(t.isImportant());
        checkDone.setChecked(t.isDone());

        String[] types = {"Flight", "Hotel", "Beach", "Food", "Shopping"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                types
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        int typeIndex = Arrays.asList(types).indexOf(t.getType());
        spinnerType.setSelection(typeIndex);

        btnSelectDate.setOnClickListener(v -> pickDate());
        btnUpdate.setOnClickListener(v -> updateTask());
        btnDelete.setOnClickListener(v -> deleteTask());
    }

    private void pickDate() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    month++;
                    selectedDate = day + "/" + month + "/" + year;
                    tvDate.setText(selectedDate);
                },
                y, m, d
        );
        dialog.show();
    }

    private void updateTask() {
        TripTask t = taskList.get(index);

        t.setTitle(etTitle.getText().toString());
        t.setCity(etCity.getText().toString());
        t.setDate(selectedDate);

        int id = rgTime.getCheckedRadioButtonId();
        if (id == R.id.rbAfternoon) t.setTimeOfDay("Afternoon");
        else if (id == R.id.rbNight) t.setTimeOfDay("Night");
        else t.setTimeOfDay("Morning");

        t.setImportant(switchImportant.isChecked());
        t.setDone(checkDone.isChecked());

        t.setType(spinnerType.getSelectedItem().toString());

        saveList();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updated", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void deleteTask() {
        taskList.remove(index);
        saveList();
        finish();
    }

    private void loadList() {
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
