package com.example.agustin.lab07;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ReclamoActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {
    private GoogleMap mapa;
    private static int CODIGO_RESULTADO_ALTA_RECLAMO=-1;
    private LatLng ubicacion;
    private ArrayList<Reclamo> listaReclamos;
    private Reclamo nuevoReclamo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager() .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listaReclamos = new ArrayList<Reclamo>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reclamo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMyLocationEnabled(true);
        mapa.setOnMapLongClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
          //  return;
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent i = new Intent(this, AltaReclamoActivity.class);
        i.putExtra("coordenadas",latLng);
        ubicacion=latLng;
        startActivityForResult(i, CODIGO_RESULTADO_ALTA_RECLAMO);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        System.out.println("ASDASDADASDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        /*
        switch(resultCode){
            case Activity.RESULT_OK:{
                Bundle extras = data.getExtras();
                nuevoReclamo = (Reclamo) extras.get("reclamo");
                listaReclamos.add(nuevoReclamo);
                System.out.println("LA LISTA DE RECLAMOS MIDE: "+listaReclamos.size());
                ubicacion = nuevoReclamo.getCoordenadas();
                agregarMarcador(ubicacion);
                break;
            }
            case Activity.RESULT_CANCELED:{
                break;
            }
        }
        */
    }


    public void agregarMarcador(LatLng latLng){
        mapa.addMarker(new MarkerOptions().position(latLng).title("Nuevo reclamo"));
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
    }
}
