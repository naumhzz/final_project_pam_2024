package com.example.p6firebase.Main;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p6firebase.R;
import com.example.p6firebase.Usecase2.item_riwayat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AirAdapter extends RecyclerView.Adapter<AirAdapter.VH> {

    private static final String DBURL = "https://p6-firebase-56863-default-rtdb.firebaseio.com/";

    private final Context ctx;
    private final List<Air> daftar;
    private DatabaseReference databaseReference;

    public AirAdapter(@NonNull Context ctx, List<Air> daftar) {
        this.ctx = ctx;
        this.daftar = daftar;
        databaseReference = FirebaseDatabase.getInstance(DBURL).getReference("Air");
    }

    public class VH extends RecyclerView.ViewHolder {
        private final TextView tvWaktu;
        private final TextView tvJumlahAir;
        private final ImageView gambarGelas;
        private final ImageButton menu;
        private Air airItem;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvWaktu = itemView.findViewById(R.id.tvWaktu);
            tvJumlahAir = itemView.findViewById(R.id.tvJumlahAir);
            gambarGelas = itemView.findViewById(R.id.ivGelas);
            menu = itemView.findViewById(R.id.btMenu);

            menu.setOnClickListener(this::showPopUpMenu);
        }

        private void showPopUpMenu(View view) {
            PopupMenu popup = new PopupMenu(ctx, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int position = getAdapterPosition();
                if (item.getItemId() == R.id.btUbah) {
                    showNumberInputDialog();
                    return true;
                } else if (item.getItemId() == R.id.btHapus) {
                    if (position != RecyclerView.NO_POSITION) {
                        deleteAir(airItem);
                    }
                    return true;
                }
                return false;
            });
            popup.show();
        }

        private void showNumberInputDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Edit Jumlah Air");

            final EditText input = new EditText(ctx);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setText(airItem.getJumlahAir().replace("ML", ""));
            builder.setView(input);

            builder.setPositiveButton("Simpan", (dialog, which) -> {
                String jumlahAir = input.getText().toString();
                if (!jumlahAir.isEmpty()) {
                    airItem.setJumlahAir(jumlahAir + "ML");
                    updateAir(airItem);
                    notifyItemChanged(getAdapterPosition());
                }
            });

            builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
            builder.show();
        }

        public void setAirItem(Air airItem) {
            this.airItem = airItem;
        }
    }

    private void updateAir(Air air) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(DBURL);
        DatabaseReference reference = database.getReference("Air");

        if (air.isSudahSum()) {
            air.setSudahSum(false);
        } else {
            air.setSudahSum(true);
        }

        reference.child(air.getId()).setValue(air).addOnSuccessListener(aVoid -> {
            Toast.makeText(ctx, "Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(ctx, "Gagal Memperbarui", Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteAir(Air air) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(DBURL);
        DatabaseReference reference = database.getReference("Air");

        reference.child(air.getId()).removeValue().addOnSuccessListener(aVoid -> {
            int position = daftar.indexOf(air);
            if (position != RecyclerView.NO_POSITION) {
                daftar.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, daftar.size());
                Toast.makeText(ctx, "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(ctx, "Gagal Menghapus", Toast.LENGTH_SHORT).show();
        });
    }



    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.ctx).inflate(R.layout.modifikasi_air, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Air air = this.daftar.get(position);
        holder.setAirItem(air);
        holder.tvWaktu.setText(air.getWaktu());
        holder.tvJumlahAir.setText(air.getJumlahAir());
        holder.gambarGelas.setImageResource(R.drawable.gambar_gelas);
        holder.menu.setImageResource(R.drawable.logo_menu);
    }

    @Override
    public int getItemCount() {
        return this.daftar.size();
    }
}
