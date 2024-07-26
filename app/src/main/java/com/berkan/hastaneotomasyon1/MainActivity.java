package com.berkan.hastaneotomasyon1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.berkan.hastaneotomasyon1.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    private FirebaseAuth auth;
    public FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        yukleme();





    }
    public  void giris(View view){
        String mail=binding.mailText.getText().toString();
        String sifre=binding.sifreText.getText().toString();
        if (mail.equals("")||sifre.equals("")){
            Toast.makeText(this, "E-Posta yada Şifre Boş Geçilmez", Toast.LENGTH_SHORT).show();
        }else {
            auth.signInWithEmailAndPassword(mail,sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //Giriş Başarılı
                    Intent hastaAnasayfa = new Intent(MainActivity.this,com.berkan.hastaneotomasyon1.hastaAnaSayfa.class);
                    startActivity(hastaAnasayfa);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //BAŞARISIZ
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public  void kayit(View view){
        Intent gecis= new Intent(this, kayit.class);
        startActivity(gecis);

    }
    public void doktorHastaneKayit(){
        String[] hastaneler={"Ankara Üniversitesi Hastanesi","Gazi üniversitesi Hastanesi","Hacattepe Üniversitesi","İbn-i Sina Hastanesi"};
        String[] bolumler={"Ortopedi","Nöroloji","Dermatoloji","Dahiliye","Psikiyatri"};
        String[] doktorlar = {"Dr. Ayşe Yılmaz", "Dr. Mehmet Demir", "Dr. Fatma Kılıç", "Dr. Ahmet Kaya", "Dr. Zeynep Çelik", "Dr. Ali Yıldız", "Dr. Emine Koç", "Dr. Hasan Güneş", "Dr. Sevgi Acar", "Dr. Mustafa Şahin", "Dr. Elif Tekin", "Dr. Murat Özkan", "Dr. Ebru Çetin", "Dr. Yusuf Tan", "Dr. Serap Özdemir", "Dr. İbrahim Arslan", "Dr. Nalan Aslan", "Dr. Fikret Polat", "Dr. Aylin Yıldırım", "Dr. Selim Karaca"};
        String[] doktorKullaniciAdi= {"ayseyilmaz", "mehmetdemir", "fatmakilic", "ahmetkaya", "zeynepcelik", "aliyildiz", "eminekoc", "hasangunes", "sevgiacar", "mustafasahin", "eliftekin", "muratozkan", "ebrucetin", "yusuftan", "serapozdemir", "ibrahimarslan", "nalanaslan", "fikretpolat", "aylinyildirim", "selimkaraca"
        };


        for (int i =0 ;i<doktorlar.length;i++){
            HashMap<String,Object> veriKaydet= new HashMap<>();
            veriKaydet.put("doktorAdi",doktorlar[i]);
            veriKaydet.put("hastane",hastaneler[i% hastaneler.length]);
            veriKaydet.put("bolumler",bolumler[i% bolumler.length]);
            veriKaydet.put("doktorKullaniciAdi",doktorKullaniciAdi[i]);
            firestore.collection("doktorlar").add(veriKaydet).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,"Kaydedilmeyen Doktorlar Mevcud",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }

    }
    public void yukleme(){
        firestore.collection("doktorlar").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    finish();
                }
                int i =0;
                for (DocumentSnapshot dokuman :value.getDocuments()){
                    i++;
                }
                if (i==0){
                    doktorHastaneKayit();
                }
            }
        });
    }
    public void doktorGiris(View view){
        Intent doktorGiris = new Intent(this,doktorSistemGiris.class);
        startActivity(doktorGiris);
        finish();
    }
}