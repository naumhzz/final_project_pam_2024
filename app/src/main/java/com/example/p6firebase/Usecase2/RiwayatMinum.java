package com.example.p6firebase.Usecase2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p6firebase.Main.Air;
import com.example.p6firebase.Main.MainActivity;
import com.example.p6firebase.R;
import com.example.p6firebase.Reminder.HalamanSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiwayatMinum extends AppCompatActivity {

    private RecyclerView recyclerView;
    private riwayatAdapter adapter;
    private List<item_riwayat> riwayatList;
    private DatabaseReference airRef;
    private DatabaseReference riwayatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_minum);

        recyclerView = findViewById(R.id.rvHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        riwayatList = new ArrayList<>();
        adapter = new riwayatAdapter(this, riwayatList);
        recyclerView.setAdapter(adapter);

        airRef = FirebaseDatabase.getInstance().getReference("Air");
        riwayatRef = FirebaseDatabase.getInstance().getReference("Riwayat");

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(RiwayatMinum.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_history) {
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(RiwayatMinum.this, HalamanSettings.class));
                return true;
            }
            return false;
        });

        loadAndSaveRiwayat();
    }

    private void loadAndSaveRiwayat() {
        airRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> totalAirPerTanggal = new HashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Air air = snapshot.getValue(Air.class);
                    if (air != null && !air.isSudahSum()) {
                        String tanggal = air.getTanggal();
                        String jumlahAirStr = air.getJumlahAir().replaceAll("[^0-9]", "");
                        int jumlahAir = Integer.parseInt(jumlahAirStr);

                        totalAirPerTanggal.put(tanggal, totalAirPerTanggal.getOrDefault(tanggal, 0) + jumlahAir);

                            snapshot.getRef().child("sudahSum").setValue(true);

                    }
                }

                for (Map.Entry<String, Integer> entry : totalAirPerTanggal.entrySet()) {
                    String tanggal = entry.getKey();
                    int totalAir = entry.getValue();

                    riwayatRef.child(tanggal).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            item_riwayat riwayat = dataSnapshot.getValue(item_riwayat.class);
                            if (riwayat != null) {
                                int totalAirSekarang = Integer.parseInt(riwayat.getTotalAir().replaceAll("[^0-9]", ""));
                                int totalAirBaru = totalAirSekarang + totalAir;
                                riwayat.setTotalAir(totalAirBaru + " ML");
                                riwayatRef.child(tanggal).setValue(riwayat);

                            } else {
                                String id = riwayatRef.push().getKey();
                                item_riwayat newRiwayat = new item_riwayat();
                                newRiwayat.setId(id);
                                newRiwayat.setTanggal(tanggal);
                                newRiwayat.setTotalAir(totalAir + " ML");
                                riwayatRef.child(tanggal).setValue(newRiwayat);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

                fetchRiwayat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void fetchRiwayat() {
        riwayatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                riwayatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    item_riwayat riwayat = snapshot.getValue(item_riwayat.class);
                    if (riwayat != null) {
                        riwayatList.add(riwayat);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RiwayatMinum.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}