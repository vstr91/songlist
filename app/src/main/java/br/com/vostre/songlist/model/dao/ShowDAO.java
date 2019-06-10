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
import br.com.vostre.songlist.model.Show;

@Dao
public interface ShowDAO {

    @Query("SELECT * FROM show ORDER BY nome")
    LiveData<List<Show>> listarTodos();

    @Query("SELECT * FROM show ORDER BY nome")
    List<Show> listarTodosSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Show show);

    @Update
    void editar(Show show);

    @Delete
    void deletar(Show show);

    @Query("DELETE FROM show")
    void deletarTodos();

    @Query("DELETE FROM show WHERE artista = :id")
    void deletarTodosPorArtista(String id);

}
