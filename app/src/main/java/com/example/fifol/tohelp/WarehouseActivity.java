package com.example.fifol.tohelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.GridView;

public class WarehouseActivity extends AppCompatActivity {

    GridLayout gridLayout;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        gridLayout = findViewById(R.id.gridLayout);
        gridView = findViewById(R.id.gridView);

    }
}
