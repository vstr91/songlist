package br.com.vostre.songlist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"nome", "descricao"},
        unique = true)})
public class Artista extends EntidadeBase {

    @NonNull
    private String descricao;

    @Ignore
    private boolean selecionado;

    @NonNull
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NonNull String descricao) {
        this.descricao = descricao;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    @Override
    public String toString() {

        if(this.getDescricao() == null || this.getDescricao().isEmpty()){
            return this.getNome();
        } else{
            return this.getNome()+" - "+this.getDescricao();
        }

    }
}
