package com.example.coinquilini;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    Button home, foto, mappa;
    Toolbar mToolbar;
    String utente,nome_casa;
    TextView name_home,coinquilini;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            nome_casa = bundle.getString("nome casa");
            utente =  bundle.getString("utente");
        }

        if(savedInstanceState!=null)
        {
            nome_casa = savedInstanceState.getString("nome casa");
            utente = savedInstanceState.getString("utente");
        }

        home = findViewById(R.id.btn_home);
        foto = findViewById(R.id.btn_foto);
        mappa = findViewById(R.id.btn_mappa);
        name_home = findViewById(R.id.casa);
        coinquilini = findViewById(R.id.coinquilini);

        name_home.setText(nome_casa);

        home.setFocusableInTouchMode(false);
        foto.setOnClickListener(v -> vaiFotoActivity());
        mappa.setOnClickListener(v -> vaiMappaActivity());

        String url = "http://192.168.113.254/appCoinquilini/leggicasa.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {

                    String st = response.replace("[","");
                    st = st.replace("]","");
                    String[] str = st.split(",");
                    String stringa = "";
                    for (String s : str) {
                        stringa += s.substring(16, s.length() - 2) + " ";
                    }

                    coinquilini.setText(stringa);
                },
                error -> Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show()){

            public Map<String,String> getParams() throws AuthFailureError{

                Map<String,String> params = new HashMap<>();
                params.put("utente",utente);
                params.put("nome_casa",nome_casa);
                return params;
            }

        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("nome casa",nome_casa);
        outState.putString("utente",utente);

        super.onSaveInstanceState(outState);
    }

    private void vaiFotoActivity()
    {
        Intent intent = new Intent(getApplicationContext(),FotoActivity.class);
        intent.putExtra("nome casa", nome_casa);
        intent.putExtra("utente",utente);
        startActivity(intent);
    }

    private void vaiMappaActivity()
    {
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);
    }

}
