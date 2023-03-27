package com.example.coinquilini;

public class BolletteModel
{

    String tipo;
    String importo;

    public BolletteModel(String tipo, String importo)
    {
        this.tipo = tipo;
        this.importo = importo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getImporto() {
        return importo;
    }
}
