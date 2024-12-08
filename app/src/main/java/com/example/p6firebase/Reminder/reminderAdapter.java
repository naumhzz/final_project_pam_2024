package com.example.p6firebase.Reminder;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p6firebase.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class reminderAdapter extends RecyclerView.Adapter{

    private final Context ctx;
    private List<item_reminder> dataset;
    private DatabaseReference dbRef;

    public reminderAdapter(Context ctx, List<item_reminder> dataset) {
        this.ctx = ctx;
        this.dataset = dataset;

    }

    public void setDbRed(DatabaseReference dbRef){
        this.dbRef = dbRef;
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvJam;
        private final TextView tvStatus;
        private final ImageButton imageButton2;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvJam = itemView.findViewById(R.id.tvJam);
            imageButton2 = itemView.findViewById(R.id.imageButton2);

            imageButton2.setOnClickListener(this);
        }
        public void bind(item_reminder item){
            tvJam.setText(item.getJam());
            String status = item.isStatus() ? "Aktif" : "Tidak Aktif";
            tvStatus.setText(status);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                showOptionsDialog(position);
            }
        };

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.ctx).inflate(R.layout.recycle_row, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        vh.bind(dataset.get(position));

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    private void showOptionsDialog(int position) {
        item_reminder item = dataset.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Pilih Aksi")
                .setItems(new CharSequence[]{"Ubah", "Hapus", "Ubah Status"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Bundle bundle = new Bundle();
                            bundle.putString("id", item.getId());
                            bundle.putString("jam", item.getJam());

                            AddPengingatFragment fragment = new AddPengingatFragment();
                            fragment.setArguments(bundle);

                            FragmentTransaction transaction = ((AppCompatActivity) ctx).getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.container_frag, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;
                        case 1:
                            dbRef.child(item.getId()).removeValue();
                            dataset.remove(position);
                            Toast.makeText(ctx, "Hapus pengingat: " + item.getJam(), Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            boolean statusPengignat = !item.isStatus();
                            item.setStatus(statusPengignat);
                            dbRef.child(item.getId()).child("status").setValue(statusPengignat);
                            String status = statusPengignat ? "Aktif" : "Tidak Aktif";
                            Toast.makeText(ctx, "Pengingat: " + item.getJam() + " diubah menjadi " + status, Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            break;
                    }
                })
                .create()
                .show();
    }
    public void updateData(List<item_reminder> newDataset) {
        this.dataset = newDataset;
        notifyDataSetChanged();
    }

}
