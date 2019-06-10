package br.com.vostre.songlist.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.songlist.listener.MusicaAPIListener;
import br.com.vostre.songlist.listener.PlaylistAtualizadaAPIListener;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.Show;
import br.com.vostre.songlist.model.dao.AppDatabase;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import br.com.vostre.songlist.util.APIUtils;

public class MainViewModel extends BaseViewModel {


    public LiveData<List<Artista>> artistas;
    public LiveData<List<MusicaQuantidade>> musicas;
    public List<String> musicasSpotify;

    private String[] idArtistas;

    public String[] getIdArtistas() {
        return idArtistas;
    }

    public void setIdArtistas(String[] idArtistas) {
        this.idArtistas = idArtistas;
        musicas = appDatabase.musicaDAO().listarTodasPorArtistasComQuantidade(idArtistas);
    }

    public MainViewModel(Application app){
        super(app);
        artistas = appDatabase.artistaDAO().listarTodos();
        //musicas = appDatabase.musicaDAO().listarTodasPorArtistaComQuantidade("");
    }



    public void relacionarMusicasSpotify(List<MusicaQuantidade> musicas, MusicaAPIListener listener){

        musicasSpotify = new ArrayList<>();

        for(MusicaQuantidade m : musicas){
            buscarMusica(m, listener);
        }

    }

    public void adicionarMusicasPlaylist(String idPlaylist, List<String> musicas,
                                         PlaylistAtualizadaAPIListener listener){
        APIUtils.adicionarMusicas(getApplication().getApplicationContext(), idPlaylist, musicas, listener);
    }

    public void resetaSelecionados(){
        idArtistas = new String[0];
        List<Artista> art = artistas.getValue();

        for(Artista a : art){
            a.setSelecionado(false);
        }

    }

}
