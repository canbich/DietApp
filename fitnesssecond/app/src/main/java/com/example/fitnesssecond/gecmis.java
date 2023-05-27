package com.example.fitnesssecond;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesssecond.GecmisAdapter;
import com.example.fitnesssecond.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class gecmis extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GecmisAdapter adapter;
    private ArrayList<Map<String, Object>> dataList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecmis);

        recyclerView = findViewById(R.id.gecmis_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList = new ArrayList<>();

        adapter = new GecmisAdapter(dataList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Oturum açmış kullanıcının kimliğini alın
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Verileri Firestore veritabanından almak için query oluşturun
            Query query = db.collection("users").document(userId).collection("kalori");

            // Firestore sorgusunu gerçekleştirin
            query.addSnapshotListener((value, error) -> {
                if (error != null) {
                    // Sorgu başarısız olduğunda yapılacak işlemler
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    DocumentSnapshot document = dc.getDocument();
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Map<String, Object> gecmisData = document.getData();
                        dataList.add(gecmisData);
                    }
                }

                adapter.notifyDataSetChanged(); // Veri değişikliklerini adapter'a bildirin
            });
        }
    }
}
