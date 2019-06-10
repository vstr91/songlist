package br.com.vostre.songlist.model.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.vostre.songlist.model.Artista;

@Dao
public interface ArtistaDAO {

    @Query("SELECT * FROM artista ORDER BY nome")
    LiveData<List<Artista>> listarTodos();

    @Query("SELECT * FROM artista ORDER BY nome")
    List<Artista> listarTodosSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Artista artista);

    @Update
    void editar(Artista artista);

    @Delete
    void deletar(Artista artista);

    @Query("DELETE FROM artista")
    void deletarTodos();

}
