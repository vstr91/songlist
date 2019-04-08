package br.com.vostre.songlist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

@Entity(indices = {@Index(value = {"artista","data","cidade"},
        unique = true)})
public class Show extends EntidadeBase {

    @NonNull
    private String artista;

    @NonNull
    private DateTime data;

    @NonNull
    private String cidade;

    @NonNull
    private String pais;

    @NonNull
    public String getArtista() {
        return artista;
    }

    public void setArtista(@NonNull String artista) {
        this.artista = artista;
    }

    @NonNull
    public String getCidade() {
        return cidade;
    }

    public void setCidade(@NonNull String cidade) {
        this.cidade = cidade;
    }

    @NonNull
    public String getPais() {
        return pais;
    }

    public void setPais(@NonNull String pais) {
        this.pais = pais;
    }

}
