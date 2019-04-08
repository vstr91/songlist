package br.com.vostre.songlist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

@Entity
public class Musica extends EntidadeBase {

    private String observacao;

    @NonNull
    private String show;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @NonNull
    public String getShow() {
        return show;
    }

    public void setShow(@NonNull String show) {
        this.show = show;
    }

}
