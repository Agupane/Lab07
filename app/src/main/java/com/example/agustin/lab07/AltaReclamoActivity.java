package com.example.agustin.lab07;

/**
 * Created by Agustin on 11/10/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AltaReclamoActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnCancelar;
    private Button btnAgregar;
    private Button btnAgregarFoto;
    private EditText txtDescripcion;
    private EditText txtMail;
    private EditText txtTelefono;
    private LatLng ubicacion;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_RESULT = 2;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView mImageView;
    private File photoFile;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        ubicacion = (LatLng) extras.get("coordenadas");
        setContentView(R.layout.activity_alta_reclamo);
        btnAgregar = (Button) findViewById(R.id.btnReclamar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnAgregarFoto = (Button) findViewById(R.id.btnAgregarFoto);
        mImageView = (ImageView) findViewById(R.id.imageViewVerFoto);
        txtDescripcion = (EditText) findViewById(R.id.etReclamoTexto);
        txtTelefono= (EditText) findViewById(R.id.etReclamoTelefono);
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
        btnAgregarFoto.setOnClickListener(this);
    }
    public void accionAgregarReclamo(){
        Reclamo reclamo = new Reclamo();
        reclamo.setEmail(txtMail.getText().toString());
        reclamo.setTelefono(txtTelefono.getText().toString());
        reclamo.setTitulo(txtDescripcion.getText().toString());
        reclamo.setCoordenadas(ubicacion.latitude,ubicacion.longitude);
        reclamo.setFoto(photoFile);
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

    @Override
    public void onClick(View v) {
        Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, REQUEST_IMAGE_CAPTURE);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }
}