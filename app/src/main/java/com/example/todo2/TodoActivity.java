package com.example.todo2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TodoActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonSetDate;
    private TextView textViewDueDate;
    private RadioGroup radioGroupCategory;
    private Button buttonAddTask;
    private ListView listViewTasks;
    private ArrayAdapter<String> tasksAdapter;
    private ArrayList<String> tasksList;
    private SimpleDateFormat dateFormatter;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


        dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        calendar = Calendar.getInstance();

        editTextTask = findViewById(R.id.editTextTask);
        buttonSetDate = findViewById(R.id.buttonSetDate);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        radioGroupCategory = findViewById(R.id.radioGroupCategory);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        listViewTasks = findViewById(R.id.listViewTasks);


        tasksList = new ArrayList<>();
        tasksAdapter = new ArrayAdapter<>(this, R.layout.list_item_task, R.id.textViewTask, tasksList);
        listViewTasks.setAdapter(tasksAdapter);

        buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog();
            }
        });

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String taskWithDate = tasksList.get(position);
                Toast.makeText(TodoActivity.this, "Task: " + taskWithDate, Toast.LENGTH_SHORT).show();
            }
        });


        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TodoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showDateTimeDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog timePickerDialog = new TimePickerDialog(TodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        String formattedDate = dateFormatter.format(calendar.getTime());
                        textViewDueDate.setText(formattedDate);
                        textViewDueDate.setVisibility(View.VISIBLE);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void addTask() {
        String task = editTextTask.getText().toString();
        RadioButton selectedRadioButton = findViewById(radioGroupCategory.getCheckedRadioButtonId());
        String category = selectedRadioButton.getText().toString();
        String dueDate = textViewDueDate.getText().toString();

        if (task.isEmpty() || dueDate.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskWithCategory = task + " (" + category + ")";
        String taskWithDateAndCategory = taskWithCategory + " (Due: " + dueDate + ")";
        tasksList.add(taskWithDateAndCategory);
        tasksAdapter.notifyDataSetChanged();

        editTextTask.setText("");
        textViewDueDate.setText("");
        textViewDueDate.setVisibility(View.GONE);

        Toast.makeText(this, "Task added: " + taskWithDateAndCategory, Toast.LENGTH_SHORT).show();
    }
}
