package com.example.fitnesssecond;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class GecmisAdapter extends RecyclerView.Adapter<GecmisAdapter.GecmisHolder> {

    private ArrayList<Map<String, Object>> dataList;
    private Context context;

    public GecmisAdapter(ArrayList<Map<String, Object>> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public GecmisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new GecmisHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GecmisHolder holder, int position) {
        Map<String, Object> gecmisData = dataList.get(position);

        String sabahOgun = (String) gecmisData.get("sabah");
        String ogleOgun = (String) gecmisData.get("ogle");
        String aksamOgun = (String) gecmisData.get("aksam");
        String tarih = (gecmisData.get("selectedDate") != null) ? (String) gecmisData.get("selectedDate") : "Belirtilmedi";
        Long toplamKalori = (Long) gecmisData.get("toplamKalori");

        holder.sabahogun.setText("Sabah: " + sabahOgun);
        holder.ogleogun.setText("Öğle: " + ogleOgun);
        holder.aksamogun.setText("Akşam: " + aksamOgun);
        holder.tarih.setText(tarih);
        holder.toplamkaloriler.setText("Toplam Kalori: " + String.valueOf(toplamKalori));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class GecmisHolder extends RecyclerView.ViewHolder {

        TextView sabahogun, ogleogun, aksamogun, tarih, toplamkaloriler;

        public GecmisHolder(@NonNull View itemView) {
            super(itemView);
            sabahogun = itemView.findViewById(R.id.textSabahYemek);
            ogleogun = itemView.findViewById(R.id.textOgleYemek);
            aksamogun = itemView.findViewById(R.id.textAksamYemek);
            tarih = itemView.findViewById(R.id.textTarih);
            toplamkaloriler = itemView.findViewById(R.id.textToplamKalori);
        }
    }
}
