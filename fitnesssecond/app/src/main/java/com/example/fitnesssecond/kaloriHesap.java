package com.example.fitnesssecond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class kaloriHesap extends AppCompatActivity {

    SeekBar seekBar;
    Spinner spinnerSabah, spinnerOgle, spinnerAksam;
    ArrayAdapter<CharSequence> adapterSabah;
    ArrayAdapter<CharSequence> adapterOgle;
    ArrayAdapter<CharSequence> adapterAksam;
    ArrayList<Food> foodListsabah = new ArrayList<>();
    ArrayList<Food> foodListogle = new ArrayList<>();
    ArrayList<Food> foodListaksam = new ArrayList<>();
    TextView kalori;
    Button hesaplayici;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userId;

    void init() {
        spinnerSabah = findViewById(R.id.sabah);
        spinnerOgle = findViewById(R.id.ogle);
        spinnerAksam = findViewById(R.id.aksam);
        adapterSabah = ArrayAdapter.createFromResource(this, R.array.SabahFood, android.R.layout.simple_spinner_item);
        adapterSabah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSabah.setAdapter(adapterSabah);
        adapterOgle = ArrayAdapter.createFromResource(this, R.array.SabahFood, android.R.layout.simple_spinner_item);
        adapterOgle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOgle.setAdapter(adapterOgle);
        adapterAksam = ArrayAdapter.createFromResource(this, R.array.SabahFood, android.R.layout.simple_spinner_item);
        adapterAksam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAksam.setAdapter(adapterAksam);


        String sabahOgun = spinnerSabah.getSelectedItem().toString();
        String ogleOgun = spinnerOgle.getSelectedItem().toString();
        String aksamOgun = spinnerAksam.getSelectedItem().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalori_hesap);
        hesaplayici = findViewById(R.id.hesaplaButton);
        seekBar = findViewById(R.id.seekBar);
        kalori = findViewById(R.id.kalorisonuc);
        foodListsabah = Food.getData();
        foodListogle = Food.getData();
        foodListaksam = Food.getData();
        RecyclerView recyclerView;
        init();


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int maxProgress = seekBar.getMax();
                float threshold = (float) (maxProgress / 1.50); // İlerlemenin yarısı eşik değer olarak alındı

                if (progress <= threshold) {
                    seekBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                } else {
                    seekBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // İlerleme takip edilmeye başlandığında yapılacak işlemler
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // İlerleme takibi bittiğinde yapılacak işlemler
            }
        });

        hesaplayici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sabahOgun = spinnerSabah.getSelectedItem().toString();
                String ogleOgun = spinnerOgle.getSelectedItem().toString();
                String aksamOgun = spinnerAksam.getSelectedItem().toString();

                int kaloriSabah = -1; // Başlangıçta bir değer atayın
                int kaloriOgle = -1; // Başlangıçta bir değer atayın
                int kaloriAksam = -1; // Başlangıçta bir değer atayın

                for (Food food : foodListsabah) {
                    if (food.getName().equals(sabahOgun)) {
                        kaloriSabah = food.getCalories(); // Yemek adına karşılık gelen kalori değerini al
                    } else if (food.getName().equals(ogleOgun)) {
                        kaloriOgle = food.getCalories();
                    } else if (food.getName().equals(aksamOgun)) {
                        kaloriAksam = food.getCalories();
                    }
                }

                if (kaloriSabah != -1 && kaloriOgle != -1 && kaloriAksam != -1) {
                    int toplamKalori = kaloriSabah + kaloriOgle + kaloriAksam;
                    kalori.setText("Toplam Kaloriniz: " + toplamKalori);
                    seekBar.setProgress(toplamKalori);

                    // Firestore'a verileri kaydet
                    saveUserCalories(sabahOgun, ogleOgun, aksamOgun, toplamKalori);
                } else {
                    kalori.setText("Bilgileri eksiksiz seçiniz.");
                }
            }
        });
    }

    private void saveUserCalories(String sabahOgun, String ogleOgun, String aksamOgun, int toplamKalori) {
        Map<String, Object> kaloriMap = new HashMap<>();
        kaloriMap.put("sabah", sabahOgun);
        kaloriMap.put("ogle", ogleOgun);
        kaloriMap.put("aksam", aksamOgun);
        kaloriMap.put("toplamKalori", toplamKalori);

        // MainActivity'den seçili tarih bilgisini al
        String selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            kaloriMap.put("selectedDate", selectedDate); // Tarih bilgisini kaloriMap'e ekle
        }

        DocumentReference documentReference = db.collection("users").document(userId).collection("kalori").document();

        documentReference.set(kaloriMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Başarıyla kaydedildiğinde yapılacak işlemler
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Kaydetme başarısız olduğunda yapılacak işlemler
                    }
                });
    }


}
