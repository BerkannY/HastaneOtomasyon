package com.berkan.hastaneotomasyon1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.berkan.hastaneotomasyon1.databinding.ActivityRandevuAlBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class randevuAl extends AppCompatActivity {
    String[] hastaneler = {"Hastane Seçiniz...", "Ankara Üniversitesi Hastanesi", "Gazi üniversitesi Hastanesi", "Hacattepe Üniversitesi", "İbn-i Sina Hastanesi"};
    String[] bolumler = {"Ortopedi", "Nöroloji", "Dermatoloji", "Dahiliye", "Psikiyatri"};
    ArrayList<String> doktorlar = new ArrayList<>();
    private ActivityRandevuAlBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    int zaman = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_al);
        binding = ActivityRandevuAlBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        doktorlar.add("Doktor Seçiniz");

        binding.bolumspinner.setVisibility(View.INVISIBLE);
        binding.doktorspinner.setVisibility(View.INVISIBLE);
        binding.button5.setVisibility(View.INVISIBLE);
        binding.textView9.setVisibility(View.INVISIBLE);
        binding.textView10.setVisibility(View.INVISIBLE);
        binding.textView11.setVisibility(View.INVISIBLE);
        binding.textView12.setVisibility(View.INVISIBLE);
        binding.textView13.setVisibility(View.INVISIBLE);
        binding.textView14.setVisibility(View.INVISIBLE);
        binding.textView15.setVisibility(View.INVISIBLE);
        binding.textView16.setVisibility(View.INVISIBLE);


        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hastaneler);
        binding.hastanespinner.setAdapter(adapter);

        binding.hastanespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!binding.hastanespinner.getSelectedItem().equals(hastaneler[0])) {
                    ArrayAdapter adapter2 = new ArrayAdapter<>(randevuAl.this, android.R.layout.simple_list_item_1, bolumler);
                    binding.bolumspinner.setAdapter(adapter2);
                    binding.bolumspinner.setVisibility(View.VISIBLE);
                    binding.bolumspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (!binding.bolumspinner.getSelectedItem().equals(bolumler[0])) {
                                binding.doktorspinner.setVisibility(View.VISIBLE);
                                String hastane = binding.hastanespinner.getSelectedItem().toString();
                                String bolum = binding.bolumspinner.getSelectedItem().toString();

                                doktorlar.clear();
                                doktorlar.add("Doktor Seçiniz....");

                                firestore.collection("doktorlar").whereEqualTo("bolumler", bolum).whereEqualTo("hastane", hastane).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        for (DocumentSnapshot dokuman : value.getDocuments()){
                                            Map<String, Object> gelenVeri = dokuman.getData();
                                            String gelenDoktor = (String) dokuman.get("doktorAdi");
                                            doktorlar.add(gelenDoktor);

                                        }

                                    }
                                });
                                ArrayAdapter adapter3 = new ArrayAdapter<>(randevuAl.this, android.R.layout.simple_list_item_1,doktorlar);
                                binding.doktorspinner.setAdapter(adapter3);
//                                binding.button5.setVisibility(View.VISIBLE);
                                binding.doktorspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        if (!binding.doktorspinner.getSelectedItem().equals("Doktor seçiniz")){
                                            binding.textView9.setVisibility(View.VISIBLE);
                                            binding.textView10.setVisibility(View.VISIBLE);
                                            binding.textView11.setVisibility(View.VISIBLE);
                                            binding.textView12.setVisibility(View.VISIBLE);
                                            binding.textView13.setVisibility(View.VISIBLE);
                                            binding.textView14.setVisibility(View.VISIBLE);
                                            binding.textView15.setVisibility(View.VISIBLE);
                                            binding.textView16.setVisibility(View.VISIBLE);
                                            binding.button5.setVisibility(view.VISIBLE);
                                            String doktor = binding.doktorspinner.getSelectedItem().toString();
                                            firestore.collection("randevular").whereEqualTo("doktor",doktor).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    for (DocumentSnapshot dokuman : value.getDocuments()){
                                                        Map<String,Object> gelenVeri=dokuman.getData();
                                                        String saat = String.valueOf(gelenVeri.get("saat"));
                                                        if (saat.equals("9")){
                                                            binding.textView9.setVisibility(View.INVISIBLE);
                                                        }
                                                        if (saat.equals("10")){
                                                            binding.textView10.setVisibility(View.INVISIBLE);
                                                        }
                                                        if (saat.equals("11")){
                                                            binding.textView11.setVisibility(View.INVISIBLE);
                                                        }
                                                        if (saat.equals("12")){
                                                            binding.textView12.setVisibility(View.INVISIBLE);
                                                        }
                                                        if (saat.equals("13")){
                                                            binding.textView13.setVisibility(View.INVISIBLE);
                                                        }
                                                        if (saat.equals("14")){
                                                            binding.textView14.setVisibility(View.INVISIBLE);
                                                        }
                                                        if (saat.equals("15")){
                                                            binding.textView15.setVisibility(View.INVISIBLE);
                                                        }
                                                        if (saat.equals("16")){
                                                            binding.textView16.setVisibility(View.INVISIBLE);
                                                        }
                                                    }

                                                }
                                            });
                                        }else {
                                            binding.textView9.setVisibility(View.INVISIBLE);
                                            binding.textView10.setVisibility(View.INVISIBLE);
                                            binding.textView11.setVisibility(View.INVISIBLE);
                                            binding.textView12.setVisibility(View.INVISIBLE);
                                            binding.textView13.setVisibility(View.INVISIBLE);
                                            binding.textView14.setVisibility(View.INVISIBLE);
                                            binding.textView15.setVisibility(View.INVISIBLE);
                                            binding.textView16.setVisibility(View.INVISIBLE);
                                            binding.button5.setVisibility(View.INVISIBLE);
                                            binding.textView9.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            binding.textView10.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            binding.textView11.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            binding.textView12.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            binding.textView13.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            binding.textView14.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            binding.textView15.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            binding.textView16.setBackgroundColor(Color.parseColor("#FFFFFF"));

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } else{
                                binding.doktorspinner.setVisibility(View.INVISIBLE);

                            }
                        }

                        @Override public void onNothingSelected (AdapterView < ? > parent){

                        }
                    });
                }else{
                    binding.bolumspinner.setVisibility(View.INVISIBLE);

                }




            }



            @Override public void onNothingSelected (AdapterView < ? > parent){

            }
        });
    } public void randevuOlustur (View view){
        if (zaman==0){
            Toast.makeText(this,"Lütfen Saat Seçiniz",Toast.LENGTH_SHORT).show();
        }
        else {
            randevuEkle(zaman);
        }

    }
    public void saat9(View view){
        zaman=9;
        binding.textView9.setBackgroundColor(Color.parseColor("#D0F0C0"));
        binding.textView10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView12.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView13.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView14.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView15.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView16.setBackgroundColor(Color.parseColor("#FFFFFF"));

    }
    public void saat10(View view){
        zaman=10;
        binding.textView9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView10.setBackgroundColor(Color.parseColor("#D0F0C0"));
        binding.textView11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView12.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView13.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView14.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView15.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView16.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    public void saat11(View view){
        zaman=11;
        binding.textView9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView11.setBackgroundColor(Color.parseColor("#D0F0C0"));
        binding.textView12.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView13.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView14.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView15.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView16.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    public void saat12(View view){
        zaman=12;
        binding.textView9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView12.setBackgroundColor(Color.parseColor("#D0F0C0"));
        binding.textView13.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView14.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView15.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView16.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    public void saat13(View view){
        zaman=13;
        binding.textView9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView12.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView13.setBackgroundColor(Color.parseColor("#D0F0C0"));
        binding.textView14.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView15.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView16.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    public void saat14(View view){
        zaman=14;
        binding.textView9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView12.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView13.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView14.setBackgroundColor(Color.parseColor("#D0F0C0"));
        binding.textView15.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView16.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    public void saat15(View view){
        zaman=15;
        binding.textView9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView12.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView13.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView14.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView15.setBackgroundColor(Color.parseColor("#D0F0C0"));
        binding.textView16.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    public void saat16(View view){
        zaman=16;
        binding.textView9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView12.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView13.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView14.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView15.setBackgroundColor(Color.parseColor("#FFFFFF"));
        binding.textView16.setBackgroundColor(Color.parseColor("#D0F0C0"));
    }
    public void  randevuEkle(int saat){
        String hastane=binding.hastanespinner.getSelectedItem().toString();
        String bolum = binding.bolumspinner.getSelectedItem().toString();
        String doktor = binding.doktorspinner.getSelectedItem().toString();
        FirebaseUser aktifKullanici = auth.getCurrentUser();
        String mail = aktifKullanici.getEmail();

        HashMap<String ,Object>veriGonder = new HashMap<>();
        veriGonder.put("mail",mail);
        veriGonder.put("hastane",hastane);
        veriGonder.put("bolum",bolum);
        veriGonder.put("doktor",doktor);
        veriGonder.put("saat",saat);
        veriGonder.put("olusturmaTarihi", FieldValue.serverTimestamp());

        firestore.collection("randevular").add(veriGonder).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(randevuAl.this,"Randevu Başarıyla Kaydedildi",Toast.LENGTH_SHORT).show();
                Intent anasayfa = new Intent(randevuAl.this,hastaAnaSayfa.class);
                startActivity(anasayfa);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(randevuAl.this,"Randevu Oluşturulmadı -"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                Intent anasayfa=new Intent(randevuAl.this,hastaAnaSayfa.class);
                startActivity(anasayfa);
                finish();

            }
        });

    }

}