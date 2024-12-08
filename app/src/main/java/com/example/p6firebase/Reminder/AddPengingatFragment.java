package com.example.p6firebase.Reminder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.p6firebase.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class AddPengingatFragment extends Fragment {

    private static final String DBURL = "https://p6-firebase-56863-default-rtdb.firebaseio.com/";

    private ImageButton btRevert;
    private Button btSimpan;
    private TimePicker timePicker;
    private List<item_reminder> dataset;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;


    public AddPengingatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_pengingat, container, false);

        this.timePicker = v.findViewById(R.id.timePicker);
        this.btSimpan = v.findViewById(R.id.btSimpan);
        this.btRevert = v.findViewById(R.id.btRevert);
        this.dataset = new ArrayList<>();
        this.db = FirebaseDatabase.getInstance(DBURL);

        this.dbRef = this.db.getReference("reminder");


        Bundle bundle = getArguments();
        final boolean modeEdit;
        final String editID;

        if (bundle != null && bundle.containsKey("id")) {
            modeEdit = true;
            editID = bundle.getString("id");
            int hour = bundle.getInt("hour");
            int minute = bundle.getInt("minute");
            boolean status = bundle.getBoolean("status");
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            modeEdit = false;
            editID = null;
        }
        btRevert.setOnClickListener(view -> getActivity().getSupportFragmentManager().popBackStack());
        btSimpan.setOnClickListener(view ->{
            int hour = this.timePicker.getHour();
            int minute = this.timePicker.getMinute();
            String jam = String.format("%02d:%02d", hour, minute);
            if(modeEdit){
                item_reminder item = new item_reminder();
                item.setId(editID);
                item.setJam(jam);
                item.setStatus(true);

                this.dbRef.child(editID).setValue(item);
                Toast.makeText(getContext(), "Ubah pengingat: " + item.getJam(), Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }else{
                String id = this.dbRef.push().getKey();
                item_reminder item = new item_reminder();
                item.setId(id);
                item.setJam(jam);
                item.setStatus(true);
                this.dbRef.child(id).setValue(item);
                getActivity().onBackPressed();
            }

        });
        return v;
    }

}