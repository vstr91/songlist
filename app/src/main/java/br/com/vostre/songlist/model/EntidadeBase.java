package br.com.vostre.songlist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

public class EntidadeBase {

    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    private String nome;

    @NonNull
    @ColumnInfo(name = "data_cadastro")
    private DateTime dataCadastro;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    @NonNull
    public DateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(@NonNull DateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
