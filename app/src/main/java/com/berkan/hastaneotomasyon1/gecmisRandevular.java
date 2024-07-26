package com.berkan.hastaneotomasyon1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.berkan.hastaneotomasyon1.databinding.ActivityGecmisRandevularBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class gecmisRandevular extends AppCompatActivity {
    private ActivityGecmisRandevularBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    ArrayList<String> yazdirilacak = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGecmisRandevularBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        randevuGetir();
    }

    public void gecmisDon(View view) {
        Intent gecmisDon = new Intent(gecmisRandevular.this, hastaAnaSayfa.class);
        startActivity(gecmisDon);
        finish();
    }

    public void randevuGetir() {
        FirebaseUser aktifKullanici = auth.getCurrentUser();
        if (aktifKullanici != null) {
            String mail = aktifKullanici.getEmail();

            firestore.collection("randevular").whereEqualTo("mail", mail).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        // Hata durumu işleme
                        return;
                    }

                    yazdirilacak.clear(); // Önceki verileri temizle
                    for (DocumentSnapshot dokuman : value.getDocuments()) {
                        Map<String, Object> gelenveri = dokuman.getData();
                        Timestamp tarih = (Timestamp) gelenveri.get("olusturmatarih");
                        Date tarih2=tarih.toDate();
                        DateFormat tarihformati = new SimpleDateFormat("dd/MM/yyyy");
                        String yazilacakTarih = tarihformati.format(tarih2);
                            String hastane = (String) gelenveri.get("hastane");
                            String bolum = (String) gelenveri.get("bolum");
                            String doktor = (String) gelenveri.get("doktor");
                            yazdirilacak.add(yazilacakTarih+"\n"+hastane+"\n"+bolum+"\n"+doktor+"\n-----------");

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(gecmisRandevular.this, android.R.layout.simple_list_item_1, yazdirilacak);
                    binding.gecmisRandevular.setAdapter(adapter);
                }
            });
        }
    }

    public void geriGit(View view) {
        Intent anasayfayaDon = new Intent(gecmisRandevular.this, hastaAnaSayfa.class);
        startActivity(anasayfayaDon);
        finish();
    }
}
