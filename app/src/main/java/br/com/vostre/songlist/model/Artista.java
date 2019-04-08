package br.com.vostre.songlist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"nome", "descricao"},
        unique = true)})
public class Artista extends EntidadeBase {

    @NonNull
    private String descricao;

    @NonNull
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NonNull String descricao) {
        this.descricao = descricao;
    }
}
