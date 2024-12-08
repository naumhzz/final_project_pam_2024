package com.example.p6firebase.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p6firebase.R;
import com.example.p6firebase.SharedViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentBawah extends Fragment {

    private static final String DBURL = "https://p6-firebase-56863-default-rtdb.firebaseio.com/";

    private Button btRiwayat;
    private RecyclerView rvRiwayat;
    private AirAdapter adapterAir;
    private List<Air> daftar;
    private SharedViewModel viewModel;

    public FragmentBawah() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daftar = new ArrayList<>();
        adapterAir = new AirAdapter(requireContext(), daftar);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bawah, container, false);

        rvRiwayat = v.findViewById(R.id.rvRiwayat);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRiwayat.setAdapter(adapterAir);

        btRiwayat = v.findViewById(R.id.btRiwayat);
        btRiwayat.setOnClickListener(this::tampilkanRiwayat);

        return v;
    }

    public void tampilkanRiwayat(View view) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        FirebaseDatabase database = FirebaseDatabase.getInstance(DBURL);
        DatabaseReference reference = database.getReference("Air");

        reference.orderByChild("tanggal").equalTo(today).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                daftar.clear();
                int totalIntake = 0;

                for (DataSnapshot data : snapshot.getChildren()) {
                    Air air = data.getValue(Air.class);
                    if (air != null) {
                        daftar.add(air);
                        totalIntake += Integer.parseInt(air.getJumlahAir().replace("ML", ""));
                    }
                }

                adapterAir.notifyDataSetChanged();
                viewModel.setCurrentIntake(totalIntake);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
