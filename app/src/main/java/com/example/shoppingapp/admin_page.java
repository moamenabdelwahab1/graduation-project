package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class admin_page extends AppCompatActivity {

    private Button addProductButton, editProductButton, deleteProductButton;
    private Button addCategoryButton, editCategoryButton, deleteCategoryButton;
    private Button generateReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        // Initialize buttons
        addProductButton = findViewById(R.id.addProductButton);
        editProductButton = findViewById(R.id.editProductButton);
        deleteProductButton = findViewById(R.id.deleteProductButton);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        editCategoryButton = findViewById(R.id.editCategoryButton);
        deleteCategoryButton = findViewById(R.id.deleteCategoryButton);
        generateReportButton = findViewById(R.id.generateReportButton);

        // Set button click listeners
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_page.this, Add_product.class);
                startActivity(intent);
            }
        });

        editProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_page.this, Edit_product.class);
                startActivity(intent);
            }
        });

        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_page.this, Delete_product.class);
                startActivity(intent);
            }
        });

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_page.this, add_category.class);
                startActivity(intent);
            }
        });

        editCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_page.this, edit_category.class);
                startActivity(intent);
            }
        });

        deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_page.this, delete_category.class);
                startActivity(intent);
            }
        });

        generateReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_page.this, Generate_reports.class);
                startActivity(intent);
            }
        });
    }
}
