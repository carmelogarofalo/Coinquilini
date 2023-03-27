package com.example.coinquilini;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class NuovaCasaActivity extends AppCompatActivity {

    EditText casa;
    Button registra;
    TextView login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuova_casa_activity);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        casa = findViewById(R.id.nome_casa);
        registra = findViewById(R.id.registra_casa);
        login = findViewById(R.id.casa_login);

        login.setOnClickListener(v -> vaiLoginActivity());

        registra.setOnClickListener(v -> {

            String strcasa = casa.getText().toString().trim();

            if(!strcasa.equals(""))
            {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {

                    String[] campi = new String[1];
                    campi[0] = "nome_casa";

                    String[] dati = new String[1];
                    dati[0] = strcasa;

                    HttpsTrustManager.allowAllSSL();
                    PutData putData = new PutData("https://192.168.113.254/appCoinquilini/newhome.php", "POST", campi, dati);

                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if(result.equals("Registrazione casa completata"))
                            {
                                Toast.makeText(getApplicationContext(),"Casa registrata",Toast.LENGTH_SHORT).show();
                                vaiLoginActivity();
                            }

                            else Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            else Toast.makeText(getApplicationContext(),"Inserisci tutti i campi!",Toast.LENGTH_SHORT).show();

        });

    }

    public void vaiLoginActivity()
    {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

}
