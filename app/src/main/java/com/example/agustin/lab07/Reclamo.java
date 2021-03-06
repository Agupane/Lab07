package com.example.agustin.lab07;

/**
 * Created by Agustin on 11/10/2016.
 */
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.Serializable;

public class Reclamo implements Serializable {
    private Double latitud;
    private Double longitud;
    private String titulo;
    private String telefono;
    private String email;
    private String imagenPath;
    private File foto;

    public Reclamo() {

    }

    public Reclamo(Double lat, Double lng, String titulo, String telefono, String email) {
        this.latitud = lat;
        this.longitud = lng;
        this.titulo = titulo;
        this.telefono = telefono;
        this.email = email;
    }

    public LatLng coordenadaUbicacion() {
        return new LatLng(this.latitud, this.longitud);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        this.imagenPath = imagenPath;
    }

    public void setCoordenadas(Double latitud,Double longitud){
        this.latitud=latitud;
        this.longitud=longitud;
    }

    public LatLng getCoordenadas(){
        LatLng latLng = new LatLng(latitud,longitud);
        return latLng;
    }

    public void setFoto(File foto){
        this.foto=foto;
    }
    public File getFoto(){
        return this.foto;
    }
}