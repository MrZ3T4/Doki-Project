package dev.mrz3t4.literatureclub.Retrofit;

import java.io.Serializable;

public class Seiyuu implements Serializable {

    String seiyuu,seiyuuImagen, nacionalidad, SeiyuuUrl;

    public String getSeiyuuUrl() { return SeiyuuUrl; }

    public void setSeiyuuUrl(String seiyuuUrl) { SeiyuuUrl = seiyuuUrl; }

    public String getSeiyuu() {
        return seiyuu;
    }

    public void setSeiyuu(String seiyuu) {
        this.seiyuu = seiyuu;
    }

    public String getSeiyuuImagen() {
        return seiyuuImagen;
    }

    public void setSeiyuuImagen(String seiyuuImagen) {
        this.seiyuuImagen = seiyuuImagen;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}
