package br.com.vostre.songlist.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import org.joda.time.DateTime;
import org.json.JSONException;

import br.com.vostre.songlist.listener.APIListener;
import br.com.vostre.songlist.listener.MusicaAPIListener;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.Show;
import br.com.vostre.songlist.model.dao.AppDatabase;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import br.com.vostre.songlist.util.APIUtils;

public class BaseViewModel extends AndroidViewModel {

    public AppDatabase appDatabase;
    public static MutableLiveData<Integer> retorno;

    public BaseViewModel(Application app){
        super(app);
        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
        retorno = new MutableLiveData();
    }

    public void salvarArtista(Artista artista){
        add(artista);
    }

    public void salvarShow(Show show){
        addShow(show);
    }

    public void salvarMusica(Musica musica){
        addMusica(musica);
    }

    public void salvarUsuarioLogado(String token, APIListener listener){

        try {
            APIUtils.consultaUsuarioLogado(token, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public void buscarPlaylists(Context ctx){
//
//        try {
//            APIUtils.buscarPlaylists(ctx);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    // adicionar artista

    public void add(final Artista artista) {

        artista.setDataCadastro(new DateTime());

        new addAsyncTask(appDatabase).execute(artista);
    }

    private static class addAsyncTask extends AsyncTask<Artista, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Artista... params) {
            db.artistaDAO().inserir((params[0]));
            return null;
        }

    }

    // fim adicionar artista

    // adicionar show

    public void addShow(final Show show) {

        show.setDataCadastro(new DateTime());

        new addShowAsyncTask(appDatabase).execute(show);
    }

    private static class addShowAsyncTask extends AsyncTask<Show, Void, Void> {

        private AppDatabase db;

        addShowAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Show... params) {
            db.showDAO().inserir((params[0]));
            return null;
        }

    }

    // fim adicionar show

    // adicionar musica

    public void addMusica(final Musica musica) {

        musica.setDataCadastro(new DateTime());

        new addMusicaAsyncTask(appDatabase).execute(musica);
    }

    private static class addMusicaAsyncTask extends AsyncTask<Musica, Void, Void> {

        private AppDatabase db;

        addMusicaAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Musica... params) {
            db.musicaDAO().inserir((params[0]));
            return null;
        }

    }

    // fim adicionar musica

    // adicionar musica

    public void atualizarMusica(final Musica musica) {

        new atualizarMusicaAsyncTask(appDatabase).execute(musica);
    }

    private static class atualizarMusicaAsyncTask extends AsyncTask<Musica, Void, Void> {

        private AppDatabase db;

        atualizarMusicaAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Musica... params) {
            db.musicaDAO().editar((params[0]));
            return null;
        }

    }

    // fim adicionar musica

    // limpar dados artista

    public void limparDadosArtista(final Artista artista) {

        new limparDadosArtistaAsyncTask(appDatabase).execute(artista);
    }

    private static class limparDadosArtistaAsyncTask extends AsyncTask<Artista, Void, Void> {

        private AppDatabase db;

        limparDadosArtistaAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Artista... params) {

            db.musicaDAO().deletarTodosPorArtista(params[0].getId());
            db.showDAO().deletarTodosPorArtista(params[0].getId());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            retorno.postValue(1);

        }
    }

    // fim limpar dados artista

    public void buscarMusica(MusicaQuantidade m, MusicaAPIListener listener){

        if(m.getMusica().getNome().contains("/")){
            String[] musicas = m.getMusica().getNome().split("/");

            for(String mu : musicas){

                Musica mus = new Musica();
                MusicaQuantidade mu2 = new MusicaQuantidade();

                mus.setNome(mu.trim());
                mu2.setMusica(mus);
                mu2.setArtista(m.getArtista());

                APIUtils.buscarMusica(getApplication().getApplicationContext(), mu2, listener, true);
            }

        } else{
            APIUtils.buscarMusica(getApplication().getApplicationContext(), m, listener, false);
        }


    }

    public void removerPlaylist(String idPlaylist){

        APIUtils.removerPlaylist(getApplication().getApplicationContext(), idPlaylist);

    }

}
