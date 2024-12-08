package com.example.p6firebase.Reminder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.p6firebase.R;

public class PengingatMinum extends AppCompatActivity {

    private Button btTampilkan;
    private ImageButton btTambah;
    private ImageButton btBalik;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengingat_minum);


        btTampilkan = findViewById(R.id.btTampilkan);
        btTambah = findViewById(R.id.btTambah);
        btBalik = findViewById(R.id.btBalik);
        btBalik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }});
        this.fm = getSupportFragmentManager();

        btTampilkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .replace(R.id.container_frag, new ReminderFragment())
                        .commit();
            }
        });
        btTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .replace(R.id.container_frag, new AddPengingatFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
