package com.example.p6firebase.Usecase2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p6firebase.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class riwayatAdapter extends RecyclerView.Adapter<riwayatAdapter.VH> {

    private List<item_riwayat> dataset;
    private Context ctx;

    public riwayatAdapter(Context ctx, List<item_riwayat> dataset) {
        this.ctx = ctx;
        this.dataset = dataset;
    }

    public class VH extends RecyclerView.ViewHolder {
        private final TextView tvTanggal;
        private final TextView tvTotalAir;
        private final TextView tvHari;
        private final TextView tvTercapai;
        private final Button btDelete;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvTotalAir = itemView.findViewById(R.id.tvTotalAir);
            tvHari = itemView.findViewById(R.id.tvHari);
            tvTercapai = itemView.findViewById(R.id.tvTercapai);
            btDelete = itemView.findViewById(R.id.btDelete);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.history_row, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        item_riwayat riwayat = dataset.get(position);
        holder.tvTanggal.setText(riwayat.getTanggal());
        SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));
        try {
            Date date = formatTanggal.parse(riwayat.getTanggal());

            SimpleDateFormat formatHari = new SimpleDateFormat("EEEE", new Locale("id", "ID"));
            String hari = formatHari.format(date);

            holder.tvHari.setText(hari);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvHari.setText("");
        }
        String totalAir = riwayat.getTotalAir().replaceAll("[^0-9]", "");
        int target = 2000;
        if (Integer.parseInt(totalAir) >= target) {
            holder.tvTercapai.setText("Tercapai");
        } else {
            holder.tvTercapai.setText("Tidak Tercapai");
        }

        holder.tvTotalAir.setText(riwayat.getTotalAir());

        holder.btDelete.setOnClickListener(view -> {
            String id = riwayat.getId();
            DatabaseReference riwayatRef = FirebaseDatabase.getInstance().getReference("Riwayat").child(riwayat.getTanggal());
            riwayatRef.removeValue();
            dataset.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
