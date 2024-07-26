package com.berkan.hastaneotomasyon1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.berkan.hastaneotomasyon1.databinding.ActivityDoktorSistemGirisBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class doktorSistemGiris extends AppCompatActivity {
    private ActivityDoktorSistemGirisBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    boolean giris=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_sistem_giris);
        binding = ActivityDoktorSistemGirisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth =FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

    }
    public void doktorSistemGiris(View view){
        String kullaniciAdi = binding.doktorKullaniciAdiText.getText().toString();
        String sifre=binding.doktorSifreText.getText().toString();

        firestore.collection("doktorlar").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot dokuman : value.getDocuments()){
                    Map<String,Object> gelenveri = dokuman.getData();
                    String doktorAdi = (String) gelenveri.get("doktorKullaniciAdi");
                    if (doktorAdi.equals(kullaniciAdi)&&sifre.equals("123456")){
                        giris=true;
                        break;
                    }
                }
                if (giris==true){
                    //Giriş Başarılı ise
                    Intent doktorRandevuTakip= new Intent(doktorSistemGiris.this,com.berkan.hastaneotomasyon1.doktorRandevuTakip.class);
                    doktorRandevuTakip.putExtra("doktorlar",kullaniciAdi);
                    startActivity(doktorRandevuTakip);
                    finish();
                }
                else {
                    Toast.makeText(doktorSistemGiris.this, "Giris Basarısız", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}