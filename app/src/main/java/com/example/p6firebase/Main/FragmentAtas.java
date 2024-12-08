package com.example.p6firebase.Main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.p6firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentAtas extends Fragment {

    private static final String DBURL = "https://p6-firebase-56863-default-rtdb.firebaseio.com/";

    private TextView tvJumlah, tvTarget;
    private ImageButton btTambah;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    private int currentIntake = 0;
    private int targetIntake = 2000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_atas, container, false);

        tvJumlah = v.findViewById(R.id.tvJumlah);
        tvTarget = v.findViewById(R.id.tvTarget);
        progressBar = v.findViewById(R.id.progressBar);
        btTambah = v.findViewById(R.id.btTambah);

        databaseReference = FirebaseDatabase.getInstance(DBURL).getReference("Air");

        tvTarget.setText(targetIntake + " ML");
        progressBar.setMax(targetIntake);

        loadFirebaseData();

        btTambah.setOnClickListener(view -> showNumberInputDialog());
        return v;
    }

    private void loadFirebaseData() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        databaseReference.orderByChild("tanggal").equalTo(today).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentIntake = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    Air air = data.getValue(Air.class);
                    if (air != null) {
                        currentIntake += Integer.parseInt(air.getJumlahAir().replace("ML", ""));
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNumberInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Masukkan Jumlah Air");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String jumlahAir = input.getText().toString();
            if (!jumlahAir.isEmpty() && jumlahAir.matches("\\d+")) {
                int jumlah = Integer.parseInt(jumlahAir);
                addWater(jumlah);
            } else {
                Toast.makeText(getContext(), "Masukkan angka yang valid", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addWater(int amount) {
        String id = databaseReference.push().getKey();

        if (id != null) {
            String tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String waktu = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            Air air = new Air();
            air.setId(id);
            air.setWaktu(waktu);
            air.setJumlahAir(amount + "ML");
            air.setTanggal(tanggal);

            databaseReference.child(id).setValue(air)
                    .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show());
                    loadFirebaseData();
        }
    }

    private void updateUI() {
        tvJumlah.setText(currentIntake + " ML");
        progressBar.setProgress(currentIntake);
    }
}