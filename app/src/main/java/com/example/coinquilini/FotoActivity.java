package com.example.coinquilini;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FotoActivity extends AppCompatActivity {

    Button aggiungi;
    String nome_casa,utente;
    RequestQueue requestQueue;
    TextView textViewTipo, textViewImporto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        aggiungi = findViewById(R.id.aggiungiBtn);
        textViewTipo = findViewById(R.id.textviewtipo);
        textViewImporto = findViewById(R.id.textviewimporto);

        textViewTipo.setText("");
        textViewImporto.setText("");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            nome_casa = bundle.getString("nome casa");
            utente =  bundle.getString("utente");
        }

        aggiungi.setOnClickListener(v -> vaiRiconosciAtivity());
        richiesta();

    }

    private void richiesta()
    {

        String url = "http://192.168.113.254/appCoinquilini/leggiBolletta.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {

                    String st = response.replace("[", "");
                    st = st.replace("]","");
                    String[] str = st.split(",");
                    String tipo;
                    String importo;
                    int i;

                    for(i=0; i<str.length; i++)
                    {
                        if(i%2 == 0)
                        {
                            tipo = str[i].substring(9,str[i].length()-1);
                            textViewTipo.setText(textViewTipo.getText()+"\n"+tipo);
                        }

                        else
                        {
                            importo = str[i].substring(11,str[i].length()-2);
                            textViewImporto.setText(textViewImporto.getText()+"\n"+importo);
                        }

                    }

                },
                error -> Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show()){

            public Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("utente",utente);
                params.put("nome_casa",nome_casa);
                return params;
            }

        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void vaiRiconosciAtivity()
    {
        Intent intent = new Intent(getApplicationContext(),RiconoscitoreActivity.class);
        intent.putExtra("nome casa", nome_casa);
        startActivity(intent);
    }

}