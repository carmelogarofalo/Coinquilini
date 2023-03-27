package com.example.coinquilini;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class RegistratiActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText cnfpassword;
    Button registrati;
    TextView login;

    //DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrati_activity);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        username = findViewById(R.id.rgst_username);
        password = findViewById(R.id.rgst_password);
        cnfpassword = findViewById(R.id.rgst_cnfpassword);
        registrati = findViewById(R.id.rgst_registra);
        login = findViewById(R.id.rgst_login);

        login.setOnClickListener(v -> vaiLoginActivity());

        registrati.setOnClickListener(v -> {

            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String cnfpass = cnfpassword.getText().toString().trim();

            if(!user.equals("")  && !pass.equals("") && !cnfpass.equals("") && pass.equals(cnfpass))
            {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {

                    String[] campi = new String[2];
                    campi[0] = "username";
                    campi[1] = "password";

                    String[] dati = new String[2];
                    dati[0] = user;
                    dati[1] = pass;

                    HttpsTrustManager.allowAllSSL();
                    PutData putData = new PutData("https://192.168.113.254/appCoinquilini/signup.php", "POST", campi, dati);

                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult().trim();
                            if(result.equals("Registrazione completata"))
                            {
                                Toast.makeText(getApplicationContext(),"Utente registrato",Toast.LENGTH_SHORT).show();
                                vaiLoginActivity();
                            }

                            if(result.equals("Registrazione fallita")) Toast.makeText(getApplicationContext(),"Registrazione fallita",Toast.LENGTH_SHORT).show();
                            if(result.equals("Error: Database connection")) Toast.makeText(getApplicationContext(),"Error: Database connection",Toast.LENGTH_SHORT).show();
                            else Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            else if (!pass.equals(cnfpass) && !user.equals("") && !pass.equals("") && !cnfpass.equals("")) Toast.makeText(getApplicationContext(),"Controlla le password!",Toast.LENGTH_SHORT).show();

            else Toast.makeText(getApplicationContext(),"Inserisci tutti i campi!",Toast.LENGTH_SHORT).show();

        });
    }

    public void vaiLoginActivity()
    {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

