package com.example.agustin.lab07;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class ReclamoActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener,GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mapa;
    private static int CODIGO_RESULTADO_ALTA_RECLAMO=99;
    private LatLng ubicacion;
    private ArrayList<Reclamo> listaReclamos;
    private ArrayList<Reclamo> listaReclamosCercanos;
    private Reclamo nuevoReclamo;
   // private EditText etCantKm;
    private Integer cantKmReclamo;
    private Integer cantMetrosReclamo;
    private Marker marcadorSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager() .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listaReclamos = new ArrayList<Reclamo>();
        listaReclamosCercanos = new ArrayList<Reclamo>();
        cantMetrosReclamo=0;
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
        mapa.setOnInfoWindowClickListener(this);
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
        Intent i = new Intent(ReclamoActivity.this, AltaReclamoActivity.class);
        i.putExtra("coordenadas",latLng);
        ubicacion=latLng;
        startActivityForResult(i, CODIGO_RESULTADO_ALTA_RECLAMO);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        switch(resultCode){
            case Activity.RESULT_OK:{
                Bundle extras = data.getExtras();
                nuevoReclamo = (Reclamo) extras.get("reclamo");
                listaReclamos.add(nuevoReclamo);
                agregarMarcador(nuevoReclamo.getCoordenadas(),nuevoReclamo.getTitulo());
                break;
            }
            case Activity.RESULT_CANCELED:{
                break;
            }
        }

    }


    public void agregarMarcador(LatLng latLng,String titulo){
        mapa.addMarker(new MarkerOptions()
                .position(latLng)
                .title(titulo))

                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marcadorSelected = marker;
        // Crear un buildery vincularlo a la actividad que lo mostrará
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.alert_distancia_busqueda, null);
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        //Configurar las características
            builder.setView(inflator)
                    .setMessage("Desea marcar todos lo reclamos que esten a X Distancia?");
            final EditText etCantKm = (EditText) inflator.findViewById(R.id.etDistanciaReclamo);

            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String valorKm = etCantKm.getText().toString();
                            cantKmReclamo = Integer.parseInt(valorKm);
                            cantMetrosReclamo = cantKmReclamo*1000;
                            listaReclamosCercanos = obtenerListaMarcadoresCercanos(marcadorSelected,cantMetrosReclamo);
                            unirReclamos(listaReclamosCercanos);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        AlertDialog dialog= builder.create();
        //Mostrarlo
        dialog.show();
    }
    private ArrayList<Reclamo> obtenerListaMarcadoresCercanos(Marker puntoInicial, Integer distancia){
        Location targetLocation = new Location("");
        Location initLocation = new Location("");
        initLocation.setLatitude(puntoInicial.getPosition().latitude);
        initLocation.setLongitude(puntoInicial.getPosition().longitude);
        float distanceInMeters;
        for(Reclamo reclamo:listaReclamos){
            targetLocation.setLatitude(reclamo.getCoordenadas().latitude);
            targetLocation.setLongitude(reclamo.getCoordenadas().longitude);

            distanceInMeters = targetLocation.distanceTo(initLocation);
            if(distanceInMeters<=distancia){
                if(!listaReclamosCercanos.contains(reclamo)) {
                    listaReclamosCercanos.add(reclamo);
                }
            }
        }
        return listaReclamosCercanos;
    }
    private void unirReclamos(ArrayList<Reclamo> listaReclamos){
        LatLng reclamoPos = new LatLng(ubicacion.latitude,ubicacion.longitude);
        PolylineOptions rectOptions= new PolylineOptions();
        for(Reclamo reclamo:listaReclamos){
            reclamoPos = reclamo.getCoordenadas();
            rectOptions.add(reclamoPos).color(Color.BLUE);
        }
        Polyline polyline= mapa.addPolyline(rectOptions);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(reclamoPos, 14));
    }
}
