package br.com.vostre.songlist.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.joda.time.DateTime;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.songlist.listener.MusicaAPIListener;
import br.com.vostre.songlist.listener.PlaylistAPIListener;
import br.com.vostre.songlist.listener.PlaylistAtualizadaAPIListener;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.Show;
import br.com.vostre.songlist.model.dao.AppDatabase;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import br.com.vostre.songlist.util.APIUtils;

public class RepertorioViewModel extends BaseViewModel {

    public LiveData<List<MusicaQuantidade>> musicas;
    public List<String> musicasSpotify;

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        musicas = appDatabase.musicaDAO().listarTodasPorArtistaComQuantidade(id);
    }

    public RepertorioViewModel(Application app){
        super(app);
        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
        musicas = appDatabase.musicaDAO().listarTodasPorArtistaComQuantidade("");
    }

    public void criarPlaylist(String nome, MusicaAPIListener listener, PlaylistAPIListener pListener){

        APIUtils.criarPlaylist(getApplication().getApplicationContext(), nome,
                musicas.getValue(), listener, pListener);

    }

    public void adicionarMusicasPlaylist(String idPlaylist, List<String> musicas,
                                         PlaylistAtualizadaAPIListener listener){
        APIUtils.adicionarMusicas(getApplication().getApplicationContext(), idPlaylist, musicas, listener);
    }

    public void relacionarMusicasSpotify(MusicaAPIListener listener){

        musicasSpotify = new ArrayList<>();

        List<MusicaQuantidade> mus = musicas.getValue();

        for(MusicaQuantidade m : mus){
            buscarMusica(m, listener);
        }

    }

}
