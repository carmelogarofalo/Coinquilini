package com.example.coinquilini;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class LoginActivity extends AppCompatActivity
{
    EditText txtUsername;
    EditText txtPassword;
    EditText txtCodice;
    CheckBox ricorda;
    Button btnLogin;
    TextView Registrati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("ricordami", "");

        if(checkbox.equals("true")) vaiHomeActivity();

        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        txtCodice = findViewById(R.id.codice);
        ricorda = findViewById(R.id.ricorda);
        Registrati = findViewById(R.id.registrati);
        btnLogin = findViewById(R.id.btn_login);

        Registrati.setOnClickListener(view -> vaiRegistratiActivity());

        btnLogin.setOnClickListener(v -> {

            String user = txtUsername.getText().toString().trim();
            String pass = txtPassword.getText().toString().trim();
            String cod = txtCodice.getText().toString().trim();

            if (!txtUsername.equals("") && !txtPassword.equals("") && !txtCodice.equals(""))
            {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {

                    String[] campi = new String[3];
                    campi[0] = "nome_utente";
                    campi[1] = "password";
                    campi[2] = "id_casa";

                    String[] dati = new String[3];
                    dati[0] = user;
                    dati[1] = pass;
                    dati[2] = cod;

                    HttpsTrustManager.allowAllSSL();
                    PutData putData = new PutData("https://192.168.113.254/appCoinquilini/login.php", "POST", campi, dati);

                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult().trim();
                            if (result.equals("Login con successo")) {
                                Toast.makeText(getApplicationContext(), "Login con successo", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                intent.putExtra("nome casa", cod);
                                intent.putExtra("utente",user);
                                startActivity(intent);
                            }

                            if (result.equals("Nome utente o Password errati")) Toast.makeText(getApplicationContext(), "Nome utente o Password errati", Toast.LENGTH_SHORT).show();
                            if (result.equals("Error: Database connection")) Toast.makeText(getApplicationContext(), "Error: Database connection", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            else Toast.makeText(getApplicationContext(), "Inserisci tutti i campi!", Toast.LENGTH_SHORT).show();

        });

        ricorda.setOnCheckedChangeListener((compoundButton, b) -> {

            if(compoundButton.isChecked())
            {
                SharedPreferences preference = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preference.edit();
                editor.putString("ricordami","true");
                editor.apply();
            }

            else if (!compoundButton.isChecked())
            {
                SharedPreferences preference = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preference.edit();
                editor.putString("ricordami","false");
                editor.apply();
            }

        });

    }

    private void vaiRegistratiActivity()
    {
        Intent intent = new Intent(getApplicationContext(),RegistratiActivity.class);
        startActivity(intent);
    }

    private void vaiHomeActivity()
    {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }
}