package br.com.vostre.songlist.model.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;

@Dao
public interface MusicaDAO {

    @Query("SELECT * FROM musica ORDER BY nome")
    LiveData<List<Musica>> listarTodos();

    @Query("SELECT * FROM musica ORDER BY nome")
    List<Musica> listarTodosSync();

    @Query("SELECT m.nome, a.nome AS artista, COUNT(s.nome) AS total " +
            "FROM musica m INNER JOIN show s ON s.id = m.show INNER JOIN artista a ON a.id = s.artista " +
            "WHERE a.id = :id " +
            "GROUP BY a.nome, m.nome " +
            "ORDER BY COUNT(s.nome) DESC, m.nome")
    LiveData<List<MusicaQuantidade>> listarTodasPorArtistaComQuantidade(String id);

    @Query("SELECT m.nome, a.nome AS artista, COUNT(s.nome) AS total " +
            "FROM musica m INNER JOIN show s ON s.id = m.show INNER JOIN artista a ON a.id = s.artista " +
            "WHERE a.id IN (:ids) " +
            "GROUP BY a.nome, m.nome " +
            "ORDER BY a.nome, COUNT(s.nome) DESC, m.nome")
    LiveData<List<MusicaQuantidade>> listarTodasPorArtistasComQuantidade(String[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Musica musica);

    @Update
    void editar(Musica musica);

    @Delete
    void deletar(Musica musica);

    @Query("DELETE FROM musica")
    void deletarTodos();

    @Query("DELETE FROM musica WHERE show = :id")
    void deletarTodosPorShow(String id);

    @Query("DELETE FROM musica WHERE id IN (" +
            "SELECT m.id FROM musica m INNER JOIN show s ON s.id = m.show " +
            "WHERE s.artista = :id)")
    void deletarTodosPorArtista(String id);

}
