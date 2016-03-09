package com.example.sistemas.centralactuarios;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class JustificarActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justificar);

        Toast.makeText(this, getIntent().getStringExtra("boleta"), Toast.LENGTH_SHORT).show();
    }
}
