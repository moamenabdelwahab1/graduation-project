package com.example.shoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class add_category extends AppCompatActivity {

    private EditText categoryNameEditText;
    private Button addCategoryButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        addCategoryButton = findViewById(R.id.addCategoryButton);

        databaseHelper = new DatabaseHelper(this);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryNameEditText.getText().toString().trim();
                if (categoryName.isEmpty()) {
                    Toast.makeText(add_category.this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (databaseHelper.addCategory(categoryName)) {
                        Toast.makeText(add_category.this, "Category added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(add_category.this, "Failed to add category", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
