package com.berkan.hastaneotomasyon1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.berkan.hastaneotomasyon1.databinding.ActivityDoktorRandevuTakipBinding;
import com.berkan.hastaneotomasyon1.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class doktorRandevuTakip extends AppCompatActivity {
    private ActivityDoktorRandevuTakipBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    String doktorAdi;
    ArrayList<String> saatler= new ArrayList<>();
    ArrayList<String> hastaMailleri= new ArrayList<>();



    ArrayList<String> randevular = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_randevu_takip);
        binding=ActivityDoktorRandevuTakipBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        Intent gelenVeri = getIntent();
        String doktorKullaniciAdi = gelenVeri.getStringExtra("doktor");

        firestore.collection("doktorlar").whereEqualTo("doktorKullaniciAdi",doktorKullaniciAdi).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot dokuman : value.getDocuments()){
                    Map<String,Object>gelenVeri=dokuman.getData();
                    String doktorAdi =(String) gelenVeri.get("doktorAdi");
                }
            }
        });
        firestore.collection("randevular").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot dokuman : value.getDocuments()){
                    Map<String,Object> gelenveri =dokuman.getData();
                    String doktor =(String) gelenveri.get("doktor");
                    if (doktor.equals(doktorAdi)){
                        String saat = String.valueOf(gelenveri.get("saat"));
                        saatler.add(saat);
                        String hastaMaili=(String) gelenveri.get("mail");
                        hastaMailleri.add(hastaMaili);
                    }
                }
                for (int i = 0; i<saatler.size();i++){
                    int finalI=i;
                    firestore.collection("hastalar").whereEqualTo("mail",hastaMailleri.get(i)).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (DocumentSnapshot dokuman : value.getDocuments()){
                                Map<String,Object>gelenVeri = dokuman.getData();
                                String hastaAdi = (String) gelenVeri.get("isim");
                                String hastaSoyadi=(String) gelenVeri.get("soyisim");
                                String eklenecek =hastaAdi+" "+hastaSoyadi+"\n"+saatler.get(finalI)+".00";
                                randevular.add(eklenecek);
                                ArrayAdapter adapter = new ArrayAdapter<>(doktorRandevuTakip.this, android.R.layout.simple_list_item_1,randevular);
                                binding.randevuList.setAdapter(adapter);
                            }

                        }
                    });
                }
            }
        });


    }
    public void cikisyap(View view){
        Intent cikis = new Intent(this,doktorSistemGiris.class);
        startActivity(cikis);
        finish();

    }
}