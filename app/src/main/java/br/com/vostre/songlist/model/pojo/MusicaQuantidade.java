package br.com.vostre.songlist.model.pojo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;

import br.com.vostre.songlist.model.Musica;

public class MusicaQuantidade {

    @Embedded
    Musica musica;

    @ColumnInfo(name = "artista")
    String artista;

    @ColumnInfo(name = "total")
    Integer total;

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
