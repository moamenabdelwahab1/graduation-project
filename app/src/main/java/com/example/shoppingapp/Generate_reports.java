package com.example.shoppingapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Generate_reports extends AppCompatActivity {

    private Spinner spinnerDay, spinnerMonth, spinnerYear;
    private Button buttonGenerate;
    private RecyclerView recyclerViewReports;
    private ReportsAdapter reportsAdapter; // Custom adapter
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_reports);

        databaseHelper = new DatabaseHelper(this);

        spinnerDay = findViewById(R.id.spinner_day);
        spinnerMonth = findViewById(R.id.spinner_month);
        spinnerYear = findViewById(R.id.spinner_year);
        buttonGenerate = findViewById(R.id.button_generate);
        recyclerViewReports = findViewById(R.id.recycler_view_reports);

        populateSpinners();

        recyclerViewReports.setLayoutManager(new LinearLayoutManager(this));
        reportsAdapter = new ReportsAdapter(new ArrayList<>());
        recyclerViewReports.setAdapter(reportsAdapter);

        buttonGenerate.setOnClickListener(v -> fetchReports());
    }

    private void populateSpinners() {
        List<String> days = new ArrayList<>();
        List<String> months = new ArrayList<>();
        List<String> years = new ArrayList<>();

        for (int i = 1; i <= 31; i++) days.add(String.valueOf(i));
        for (int i = 1; i <= 12; i++) months.add(String.valueOf(i));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 50; i <= currentYear; i++) years.add(String.valueOf(i));

        spinnerDay.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days));
        spinnerMonth.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months));
        spinnerYear.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years));
    }

    private void fetchReports() {
        String day = spinnerDay.getSelectedItem().toString();
        String month = spinnerMonth.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();
        String selectedDate = String.format("%s-%02d-%02d", year, Integer.parseInt(month), Integer.parseInt(day));

        Cursor cursor = databaseHelper.getTransactionsByDate(selectedDate);
        if (cursor != null) {
            List<Transaction> transactions = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("transaction_id"));
                int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
                String date = cursor.getString(cursor.getColumnIndex("transaction_date"));
                String profit = cursor.getString(cursor.getColumnIndex("profit"));
                transactions.add(new Transaction(id, userId, date, profit));
            }
            cursor.close();
            reportsAdapter.updateData(transactions);
        } else {
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        }
    }
}
