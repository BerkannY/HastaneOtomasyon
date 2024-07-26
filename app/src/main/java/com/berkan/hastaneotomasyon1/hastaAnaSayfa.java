package com.berkan.hastaneotomasyon1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.berkan.hastaneotomasyon1.databinding.ActivityHastaAnaSayfaBinding;

public class hastaAnaSayfa extends AppCompatActivity {

    private ActivityHastaAnaSayfaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasta_ana_sayfa);
        binding = ActivityHastaAnaSayfaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }
    public  void randevuAl(View view){
        Intent randevuAl = new Intent(hastaAnaSayfa.this,randevuAl.class);
        startActivity(randevuAl);
        finish();
    }
    public void gecmisRandevu(View view){
        Intent gecmisRandevu = new Intent(hastaAnaSayfa.this,gecmisRandevular.class);
        startActivity(gecmisRandevu);
        finish();

    }
    public  void cikis(View view){
        Intent cikis = new Intent(hastaAnaSayfa.this,MainActivity.class);
        startActivity(cikis);
        finish();

    }
}