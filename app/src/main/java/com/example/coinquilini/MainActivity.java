package com.example.coinquilini;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button login;
    Button crea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        login = findViewById(R.id.login_main);
        crea = findViewById(R.id.crea);

        login.setOnClickListener(view -> VaiLoginActivity());
        crea.setOnClickListener(view -> VaiNuovaCasaActivity());

    }

    public void VaiLoginActivity()
    {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    public void VaiNuovaCasaActivity()
    {
        Intent intent = new Intent(getApplicationContext(), NuovaCasaActivity.class);
        startActivity(intent);
    }

}