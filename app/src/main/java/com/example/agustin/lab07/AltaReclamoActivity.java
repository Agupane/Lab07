package com.example.agustin.lab07;

/**
 * Created by Agustin on 11/10/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class AltaReclamoActivity extends AppCompatActivity   {

    private Button btnCancelar;
    private Button btnAgregar;
    private EditText txtDescripcion;
    private EditText txtMail;
    private EditText txtTelefono;
    private LatLng ubicacion;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        ubicacion = (LatLng) extras.get("coordenadas");
        setContentView(R.layout.activity_alta_reclamo);
        btnAgregar = (Button) findViewById(R.id.btnReclamar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        txtDescripcion = (EditText) findViewById(R.id.reclamoTexto);
        txtTelefono= (EditText) findViewById(R.id.reclamoTelefono);
        txtMail= (EditText) findViewById(R.id.reclamoMail);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionAgregarReclamo();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionCancelar();
            }
        });
    }
    public void accionAgregarReclamo(){
        Reclamo reclamo = new Reclamo();
        reclamo.setEmail(txtMail.getText().toString());
        reclamo.setTelefono(txtTelefono.getText().toString());
        /** TODO MODIFICAR LO DEL TITULO */
        reclamo.setTitulo("Nuevo reclamo");
        reclamo.setCoordenadas(ubicacion.latitude,ubicacion.longitude);

        Intent resultado = new Intent();
        resultado.putExtra("reclamo",reclamo);
        setResult(Activity.RESULT_OK,resultado);
        finish();
    }
    public void accionCancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}