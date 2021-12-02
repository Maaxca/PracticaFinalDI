package com.example.practicafinaldi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnSeries,btnTele,btnMovieDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSeries=findViewById(R.id.btnSeries);
        btnTele=findViewById(R.id.btnTele);
        btnMovieDB=findViewById(R.id.btnMovieDB);

        btnSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MiBaseDeDatos.class);
                startActivityForResult(intent,0);
            }
        });
    }
}