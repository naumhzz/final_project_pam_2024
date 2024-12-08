package com.example.p6firebase.Reminder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.p6firebase.Main.MainActivity;
import com.example.p6firebase.R;
import com.example.p6firebase.Usecase2.RiwayatMinum;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HalamanSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_settings);

        ImageView backArrow = findViewById(R.id.btBalik);
        backArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(HalamanSettings.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_history) {
                startActivity(new Intent(HalamanSettings.this, RiwayatMinum.class));
                return true;
            } else if (itemId == R.id.nav_settings) {
                return true;
            }
            return false;
        });

        ConstraintLayout reminder = findViewById(R.id.constraintLayoutReminder);
        reminder.setOnClickListener(v -> {
            Intent intent = new Intent(HalamanSettings.this, PengingatMinum.class);
            startActivity(intent);
        });
                }
    }
