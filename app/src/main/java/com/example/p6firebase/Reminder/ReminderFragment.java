package com.example.p6firebase.Reminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.p6firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReminderFragment extends Fragment {

    private static final String DBURL = "https://p6-firebase-56863-default-rtdb.firebaseio.com/";

    private RecyclerView rvRemind;
    private List<item_reminder> dataset;
    private reminderAdapter reminderAdapter;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public ReminderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder, container, false);

        rvRemind = v.findViewById(R.id.rvRemind);
        dataset = new ArrayList<>();
        reminderAdapter = new reminderAdapter(getContext(), dataset);

        db = FirebaseDatabase.getInstance(DBURL);
        dbRef = db.getReference("reminder");
        reminderAdapter.setDbRed(this.dbRef);

        rvRemind.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRemind.setAdapter(reminderAdapter);

        getData();


        return v;
    }

    private void getData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<item_reminder> dataset = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    item_reminder item = ds.getValue(item_reminder.class);
                    dataset.add(item);
                }

                reminderAdapter.updateData(dataset);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}