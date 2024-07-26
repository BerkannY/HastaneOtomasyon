package com.berkan.hastaneotomasyon1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.berkan.hastaneotomasyon1.databinding.ActivityKayitBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class kayit extends AppCompatActivity {

    private ActivityKayitBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String[] sehirler = {"Şehir Seçiniz ...", "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin", "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Iğdır", "Isparta", "İstanbul", "İzmir", "Kahramanmaraş", "Karabük", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir", "Kilis", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Mardin", "Mersin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Şanlıurfa", "Siirt", "Sinop", "Sivas", "Şırnak", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKayitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sehirler);
        binding.sehirSpinner.setAdapter(adapter);
    }

    public void hastaKayit(View view) {
        String mail = binding.kayitMailText.getText().toString();
        String sifre = binding.kayitSifreText.getText().toString();
        String sifreTekrar = binding.kayitSifreTekrarText.getText().toString();
        String isim = binding.kayitIsimText.getText().toString();
        String soyisim = binding.kayitSoyisimText.getText().toString();
        String sehir = binding.sehirSpinner.getSelectedItem().toString();

        if (mail.equals("") || sifre.equals("") || sifreTekrar.equals("") || isim.equals("") || soyisim.equals("")) {
            Toast.makeText(this, "Tüm Alanları Doldurunuz", Toast.LENGTH_SHORT).show();
        } else {
            if (!sifre.equals(sifreTekrar)) {
                Toast.makeText(this, "Şifreler birbirine denk olmalıdır", Toast.LENGTH_SHORT).show();
            } else {
                if (sehir.equals(sehirler[0])) {
                    Toast.makeText(this, "Lütfen Şehir Seçimi Yapınız.", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(mail, sifre)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // Başarıyla kaydedildi
                                    HashMap<String, Object> veriKaydet = new HashMap<>();
                                    veriKaydet.put("mail", mail);
                                    veriKaydet.put("sifre", sifre);
                                    veriKaydet.put("isim", isim);
                                    veriKaydet.put("soyisim", soyisim);
                                    veriKaydet.put("sehir", sehir);

                                    firestore.collection("hastalar").add(veriKaydet)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(kayit.this, "Hasta Kayıt Başarıyla Tamamlandı", Toast.LENGTH_SHORT).show();
                                                    Intent giriseDon = new Intent(kayit.this, MainActivity.class);
                                                    startActivity(giriseDon);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(kayit.this, "Hasta Kayıt Yapılamadı, Tekrar Deneyiniz", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Kaydetme işlemi başarısız
                                    Toast.makeText(kayit.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }
    }
}
